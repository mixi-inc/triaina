//
//  MixiWebBridgeModel+StandardAPI.m
//  mixi
//
//  Created by Taketo Sano on 12/11/27.
//
//

#import "TriainaWebBridgeModel+StandardAPI.h"
#import "UIAlertView+BlocksExtension.h"

@implementation TriainaWebBridgeModel (StandardAPI)

- (void)handleSystemWebStatusRequestWithParams:(NSDictionary *)params bridgeId:(NSString *)bridgeId
{
    NSDictionary *result = [NSDictionary dictionary];
    [self.adapter respondToWebWithBridgeID:bridgeId result:result];
}

- (void)handleSystemDestIsAvailableRequestWithParams:(NSDictionary *)params bridgeId:(NSString *)bridgeId
{
    NSString *dest = [params objectForKey:@"dest_name"];
    SEL handler = [self requestHandlerForDest:dest];
    NSString *value = ([self respondsToSelector:handler] || [self.delegate respondsToSelector:handler]) ? @"true" : @"false";
    NSDictionary *result = [NSDictionary dictionaryWithObject:value forKey:@"value"];
    [self.adapter respondToWebWithBridgeID:bridgeId result:result];
}

- (void)handleSystemDialogConfirmRequestWithParams:(NSDictionary *)params bridgeId:(NSString *)bridgeId
{
    [[[[UIAlertView alloc]
       initWithTitle:[params objectForKey:@"title"]
       message:[params objectForKey:@"body"]
       callback:^(NSInteger index) {
           NSNumber *value = [NSNumber numberWithBool:(index == 1)];
           NSDictionary *result = [NSDictionary dictionaryWithObject:value forKey:@"value"];
           [self.adapter respondToWebWithBridgeID:bridgeId result:result];
       }
       cancelButtonTitle:@"キャンセル"
       otherButtonTitles:@"OK", nil]
      autorelease] show];
}

- (void)handleSystemNetWebviewOpenRequestWithParams:(NSDictionary *)params bridgeId:(NSString *)bridgeId
{
    NSURL *URL = [NSURL URLWithString:[params objectForKey:@"url"]];
    [self.adapter.webView loadRequest:[NSURLRequest requestWithURL:URL]];
}

@end
