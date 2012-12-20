//
//  NSString+Util.m
//  libMixiGraph
//
//  Created by kinukawa on 11/05/08.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "NSString+Util.h"


@implementation NSString (NSString_Util)
-(NSString*) encodeURIComponent {
    NSMutableString *tstr = [NSMutableString stringWithString:self];
    [tstr replaceOccurrencesOfString:@"¥"
                          withString:@"\\"
                             options:0
                               range:NSMakeRange( 0 , [tstr length] )];
    return [((NSString*)CFURLCreateStringByAddingPercentEscapes(kCFAllocatorDefault,
                                                                (CFStringRef)tstr,
                                                                NULL,
                                                                (CFStringRef)@"!*'();:@&=+$,/?%#[]",
                                                                kCFStringEncodingUTF8)) autorelease];
}

-(NSString*) decodeURIComponentes {
    return [(NSString *) CFURLCreateStringByReplacingPercentEscapesUsingEncoding(
                                                                                 NULL,
                                                                                 (CFStringRef) self,
                                                                                 CFSTR(""),
                                                                                 kCFStringEncodingUTF8) autorelease];
}
@end
