# Overview of the implementation of TICKET-101

## What was good

- Constants were not hardcoded, but were instead static final variables in a separate file
- Instead of writing boilerplate code Lombok was used
- All error cases were handled with descriptive responses
- Backend functionality was delivered
- Good coverage on unit tests for new code
- Descriptive comments and a detailed README file were written

## Places for improvement

- A lot of logic for request validation was duplicated
- Multiple exceptions with identical fields, could be combined into one exception with a changing error message 
- DecisionResponse was being held as a global state and injected into the service. A DTO should be immutable and should get instantiated for every request to avoid accidental data loss
- DTOs were inconsistent with each other and the project structure had them all over the place
- Controller and service were also responsible for validation, which should be a layer of its own to implement single responsibility constraint
- Tests could have also included full flow integration testing
- Decision engine logic could have been cleaner, as there are simple formulas to calculate the required loan amounts/periods, instead of using a looping solution
- DecisionEngineConstants would be better as a final class, to make sure it is immutable
- While the backend of the project was functional, the frontend had a major issue which made the final product not function the way it was supposed to. __This is the most important shortcoming__ and is covered in more detail in the conclusion file of the frontend repository

## Changes that were implemented

### General

- Created folder for DTOs and renamed them for clarity. Also made the DTOs consistent with each other, removed unnecessary elements and made sure they were immutable by changing them to records
- Fixed the issue with DecisionResponse being injected into the service, made sure it was instantiated for every request
- Changed the loanAmount data type from long to int to maintain consistency
- Made the DecisionEngineConstants class final to make sure it is immutable, also changed the data types inside the class to primitive versions
- Separated validation logic into a layer of its own using spring's [validator interface](https://docs.spring.io/spring-framework/reference/core/validation/validator.html). This way all the potential errors get checked before even entering the controller. This made the controller a lot cleaner and concise while also implementing single responsiblity principle
- Removed all different exception types, as they were duplicates of each other, and replaced them with a simple validation exception, so it can be easily handled inside the controller. The validation exception still supports an error message to provide the same feedback to the end user
- Changed the decision engine logic, made it simpler and more readable by using a formula instead of the previous while loop solution
- Decision engine no longer throws errors, as they are handled by the validation logic
- No valid loan is no longer thrown as an error, but is instead sent back as a normal decisionResponse object with the correct error message
- Made the no valid loan error message more detailed to give the end user better feedback
- Removed some of the deprecated comments as I believe the code is now clear enough to where they are not strictly necessary
- Small adjustments all across the project to create a cleaner and more efficient codebase

### Testing

- Made unit testing method names more descriptive and consistent with each other
- Created a separate test file for the validator
- Cleaned up test classes by removing some unnecessary elements and duplicate test methods
- Included integration testing for the DecisionEngineController

# TICKET-102 implementation

- Added a simple age verification clause inside the validator
- Validator uses the getAge method of the EstonianPersonalCodeParser from the [java-personal-code](https://github.com/vladislavgoltjajev/java-personal-code) library
- The minimum age is 18 and the maximum age is 78 minus the maximum loan period available
- Age verification was implemented using months to achieve a more accurate result
- Also added age verification testing inside the validator test class
