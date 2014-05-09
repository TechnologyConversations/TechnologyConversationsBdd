


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

Examples:

Scenario: Shold have Introduction

Meta:


When Web user clicks the element tutorialIndexIntroduction
Then Web element pageSubheader should have text Introduction
Then Web element tutorialNavigation is visible

Examples:

Scenario: Should have Navigation

Meta:


When Web user clicks the element tutorialIndexNavigation
Then Web element pageSubheader should have text Navigation

Examples:
