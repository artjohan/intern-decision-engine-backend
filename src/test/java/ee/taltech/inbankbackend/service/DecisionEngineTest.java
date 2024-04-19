package ee.taltech.inbankbackend.service;

import ee.taltech.inbankbackend.dto.DecisionDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecisionEngineTest {

    private DecisionEngine decisionEngine = new DecisionEngine();
    private final String debtorPersonalCode = "37605030299";
    private final String segment1PersonalCode = "50307172740";
    private final String segment2PersonalCode = "38411266610";
    private final String segment3PersonalCode = "35006069515";

    @Test
    void givenDebtorPersonalCode_whenCalculatingApprovedLoan_thenShouldReturnNegativeDecision() {
        DecisionDTO expected = new DecisionDTO(null, null, "Credit too low, no valid loan found!");

        DecisionDTO actual = decisionEngine.calculateApprovedLoan(debtorPersonalCode, 12);

        assertEquals(expected, actual);
    }

    @Test
    void givenSegment1PersonalCode_whenCalculatingApprovedLoan_thenShouldApproveWithAdjustedLoanPeriod() {
        DecisionDTO expected = new DecisionDTO(2000, 20, null);

        DecisionDTO actual = decisionEngine.calculateApprovedLoan(segment1PersonalCode, 12);

        assertEquals(expected, actual);
    }

    @Test
    void givenSegment2PersonalCode_whenCalculatingApprovedLoan_thenShouldApprove() {
        DecisionDTO expected = new DecisionDTO(3600, 12, null);

        DecisionDTO actual = decisionEngine.calculateApprovedLoan(segment2PersonalCode, 12);

        assertEquals(expected, actual);
    }

    @Test
    void givenSegment3PersonalCode_whenCalculatingApprovedLoan_thenShouldApprove() {
        DecisionDTO expected = new DecisionDTO(10000, 12, null);

        DecisionDTO actual = decisionEngine.calculateApprovedLoan(segment3PersonalCode, 12);

        assertEquals(expected, actual);
    }
}

