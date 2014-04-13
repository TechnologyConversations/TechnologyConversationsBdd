


Meta:

Narrative:
In order to open existing and create new stories
As a application user
I want to be able to browse files and directories

GivenStories: 

Lifecycle:

Before:
Given File public/stories/myDirectory/myStory.story exists
Given File public/stories/myStory.story exists

After:
Given Directory public/stories/myDirectory does NOT exist
Given File public/stories/myStory.story does NOT exist
Given Directory public/stories/myNewDirectory does NOT exist


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
Then Web element path should have text myDirectory

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
Then Web element pageTitle should have text View Story

Examples:

Scenario: Can create new story

Meta:


Given Web user is in the Browse Stories dialog
When Web user clicks the element createNewStory
Then Web element pageTitle should have text New Story

Examples:

Scenario: Can delete story

Meta:


Given Web user is in the Browse Stories dialog
When Web user clicks the element deletemyStory
When Web user clicks the element ok
Then Web element text:myStory disappeared

Examples:

Scenario: Can create new directory

Meta:


Given Web user is in the Browse Stories dialog
When Web user sets value myNewDirectory to the element searchStories
When Web user clicks the element createNewDirectory
Then Web element text:myNewDirectory appears

Examples:
