//
//  TriainaWebViewController.h
//  TriainaSample
//
//  Created by Sano Taketo on 12/07/19.
//  Copyright (c) 2012年 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "TriainaWebView.h"

@interface TriainaWebViewController : UIViewController<UIAlertViewDelegate>

@property (nonatomic, retain) IBOutlet TriainaWebView *triainaWebView;

- (id)initWithURL:(NSURL *)URL;

@end
