package ee.taltech.inbankbackend.service;

import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.dto.DecisionDTO;
import org.springframework.stereotype.Service;

/**
 * A service class that provides a method for calculating an approved loan amount and period for a customer.
 * The loan amount is calculated based on the customer's credit modifier,
 * which is determined by the last four digits of their ID code.
 */
@Service
public class DecisionEngine {

    public DecisionDTO calculateApprovedLoan(String personalCode, int loanPeriod) {
        int creditModifier = getCreditModifier(personalCode);

        if (creditModifier == 0) {
            return new DecisionDTO(null, null, "Credit too low, no valid loan found!");
        }

        int maxAvailableLoanAmount = creditModifier * loanPeriod;

        if (maxAvailableLoanAmount >= DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT) {
            return new DecisionDTO(DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT, loanPeriod, null);
        }

        if (maxAvailableLoanAmount <= DecisionEngineConstants.MINIMUM_LOAN_AMOUNT) {
            int adjustedLoanPeriod = DecisionEngineConstants.MINIMUM_LOAN_AMOUNT / creditModifier;
            return new DecisionDTO(DecisionEngineConstants.MINIMUM_LOAN_AMOUNT, adjustedLoanPeriod, null);
        }

        return new DecisionDTO(maxAvailableLoanAmount, loanPeriod, null);
    }


    /**
     * Calculates the credit modifier of the customer to according to the last four digits of their ID code.
     * Debt - 0000...2499
     * Segment 1 - 2500...4999
     * Segment 2 - 5000...7499
     * Segment 3 - 7500...9999
     *
     * @param personalCode ID code of the customer that made the request.
     * @return Segment to which the customer belongs.
     */
    private int getCreditModifier(String personalCode) {
        int segment = Integer.parseInt(personalCode.substring(personalCode.length() - 4));

        if (segment < 2500) {
            return 0;
        } else if (segment < 5000) {
            return DecisionEngineConstants.SEGMENT_1_CREDIT_MODIFIER;
        } else if (segment < 7500) {
            return DecisionEngineConstants.SEGMENT_2_CREDIT_MODIFIER;
        }

        return DecisionEngineConstants.SEGMENT_3_CREDIT_MODIFIER;
    }
}
