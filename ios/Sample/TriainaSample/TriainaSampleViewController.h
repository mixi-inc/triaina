//
//  TriainaSampleViewController.h
//  TriainaSample
//
//  Created by Sano Taketo on 12/07/19.
//  Copyright (c) 2012年 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TriainaWebView.h"

@interface TriainaSampleViewController : UIViewController <UIWebViewDelegate, TriainaWebBridgeDelegate>

@property (nonatomic, strong) IBOutlet TriainaWebView *webView;
@property (nonatomic, strong) IBOutlet UITextField *urlField;
@property (nonatomic, strong) IBOutlet UIBarButtonItem *triainaButton;
@property (nonatomic, strong) IBOutlet UIBarButtonItem *backButton, *forwardButton, *refreshButton, *stopButton;

- (IBAction)triainaButtonTapped:(id)sender;
- (IBAction)backButtonTapped:(id)sender;
- (IBAction)forwardButtonTapped:(id)sender;
- (IBAction)refreshButtonTapped:(id)sender;
- (IBAction)stopButtonTapped:(id)sender;

@end
