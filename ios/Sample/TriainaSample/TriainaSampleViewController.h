//
//  TriainaSampleViewController.h
//  TriainaSample
//
//  Created by Sano Taketo on 12/07/19.
//  Copyright (c) 2012年 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TriainaWebViewController.h"

@interface TriainaSampleViewController : UIViewController

@property (nonatomic, retain) IBOutlet TriainaWebViewController *triainaWebViewController;
@property (nonatomic, retain) IBOutlet UIView *webViewPlaceholder;
@property (nonatomic, retain) TriainaWebView *webView;
@property (nonatomic, retain) IBOutlet UITextField *urlField;
@property (nonatomic, retain) IBOutlet UIBarButtonItem *triainaButton;
@property (nonatomic, retain) IBOutlet UIBarButtonItem *backButton, *forwardButton, *refreshButton, *stopButton;

- (IBAction)triainaButtonTapped:(id)sender;
- (IBAction)backButtonTapped:(id)sender;
- (IBAction)forwardButtonTapped:(id)sender;
- (IBAction)refreshButtonTapped:(id)sender;
- (IBAction)stopButtonTapped:(id)sender;

@end
