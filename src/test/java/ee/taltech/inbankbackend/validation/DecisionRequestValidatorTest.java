package ee.taltech.inbankbackend.validation;

import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.dto.DecisionRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.springframework.validation.Errors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DecisionRequestValidatorTest {

    private final DecisionRequestValidator validator = new DecisionRequestValidator();

    private final String validPersonalCode = "50307172740";

    @Mock
    private Errors errors;

    @Test
    void givenInvalidPersonalCode_whenRequestValidation_thenShouldThrowValidationException() {
        String invalidPersonalCode = "12345678901";

        DecisionRequestDTO request = new DecisionRequestDTO(invalidPersonalCode, 4000, 12);

        ValidationException actual = assertThrows(ValidationException.class,
                () -> validator.validate(request, errors));
        assertEquals("Invalid personal ID code!", actual.getValidationError());
    }

    @Test
    void givenTooLowLoanAmount_whenRequestValidation_thenShouldThrowValidationException() {
        int tooLowLoanAmount = DecisionEngineConstants.MINIMUM_LOAN_AMOUNT - 1;

        DecisionRequestDTO request = new DecisionRequestDTO(validPersonalCode, tooLowLoanAmount, 12);

        ValidationException actual = assertThrows(ValidationException.class,
                () -> validator.validate(request, errors));
        assertEquals("Invalid loan amount!", actual.getValidationError());
    }

    @Test
    void givenTooHighLoanAmount_whenRequestValidation_thenShouldThrowValidationException() {
        int tooHighLoanAmount = DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT + 1;

        DecisionRequestDTO request = new DecisionRequestDTO(validPersonalCode, tooHighLoanAmount, 12);

        ValidationException actual = assertThrows(ValidationException.class,
                () -> validator.validate(request, errors));
        assertEquals("Invalid loan amount!", actual.getValidationError());
    }

    @Test
    void givenTooShortLoanPeriod_whenRequestValidation_thenShouldThrowValidationException() {
        int tooShortLoanPeriod = DecisionEngineConstants.MINIMUM_LOAN_PERIOD - 1;

        DecisionRequestDTO request = new DecisionRequestDTO(validPersonalCode, 4000, tooShortLoanPeriod);

        ValidationException actual = assertThrows(ValidationException.class,
                () -> validator.validate(request, errors));
        assertEquals("Invalid loan period!", actual.getValidationError());
    }
    @Test
    void givenTooLongLoanPeriod_whenRequestValidation_thenShouldThrowValidationException() {
        int tooLongLoanPeriod = DecisionEngineConstants.MAXIMUM_LOAN_PERIOD + 1;

        DecisionRequestDTO request = new DecisionRequestDTO(validPersonalCode, 4000, tooLongLoanPeriod);

        ValidationException actual = assertThrows(ValidationException.class,
                () -> validator.validate(request, errors));
        assertEquals("Invalid loan period!", actual.getValidationError());
    }

    @Test
    void givenUnderAgePersonalCode_whenRequestValidation_thenShouldThrowValidationException() {
        String underAgePersonalCode = "51107121760";

        DecisionRequestDTO request = new DecisionRequestDTO(underAgePersonalCode, 4000, 12);

        ValidationException actual = assertThrows(ValidationException.class,
                () -> validator.validate(request, errors));
        assertEquals("Invalid age!", actual.getValidationError());
    }

    @Test
    void givenOverAgePersonalCode_whenRequestValidation_thenShouldThrowValidationException() {
        String overAgePersonalCode = "29912120004";

        DecisionRequestDTO request = new DecisionRequestDTO(overAgePersonalCode, 4000, 12);

        ValidationException actual = assertThrows(ValidationException.class,
                () -> validator.validate(request, errors));
        assertEquals("Invalid age!", actual.getValidationError());
    }

    @Test
    void givenInvalidDateOfBirthInPersonalCode_whenRequestValidation_thenShouldThrowValidationException() {
        String personalCodeWithDateOfBirthInFuture = "69901010237";

        DecisionRequestDTO requestShort = new DecisionRequestDTO(personalCodeWithDateOfBirthInFuture, 4000, 12);

        ValidationException actual = assertThrows(ValidationException.class,
                () -> validator.validate(requestShort, errors));
        assertEquals("Invalid age!", actual.getValidationError());
    }

    @Test
    void givenValidRequest_whenRequestValidation_thenShouldNotThrowValidationException() {
        DecisionRequestDTO requestShort = new DecisionRequestDTO("38411266610", 4000, 12);

        Executable e = () -> validator.validate(requestShort, errors);

        assertDoesNotThrow(e);
    }
}
