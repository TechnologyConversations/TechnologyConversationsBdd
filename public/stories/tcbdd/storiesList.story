


Meta:


Narrative:
In order to open existing and create new stories
As a application user
I want to be able to browse files and directories

GivenStories: 

Lifecycle:

Before:


After:



Scenario: Can open dialog

Meta:


Given Web home page is opened
When Web user clicks the element browseStories
Then Web element modalHeader should have text Browse Stories

Examples:

Scenario: Can navigate through directories

Meta:


Given Web user is in the Browse Stories dialog
When Web user clicks the element tcbdd
Then Web element dirPath should have text tcbdd/

Examples:
