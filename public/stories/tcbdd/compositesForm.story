


Meta:


Narrative:
In order to have more understandable scenarios
As a application user
I want to be able to create composite steps

GivenStories: 

Lifecycle:

Before:
Given File app/composites/com/technologyconversations/bdd/steps/TestComposites.java does NOT exist
Given Web user is in the New Composites screen

After:



Scenario: Can display the name of the selected composite class

Meta:


Then Web element className should have value TestComposites

Examples:

Scenario: Can create new composite

Meta:


When Web user clicks the element addNewComposite
When Web user sets value Given I can create new composite to the element compositeText
Then Web element composites should have text Given I can create new composite

Examples:

Scenario: Can list composites

Meta:


Then Web element composites should have text @compositeText

Examples:

Scenario: Can filter composites

Meta:


When Web user sets value Given this composite does NOT exist to the element compositeTextFilter
Then Web element text:@compositeText is NOT visible
When Web user sets value @compositeText to the element compositeTextFilter
Then Web element text:@compositeText exists

Examples:

Scenario: Can edit composite class name

Meta:


When Web user sets value ThisIsNewCompositesClass to the element className
Then Web element className should have value ThisIsNewCompositesClass

Examples:

Scenario: Can edit composite name

Meta:


When Web user clicks the element text:@compositeText
When Web user sets value When composite text is changed to the element compositeText
Then Web element composites should have text When composite text is changed

Examples:

Scenario: Can add, move and remove composite steps

Meta:


When Web user clicks the element addNewComposite
When Web user clicks the element addNewCompositeStep
Then Web element compositeStep1 exists
When Web user sets value When I set the first composite step to the element compositeStep1
When Web user clicks the element addNewCompositeStep
Then Web element compositeStep2 exists
When Web user drags the element dragCompositeStep2 to the dragCompositeStep1
When Web user sets value When I set the second composite step to the element compositeStep2
Then Web element compositeStep1 should have value When I set the second composite step
When Web user clicks the element removeCompositeStep2
Then Web element compositeStep2 is NOT visible
When Web user clicks the element removeCompositeStep1
Then Web element compositeStep1 is NOT visible

Examples:

Scenario: Can edit composite steps

Meta:


When Web user clicks the element composite1
Then Web element compositeStep1 is enabled

Examples:

Scenario: Can delete composite

Meta:


When Web user clicks the element deleteComposite1
Then Web element composite1 is NOT visible

Examples:

Scenario: Can use code-complete on composite steps

Meta:


When Web user clicks the element addNewComposite
When Web user clicks the element addNewCompositeStep
When Web user sets value Then variable to the element compositeStep1
When Web user presses the enter key in the element compositeStep1
Then Web element compositeStep1 should have value Then variable $key has value $value

Examples:
