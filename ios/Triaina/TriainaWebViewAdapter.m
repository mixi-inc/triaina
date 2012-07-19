//
//  TrianiaWebViewAdapter.m
//  mixi
//
//  Created by Taketo Sano on 12/02/15.
//  Copyright (c) 2012年 mixi, Inc. All rights reserved.
//

#import "TriainaWebViewAdapter.h"
#import "TriainaConfig.h"
#import "SBJson.h"

#define kTriainaBridgeURLScheme @"triaina-bridge"

@interface TriainaWebViewAdapter()

@property (nonatomic,retain) NSString *sessionKey;

@end

@implementation TriainaWebViewAdapter

@synthesize triainaEnabled;
@synthesize webViewDelegate, webBridgeDelegate;
@synthesize webView;
@synthesize sessionKey;

- (id)initWithWebView:(UIWebView *)aWebView {
    self = [super init];
    if(self) {
        self.webView = aWebView;
        self.sessionKey = [NSString stringWithFormat:@"%x", rand()];
    }
    return self;
}

- (void)dealloc {
    self.webViewDelegate = nil;
    self.webBridgeDelegate = nil;
    self.webView.delegate = nil;
    self.webView = nil;
    self.sessionKey = nil;
    [super dealloc];
}

#pragma mark - Bridge Methods

- (SEL)deviceHandlerForChannel:(NSString *)channel isRequest:(BOOL)isRequest
{
    NSMutableString *selectorString = [NSMutableString stringWithString:@"handle"];
    
    NSArray *tokens = [channel componentsSeparatedByString:@"."];
    for(NSString *token in tokens) {
        [selectorString appendFormat:@"%@%@", [[token substringToIndex:1] capitalizedString], [token substringFromIndex:1]];
    }
    
    if(isRequest) {
        [selectorString appendString:@"RequestWithParams:bridgeId:"];
    } else {
        [selectorString appendString:@"ResponseWithResult:bridgeId:"];
    }
    
    return NSSelectorFromString(selectorString);
}

- (SEL)deviceResponderForChannel:(NSString *)channel 
{
    NSMutableString *selectorString = [NSMutableString stringWithString:@"respondTo"];
    
    NSArray *tokens = [channel componentsSeparatedByString:@"."];
    for(NSString *token in tokens) {
        [selectorString appendFormat:@"%@%@", [[token substringToIndex:1] capitalizedString], [token substringFromIndex:1]];
    }
    [selectorString appendString:@"WithParams:bridgeId:"];
    
    return NSSelectorFromString(selectorString);
}

- (void)receivedDeviceNotification:(NSDictionary*)notify 
{
    // TODO check version
    // NSString *version = [notify objectForKey:@"bridge"];
    
    // error notification
    if([notify objectForKey:@"code"]) {
        NSString *code = [notify objectForKey:@"code"];
        NSString *message = [notify objectForKey:@"message"];
        
        if([webBridgeDelegate respondsToSelector:@selector(handleErrorWithCode:message:)]) {
            [webBridgeDelegate performSelector:@selector(handleErrorWithCode:message:) withObject:code withObject:message];
        } else {
            [self handleErrorWithCode:code message:message];
        }
        return;
    }
    
    NSLog(@"[Triaina] received: %@", notify);
    
    NSString *channel = [notify objectForKey:@"dest"];
    NSString *bridgeId = [notify objectForKey:@"id"];
    BOOL isRequest = ([notify objectForKey:@"params"] != nil);
    NSDictionary *params = (isRequest) ? [notify objectForKey:@"params"] : [notify objectForKey:@"result"];
        
    SEL selector = [self deviceHandlerForChannel:channel isRequest:isRequest];
    if([webBridgeDelegate respondsToSelector:selector]) {
        [webBridgeDelegate performSelector:selector withObject:params withObject:bridgeId];
    } else if([self respondsToSelector:selector]) {
        [self performSelector:selector withObject:params withObject:bridgeId];
    } else {
        NSLog(@"[Triaina] channel: %@ is unsupported", channel);
        [self sendErrorWithCode:@"404" message:[NSString stringWithFormat:@"unsupported channel:%@", channel] bridgeId:bridgeId];
    }
}

- (void)notifyToWeb:(NSDictionary*)notify 
{
    NSString *json = [notify JSONRepresentation];
    NSString *call = [NSString stringWithFormat:@"WebBridge.notifyToWeb('%@');", 
                      [json stringByAddingPercentEscapesUsingEncoding:NSASCIIStringEncoding]];
    [webView stringByEvaluatingJavaScriptFromString:call];
    
    NSLog(@"[Triaina] sent: %@", notify);
}

- (NSString *)sendWebNotificationWithChannel:(NSString*)channel params:(NSDictionary *)params 
{
    static NSInteger count = 0;
    
    NSString *sendId = [NSString stringWithFormat:@"send.%d", count++];
    
    NSDictionary *notify = [NSDictionary dictionaryWithObjectsAndKeys:
                            [TriainaConfig version], @"bridge",
                            channel, @"dest", 
                            sendId, @"id",
                            params, @"params", nil];
    
    [self notifyToWeb:notify];
    return sendId;
}

- (void)respondWithResult:(NSDictionary *)result bridgeId:(NSString *)bridgeId 
{
    NSDictionary *notify = [NSDictionary dictionaryWithObjectsAndKeys:
                            [TriainaConfig version], @"bridge",
                            bridgeId, @"id",
                            result, @"result", nil];
    
    [self notifyToWeb:notify];
}

#pragma mark - Default Handlers

- (void)handleSystemWebStatusRequestWithParams:(NSDictionary *)params bridgeId:(NSString *)bridgeId
{
    [self respondWithResult:[NSDictionary dictionary] 
                   bridgeId:bridgeId];
}

- (void)handleErrorWithCode:(NSString *)code message:(NSString *)message {
    NSLog(@"[Triaina] received error(%@): %@", code, message);
}

- (void)sendErrorWithCode:(NSString *)code message:(NSString*)message bridgeId:(NSString *)bridgeId
{
    NSDictionary *notify = [NSDictionary dictionaryWithObjectsAndKeys:
                            [TriainaConfig version], @"bridge",
                            bridgeId, @"id",
                            [NSDictionary dictionaryWithObjectsAndKeys:
                             code, @"code",
                             message, @"message",
                             nil], @"error", 
                            nil];
    
    [self notifyToWeb:notify];
}

#pragma mark - UIWebViewDelegate

- (BOOL)webView:(UIWebView *)aWebView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    if(triainaEnabled && [request.URL.scheme isEqualToString:kTriainaBridgeURLScheme]) {
        NSString *path = request.URL.path;
        NSString *key = [path substringWithRange:NSMakeRange(1, sessionKey.length)];
        NSString *json = [path substringFromIndex:(1 + sessionKey.length + 1)];
        
        if(![key isEqualToString:sessionKey]) {
            NSLog(@"[Triaina] invalid session-key: %@", key);
            return NO;
        }
        
        NSDictionary *notify = [json JSONValue];
        if(notify) {
            [self receivedDeviceNotification:notify];
        } else {
            NSLog(@"[Triaina] broken json: %@", json);
        }
        return NO;
    }
    
    if([webViewDelegate respondsToSelector:@selector(webView:shouldStartLoadWithRequest:navigationType:)])
        return [webViewDelegate webView:aWebView shouldStartLoadWithRequest:request navigationType:navigationType];
    else
        return YES;
}

- (void)webViewDidStartLoad:(UIWebView *)aWebView 
{
    if([webViewDelegate respondsToSelector:@selector(webViewDidStartLoad:)])
        [webViewDelegate webViewDidStartLoad:aWebView];
}

- (void)webViewDidFinishLoad:(UIWebView *)aWebView 
{
    if(triainaEnabled
       && [[aWebView stringByEvaluatingJavaScriptFromString:@"WebBridge != undefined"] isEqualToString:@"true"]
       && ![[aWebView stringByEvaluatingJavaScriptFromString:@"DeviceBridge != undefined"] isEqualToString:@"true"]) {
        [aWebView stringByEvaluatingJavaScriptFromString:@"window.DeviceBridge = {};"];
        [aWebView stringByEvaluatingJavaScriptFromString:[NSString stringWithFormat:@"window.DeviceBridge.notifyToDevice = function(str){ document.location = ('%@://notify-device/%@/' + str); };", kTriainaBridgeURLScheme, sessionKey]];
        
        [self sendWebNotificationWithChannel:@"system.device.status" 
                                      params:[NSDictionary dictionaryWithObject:@"opened" forKey:@"status"]];
    }
    
    if([webViewDelegate respondsToSelector:@selector(webViewDidFinishLoad:)])
        [webViewDelegate webViewDidFinishLoad:aWebView];
}

- (void)webView:(UIWebView *)aWebView didFailLoadWithError:(NSError *)error
{
    if([webViewDelegate respondsToSelector:@selector(webView:didFailLoadWithError:)])
        [webViewDelegate webView:aWebView didFailLoadWithError:error];
}


@end
