


Meta:


Narrative:
In order to 
As a 
I want to 

GivenStories: 

Lifecycle:

Before:
Given File data/stories/testDirectory/testStory.story does NOT exist
Given File data/stories/testDirectory/storyNameRenamed.story does NOT exist

After:



Scenario: Can save new story

Meta:


Given Web user is in the New Story screen
When Web user clicks the element expandPanels
When Web user fills in the new story form
When Web user adds scenario to the story form
When Web user clicks the element saveStory
Then Web story is saved

Examples:

Scenario: Can update an existing story

Meta:


Given Web user is in the View Story page
When Web user fills in the existing story form
When Web user clicks the element saveStory
Then Web story is saved

Examples:

Scenario: Can revert changes

Meta:


Given Web user is in the View Story page
When Web user fills in the existing story form
When Web user clicks the element revertStory
Then Web story is reverted to the original version

Examples:

Scenario: Can delete story

Meta:


Given Web user is in the View Story page
When Web user clicks the element deleteStory
When Web user clicks the element ok
Then Web story is deleted

Examples:

Scenario: Can rename story

Meta:


Given Web user is in the View Story page
When Web user sets value storyNameRenamed to the element storyName
When Web user clicks the element saveStory
Then Web story is renamed

Examples:

Scenario: Can NOT save story when data is intact

Meta:


Given Web user is in the View Story page
Then Web element saveStory is disabled

Examples:

Scenario: Can NOT revert changes is data is intact

Meta:


Given Web user is in the View Story page
Then Web element revertStory is disabled

Examples:

Scenario: Can NOT delete new story before it is saved

Meta:


Given Web user is in the New Story screen
Then Web element deleteStory is disabled

Examples:
