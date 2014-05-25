


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



Scenario: Should have the logo with the link to the home screen

Meta:


When Web user clicks the element home
Then Web element pageHeader should have text Welcome to BDD Assistant

Examples:

Scenario: Should have the link to the BDD explanation screen

Meta:


Then Web element bdd should have text BDD
When Web user clicks the element bdd
Then Web element pageHeader should have text Behavior-Driven Development (BDD)

Examples:

Scenario: Should have the link to the BDD Assistant screen

Meta:


Then Web element bddAssistant should have text BDD Assistant
When Web user clicks the element bddAssistant
Then Web element pageHeader should have text BDD Assistant

Examples:

Scenario: Should have the link to the tutorial screen

Meta:


Then Web element tutorial should have text Tutorial
When Web user clicks the element tutorial
Then Web element pageHeader should have text Tutorial

Examples:

Scenario: Should have the link to the Technology Conversations screen

Meta:


Then Web element technologyConversations should have text Technology Conversations
When Web user clicks the element technologyConversations
Then Web page title should have text Technology | CONVERSATIONS

Examples:

Scenario: Should have navigation in all screens

Meta:


When Web user clicks the element <navLink>
Then Web element navigation is visible

Examples:
|navLink|
|home|
|bdd|
|bddAssistant|
|tutorial|
