package ee.taltech.inbankbackend.dto;

/**
 * Holds the request data of the REST endpoint
 */

public record DecisionRequestDTO(String personalCode, Integer loanAmount, Integer loanPeriod) {
}
