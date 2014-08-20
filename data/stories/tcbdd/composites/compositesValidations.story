


Meta:


Narrative:
In order to have more understandable scenarios
As a application user
I want to be able to create composite steps

GivenStories: 

Lifecycle:

Before:
Given Web user is in the New Composites screen

After:



Scenario: Composites class name is required

Meta:


When Web user clears the element className
Then Web element saveComposites is disabled

Examples:

Scenario: Composite is required

Meta:


When Web user clears the element compositeText
Then Web element saveComposites is disabled

Examples:

Scenario: Composite must start with Given, When or Then

Meta:


When Web user sets value This text does not start with Given, When or Then to the element compositeText
Then Web element compositeTextError should have text Step must start with Given, When or Then words.
Then Web element saveComposites is disabled

Examples:

Scenario: Composite step is required

Meta:


When Web user clears the element compositeStep1
Then Web element saveComposites is disabled

Examples:

Scenario: Composite step must start with Given, When or Then

Meta:


When Web user sets value This text does not start with Given, When or Then to the element compositeStep1
Then Web element compositeStepsError should have text All steps must start with Given, When or Then words.
Then Web element saveComposites is disabled

Examples:

Scenario: Composite must use valid parameters naming

Meta:


When Web user sets value <step> to the element compositeText
When Web user clicks the element saveComposites
Then Web element errorText should have text <errorText>

Examples:
|step|errorText|
|Given there is param <with space>|with space is incorrect|
|Given there is param <1IsNotAGoodStart>|1IsNotAGoodStart is incorrect|

Scenario: Composites class name must be valid

Meta:


When Web user sets value <classNameValue> to the element className
Then Web element classNameError should have text Class name cannot start with a number, but after the initial character it may use any combination of letters and digits, underscores or dollar signs.
Then Web element saveComposites is disabled

Examples:
|classNameValue|
|1IsNotAGoodStart|
|space is not allowed|

Scenario: Composite must contain at least one step

Meta:


When Web user clicks the element removeCompositeStep1
Then Web element compositeStepsError should have text At least one step must be defined.

Examples:
