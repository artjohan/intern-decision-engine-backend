package ee.taltech.inbankbackend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Holds the request data of the REST endpoint
 */
@Getter
@AllArgsConstructor
public class DecisionRequestDTO {
    private final String personalCode;
    private final Integer loanAmount;
    private final Integer loanPeriod;
}
