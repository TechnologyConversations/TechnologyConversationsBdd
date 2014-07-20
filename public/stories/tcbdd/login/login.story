


Meta:


Narrative:
In order to be able to provide personized experience
As a user
I want to be able to identify myself

GivenStories: 

Lifecycle:

Before:
Given Web user is in the Login screen

After:



Scenario: Should have all fields

Meta:


Then Web element username is visible
Then Web element password is visible
Then Web element login is visible

Examples:

Scenario: Should have username required

Meta:


When Web user sets value myPassword to the element password
Then Web element login is disabled
Then Web element message should have text Username is required

Examples:

Scenario: Should have password required

Meta:


When Web user sets value myUsername to the element username
Then Web element login is disabled
Then Web element message should have text Password is required

Examples:

Scenario: Should redirect to the confirmation screen

Meta:


Given Web user myUsername with password myPassword exists
When Web user myUsername logs in with password myPassword
Then Web user is in the welcome screen

Examples:

Scenario: Should display error message when username does not exist

Meta:


Given Web user myUsername does NOT exists
When Web user myUsername logs in with password myPassword
Then Web element message should have text Specified username and/or password is incorrect

Examples:
