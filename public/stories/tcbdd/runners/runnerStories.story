


Meta:


Narrative:
In order to perform regression
As a application user
I want to run all selected stories

GivenStories: 

Lifecycle:

Before:
Given Directory public/stories/testDirectory/testSubDirectory exists
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

Scenario: Can cancel the dialog

Meta:


When Web user clicks the element cancelRunnerSelector
Then Web element modalHeader is NOT visible

Examples:

Scenario: Can move to the parameters dialog

Meta:


When Web user clicks the element story1Selector
When Web user clicks the element okRunnerSelector
Then Web element modalHeader should have text Runner Parameters

Examples:

Scenario: Can run selected stories

Meta:


When Web user clicks the element story1Selector
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

Scenario: Can go back to the parent directory

Meta:


Given step is written

Examples:

Scenario: Should display the current path

Meta:


Then Web element path should have text testDirectory

Examples:

Scenario: Should display REST interface for remote calls

Meta:


When Web user clicks the element directory1Selector
When Web user clicks the element file1Selector
When Web user clicks the element file2Selector
Then Web element rest should have text CHANGE_THIS_TO_THE_REAL_JSON

Examples:

Scenario: Should populate the selector modal with data from the last run

Meta:


Given steps are written

Examples:

Scenario: Can NOT move to parameters dialog without at least one directory or story selected

Meta:


Given steps are written

Examples:

Scenario: Runner Parameters confirm button should have text Run Stories

Meta:


Given steps are written

Examples:
