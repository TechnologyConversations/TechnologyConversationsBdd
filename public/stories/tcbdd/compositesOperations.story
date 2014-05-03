


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
When Web user clicks the element saveComposites
Then Web composites are saved

Examples:

Scenario: Can revert changes

Meta:


When Web user sets value ThisIsNewCompositesClass to the element className
When Web user clicks the element reverComposites
Then Web element className should NOT have text ThisIsNewCompositesClass

Examples:

Scenario: Can delete composites class

Meta:


When Web user clicks the element deleteComposites
Then Web composites are deleted

Examples:

Scenario: Created composites use BddVariable as param type

Meta:


When Web user sets value Given I can use parameter <myParam> to the element compositeText
When Web user clicks the element saveComposites
Then Web composites have BddVariable myParam

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


When Web user clears the element className
Then Web element saveComposites is disabled

Examples:

Scenario: Can NOT save when composites are unchanged

Meta:


Then Web element saveComposites is disabled

Examples:

Scenario: Can rename existing composites class

Meta:


When Web user sets value TestCompositesRenamed to the element className
When Web user clicks the element saveComposites

Examples:
