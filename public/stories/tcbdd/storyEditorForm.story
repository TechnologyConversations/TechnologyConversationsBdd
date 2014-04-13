


Meta:


Narrative:
In order to create and modify stories
As a application user
I want to be able to put story information to the form and save it

GivenStories: 

Lifecycle:

Before:
Given Web user is in the View Story page

After:



Scenario: Has name field

Meta:


Then Web element storyName is visible

Examples:

Scenario: Has description field

Meta:


When Web user clicks the element storyPanel
When Web user clicks the element storyDescriptionPanel
Then Web element storyDescription is visible

Examples:

Scenario: Has meta fields

Meta:


When Web user clicks the element storyPanel
When Web user clicks the element storyMetaPanel
When Web user clicks the element addStoryMeta
Then Web element storyMeta1 is visible

Examples:

Scenario: Has narrative fields

Meta:


When Web user clicks the element storyPanel
When Web user clicks the element storyNarrativePanel
Then Web element storyNarrativeInOrderTo is visible
Then Web element storyNarrativeAsA is visible
Then Web element storyNarrativeIWantTo is visible

Examples:

Scenario: Has given stories

Meta:


When Web user clicks the element storyPanel
When Web user clicks the element storyGivenStoriesPanel
When Web user clicks the element addStoryGivenStory
Then Web element storyGivenStory1 is visible

Examples:

Scenario: Has lifecycle fields

Meta:


When Web user clicks the element storyPanel
When Web user clicks the element storyLifecyclePanel
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

Scenario: Has validations

Meta:




Examples:
