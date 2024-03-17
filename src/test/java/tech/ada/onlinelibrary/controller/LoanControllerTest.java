package tech.ada.onlinelibrary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.ada.onlinelibrary.advice.RestControllerAdvice;
import tech.ada.onlinelibrary.domain.Loan;
import tech.ada.onlinelibrary.dto.CreateLoanRequest;
import tech.ada.onlinelibrary.dto.UpdateLoanRequest;
import tech.ada.onlinelibrary.repository.LoanRepository;
import tech.ada.onlinelibrary.service.LoanService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LoanControllerTest {

    private Loan loan;
    private List<Loan> loans = new ArrayList<>();

    private MockMvc mockMvc;

    @Mock
    private LoanService loanService;
    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanController loanController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(loanController).setControllerAdvice(RestControllerAdvice.class).build();
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void newLoanTest_WhenAuthorized() throws Exception {
        // Arrange
        CreateLoanRequest loanRequest = new CreateLoanRequest(1l, 1l);
        Long userId = loanRequest.getUserId();
        when(loanService.checkLoanAuthorization(userId)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/library/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loanRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void newLoanTest_WhenUnauthorized() throws Exception {
        // Arrange
        CreateLoanRequest loanRequest = new CreateLoanRequest(1l, 1l); // Populate loan request
        Long userId = loanRequest.getUserId();
        when(loanService.checkLoanAuthorization(userId)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/library/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loanRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value("FORBIDDEN"))
                .andExpect(jsonPath("$.message").value("User with id: 1 is not authorized to make new loans."));
    }

    @Test
    void getAllLoansTest() throws Exception {
        // Arrange
        when(loanRepository.findAll()).thenReturn(loans);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/library/loans")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getLoansByUserId() throws Exception {
        // Arrange
        Long userId = 1l;
        when(loanRepository.findByUserId(userId)).thenReturn(loans);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/library/loans")
                        .param("userId", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void registerLoanReturn() throws Exception {
        // Arrange
        UpdateLoanRequest loanRequest = new UpdateLoanRequest(1l);
        Loan loan = new Loan();
        when(loanService.registerLoanReturn(Mockito.any(UpdateLoanRequest.class))).thenReturn(Optional.of(loan));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/library/loans/return")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loanRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(loan.getId())));
    }

    @Test
    void renewLoan() throws Exception {
        // Arrange
        UpdateLoanRequest loanRequest = new UpdateLoanRequest(1l);
        Optional<Loan> loan = Optional.of(new Loan());
        when(loanService.renewLoan(Mockito.any(UpdateLoanRequest.class))).thenReturn(loan);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/library/loans/renewal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loanRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(loan.get().getId())));
    }

    @Test
    void deleteLoan() throws Exception {
        // Arrange
        Long loanId = 1l;
        Optional<Loan> loan = Optional.of(new Loan()); // Example loan object
        when(loanRepository.findById(loanId)).thenReturn(loan);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/library/loans/{id}", loanId))
                .andExpect(status().isNoContent());
    }
}