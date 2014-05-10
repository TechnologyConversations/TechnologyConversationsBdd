


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

Examples:

Scenario: Shold have all sections in the Introduction

Meta:


When Web user clicks the element tutorialIndexIntroduction
Then Web element tutorialNavigation is visible
Then Web element tutorialStories is visible

Examples:

Scenario: Should have index repeated in all tutorial screens

Meta:


When Web user clicks the element tutorialIndexIntroduction
Then Web element pageSubheader should have text Introduction
When Web user clicks the element tutorialIndexNavigation
Then Web element pageSubheader should have text Navigation
When Web user clicks the element tutorialIndexStories
Then Web element pageSubheader should have text Browse Stories

Examples:
