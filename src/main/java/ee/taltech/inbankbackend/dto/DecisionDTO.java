package ee.taltech.inbankbackend.dto;

/**
 * Holds the response data of the REST endpoint.
 */
public record DecisionDTO(Integer loanAmount, Integer loanPeriod, String errorMessage) {
}
