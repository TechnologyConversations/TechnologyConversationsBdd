


Meta:


Narrative:
In order to verify behaviours
As a application user
I want to be able to run story and see results

GivenStories: 

Lifecycle:

Before:


After:



Scenario: Can run story

Meta:


Given Web user is in the Story page with all successful steps
When Web user clicks the element runStory
When Web user clicks the element confirmRunStory
Then story is running

Examples:

Scenario: Can NOT run multiple stories

Meta:


Given Web user is in the Story page with all successful steps
When Web user clicks the element runStory
When Web user clicks the element confirmRunStory
Then Web element runStory is disabled

Examples:

Scenario: Has progress bar

Meta:


Given Web user is in the Story page with all successful steps
When Web user clicks the element runStory
When Web user clicks the element confirmRunStory
Then Web element runnerStatus should have text Story run is in progress

Examples:

Scenario: Has parameters dialog

Meta:


Given Web user is in the Story page with all successful steps
When Web user clicks the element runStory
Then Web element runnerParams is visible

Examples:

Scenario: Can retrieve values of last parameters used

Meta:


Given Web user is in the Story page with all successful steps
When Web user clicks the element runStory
When Web user sets value phantomjs to the element webStepsBrowser
When Web user clicks the element confirmRunStory
Then Web element runnerStatus should have text Story run was successful
When Web user clicks the element runStory
Then Web element webStepsBrowser should have value phantomjs

Examples:

Scenario: Can display successful status

Meta:


Given Web user is in the Story page with all successful steps
When Web user clicks the element runStory
When Web user clicks the element confirmRunStory
Then Web element runnerStatus should have text Story run was successful

Examples:

Scenario: Can display the list of pending steps

Meta:


Given Web user is in the Story page with pending steps
When Web user clicks the element runStory
When Web user clicks the element confirmRunStory
Then Web element pendingSteps is visible
Then Web element pendingStep1 is visible

Examples:

Scenario: Can display runner reports

Meta:


Given Web user is in the Story page with all successful steps
When Web user clicks the element runStory
When Web user clicks the element confirmRunStory
Then Web element runnerReports is visible

Examples:

Scenario: Can cancel story runner from the parameters dialog

Meta:


Given Web user is in the Story page with all successful steps
When Web user clicks the element runStory
When Web user clicks the element cancelRunStory
Then Web element runnerParams is NOT visible

Examples:

Scenario: Can display failed status

Meta:


Given Web user is in the Story page with failed steps
When Web user clicks the element runStory
When Web user clicks the element confirmRunStory
Then Web element runnerStatus should have text Story run failed

Examples:

Scenario: Can display pending status

Meta:


Given Web user is in the Story page with pending steps
When Web user clicks the element runStory
When Web user clicks the element confirmRunStory
Then Web element runnerStatus should have text Story run was successful with 1 pending steps

Examples:

Scenario: Pending steps are links to composites page

Meta:


Given Web user is in the Story page with pending steps
When Web user clicks the element runStory
When Web user clicks the element confirmRunStory
When Web user clicks the element pendingStep1
Then Web user is in the composites modal screen

Examples:
