//
//  TriainaWebViewController.m
//  TriainaSample
//
//  Created by Sano Taketo on 12/07/19.
//  Copyright (c) 2012年 __MyCompanyName__. All rights reserved.
//

#import "TriainaWebViewController.h"

@interface TriainaWebViewController ()

@property (nonatomic, retain) NSURL *initialURL;
@property (nonatomic, retain) NSString *currentBridgeId;

@end

@implementation TriainaWebViewController

@synthesize triainaWebView;
@synthesize initialURL, currentBridgeId;

- (id)initWithURL:(NSURL *)URL
{
    self = [super initWithNibName:NSStringFromClass(self.class) bundle:nil];
    if (self) {
        self.initialURL = URL;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    triainaWebView.adapter.triainaEnabled = YES;
    
    if (initialURL) {
        [triainaWebView loadRequest:[NSURLRequest requestWithURL:initialURL]];
        self.initialURL = nil;
    }
}

- (void)viewDidUnload
{
    [super viewDidUnload];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (void)dealloc
{
    self.triainaWebView = nil;
    self.initialURL = nil;
    self.currentBridgeId = nil;
    [super dealloc];
}

#pragma mark - Triaina Web Bridge Handlers

- (void)handleSystemDialogConfirmRequestWithParams:(NSDictionary *)params bridgeId:(NSString *)bridgeId
{
    self.currentBridgeId = bridgeId;
    
    [[[[UIAlertView alloc] 
       initWithTitle:[params objectForKey:@"title"] 
       message:[params objectForKey:@"body"] 
       delegate:self
       cancelButtonTitle:@"キャンセル" 
       otherButtonTitles:@"OK", nil] 
      autorelease] show];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    NSNumber *value = [NSNumber numberWithBool:(buttonIndex == 1)];
    NSDictionary *result = [NSDictionary dictionaryWithObject:value forKey:@"value"];
    [triainaWebView.adapter respondWithResult:result bridgeId:currentBridgeId];
    
    self.currentBridgeId = nil;
}

- (void)handleSystemNetWebviewOpenRequestWithParams:(NSDictionary *)params bridgeId:(NSString *)bridgeId
{
    NSURL *URL = [NSURL URLWithString:[params objectForKey:@"url"]];
    [triainaWebView loadRequest:[NSURLRequest requestWithURL:URL]];
}

@end
