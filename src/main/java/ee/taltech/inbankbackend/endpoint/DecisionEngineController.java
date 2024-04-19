package ee.taltech.inbankbackend.endpoint;

import ee.taltech.inbankbackend.dto.DecisionDTO;
import ee.taltech.inbankbackend.dto.DecisionRequestDTO;
import ee.taltech.inbankbackend.dto.DecisionResponseDTO;
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
        return ResponseEntity.badRequest().body(new DecisionResponseDTO(null, null, e.getValidationError()));
    }

    @PostMapping("/decision")
    public ResponseEntity<DecisionResponseDTO> requestDecision(@RequestBody @Validated DecisionRequestDTO request) {
        DecisionDTO decision = decisionEngine.
                calculateApprovedLoan(request.personalCode(), request.loanPeriod());

        DecisionResponseDTO response = new DecisionResponseDTO(decision.loanAmount(), decision.loanPeriod(), decision.errorMessage());

        return ResponseEntity.ok(response);
    }
}
