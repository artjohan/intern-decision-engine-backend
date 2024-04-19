package ee.taltech.inbankbackend.endpoint;

import ee.taltech.inbankbackend.model.DecisionDTO;
import ee.taltech.inbankbackend.model.DecisionRequestDTO;
import ee.taltech.inbankbackend.model.DecisionResponseDTO;
import ee.taltech.inbankbackend.service.DecisionEngine;
import ee.taltech.inbankbackend.validation.DecisionRequestValidator;
import ee.taltech.inbankbackend.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loan")
@CrossOrigin
@RequiredArgsConstructor
public class DecisionEngineController {

    private final DecisionEngine decisionEngine;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new DecisionRequestValidator());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException e) {
        return ResponseEntity.badRequest().body(new DecisionResponseDTO(e.getValidationError()));
    }

    /**
     * A REST endpoint that handles requests for loan decisions.
     * The endpoint accepts POST requests with a request body containing the customer's personal ID code,
     * requested loan amount, and loan period.<br><br>
     * - If the loan amount or period is invalid, the endpoint returns a bad request response with an error message.<br>
     * - If the personal ID code is invalid, the endpoint returns a bad request response with an error message.<br>
     * - If an unexpected error occurs, the endpoint returns an internal server error response with an error message.<br>
     * - If no valid loans can be found, the endpoint returns a not found response with an error message.<br>
     * - If a valid loan is found, a DecisionResponse is returned containing the approved loan amount and period.
     *
     * @param request The request body containing the customer's personal ID code, requested loan amount, and loan period
     * @return A ResponseEntity with a DecisionResponse body containing the approved loan amount and period, and an error message (if any)
     */
    @PostMapping("/decision")
    public ResponseEntity<DecisionResponseDTO> requestDecision(@RequestBody @Validated DecisionRequestDTO request) {
        DecisionDTO decision = decisionEngine.
                calculateApprovedLoan(request.getPersonalCode(), request.getLoanAmount(), request.getLoanPeriod());

        DecisionResponseDTO response = new DecisionResponseDTO(decision.getLoanAmount(), decision.getLoanPeriod(), decision.getErrorMessage());

        return ResponseEntity.ok(response);
    }
}
