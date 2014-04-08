


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
When Web user clicks the directory tcbdd
Then Web story storiesList exists

Examples:

Scenario: Can display stories

Meta:




Examples:

Scenario: Can open story

Meta:




Examples:

Scenario: Can create new story

Meta:




Examples:

Scenario: Can delete story

Meta:




Examples:

Scenario: Can create directory

Meta:




Examples:

Scenario: Can create story

Meta:




Examples:
