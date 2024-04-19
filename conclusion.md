TICKET-101

- created model folder for DTOs and renamed them for clarity, also made the DTOs consistent with each other, removing unnecessary elements
- the decision engine DTO was previously being instantiated just once in the controller, then being overwritten inside the REST endpoint every time a new request was made, this violates the single responsibility principle (DTOs should also be immutable)
- changed the loanAmount data type from long to int to maintain consistency
- separated validation logic and controller/service logic, this way all the potential errors get checked before even entering controller, this made the controller a lot cleaner and consice
- validation logic uses spring's [validator interface](https://docs.spring.io/spring-framework/reference/core/validation/validator.html)
- removed all different exception types, replaced them with a simple validation exception, so it can be handled easily inside the controller, the validation exception still supports an error message so the same feedback will be provided to the end user
- changed decision engine logic, made it more readable by using a simple formula instead of the previous while loop solution
- decision engine no longer throws errors, as they are handled by the validation logic
- no valid loan is not thrown as an error, but is instead sent back as a normal decisionresponse with the correct error message

TICKET-102

- added a simple age verification clause inside the validator
- validator uses the getAge method of the EstonianPersonalCodeParser from the [java-personal-code](https://github.com/vladislavgoltjajev/java-personal-code) library
- the minimum age is 18 and the maximum age is 78 minus the maximum loan period available
- age verification is done using months for a more accurate result
