//
//  NSData+JSON.m
//

#import "NSData+JSON.h"

@implementation NSData (JSON)
- (id)JSONValue
{
    NSError *error = nil;
    id jsonValue = [NSJSONSerialization JSONObjectWithData:self
                                                   options:NSJSONReadingMutableContainers
                                                     error:&error];
    if (error) {
        NSLog(@"%@",error);
        NSLog(@"%@",[[NSString alloc]initWithData:self encoding:NSUTF8StringEncoding]);
        return nil;
    }
    return jsonValue;
}
@end
