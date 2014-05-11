


Meta:


Narrative:
In order to work on multiple segments (stories, components, etc) in parallel
As a application user
I want to have tabs

GivenStories: 

Lifecycle:

Before:
Given File public/stories/testDirectory/tabsSpecs.story does NOT exist
Given Web home page is opened
Then Web element tab1 is NOT visible

After:



Scenario: Create new story adds a new tab

Meta:


Given Web user is in the New Story page
Then Web element tab1 should have text New testDirectory story

Examples:

Scenario: Open an existing story adds a new tab

Meta:


Given Web user is in the View Story page
Then Web page title should have text @storyName story

Examples:

Scenario: Save new story changes reference of the existing tab

Meta:


Given Web user is in the New Story page
When Web user sets value tabsSpecs to the element storyName
When Web user clicks the element saveStory
Then Web element tab1 should have text tabsSpecs story

Examples:

Scenario: Create new composites class adds a new tab

Meta:


Given Web user is in the New Composites screen
Then Web element tab1 should have text New composites

Examples:

Scenario: Open an existing composites class adds a new tab

Meta:


Given Web user is in the View Composites screen
Then Web element tab1 should have text @className composites

Examples:

Scenario: Open pending composite from the story runner adds a new tab

Meta:


Given Web user is in the View Story page
When Web user clicks the element runStory
When Web user clicks the element pendingStep1
Then Web element tab1 should have text @className composites

Examples:

Scenario: Each tab can be closed

Meta:


Given Web user is in the View Story page
When Web user clicks the element closeTab1
Then Web element tab1 is NOT visible

Examples:

Scenario: Tabs are restored whenever the application is opened

Meta:


Given Web user is in the View Story page
Given Web user is in the View Composites screen
Given Web home page is opened
Given Web page is refreshed
Then Web element tab1 should have text @storyName
Then Web element tab2 should have text @className

Examples:

Scenario: Save new composites class changes reference of the existing tab

Meta:


Given Web user is in the New Composites screen
When Web user sets value tabsComposites to the element className
When Web user clicks the element saveComposites
Then Web element tab1 should have text tabsComposites composites

Examples:

Scenario: All tabs can be closed at once

Meta:


Given Web user is in the View Story page
Given Web user is in the View Composites screen
When Web user clicks the element closeAllTabs
Then Web element tab1 is NOT visible

Examples:

Scenario: Users can switch from one tab to another

Meta:


Given Web user is in the View Composites screen
Given Web user is in the View Story page
When Web user clicks the element tab1
Then Web user is in the View Composites screen

Examples:
