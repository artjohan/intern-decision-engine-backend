package ee.taltech.inbankbackend.validation;

import com.github.vladislavgoltjajev.personalcode.exception.PersonalCodeException;
import com.github.vladislavgoltjajev.personalcode.locale.estonia.EstonianPersonalCodeParser;
import com.github.vladislavgoltjajev.personalcode.locale.estonia.EstonianPersonalCodeValidator;
import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.model.DecisionRequestDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.Period;

public class DecisionRequestValidator implements Validator {

    private final EstonianPersonalCodeValidator personalCodeValidator = new EstonianPersonalCodeValidator();
    private final EstonianPersonalCodeParser personalCodeParser = new EstonianPersonalCodeParser();

    @Override
    public boolean supports(Class<?> clazz) {
        return DecisionRequestDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DecisionRequestDTO request = (DecisionRequestDTO) target;

        if (!personalCodeValidator.isValid(request.getPersonalCode())) {
            throw new ValidationException("Invalid personal ID code!");
        }

        try {
            Period personAge = personalCodeParser.getAge(request.getPersonalCode());
            int personAgeInMonths = personAge.getYears() * 12 + personAge.getMonths();

            if(personAgeInMonths < DecisionEngineConstants.LOWER_AGE_LIMIT_IN_MONTHS || personAgeInMonths > DecisionEngineConstants.UPPER_AGE_LIMIT_IN_MONTHS) {
                throw new ValidationException("Invalid age!");
            }
        } catch (PersonalCodeException e) {
            throw new ValidationException(e.getMessage());
        }

        if (request.getLoanAmount() > DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT || request.getLoanAmount() < DecisionEngineConstants.MINIMUM_LOAN_AMOUNT) {
            throw new ValidationException("Invalid loan amount!");
        }

        if (request.getLoanPeriod() < DecisionEngineConstants.MINIMUM_LOAN_PERIOD || request.getLoanPeriod() > DecisionEngineConstants.MAXIMUM_LOAN_PERIOD) {
            throw new ValidationException("Invalid loan period!");
        }
    }
}
