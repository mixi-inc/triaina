//
//  TriainaWebView.m
//  TriainaSample
//
//  Created by Sano Taketo on 12/07/19.
//  Copyright (c) 2012年 __MyCompanyName__. All rights reserved.
//

#import "TriainaWebView.h"
#import "TriainaWebBridgeModel.h"

@implementation TriainaWebView

@dynamic webBridgeDelegate;

- (void)prepareAdapter {
    self.adapter = [[[TriainaWebViewAdapter alloc] initWithWebView:self] autorelease];
}

- (id)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame]) {
        [self prepareAdapter];
    }
    return self;
}

- (id)initWithCoder:(NSCoder *)coder 
{
    if (self = [super initWithCoder:coder]) {
        [self prepareAdapter];
    }
    return self;
}

- (void)dealloc {
    self.adapter = nil;
    [super dealloc];
}

// override delegate getter
- (id)delegate 
{
    return _adapter.webViewDelegate;
}

// override delegate setter
- (void)setDelegate:(id<UIWebViewDelegate>)delegate 
{
    _adapter.webViewDelegate = delegate;
    [super setDelegate:_adapter];
}

- (id)webBridgeDelegate
{
    return _adapter.webBridgeDelegate;
}

- (void)setWebBridgeDelegate:(id)webBridgeDeleagte
{
    _adapter.webBridgeDelegate = webBridgeDeleagte;
}

@end
