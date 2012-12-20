//
//  TriainaWebView.h
//  TriainaSample
//
//  Created by Sano Taketo on 12/07/19.
//  Copyright (c) 2012年 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TriainaWebViewAdapter.h"

@interface TriainaWebView : UIWebView

@property (nonatomic, retain) TriainaWebViewAdapter *adapter;
@property (nonatomic, assign) IBOutlet id webBridgeDelegate;

- (void)prepareAdapter; // override if model-class is overridden.

@end
