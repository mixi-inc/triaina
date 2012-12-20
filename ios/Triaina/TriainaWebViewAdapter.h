//
//  TrianiaWebViewAdapter.h
//  mixi
//
//  Created by Taketo Sano on 12/02/15.
//  Copyright (c) 2012年 mixi, Inc. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum {
    TriainaLogTypeNone,
    TriainaLogTypeError,
    TriainaLogTypeWarn,
    TriainaLogTypeInfo
} TriainaLogType;

@class TriainaWebBridgeModel;
@protocol TriainaWebBridgeDelegate;

@interface TriainaWebViewAdapter : NSObject <UIWebViewDelegate>

@property (nonatomic, assign) BOOL triainaEnabled;
@property (nonatomic, readonly) BOOL triainaInitialized;
@property (nonatomic, retain) NSString *triainaAllowedDomain;
@property (nonatomic, assign) BOOL consoleEnabled;
@property (nonatomic, readonly) BOOL consoleInitialized;
@property (nonatomic, assign) TriainaLogType logFilter;

@property (nonatomic, retain) IBOutlet TriainaWebBridgeModel *model;
@property (nonatomic, assign) IBOutlet UIWebView *webView;
@property (nonatomic, assign) IBOutlet id<UIWebViewDelegate> webViewDelegate;
@property (nonatomic, assign) IBOutlet id<TriainaWebBridgeDelegate> webBridgeDelegate;

- (id)initWithWebView:(UIWebView *)webView;
- (id)initWithWebView:(UIWebView *)webView model:(TriainaWebBridgeModel *)model;

- (NSString *)sendMessageToDeviceWithDest:(NSString*)channel params:(NSDictionary *)params;
- (NSString *)sendMessageToWebWithDest:(NSString*)channel params:(NSDictionary *)params;
- (void)respondToWebWithBridgeID:(NSString *)bridgeID result:(NSDictionary *)result;
- (void)respondErrorToWebWithBridgeID:(NSString *)bridgeID code:(NSString *)code message:(NSString*)message;

- (void)log:(NSString *)msg;
- (void)log:(NSString *)msg type:(TriainaLogType)type;

@end

@protocol TriainaWebBridgeDelegate <NSObject>

@optional
- (void)webBridge:(TriainaWebBridgeModel *)model didStartAsyncConnectionWithBridgeId:(NSString *)bridgeId;
- (void)webBridge:(TriainaWebBridgeModel *)model didEndAsyncConnectionWithBridgeId:(NSString *)bridgeId;

@end