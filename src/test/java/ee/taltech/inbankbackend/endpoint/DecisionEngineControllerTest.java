package ee.taltech.inbankbackend.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.taltech.inbankbackend.dto.DecisionRequestDTO;
import ee.taltech.inbankbackend.dto.DecisionResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * This class holds integration tests for the DecisionEngineController endpoint.
 */
@SpringBootTest
@AutoConfigureMockMvc
//@ExtendWith(SpringExtension.class)
public class DecisionEngineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * This method tests the /loan/decision endpoint with valid inputs.
     */
    @Test
    public void givenValidRequest_whenRequestDecision_thenReturnsExpectedResponse()
            throws Exception {

        DecisionRequestDTO request = new DecisionRequestDTO("38411266610", 10000, 60);

        DecisionResponseDTO expected = new DecisionResponseDTO(10000, 60, null);

        MvcResult result = mockMvc.perform(post("/loan/decision")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        DecisionResponseDTO actual = objectMapper.readValue(result.getResponse().getContentAsString(), DecisionResponseDTO.class);

        assertEquals(expected, actual);
    }

    /**
     * This test ensures that if an invalid personal code is provided, the controller returns
     * an HTTP Bad Request (400) response with the appropriate error message in the response body.
     */
    @Test
    public void givenInvalidPersonalCode_whenRequestDecision_thenReturnsBadRequest()
            throws Exception {

        DecisionRequestDTO request = new DecisionRequestDTO("1234", 4000, 12);
        DecisionResponseDTO expected = new DecisionResponseDTO(null, null, "Invalid personal ID code!");

        MvcResult result = mockMvc.perform(post("/loan/decision")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        DecisionResponseDTO actual = objectMapper.readValue(result.getResponse().getContentAsString(), DecisionResponseDTO.class);

        assertEquals(expected, actual);
    }

    /**
     * This test ensures that if an invalid loan amount is provided, the controller returns
     * an HTTP Bad Request (400) response with the appropriate error message in the response body.
     */
    @Test
    public void givenInvalidLoanAmount_whenRequestDecision_thenReturnsBadRequest()
            throws Exception {

        DecisionRequestDTO request = new DecisionRequestDTO("38411266610", 1000, 60);

        DecisionResponseDTO expected = new DecisionResponseDTO(null, null, "Invalid loan amount!");

        MvcResult result = mockMvc.perform(post("/loan/decision")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        DecisionResponseDTO actual = objectMapper.readValue(result.getResponse().getContentAsString(), DecisionResponseDTO.class);

        assertEquals(expected, actual);
    }

    /**
     * This test ensures that if an invalid loan period is provided, the controller returns
     * an HTTP Bad Request (400) response with the appropriate error message in the response body.
     */
    @Test
    public void givenInvalidLoanPeriod_whenRequestDecision_thenReturnsBadRequest()
            throws Exception {

        DecisionRequestDTO request = new DecisionRequestDTO("38411266610", 10000, 10);

        DecisionResponseDTO expected = new DecisionResponseDTO(null, null, "Invalid loan period!");

        MvcResult result = mockMvc.perform(post("/loan/decision")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        DecisionResponseDTO actual = objectMapper.readValue(result.getResponse().getContentAsString(), DecisionResponseDTO.class);

        assertEquals(expected, actual);
    }

    /**
     * This test ensures that if no valid loan is found, the controller returns
     * an HTTP Bad Request (400) response with the appropriate error message in the response body.
     */
    @Test
    public void givenNoValidLoan_whenRequestDecision_thenReturnsBadRequest()
            throws Exception {

        DecisionRequestDTO request = new DecisionRequestDTO("37605030299", 10000, 60);

        DecisionResponseDTO expected = new DecisionResponseDTO(null, null, "Credit too low, no valid loan found!");

        MvcResult result = mockMvc.perform(post("/loan/decision")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        DecisionResponseDTO actual = objectMapper.readValue(result.getResponse().getContentAsString(), DecisionResponseDTO.class);

        assertEquals(expected, actual);
    }
}
