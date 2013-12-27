//
//  TrianiaWebViewAdapter.m
//  mixi
//
//  Created by Taketo Sano on 12/02/15.
//  Copyright (c) 2012年 mixi, Inc. All rights reserved.
//

#import "TriainaWebViewAdapter.h"
#import "TriainaWebBridgeModel.h"
#import "TriainaConfig.h"
#import "NSString+Util.h"
#import "NSData+JSON.h"
#import "NSObject+JSON.h"

@interface TriainaWebViewAdapter()

@property (nonatomic, strong) NSString *sessionKey;

- (void)receivedMessage:(NSDictionary*)message;

@end

@implementation TriainaWebViewAdapter

@dynamic webBridgeDelegate;

- (id)init {
    return [self initWithWebView:nil];
}

- (id)initWithWebView:(UIWebView *)aWebView {
    TriainaWebBridgeModel *aModel = [[TriainaWebBridgeModel alloc] init];
    return [self initWithWebView:aWebView model:aModel];
}

- (id)initWithWebView:(UIWebView *)aWebView model:(TriainaWebBridgeModel *)aModel {
    self = [super init];
    if(self) {
        self.model = aModel;
        aModel.adapter = self;
        self.webView = aWebView;
        self.sessionKey = [NSString stringWithFormat:@"%x", rand()];
        self.logFilter = TriainaLogTypeWarn;
    }
    return self;
}

#pragma mark - properties

- (id)webBridgeDelegate
{
    return _model.delegate;
}

- (void)setWebBridgeDelegate:(id)webBridgeDeleagte
{
    _model.delegate = webBridgeDeleagte;
}

#pragma mark - Initializer Methods

- (void)initializeTriaina {

    NSString *script = [NSString stringWithFormat:
                        @"window.DeviceBridge = {};"
                        @"window.DeviceBridge.notifyToDevice = function(msg) {"
                        @"  document.location = ('%@://notify-device/%@/' + msg);"
                        @"};",
                        [TriainaConfig urlScheme], _sessionKey];

    [_webView stringByEvaluatingJavaScriptFromString:script];

    [self sendMessageToWebWithDest:@"system.device.status"
                            params:[NSDictionary dictionaryWithObject:@"opened" forKey:@"status"]];
}

- (BOOL)triainaInitialized {
    return [[_webView stringByEvaluatingJavaScriptFromString:@"window.DeviceBridge != undefined"] isEqualToString:@"true"];
}

- (BOOL)triainaAllowed {
    if(!_triainaAllowedDomain) return YES;
    if([self.webView.request.mainDocumentURL.host hasSuffix:_triainaAllowedDomain]) return YES;

    return NO;
}

- (void)initializeConsole {
    if(self.consoleInitialized)
        return;

    NSString *script = [NSString stringWithFormat:
                        @"window.console = {};"
                        @"window.console.log = function(str) {"
                        @"  document.location = ('%@://log/' + escape(str));"
                        @"};"
                        @"window.console.triaina_flag = 1",
                        [TriainaConfig urlScheme]];

    [_webView stringByEvaluatingJavaScriptFromString:script];
}

- (BOOL)consoleInitialized {
    return [[_webView stringByEvaluatingJavaScriptFromString:@"window.console.triaina_flag != undefined"] isEqualToString:@"true"];
}

#pragma mark - Log Methods

- (void)log:(NSString *)msg {
    [self log:msg type:TriainaLogTypeInfo];
}

- (void)log:(NSString *)msg type:(TriainaLogType)type {
    if(_logFilter < type)
        return;

    NSString *typeStr = (type == TriainaLogTypeInfo) ? @"info" :
    (type == TriainaLogTypeWarn) ? @"warn" : @"error";

    NSLog(@"[Triaina-%p %@] %@", self, typeStr, msg);
}

#pragma mark - Bridge Methods

- (void)receivedMessage:(NSDictionary*)message
{
    // TODO check version
    // NSString *version = [notify objectForKey:@"bridge"];

    // error notification
    if([message objectForKey:@"code"]) {
        NSString *code = [message objectForKey:@"code"];
        NSString *emsg = [message objectForKey:@"message"];

        [_model handleErrorWithCode:code message:emsg];
        return;
    }

    [self log:[NSString stringWithFormat:@"received: %@", message]];

    NSString *bridgeId = [message objectForKey:@"id"];
    NSString *dest = [message objectForKey:@"dest"];

    if([message objectForKey:@"params"] != nil) {
        NSDictionary *params = [message objectForKey:@"params"];
        [_model receivedRequestWithBridgeId:bridgeId dest:dest params:params];
    } else {
        NSDictionary *result = [message objectForKey:@"result"];
        [_model receivedResponseWithBridgeId:bridgeId dest:dest result:result];
    }
}

- (NSString *)sendMessageToDeviceWithDest:(NSString*)channel params:(NSDictionary *)params {
    static NSInteger count = 0;

    if(!params)
        params = [NSDictionary dictionary];

    NSString *bridgeId = [NSString stringWithFormat:@"device_to_device.%d", count++];
    NSDictionary *message = [NSDictionary dictionaryWithObjectsAndKeys:
                             [TriainaConfig version], @"bridge",
                             channel, @"dest",
                             bridgeId, @"id",
                             params, @"params", nil];

    [self receivedMessage:message];

    return bridgeId;
}

