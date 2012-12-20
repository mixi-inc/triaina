//
//  TrianiaBridgeConfig.m
//  mixi
//
//  Created by Taketo Sano on 12/02/15.
//  Copyright (c) 2012年 mixi, Inc. All rights reserved.
//

#import "TriainaConfig.h"

static NSString * const kTriainaBridgeURLScheme = @"triaina-bridge";

@implementation TriainaConfig

+ (NSString *)version {
    return @"1.1";
}

+ (NSString *)urlScheme {
    return kTriainaBridgeURLScheme;
}

@end
