//
//  MixiWebBridgeModel+StandardAPI.h
//  mixi
//
//  Created by Taketo Sano on 12/11/27.
//
//

#import "TriainaWebBridgeModel.h"

@interface TriainaWebBridgeModel (StandardAPI)

- (void)handleSystemWebStatusRequestWithParams:(NSDictionary *)params bridgeId:(NSString *)bridgeId;
- (void)handleSystemDestIsAvailableRequestWithParams:(NSDictionary *)params bridgeId:(NSString *)bridgeId;
- (void)handleSystemDialogConfirmRequestWithParams:(NSDictionary *)params bridgeId:(NSString *)bridgeId;
- (void)handleSystemNetWebviewOpenRequestWithParams:(NSDictionary *)params bridgeId:(NSString *)bridgeId;
- (void)handleSystemNotificationNotifyRequestWithParams:(NSDictionary*)params bridgeId:(NSString*)bridgeId;

@end
