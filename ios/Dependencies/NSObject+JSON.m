//
//  NSObject+JSON.m
//

#import "NSObject+JSON.h"

@implementation NSObject (JSON)
- (NSData *)JSONRepresentation
{
    NSError *error = nil;
    NSData *data = [NSJSONSerialization dataWithJSONObject:self
                                                   options:NSJSONWritingPrettyPrinted
                                                     error:&error];
    if (error) {
        NSLog(@"%@",self);
        NSLog(@"%@",error);
        return nil;
    }
    return data;
}
@end
