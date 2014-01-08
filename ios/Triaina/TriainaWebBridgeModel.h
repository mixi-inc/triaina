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

@property(nonatomic, weak) TriainaWebViewAdapter *adapter;
@property(nonatomic, weak) id<TriainaWebBridgeDelegate> delegate;
@property(nonatomic, weak, readonly) UIWebView *webView;
@property(nonatomic, weak, readonly) UIViewController *viewController;

// request/response dispatch
- (SEL)requestHandlerForDest:(NSString *)dest;
- (SEL)responseHandlerForDest:(NSString *)dest;
- (void)receivedRequestWithBridgeId:(NSString *)bridgeId dest:(NSString *)dest params:(NSDictionary *)params;
- (void)receivedResponseWithBridgeId:(NSString *)bridgeId dest:(NSString *)dest result:(NSDictionary *)result;

// default handlers
- (void)handleErrorWithCode:(NSString *)code message:(NSString *)message;

@end
