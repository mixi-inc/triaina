//
//  UIAlertView+BlocksExtension.m
//

#import "UIAlertView+BlocksExtension.h"
#import <objc/runtime.h>

static const NSString *kUIAlertViewBlocksExtensionBlockKey = @"kUIAlertViewBlocksExtensionBlockKey";

@interface UIAlertViewCallback : NSObject<UIAlertViewDelegate> {
    void (^b)(NSInteger index);
}
- (id)initWithCallback:(void (^)(NSInteger index))callback;
@end

@implementation UIAlertViewCallback

- (id)initWithCallback:(void (^)(NSInteger index))callback
{
    if(self = [super init]) {
        if(callback) {
            b = Block_copy(callback);
        }
    }
    return self;
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(b) {
        b(buttonIndex);
    }
}

- (void)dealloc
{
    if(b) {
        Block_release(b);
    }
    [super dealloc];
}

@end

@implementation UIAlertView(BlocksExtension)

- (id)initWithTitle:(NSString *)title
            message:(NSString *)message
           callback:(void (^)(NSInteger index))callback
  cancelButtonTitle:(NSString *)cancelButtonTitle
  otherButtonTitles:(NSString *)otherButtonTitles, ...
{
    
    self = [self initWithTitle:title message:message delegate:nil cancelButtonTitle:cancelButtonTitle otherButtonTitles:nil];
    if(self) {
        va_list args;
        va_start(args, otherButtonTitles);
        for (NSString *arg = otherButtonTitles; arg != nil; arg = va_arg(args, NSString*)) {
            [self addButtonWithTitle:arg];
        }
        va_end(args);
        
        id delegate = [[[UIAlertViewCallback alloc] initWithCallback:callback] autorelease];
        objc_setAssociatedObject(self, kUIAlertViewBlocksExtensionBlockKey, delegate, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
        self.delegate = delegate;
    }
    return self;
}

@end
