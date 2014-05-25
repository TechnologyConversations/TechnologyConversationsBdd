


Meta:


Narrative:
In order to create and modify stories
As a application user
I want to be able to add and modify story information through the form

GivenStories: 

Lifecycle:

Before:
Given Web user is in the New Story screen
When Web user clicks the element expandPanels

After:



Scenario: Has name field

Meta:


Then Web element storyName is visible

Examples:

Scenario: Has description field

Meta:


Then Web element storyDescription is visible

Examples:

Scenario: Has meta fields

Meta:


When Web user clicks the element addStoryMeta
Then Web element storyMeta1 is visible

Examples:

Scenario: Has narrative fields

Meta:


Then Web element storyNarrativeInOrderTo is visible
Then Web element storyNarrativeAsA is visible
Then Web element storyNarrativeIWantTo is visible

Examples:

Scenario: Has given stories

Meta:


When Web user clicks the element addStoryGivenStory
Then Web element storyGivenStory1 is visible

Examples:

Scenario: Has lifecycle fields

Meta:


When Web user clicks the element addStoryLifecycleBeforeStep
Then Web element storyLifecycleBeforeStep1 is visible
When Web user clicks the element addStoryLifecycleAfterStep
Then Web element storyLifecycleAfterStep1 is visible

Examples:

Scenario: Has scenario title

Meta:


When Web user clicks the element addScenario
When Web user clicks the element scenario1Panel
Then Web element scenario1Title is visible

Examples:

Scenario: Has scenario meta

Meta:


When Web user clicks the element addScenario
When Web user clicks the element scenario1Panel
When Web user clicks the element addScenario1Meta
Then Web element scenario1Meta1 is visible

Examples:

Scenario: Has scenario steps

Meta:


When Web user clicks the element addScenario
When Web user clicks the element scenario1Panel
When Web user clicks the element addScenario1Step
Then Web element scenario1Step1 is visible

Examples:

Scenario: Has scenario examples

Meta:


When Web user clicks the element addScenario
When Web user clicks the element scenario1Panel
Then Web element scenario1Examples is visible

Examples:

Scenario: Can add, move and remove story elements

Meta:


When Web user clicks the element <addButton>
When Web user clicks the element <addButton>
When Web user sets value this is value 1 to the element <element1>
When Web user sets value this is value 2 to the element <element2>
When Web user drags the element <dragButton2> to the <dragButton1>
Then Web element <element1> should have value this is value 2
When Web user clicks the element <removeButton1>
Then Web element <element2> is NOT visible
When Web user clicks the element <removeButton1>
Then Web element <element1> is NOT visible

Examples:
|addButton|element1|element2|dragButton1|dragButton2|removeButton1|
|addStoryMeta|storyMeta1|storyMeta2|dragStoryMeta1|dragStoryMeta2|removeStoryMeta1|
|addStoryGivenStory|storyGivenStory1|storyGivenStory2|dragStoryGivenStory1|dragStoryGivenStory2|removeStoryGivenStory1|
|addStoryLifecycleBeforeStep|storyLifecycleBeforeStep1|storyLifecycleBeforeStep2|dragStoryLifecycleBeforeStep1|dragStoryLifecycleBeforeStep2|removeStoryLifecycleBeforeStep1|
|addStoryLifecycleAfterStep|storyLifecycleAfterStep1|storyLifecycleAfterStep2|dragStoryLifecycleAfterStep1|dragStoryLifecycleAfterStep2|removeStoryLifecycleAfterStep1|

Scenario: Can add, move and remove scenario steps

Meta:


When Web user clicks the element addScenario
When Web user clicks the element scenario1Panel
When Web user clicks the element addScenario1Step
When Web user sets value When I drag and drop to the element scenario1Step1
When Web user clicks the element addScenario1Step
When Web user sets value Then everything is just great to the element scenario1Step2
When Web user drags the element dragScenario1Step2 to the dragScenario1Step1
Then Web element scenario1Step1 should have value Then everything is just great
When Web user clicks the element removeScenario1Step1
Then Web element scenario1Step2 is NOT visible
When Web user clicks the element removeScenario1Step1
Then Web element scenario1Step1 is NOT visible

Examples:

Scenario: Steps have auto-complete function

Meta:


When Web user clicks the element addScenario
When Web user clicks the element scenario1Panel
When Web user clicks the element addScenario1Step
When Web user sets value Then variable to the element scenario1Step1
When Web user presses the enter key in the element scenario1Step1
Then Web element scenario1Step1 should have value Then variable $key has value $value

Examples:

Scenario: Can add steps using the ENTER key

Meta:


When Web user clicks the element addScenario
When Web user clicks the element scenario1Panel
When Web user clicks the element addScenario1Step
When Web user presses the enter key in the element scenario1Step1
Then Web element scenario1Step2 is visible

Examples:
