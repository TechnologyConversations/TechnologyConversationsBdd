


Meta:


Narrative:
In order to perform regression
As a application user
I want to run all selected stories

GivenStories: 

Lifecycle:

Before:
Given File /public/stories/testDirectory/testSubDirectory exists
When File is copied from public/stories/test/dummy.story to public/stories/testDirectory/testStory.story
When File is copied from public/stories/test/dummyPending.story to public/stories/testDirectory/testPendingStory.story
Given Web home page is opened
When Web user clicks the element runner
When Web user clicks the element text:testDirectory

After:



Scenario: Should display directories with selectors

Meta:


Then Web element directory1 is visible
Then Web element directory1Selector is visible

Examples:

Scenario: Should display stories with selectors

Meta:


Then Web element story1 is visible
Then Web element story1Selector is visible

Examples:

Scenario: Should display REST interface for remote calls

Meta:


When Web user clicks the element directory1Selector
When Web user clicks the element file1Selector
When Web user clicks the element file2Selector
Then Web element rest should have text CHANGE_THIS_TO_THE_REAL_JSON

Examples:

Scenario: Can run selected stories

Meta:


When Web user clicks the element file1Selector
When Web user clicks the element runStory
When Web user clicks the element confirmRunStory
Then story is running

Examples:

Scenario: Should display runner reports

Meta:


When Web user clicks the element file1Selector
When Web user clicks the element runStory
When Web user clicks the element confirmRunStory
Then Web element runnerReports is visible

Examples:
