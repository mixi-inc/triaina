//
//  MixiWebBridgeModel+StandardAPI.m
//  mixi
//
//  Created by Taketo Sano on 12/11/27.
//
//

#import "TriainaWebBridgeModel+StandardAPI.h"

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

@end
