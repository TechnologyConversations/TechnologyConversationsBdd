


Meta:


Narrative:
In order to be able to access all functionalities
As a application user
I want to have the navigation menu

GivenStories: 

Lifecycle:

Before:
Given Web home page is opened

After:



Scenario: Top menu should have navigation links

Meta:


Then Web element browseStories should have text Browse Stories
Then Web element browseComposites should have text Browse Composites
Then Web element runner should have text Run Stories

Examples:

Scenario: Has link to the home screen (New Story)

Meta:


When Web user clicks the element pageTitle
Then Web element pageTitle should have text Home

Examples:

Scenario: Has link to the Browse Stories screen

Meta:


When Web user clicks the element browseStories
Then Web user is in the stories modal screen

Examples:

Scenario: Has link to the Browse Composites screen

Meta:


When Web user clicks the element browseComposites
Then Web user is in the composites modal screen

Examples:

Scenario: Has link to the Run Stories screen

Meta:


When Web user clicks the element runner
Then Web user is in the Runner Selector modal screen

Examples:
