


Meta:


Narrative:
In order to 
As a 
I want to 

GivenStories: 

Lifecycle:

Before:
Given Web home page is opened

After:



Scenario: Should have short description of the BDD Assistant

Meta:


Then Web element bddAssistantSummaryHeader should have text BDD Assistant
Then Web element bddAssistantSummaryBody is visible
When Web user clicks the element bddAssistantSummaryMore
Then Web page title should have text Application

Examples:

Scenario: Should have short description of BDD

Meta:


Then Web element bddSummaryHeader should have text Behavior-Driven Development (BDD)
Then Web element bddSummaryBody is visible
When Web user clicks the element bddSummaryMore
Then Web page title should have text Behaviour-Driven Development (BDD)

Examples:

Scenario: Should have short description of Technology Conversations

Meta:


Then Web element tcSummaryHeader should have text Technology Conversations
Then Web element tcsummaryBody is visible
When Web user clicks the element tcSummaryMore
Then Web page title should have text Technology Conversations

Examples:

Scenario: Should have short description of the BDD Assistant tutorial

Meta:


Then Web element tutorialSummaryHeader should have text Tutorial
Then Web element tutorialSummaryBody is visible
When Web user clicks the element tutorialSummaryMore
Then Web page title should have text Tutorial

Examples:
