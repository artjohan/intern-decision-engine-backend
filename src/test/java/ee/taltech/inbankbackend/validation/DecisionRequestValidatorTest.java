package ee.taltech.inbankbackend.validation;

import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.model.DecisionRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DecisionRequestValidatorTest {

    private final DecisionRequestValidator validator = new DecisionRequestValidator();

    private final String validPersonalCode = "50307172740";

    @Test
    void testInvalidPersonalCode() {
        String invalidPersonalCode = "12345678901";
        DecisionRequestDTO request = new DecisionRequestDTO(invalidPersonalCode, 4000, 12);
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(request, "request");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validate(request, errors));
        assertEquals("Invalid personal ID code!", exception.getValidationError());
    }

    @Test
    void testInvalidLoanAmount() {
        int tooLowLoanAmount = DecisionEngineConstants.MINIMUM_LOAN_AMOUNT - 1;
        int tooHighLoanAmount = DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT + 1;

        DecisionRequestDTO requestLow = new DecisionRequestDTO(validPersonalCode, tooLowLoanAmount, 12);
        BeanPropertyBindingResult errorsLow = new BeanPropertyBindingResult(requestLow, "request");

        ValidationException exceptionLow = assertThrows(ValidationException.class,
                () -> validator.validate(requestLow, errorsLow));
        assertEquals("Invalid loan amount!", exceptionLow.getValidationError());

        DecisionRequestDTO requestHigh = new DecisionRequestDTO(validPersonalCode, tooHighLoanAmount, 12);
        BeanPropertyBindingResult errorsHigh = new BeanPropertyBindingResult(requestHigh, "request");

        ValidationException exceptionHigh = assertThrows(ValidationException.class,
                () -> validator.validate(requestHigh, errorsHigh));
        assertEquals("Invalid loan amount!", exceptionHigh.getValidationError());
    }

    @Test
    void testInvalidLoanPeriod() {
        int tooShortLoanPeriod = DecisionEngineConstants.MINIMUM_LOAN_PERIOD - 1;
        int tooLongLoanPeriod = DecisionEngineConstants.MAXIMUM_LOAN_PERIOD + 1;

        DecisionRequestDTO requestShort = new DecisionRequestDTO(validPersonalCode, 4000, tooShortLoanPeriod);
        BeanPropertyBindingResult errorsShort = new BeanPropertyBindingResult(requestShort, "request");

        ValidationException exceptionShort = assertThrows(ValidationException.class,
                () -> validator.validate(requestShort, errorsShort));
        assertEquals("Invalid loan period!", exceptionShort.getValidationError());

        DecisionRequestDTO requestLong = new DecisionRequestDTO(validPersonalCode, 4000, tooLongLoanPeriod);
        BeanPropertyBindingResult errorsLong = new BeanPropertyBindingResult(requestLong, "request");

        ValidationException exceptionLong = assertThrows(ValidationException.class,
                () -> validator.validate(requestLong, errorsLong));
        assertEquals("Invalid loan period!", exceptionLong.getValidationError());
    }

    @Test
    void testInvalidAge() {
        String underAgePersonalCode = "51107121760";
        String overAgePersonalCode = "29912120004";

        DecisionRequestDTO requestShort = new DecisionRequestDTO(underAgePersonalCode, 4000, 12);
        BeanPropertyBindingResult errorsShort = new BeanPropertyBindingResult(requestShort, "request");

        ValidationException exceptionShort = assertThrows(ValidationException.class,
                () -> validator.validate(requestShort, errorsShort));
        assertEquals("Invalid age!", exceptionShort.getValidationError());

        DecisionRequestDTO requestLong = new DecisionRequestDTO(overAgePersonalCode, 4000, 12);
        BeanPropertyBindingResult errorsLong = new BeanPropertyBindingResult(requestLong, "request");

        ValidationException exceptionLong = assertThrows(ValidationException.class,
                () -> validator.validate(requestLong, errorsLong));
        assertEquals("Invalid age!", exceptionLong.getValidationError());
    }
}
