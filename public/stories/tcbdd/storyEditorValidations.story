


Meta:


Narrative:
In order to have valid stories
As a application user
I want to have my entries validated

GivenStories: 

Lifecycle:

Before:
When File is copied from public/stories/tcbdd/storiesList.story to public/stories/myStory.story
Given Web user is in the View Story page
Then Web element invalidForm is NOT visible

After:
Given File public/stories/myStory.story does NOT exist


Scenario: Name value is required

Meta:


When Web user clears the element storyName
Then Web element invalidForm is visible

Examples:

Scenario: Story meta, given story and lifecycle step values are required

Meta:


When Web user clicks the element storyPanel
When Web user clicks the element <panel>
When Web user clicks the element <addElement>
When Web user sets value some value to the element <element>
When Web user clears the element <element>
Then Web element invalidForm is visible

Examples:
|panel|addElement|element|
|storyMetaPanel|addStoryMeta|storyMeta1|
|storyGivenStoriesPanel|addStoryGivenStory|storyGivenStory1|
|storyLifecyclePanel|addStoryLifecycleBeforeStep|storyLifecycleBeforeStep1|
|storyLifecyclePanel|addStoryLifecycleAfterStep|storyLifecycleAfterStep1|

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

Scenario: Lifecycle steps must start with Given, When or Then

Meta:


When Web user clicks the element storyPanel
When Web user clicks the element storyLifecyclePanel
When Web user sets value Not given, when or then to the element <element>
Then Web element invalidForm is visible

Examples:
|element|
|storyLifecycleBeforeStep1|
|storyLifecycleAfterStep1|

Scenario: Scenario steps must start with Given, When or Then

Meta:


When Web user clicks the element scenario1Panel
When Web user sets value Not given, when or then to the element scenario1Step1
Then Web element invalidForm is visible

Examples:
