


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
Then Web modal header should have text Browse Stories

Examples:

Scenario: Can navigate through directories

Meta:


Given Web user is in the Browse Stories dialog
When Web user clicks the element text:myDirectory
Then Web element text:myDirectory should appear in the path

Examples:

Scenario: Can display stories

Meta:


Given Web user is in the Browse Stories dialog
When Web user clicks the element text:myDirectory
Then Web element text:myStory exists

Examples:

Scenario: Can open story

Meta:


Given Web user is in the Browse Stories dialog
When Web user clicks the element text:myStory
Then Web story page is opened

Examples:

Scenario: Can create new story

Meta:


Given Web user is in the Browse Stories dialog
When Web user clicks the element createNewStory
Then Web new story page is opened

Examples:

Scenario: Can delete story

Meta:


Given Web user is in the Browse Stories dialog
When Web user clicks the element text:delete myStory
Then Web element text:myStory disappeared

Examples:

Scenario: Can create new directory

Meta:


Given Web user is in the Browse Stories dialog
When Web user sets value myNewDirectory to the element search
Then Web element text:myNewDirectory appears

Examples:

Scenario: Can delete directory

Meta:


Given Web user is in the Browse Stories dialog
When Web user clicks the element text:delete myDirectory
Then Web element text:myDirectory disappeared

Examples:
