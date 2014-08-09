


Meta:


Narrative:
In order to produce video for the site
As a BDD Assistant contributor
I want to have scenarios that can be recorded

GivenStories: 

Lifecycle:

Before:


After:



Scenario: loginStory

Meta:


Given Web user is in the New Story screen
When Web user sets value login to the element storyName
When Web user clicks the element storyPanel
When Web user clicks the element storyNarrativePanel
When Web user sets value be able to provide personalized experience to the element storyNarrativeInOrderTo
When Web user sets value application user to the element storyNarrativeAsA
When Web user sets value be able to identify myself to the element storyNarrativeIWantTo
When Web user clicks the element storyLifecyclePanel
When Web user clicks the element addStoryLifecycleBeforeStep
When Web user sets value Given Web user is in the Login screen to the element storyLifecycleBeforeStep1

Examples:

Scenario: loginScenario1

Meta:


When Web user clicks the element addScenario
When Web user sets value Should have all fields to the element scenario1Title
When Web user clicks the element addScenario1Step
When Web user sets value Then Web element username is visible to the element scenario1Step1
When Web user clicks the element addScenario1Step
When Web user sets value Then Web element password is visible to the element scenario1Step2
When Web user clicks the element addScenario1Step
When Web user sets value Then Web element login is visible to the element scenario1Step3

Examples:

Scenario: loginScenario2

Meta:


When Web user clicks the element addScenario
When Web user sets value Should have username required to the element scenario2Title
When Web user clicks the element addScenario2Step
When Web user sets value When Web user sets value myPassword to the element password to the element scenario2Step1
When Web user clicks the element addScenario2Step
When Web user sets value Then Web element login is disabled to the element scenario2Step2

Examples:

Scenario: loginScenario3

Meta:


When Web user clicks the element addScenario
When Web user sets value Should have password required to the element scenario3Title
When Web user clicks the element addScenario3Step
When Web user sets value When Web user sets value myUsername to the element username to the element scenario3Step1
When Web user clicks the element addScenario3Step
When Web user sets value Then Web element login is disabled to the element scenario3Step2

Examples:

Scenario: loginScenario4

Meta:


When Web user clicks the element addScenario
When Web user sets value Should have password required to the element scenario4Title
When Web user clicks the element addScenario4Step
When Web user sets value Given Web user myUsername with password myPassword exists to the element scenario4Step1
When Web user clicks the element addScenario4Step
When Web user sets value When Web user myUsername logs in with password myPassword to the element scenario4Step2
When Web user clicks the element addScenario4Step
When Web user sets value Then Web user is in the Welcome screen to the element scenario4Step3

Examples:

Scenario: loginScenario5

Meta:


When Web user clicks the element addScenario
When Web user sets value Should display error message when user does not exist to the element scenario5Title
When Web user clicks the element addScenario5Step
When Web user sets value Given Web user myUsername does NOT exists to the element scenario5Step1
When Web user clicks the element addScenario5Step
When Web user sets value When Web user myUsername logs in with password myPassword to the element scenario5Step2
When Web user clicks the element addScenario5Step
When Web user sets value Then Web element message should have text Specified username and/or password is incorrect to the element scenario5Step3

Examples:

Scenario: loginRunner

Meta:


When Web user clicks the element saveStory
When Web user clicks the element storyPanel
When Web user clicks the element scenario1Panel
When Web user clicks the element scenario2Panel
When Web user clicks the element scenario3Panel
When Web user clicks the element scenario4Panel
When Web user clicks the element scenario5Panel
When Web user clicks the element runStory
When Web user sets value 4 to the element fileStepsTimeout
When Web user sets value 1024,700 to the element webStepsWidthHeight
When Web user sets value http://localhost:9000/ to the element webStepsUrl
When Web user selects Google Chrome from the dropdown list webStepsBrowserOption
When Web user sets value 4 to the element webStepsTimeout
When Web user clicks the element confirmRunStory

Examples:

Scenario: loginReport

Meta:


Given Web timeout is 60 seconds
Then Web element runnerStatus should have text Story run was successful
Then Web element runnerStatus should have text Story run failed

Examples:
