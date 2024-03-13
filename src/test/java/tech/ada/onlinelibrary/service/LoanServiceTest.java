package tech.ada.onlinelibrary.service;

import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.ada.onlinelibrary.domain.Book;
import tech.ada.onlinelibrary.domain.Loan;
import tech.ada.onlinelibrary.domain.User;
import tech.ada.onlinelibrary.dto.CreateLoanRequest;
import tech.ada.onlinelibrary.dto.UpdateLoanRequest;
import tech.ada.onlinelibrary.repository.BookRepository;
import tech.ada.onlinelibrary.repository.LoanRepository;
import tech.ada.onlinelibrary.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {


    @Mock
    private LoanRepository loanRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private LoanService loanService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(loanService).build();
    }

    @Test
    void checkLoanAuthorizationTest_ReturnTrueWhenCurrentDateIsAfterPenaltyDate() {
        Long userId = 1l;

        List<Loan> loans = new ArrayList<>();
        Loan loan = new Loan();
        // set loan made 30 days ago
        loan.setLoanDate(LocalDate.now().minusDays(30));
        // set scheduledReturnDate as 7 days after the loan date
        loan.setScheduledReturnDate(loan.getLoanDate().plusDays(7));
        // returns the book 2 days after scheduledReturnDate, so the user gets blocked for 15 days
        loan.setRealReturnDate(loan.getScheduledReturnDate().plusDays(2));
        loans.add(loan);

        when(loanRepository.findByUserId(userId)).thenReturn(loans);

        // Today, the user is allowed to get a new loan, since the penalty date (15 days) is over
        assertTrue(loanService.checkLoanAuthorization(userId));
    }

    @Test
    void checkLoanAuthorizationTest_ReturnTrueWhenReturnDateIsBeforeScheduledDate() {
        Long userId = 1l;

        List<Loan> loans = new ArrayList<>();
        Loan loan = new Loan();
        // set loan made 15 days ago
        loan.setLoanDate(LocalDate.now().minusDays(15));
        // set scheduledReturnDate as 7 days after the loan date
        loan.setScheduledReturnDate(loan.getLoanDate().plusDays(7));
        // Returns the book 2 days before the scheduledReturnDate
        loan.setRealReturnDate(loan.getScheduledReturnDate().minusDays(2));
        loans.add(loan);

        when(loanRepository.findByUserId(userId)).thenReturn(loans);

        // Today, the user is allowed to get a new loan, since the book was returned before the scheduled date
        assertTrue(loanService.checkLoanAuthorization(userId));
    }

    @Test
    void checkLoanAuthorizationTest_ReturnFalseWhenNoReturnDate() {
        Long userId = 1l;

        List<Loan> loans = new ArrayList<>();
        Loan loan = new Loan();
        loan.setLoanDate(LocalDate.now());
        loan.setRealReturnDate(null);
        loans.add(loan);

        when(loanRepository.findByUserId(userId)).thenReturn(loans);

        assertFalse(loanService.checkLoanAuthorization(userId));
    }

    @Test
    void checkLoanAuthorizationTest_ReturnFalseWhenPenalty() {
        Long userId = 1l;

        List<Loan> loans = new ArrayList<>();
        Loan loan = new Loan();
        loan.setLoanDate(LocalDate.now().minusDays(15));
        loan.setScheduledReturnDate(LocalDate.now().minusDays(7));
        loan.setRealReturnDate(LocalDate.now().minusDays(3)); //maior do que scheduled
        loans.add(loan);

        when(loanRepository.findByUserId(userId)).thenReturn(loans);

        assertFalse(loanService.checkLoanAuthorization(userId));
    }

    @Test
    void createLoanTest() {
        //Arrange - Preparar
        Long userId = 1l;
        Long bookId = 1l;

        CreateLoanRequest loanRequest = new CreateLoanRequest(userId, bookId);

        User user = new User();
        user.setId(userId);

        Book book = new Book();
        book.setId(bookId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        //Act - Açao
        loanService.createLoan(loanRequest);

        //Assertion - Validacao
        verify(loanRepository, times(1)).save(Mockito.any());

    }

    @Test
    void registerLoanReturnTest() {
        UpdateLoanRequest returnDateRequest = new UpdateLoanRequest(1l);

        Loan loan = new Loan();
        loan.setId(1l);
        loan.setLoanDate(LocalDate.now());

        when(loanRepository.findById(returnDateRequest.getLoanId())).thenReturn(Optional.of(loan));

        Optional<Loan> loanOpt = loanService.registerLoanReturn(returnDateRequest);

        loan.setRealReturnDate(LocalDate.now());

        assertTrue(loan.getRealReturnDate() == loanOpt.get().getRealReturnDate());
    }

    @Test
    void renewLoanTest_WhenBookNotReturnedAndWithinScheduledReturnDate() {
        UpdateLoanRequest returnDateRequest = new UpdateLoanRequest(1l);

        Loan loan = new Loan();
        loan.setId(1l);
        loan.setLoanDate(LocalDate.now().minusDays(5));
        loan.setScheduledReturnDate(loan.getLoanDate().plusDays(7));

        when(loanRepository.findById(returnDateRequest.getLoanId())).thenReturn(Optional.of(loan));

        Optional<Loan> loanOpt = loanService.renewLoan(returnDateRequest);

        loan.setScheduledReturnDate(LocalDate.now().plusDays(7));

        assertTrue(loan.getScheduledReturnDate() == loanOpt.get().getScheduledReturnDate());
    }

    @Test
    void renewLoanTest_KeepsScheduleReturnDateWhenBookIsReturned() {
        /* Keeps the scheduledReturnDate when book is already returned */

        UpdateLoanRequest returnDateRequest = new UpdateLoanRequest(1l);

        Loan loan = new Loan();
        loan.setId(1l);
        loan.setLoanDate(LocalDate.now().minusDays(4));
        loan.setScheduledReturnDate(loan.getLoanDate().plusDays(7));
        loan.setRealReturnDate(LocalDate.now().minusDays(1));

        when(loanRepository.findById(returnDateRequest.getLoanId())).thenReturn(Optional.of(loan));

        Optional<Loan> loanOpt = loanService.renewLoan(returnDateRequest);

        assertTrue(loan.getScheduledReturnDate() == loanOpt.get().getScheduledReturnDate());

    }

    @Test
    void renewLoanTest_KeepsScheduleReturnDateWhenLoanIsDelayed() {
        /* Keeps the scheduledReturnDate when loan is delayed */

        UpdateLoanRequest returnDateRequest = new UpdateLoanRequest(1l);

        Loan loan = new Loan();
        loan.setId(1l);
        loan.setLoanDate(LocalDate.now().minusDays(10));
        loan.setScheduledReturnDate(loan.getLoanDate().plusDays(7));

        when(loanRepository.findById(returnDateRequest.getLoanId())).thenReturn(Optional.of(loan));

        Optional<Loan> loanOpt = loanService.renewLoan(returnDateRequest);

        assertTrue(loan.getScheduledReturnDate() == loanOpt.get().getScheduledReturnDate());
    }
}