- (void)sendMessageToWeb:(NSDictionary*)message
{
    NSData *jsonData = [message JSONRepresentation];
    NSString *json = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    NSString *call = [NSString stringWithFormat:@"WebBridge.notifyToWeb('%@');",
                      [json encodeURIComponent]];
    [_webView stringByEvaluatingJavaScriptFromString:call];

    [self log:[NSString stringWithFormat:@"sent: %@", message]];
}

- (NSString *)sendMessageToWebWithDest:(NSString*)channel params:(NSDictionary *)params
{
    static NSInteger count = 0;

    if(!params)
        params = [NSDictionary dictionary];

    NSString *sendId = [NSString stringWithFormat:@"device_to_web.%d", count++];
    NSDictionary *message = [NSDictionary dictionaryWithObjectsAndKeys:
                             [TriainaConfig version], @"bridge",
                             channel, @"dest",
                             sendId, @"id",
                             params, @"params", nil];

    [self sendMessageToWeb:message];

    return sendId;
}

- (void)respondToWebWithBridgeID:(NSString *)bridgeId result:(NSDictionary *)result
{
    if(!result)
        result = [NSDictionary dictionary];

    NSDictionary *notify = [NSDictionary dictionaryWithObjectsAndKeys:
                            [TriainaConfig version], @"bridge",
                            bridgeId, @"id",
                            result, @"result", nil];

    [self sendMessageToWeb:notify];
}

- (void)respondErrorToWebWithBridgeID:(NSString *)bridgeId code:(NSString *)code message:(NSString *)message
{
    NSDictionary *notify = [NSDictionary dictionaryWithObjectsAndKeys:
                            [TriainaConfig version], @"bridge",
                            bridgeId, @"id",
                            [NSDictionary dictionaryWithObjectsAndKeys:
                             code, @"code",
                             message, @"message",
                             nil], @"error",
                            nil];

    [self sendMessageToWeb:notify];
}

#pragma mark - UIWebViewDelegate

- (BOOL)webView:(UIWebView *)aWebView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    if(_triainaEnabled && [request.URL.scheme isEqualToString:[TriainaConfig urlScheme]]) {
        if(![self triainaAllowed]) {
            [self log:[NSString stringWithFormat:@"Ignored triaina-request in domain: %@", self.webView.request.mainDocumentURL.host]
                 type:TriainaLogTypeWarn];
            return NO;
        }

        if(!self.triainaInitialized)
            [self initializeTriaina];

        // Deviceのメッセージ受信
        if([request.URL.host isEqualToString:@"notify-device"]) {
            NSString *path = request.URL.path;
            NSString *key = [path substringWithRange:NSMakeRange(1, _sessionKey.length)];
            NSString *json = [path substringFromIndex:(1 + _sessionKey.length + 1)];

            if(![key isEqualToString:_sessionKey]) {
                [self log:[NSString stringWithFormat:@"invalid session-key: %@", key] type:TriainaLogTypeError];
                return NO;
            }

            NSData *jsonData = [json dataUsingEncoding:NSUTF8StringEncoding];
            NSDictionary *notify = [jsonData JSONValue];
            if(notify) {
                [self receivedMessage:notify];
            } else {
                [self log:[NSString stringWithFormat:@"broken json: %@", json] type:TriainaLogTypeError];
            }

            return NO;
        }
        // ログ表示
        else if([request.URL.host isEqualToString:@"log"]) {
            NSString *msg = [request.URL.path substringFromIndex:1];
            [self log:msg];

            return NO;
        }
    }

    if([_webViewDelegate respondsToSelector:@selector(webView:shouldStartLoadWithRequest:navigationType:)]) {
        return [_webViewDelegate webView:aWebView shouldStartLoadWithRequest:request navigationType:navigationType];
    } else {
        return YES;
    }
}

- (void)webViewDidStartLoad:(UIWebView *)aWebView
{
    if([_webViewDelegate respondsToSelector:@selector(webViewDidStartLoad:)])
        [_webViewDelegate webViewDidStartLoad:aWebView];
}

- (void)webViewDidFinishLoad:(UIWebView *)aWebView
{
    if([self triainaAllowed]) {
        if(self.consoleEnabled && !self.consoleInitialized)
            [self initializeConsole];

        if(_triainaEnabled)
            [self initializeTriaina];
    }

    if([_webViewDelegate respondsToSelector:@selector(webViewDidFinishLoad:)])
        [_webViewDelegate webViewDidFinishLoad:aWebView];
}

- (void)webView:(UIWebView *)aWebView didFailLoadWithError:(NSError *)error
{
    if([_webViewDelegate respondsToSelector:@selector(webView:didFailLoadWithError:)])
        [_webViewDelegate webView:aWebView didFailLoadWithError:error];
}


@end
