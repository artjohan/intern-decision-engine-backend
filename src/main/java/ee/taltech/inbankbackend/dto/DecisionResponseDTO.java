package ee.taltech.inbankbackend.dto;

/**
 * Holds the response data of the REST endpoint.
 */
public record DecisionResponseDTO(Integer loanAmount, Integer loanPeriod, String errorMessage) {
}


