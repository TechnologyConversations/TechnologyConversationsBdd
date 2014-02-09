This is description of this story

Meta:
@integration
@product dashboard

Narrative:
In order to communicate effectively to the business some functionality
As a development team
I want to use Behaviour-Driven Development

GivenStories: story1.story, story2.story, story3.story

Lifecycle: 
Before:
Given a step that is executed before each scenario
Given another step that is executed before each scenario
After:
Given a step that is executed after each scenario 
     
Scenario: A scenario is a collection of executable steps of different type

Meta:
@live
@product shopping cart
 
Given step represents a precondition to an event
When step represents the occurrence of the event
Then step represents the outcome of the event
 
Scenario: Another scenario exploring different combination of events
 
Given a [precondition]
When a negative event occurs
Then a the outcome should [be-captured]    
 
Examples: 
|precondition|be-captured|
|abc|be captured    |
|xyz|not be captured|