


Meta:


Narrative:
In order to 
As a 
I want to 

GivenStories: 

Lifecycle:

Before:
Given File public/stories/testDirectory/testStory.story does NOT exist

After:



Scenario: Can save new story

Meta:


Given Web user is in the New Story page
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
Then Web story is renamed

Examples:

Scenario: Can add, delete and move story elements

Meta:




Examples:

Scenario: Can NOT save story when data is intact

Meta:




Examples:

Scenario: Can NOT run story if previous instance is still running

Meta:




Examples:

Scenario: Can NOT revert changes is data is intact

Meta:




Examples:

Scenario: Can NOT delete new story before it is saved

Meta:




Examples:

Scenario: Can NOT rename story into an existing one

Meta:




Examples:
