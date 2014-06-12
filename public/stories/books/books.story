


Meta:


Narrative:
In order to 
As a 
I want to 

GivenStories: 

Lifecycle:

Before:
Given Web home page is opened
When Web user clicks the element books1

After:



Scenario: Should have the list of all books

Meta:


Then Web element books is visible

Examples:

Scenario: Should have the create/update form

Meta:


Then Web element bookId is visible
Then Web element bookTitle is visible
Then Web element bookAuthor is visible
Then Web element bookImage is visible
Then Web element bookPrice is visible

Examples:

Scenario: Should display up to 10 books

Meta:


Then Web element books10 is visible
Then Web element books11 is NOT visible

Examples:

Scenario: New Book link should clear the form

Meta:


When Web user clicks the element newBook
Then Web element bookId is empty
Then Web element bookTitle is empty
Then Web element bookAuthor is empty
Then Web element bookImage is empty
Then Web element bookPrice is empty

Examples:

Scenario: Save button should be disabled when data is unchanged

Meta:


Then Web element saveBook is disabled
When Web user sets value something different to the element bookTitle
Then Web element saveBook is enabled

Examples:

Scenario: Save button is disabled when required fields are not set

Meta:


When Web user clears the element <elementId>
Then Web element saveBook is disabled

Examples:
|elementId|
|bookId|
|bookTitle|
|bookAuthor|
|bookPrice|

Scenario: Save button is disabled when price is not a number

Meta:


When Web user sets value XXX to the element bookPrice
Then Web element saveBook is disabled

Examples:

Scenario: Revert button is disabled when data is unchanged

Meta:


Then Web element revertBook is disabled
When Web user sets value this is something different to the element bookTitle
Then Web element revertBook is enabled

Examples:

Scenario: Delete button is disabled when book is new

Meta:


When Web user clicks the element newBook
Then Web element deleteBook is disabled

Examples:
