


Meta:


Narrative:
In order to perform regression
As a application user
I want to run all selected stories

GivenStories: 

Lifecycle:

Before:
Given Directory public/stories/testDirectory/testSubDirectory exists
When File is copied from public/stories/test/dummySuccess.story to public/stories/testDirectory/1dummy.story
When File is copied from public/stories/test/dummyPending.story to public/stories/testDirectory/testPendingStory.story
Given Web home page is opened
Given Web page is refreshed
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
When Web user clicks the element okRunnerSelector
When Web user clicks the element confirmRunStory
Then story is running
Then story is NOT running

Examples:

Scenario: Should display runner reports link

Meta:


When Web user clicks the element story1Selector
When Web user clicks the element okRunnerSelector
When Web user clicks the element confirmRunStory
Given Web timeout is 30 seconds
Then Web element runnerReports is visible

Examples:

Scenario: Should display the current path

Meta:


Then Web element path should have text testDirectory

Examples:

Scenario: Can go back to the parent directory

Meta:


When Web user clicks the element text:testSubDirectory
When Web user clicks the element prevDirectory
Then Web element path should have text testDirectory

Examples:

Scenario: Can NOT move to parameters dialog without at least one directory or story selected

Meta:


Then Web element okRunnerSelector is disabled

Examples:

Scenario: Runner Parameters confirm button should have text Run

Meta:


When Web user clicks the element story1Selector
When Web user clicks the element okRunnerSelector
Then Web element confirmRunStory should have text Run

Examples:

Scenario: Multiple runners can be executed one after another

Meta:


When Web user clicks the element cancelRunnerSelector
When Web user clicks the element runner
Then Web user is in the Runner Selector modal screen

Examples:

Scenario: Parameters dialog should have API info

Meta:


When Web user clicks the element story1Selector
When Web user clicks the element okRunnerSelector
When Web user clicks the element getApi
Then Web element api is visible

Examples:
