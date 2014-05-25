


Meta:


Narrative:
In order to work on multiple segments (stories, components, etc) in parallel
As a application user
I want to have history

GivenStories: 

Lifecycle:

Before:
Given File public/stories/testDirectory/historySpecs.story does NOT exist
Given Web home page is opened
Given Directory public/stories/testDirectory/historyStory.story does NOT exist

After:



Scenario: Create new story does NOT add a new history item

Meta:


Given Web user is in the New Story screen
When Web user clicks the element history
Then Web element historyItem1 is NOT visible

Examples:

Scenario: Open an existing story adds a new history item

Meta:


Given Web user is in the View Story page
When Web user clicks the element history
Then Web element historyItem1 should have text @storyName story

Examples:

Scenario: Save new story adds a new history item

Meta:


Given Web user is in the New Story screen
When Web user sets value historySpecs to the element storyName
When Web user clicks the element saveStory
When Web user clicks the element history
Then Web element historyItem1 should have text historySpecs story

Examples:

Scenario: Create new composites class does NOT add a new history item

Meta:


Given Web user is in the New Composites screen
When Web user clicks the element history
Then Web element historyItem1 is NOT visible

Examples:

Scenario: Open an existing composites class adds a new history item

Meta:


Given Web user is in the View Composites screen
When Web user clicks the element history
Then Web element historyItem1 should have text @className composites

Examples:

Scenario: Save new composites class adds a new history item

Meta:


Given Web user is in the New Composites screen
When Web user sets value historyComposites to the element className
When Web user clicks the element saveComposites
When Web user clicks the element history
Then Web element historyItem1 should have text historyComposites composites

Examples:

Scenario: Users can switch from one history item to another

Meta:


Given Web user is in the View Story page
Given Web user is in the View Composites screen
When Web user clicks the element history
When Web user clicks the element historyItem1
Then Web user is in the View Composites screen

Examples:

Scenario: Duplicated history items are not allowed

Meta:


Given Web user is in the View Story page
Given Web user is in the New Story screen
Given Web user is in the View Story page
When Web user clicks the element history
Then Web element historyItem2 is NOT visible

Examples:
