


Meta:


Narrative:
In order to learn how to use the application
As a inexperienced user
I want to have a tutorial

GivenStories: 

Lifecycle:

Before:
Given Web address #/page/tutorial is opened

After:



Scenario: Should have Index

Meta:


Then Web element tutorialIndex is visible
Then Web element tutorialIndexIntroduction is visible
Then Web element tutorialIndexNavigation is visible
Then Web element tutorialIndexStories is visible
Then Web element tutorialIndexStoryForm is visible
Then Web element tutorialIndexStoryOperations is visible
Then Web element tutorialIndexSingleStoryRunner is visible
Then Web element tutorialIndexCompositeClasses is visible

Examples:

Scenario: Shold have all sections in the Introduction

Meta:


When Web user clicks the element tutorialIndexIntroduction
Then Web element tutorialNavigation is visible
Then Web element tutorialStories is visible
Then Web element tutorialStoryForm is visible
Then Web element tutorialStoryOperations is visible
Then Web element tutorialSingleStoryRunner is visible
Then Web element tutorialCompositeClasses is visible

Examples:

Scenario: Should have complete index repeated in all tutorial screens

Meta:


When Web user clicks the element tutorialIndexIntroduction
Then Web element pageSubheader should have text Introduction
When Web user clicks the element tutorialIndexNavigation
Then Web element pageSubheader should have text Navigation
When Web user clicks the element tutorialIndexStories
Then Web element pageSubheader should have text Browse Stories
When Web user clicks the element tutorialIndexStoryForm
Then Web element pageSubheader should have text Story Form
When Web user clicks the element tutorialIndexStoryOperations
Then Web element pageSubheader should have text Story Operations
When Web user clicks the element tutorialIndexSingleStoryRunner
Then Web element pageSubheader should have text Single Story Runner
When Web user clicks the element tutorialIndexCompositeClasses
Then Web element pageSubheader should have text Browse Composites

Examples:
