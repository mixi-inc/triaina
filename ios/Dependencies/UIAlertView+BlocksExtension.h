//
//  UIAlertView+BlocksExtension.h
//

#import <Foundation/Foundation.h>

@interface UIAlertView(BlocksExtension)

- (id)initWithTitle:(NSString *)title
            message:(NSString *)message
           callback:(void (^)(NSInteger index))callback
  cancelButtonTitle:(NSString *)cancelButtonTitle
  otherButtonTitles:(NSString *)otherButtonTitles, ...;

@end
