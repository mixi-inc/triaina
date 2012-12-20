//
//  TriainaWebViewModel.m
//  mixi
//
//  Created by Taketo Sano on 12/08/20.
//
//

#import "TriainaWebBridgeModel.h"
#import "TriainaConfig.h"

@interface TriainaWebBridgeModel()

@property (nonatomic, retain) NSString *currentBridgeId;

@end

@implementation TriainaWebBridgeModel

- (void)dealloc {
    self.adapter = nil;
    self.currentBridgeId = nil;
    [super dealloc];
}

#pragma mark - properties

- (UIWebView *)webView {
    return _adapter.webView;
}

- (UIViewController *)viewController {
    for (UIView* next = [self.webView superview]; next; next = next.superview) {
		UIResponder* nextResponder = [next nextResponder];
		if ([nextResponder isKindOfClass:[UIViewController class]]) {
			return (UIViewController*)nextResponder;
		}
	}
	return nil;
}

#pragma mark - request/response dispatch

- (SEL)requestHandlerForDest:(NSString *)dest {
    NSMutableString *selectorString = [NSMutableString stringWithString:@"handle"];
    
    NSArray *tokens = [dest componentsSeparatedByCharactersInSet:[NSCharacterSet characterSetWithCharactersInString:@"._"]];
    for(NSString *token in tokens) {
        [selectorString appendFormat:@"%@%@", [[token substringToIndex:1] capitalizedString], [token substringFromIndex:1]];
    }
    
    [selectorString appendString:@"RequestWithParams:bridgeId:"];
    return NSSelectorFromString(selectorString);
}

- (SEL)responseHandlerForDest:(NSString *)dest {
    NSMutableString *selectorString = [NSMutableString stringWithString:@"handle"];
    
    NSArray *tokens = [dest componentsSeparatedByString:@"."];
    for(NSString *token in tokens) {
        [selectorString appendFormat:@"%@%@", [[token substringToIndex:1] capitalizedString], [token substringFromIndex:1]];
    }
    
    [selectorString appendString:@"ResponseWithResult:bridgeId:"];
    return NSSelectorFromString(selectorString);
}

- (void)receivedRequestWithBridgeId:(NSString *)bridgeId dest:(NSString *)dest params:(NSDictionary *)params {
    SEL handler = [self requestHandlerForDest:dest];
    if([_delegate respondsToSelector:handler]) {
        [_delegate performSelector:handler withObject:params withObject:bridgeId];
    } else if([self respondsToSelector:handler]) {
        [self performSelector:handler withObject:params withObject:bridgeId];
    } else {
        NSString *msg = [NSString stringWithFormat:@"dest: %@ is unsupported", dest];
        [_adapter log:msg type:TriainaLogTypeError];
        [_adapter respondErrorToWebWithBridgeID:bridgeId code:@"404" message:[NSString stringWithFormat:@"unsupported dest:%@", dest]];
    }
}

- (void)receivedResponseWithBridgeId:(NSString *)bridgeId dest:(NSString *)dest result:(NSDictionary *)result {
    SEL handler = [self responseHandlerForDest:dest];
    if([_delegate respondsToSelector:handler]) {
        [_delegate performSelector:handler withObject:result withObject:bridgeId];
    } else if([self respondsToSelector:handler]) {
        [self performSelector:handler withObject:result withObject:bridgeId];
    } else {
        NSString *msg = [NSString stringWithFormat:@"dest: %@ is unsupported", dest];
        [_adapter log:msg type:TriainaLogTypeError];
        [_adapter respondErrorToWebWithBridgeID:bridgeId code:@"404" message:[NSString stringWithFormat:@"unsupported dest:%@", dest]];
    }
}

- (void)handleErrorWithCode:(NSString *)code message:(NSString *)message {
    NSString *msg = [NSString stringWithFormat:@"received error(%@): %@", code, message];
    [_adapter log:msg type:TriainaLogTypeError];
}

@end
