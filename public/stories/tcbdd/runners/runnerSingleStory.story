


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
When Web single story runner is run
Then story is running

Examples:

Scenario: Can NOT run multiple stories

Meta:


Given Web user is in the Story page with all successful steps
When Web single story runner is run
Then Web element runStory is disabled

Examples:

Scenario: Has progress bar

Meta:


Given Web user is in the Story page with all successful steps
When Web single story runner is run
Then Web element runnerStatus should have text Story run is in progress

Examples:

Scenario: Has parameters dialog

Meta:


Given Web user is in the Story page with all successful steps
When Web user clicks the element runStory
Then Web element modalHeader is visible

Examples:

Scenario: Can retrieve values of last parameters used

Meta:


Given Web user is in the Story page with all successful steps
When Web user clicks the element runStory
When Web user selects Phantom JS from the dropdown list webStepsBrowserOption
When Web user sets value http://localhost:9000 to the element webStepsUrl
When Web user clicks the element confirmRunStory
Given Web timeout is 30 seconds
Then Web element runnerStatus should have text Story run was successful
When Web user clicks the element runStory
Then Web dropdown list webStepsBrowserOption has Phantom JS selected
Then Web element webStepsUrl should have value http://localhost:9000

Examples:

Scenario: Can display successful status

Meta:


Given Web user is in the Story page with all successful steps
When Web single story runner is run
Then Web element runnerStatus should have text Story run was successful

Examples:

Scenario: Can display the list of pending steps

Meta:


Given Web user is in the Story screen with pending steps
When Web single story runner is run
Then Web element pendingSteps is visible
Then Web element pendingStep1 is visible

Examples:

Scenario: Can display runner reports

Meta:


Given Web user is in the Story page with all successful steps
When Web single story runner is run
Then Web element runnerReports is visible

Examples:

Scenario: Can cancel story runner from the parameters dialog

Meta:


Given Web user is in the Story page with all successful steps
When Web user clicks the element runStory
When Web user clicks the element cancelRunStory
Then Web element modalHeader is NOT visible

Examples:

Scenario: Can display failed status

Meta:


Given Web user is in the Story page with failed steps
When Web single story runner is run
Then Web element runnerStatus should have text Story run failed

Examples:

Scenario: Can display pending status

Meta:


Given Web user is in the Story screen with pending steps
When Web single story runner is run
Then Web element runnerStatus should have text Story run was successful with 1 pending steps

Examples:

Scenario: Pending steps are links to composites page

Meta:


Given Web user is in the Story screen with pending steps
When Web single story runner is run
When Web user clicks the element pendingStep1
Then Web user is in the composites modal screen

Examples:

Scenario: Can choose which browser to run from a dropdown list

Meta:


Given Web user is in the Story page with all successful steps
When Web user clicks the element runStory
Then Web dropdown list browser contains the option firefox
Then Web dropdown list browser contains the option chrome
Then Web dropdown list browser contains the option htmlunit
Then Web dropdown list browser contains the option opera
Then Web dropdown list browser contains the option phantomjs
Then Web dropdown list browser has firefox selected

Examples:
