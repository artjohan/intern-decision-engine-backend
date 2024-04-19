package ee.taltech.inbankbackend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Holds the response data of the REST endpoint.
 */
@Getter
@AllArgsConstructor
public class DecisionResponseDTO {
    private final Integer loanAmount;
    private final Integer loanPeriod;
    private final String errorMessage;

    public DecisionResponseDTO(String errorMessage) {
        this.errorMessage = errorMessage;
        this.loanAmount = null;
        this.loanPeriod = null;
    }
}


