package ee.taltech.inbankbackend.service;

import ee.taltech.inbankbackend.model.DecisionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DecisionEngineTest {

    @InjectMocks
    private DecisionEngine decisionEngine;

    private String debtorPersonalCode;
    private String segment1PersonalCode;
    private String segment2PersonalCode;
    private String segment3PersonalCode;

    @BeforeEach
    void setUp() {
        debtorPersonalCode = "37605030299";
        segment1PersonalCode = "50307172740";
        segment2PersonalCode = "38411266610";
        segment3PersonalCode = "35006069515";
    }

    @Test
    void testDebtorPersonalCode() {
        DecisionDTO decision = decisionEngine.calculateApprovedLoan(debtorPersonalCode, 4000, 12);
        assert Objects.equals(decision.getErrorMessage(), "No valid loan found!");
    }

    @Test
    void testSegment1PersonalCode() {
        DecisionDTO decision = decisionEngine.calculateApprovedLoan(segment1PersonalCode, 4000, 12);
        assertEquals(2000, decision.getLoanAmount());
        assertEquals(20, decision.getLoanPeriod());
    }

    @Test
    void testSegment2PersonalCode() {
        DecisionDTO decision = decisionEngine.calculateApprovedLoan(segment2PersonalCode, 4000, 12);
        assertEquals(3600, decision.getLoanAmount());
        assertEquals(12, decision.getLoanPeriod());
    }

    @Test
    void testSegment3PersonalCode() {
        DecisionDTO decision = decisionEngine.calculateApprovedLoan(segment3PersonalCode, 4000, 12);
        assertEquals(10000, decision.getLoanAmount());
        assertEquals(12, decision.getLoanPeriod());
    }

    @Test
    void testFindSuitableLoanPeriod() {
        DecisionDTO decision = decisionEngine.calculateApprovedLoan(segment2PersonalCode, 2000, 12);
        assertEquals(3600, decision.getLoanAmount());
        assertEquals(12, decision.getLoanPeriod());
    }

    @Test
    void testNoValidLoanFound() {
        DecisionDTO decision = decisionEngine.calculateApprovedLoan(debtorPersonalCode, 10000, 60);
        assert Objects.equals(decision.getErrorMessage(), "No valid loan found!");
    }
}

