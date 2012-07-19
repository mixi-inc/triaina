//
//  TriainaWebView.m
//  TriainaSample
//
//  Created by Sano Taketo on 12/07/19.
//  Copyright (c) 2012年 __MyCompanyName__. All rights reserved.
//

#import "TriainaWebView.h"

@implementation TriainaWebView

@synthesize adapter;
@dynamic webBridgeDelegate;

- (id)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame]) {
        self.adapter = [[[TriainaWebViewAdapter alloc] initWithWebView:self] autorelease];
    }
    return self;
}

- (id)initWithCoder:(NSCoder *)coder 
{
    if (self = [super initWithCoder:coder]) {
        self.adapter = [[[TriainaWebViewAdapter alloc] initWithWebView:self] autorelease];
    }
    return self;
}

// override delegate getter
- (id)delegate 
{
    return adapter.webViewDelegate;
}

// override delegate setter
- (void)setDelegate:(id<UIWebViewDelegate>)delegate 
{
    adapter.webViewDelegate = delegate;
    [super setDelegate:adapter];
}

- (id)webViewDelegate 
{
    return adapter.webBridgeDelegate;
}

- (void)setWebBridgeDelegate:(id)webBridgeDeleagte
{
    adapter.webBridgeDelegate = webBridgeDeleagte;
}


- (void)dealloc {
    self.delegate = nil;
    self.adapter = nil;
    [super dealloc];
}

@end
