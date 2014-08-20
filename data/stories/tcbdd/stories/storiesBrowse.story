


Meta:
@dummy

Narrative:
In order to open existing and create new stories
As a application user
I want to be able to browse files and directories

GivenStories: 

Lifecycle:

Before:
Given Directory data/stories/testDirectory exists
When File is copied from data/stories/test/dummy.story to data/stories/testDirectory/testStory.story

After:
Given Directory data/stories/newTestDirectory does NOT exist


Scenario: Can open dialog

Meta:


Given Web home page is opened
When Web user clicks the element browseStories
Then Web user is in the stories modal screen

Examples:

Scenario: Can navigate through directories

Meta:


Given Web user is in the Browse Stories dialog
When Web user clicks the element text:testDirectory
Then Web element path should have text testDirectory

Examples:

Scenario: Can display stories

Meta:


Given Web user is in the Browse Stories dialog
When Web user clicks the element text:testDirectory
Then Web element text:testStory exists

Examples:

Scenario: Can open story

Meta:


Given Web user is in the Browse Stories dialog
When Web user clicks the element text:testDirectory
When Web user clicks the element text:testStory
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
When Web user clicks the element text:testDirectory
When Web user clicks the element deletetestStory
When Web user clicks the element ok
Then Web element text:testStory disappeared

Examples:

Scenario: Can create new directory

Meta:


Given Web user is in the Browse Stories dialog
When Web user sets value newTestDirectory to the element searchStories
When Web user clicks the element createNewDirectory
Then Web element text:newTestDirectory appears

Examples:
