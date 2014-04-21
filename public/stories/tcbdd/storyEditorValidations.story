


Meta:


Narrative:
In order to have valid stories
As a application user
I want to have my entries validated

GivenStories: 

Lifecycle:

Before:
Given Web user is in the View Story page
Then Web element invalidForm is NOT visible

After:



Scenario: Name value is required

Meta:


When Web user clears the element storyName
Then Web element invalidForm is visible

Examples:

Scenario: Story meta, given story and lifecycle step values are required

Meta:


When Web user clicks the element <addElement>
When Web user sets value some value to the element <element>
When Web user clears the element <element>
Then Web element invalidForm is visible

Examples:
|addElement|element|
|addStoryMeta|storyMeta1|
|addStoryGivenStory|storyGivenStory1|
|addStoryLifecycleBeforeStep|storyLifecycleBeforeStep1|
|addStoryLifecycleAfterStep|storyLifecycleAfterStep1|

Scenario: Scenario meta value is required

Meta:


When Web user clicks the element scenario1Panel
When Web user clicks the element <addElement>
When Web user sets value <value> to the element <element>
When Web user clears the element <element>
Then Web element invalidForm is visible

Examples:
|addElement|value|element|
|addScenario1Meta|myMeta|scenario1Meta1|
|addScenario1Step|Given something|scenario1Step1|
