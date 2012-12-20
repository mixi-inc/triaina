//
//  TriainaWebViewModel.h
//  mixi
//
//  Created by Taketo Sano on 12/08/20.
//
//

#import <Foundation/Foundation.h>
#import "TriainaWebViewAdapter.h"

@protocol TriainaWebBridgeDelegate;

@interface TriainaWebBridgeModel : NSObject

@property(nonatomic, assign) TriainaWebViewAdapter *adapter;
@property(nonatomic, assign) id<TriainaWebBridgeDelegate> delegate;
@property(nonatomic, readonly) UIWebView *webView;
@property(nonatomic, readonly) UIViewController *viewController;

// request/response dispatch
- (SEL)requestHandlerForDest:(NSString *)dest;
- (SEL)responseHandlerForDest:(NSString *)dest;
- (void)receivedRequestWithBridgeId:(NSString *)bridgeId dest:(NSString *)dest params:(NSDictionary *)params;
- (void)receivedResponseWithBridgeId:(NSString *)bridgeId dest:(NSString *)dest result:(NSDictionary *)result;

// default handlers
- (void)handleErrorWithCode:(NSString *)code message:(NSString *)message;

@end
