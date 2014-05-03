


Meta:


Narrative:
In order to have more understandable scenarios
As a application user
I want to be able to create composite steps

GivenStories: 

Lifecycle:

Before:
Given Web user is in the View Composites screen

After:



Scenario: Can create new composites

Meta:


Given Web user is in the New Composites screen
Then File app/composites/com/technologyconversations/bdd/steps/@className.java does NOT exist
When Web user clicks the element saveComposites
Then File app/composites/com/technologyconversations/bdd/steps/@className.java exists

Examples:

Scenario: Can revert changes

Meta:


When Web user sets value Given I can modify the composite to the element compositeText
When Web user clicks the element revertComposites
Then Web element className should NOT have text Given I can modify the composite

Examples:

Scenario: Can delete composites class

Meta:


When Web user clicks the element deleteComposites
When Web user clicks the element ok
Then File app/composites/com/technologyconversations/bdd/steps/@className.java does NOT exist

Examples:

Scenario: Can update existing composites

Meta:


When Web user sets value Given I can update composites to the element compositeText

Examples:

Scenario: Can NOT revert changes when composites are unchanged

Meta:


Then Web element revertComposites is disabled

Examples:

Scenario: Can NOT delete new composites class before it is saved for the first time

Meta:


Given Web user is in the New Composites screen
Then Web element deleteComposites is NOT visible

Examples:

Scenario: Can NOT save composites when there are validation errors

Meta:


When Web user clears the element compositeText
Then Web element saveComposites is disabled

Examples:

Scenario: Can NOT save when composites are unchanged

Meta:


Then Web element saveComposites is disabled

Examples:

Scenario: Can not modify the name of an existing composites class

Meta:


Given Web user is in the View Composites screen
Then Web element className is disabled

Examples:
