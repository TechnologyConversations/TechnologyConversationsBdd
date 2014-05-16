


Meta:


Narrative:
In order to have more understandable scenarios
As a application user
I want to be able to create composite steps

GivenStories: 

Lifecycle:

Before:
Given Web home page is opened
When Web user clicks the element browseComposites

After:



Scenario: Can create new composites class

Meta:


When Web user sets value TestComposites to the element className
When Web user clicks the element createNewClass
Then Web user is in the New Composites screen

Examples:

Scenario: Can open an existing composites class

Meta:


When Web user clicks the element text:TcBddComposites
Then Web user is in the View Composites screen

Examples:

Scenario: Can filter composites classes

Meta:


When Web user sets value TcBddComposites to the element className
Then Web element text:TcBddComposites is visible
When Web user sets value NonExistentComposites to the element className
Then Web element text:TcBddComposites is NOT visible

Examples:

Scenario: Can close the composites modal screen

Meta:


Then Web user is in the composites modal screen
When Web user clicks the element closeComposites
Then Web user is in the home screen

Examples:

Scenario: Must have valid composites class name (cannot start with a number, cannot have space character)

Meta:


When Web user sets value <className> to the element className
Then Web element compositeClassError is visible

Examples:
|className|
|1IsNotAGoodStart|
|space is not allowed|
