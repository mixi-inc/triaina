//
//  TrianiaWebViewAdapter.h
//  mixi
//
//  Created by Taketo Sano on 12/02/15.
//  Copyright (c) 2012年 mixi, Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface TriainaWebViewAdapter : NSObject <UIWebViewDelegate> {
    id webViewDelegate;
    id webBridgeDelegate;
    UIWebView *webView;
}

@property (assign, nonatomic) BOOL triainaEnabled; 
@property (assign, nonatomic) IBOutlet id webViewDelegate;
@property (assign, nonatomic) IBOutlet id webBridgeDelegate;
@property (assign, nonatomic) IBOutlet UIWebView *webView;

- (id)initWithWebView:(UIWebView *)webView;

- (SEL)deviceHandlerForChannel:(NSString *)channel isRequest:(BOOL)isRequest;
- (SEL)deviceResponderForChannel:(NSString *)channel;
- (void)receivedDeviceNotification:(NSDictionary*)notify;
- (NSString *)sendWebNotificationWithChannel:(NSString*)channel params:(NSDictionary *)params;
- (void)respondWithResult:(NSDictionary *)result bridgeId:(NSString *)bridgeId;

// default handler/sender
- (void)handleSystemWebStatusRequestWithParams:(NSDictionary *)params bridgeId:(NSString *)bridgeId;
- (void)handleErrorWithCode:(NSString *)code message:(NSString *)message;
- (void)sendErrorWithCode:(NSString *)code message:(NSString*)message bridgeId:(NSString *)bridgeId;

@end
