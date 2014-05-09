


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

Examples:

Scenario: Shold have Introduction

Meta:


When Web user clicks the element tutorialIntroduction
Then Web element pageSubheader should have text Introduction

Examples:
