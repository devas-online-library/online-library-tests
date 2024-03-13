package tech.ada.onlinelibrary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.ada.onlinelibrary.advice.exception.BookNotFoundException;
import tech.ada.onlinelibrary.advice.exception.UserNotFoundException;
import tech.ada.onlinelibrary.domain.Book;
import tech.ada.onlinelibrary.domain.Loan;
import tech.ada.onlinelibrary.domain.User;
import tech.ada.onlinelibrary.dto.CreateLoanRequest;
import tech.ada.onlinelibrary.dto.UpdateLoanRequest;
import tech.ada.onlinelibrary.repository.BookRepository;
import tech.ada.onlinelibrary.repository.LoanRepository;
import tech.ada.onlinelibrary.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    private LoanRepository loanRepository;
    private UserRepository userRepository;
    private BookRepository bookRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }


    public Boolean checkLoanAuthorization(Long userId){
        List<Loan> loans = loanRepository.findByUserId(userId);

        for(int i=0; i < loans.size(); i++ ) {
            Loan loan = loans.get(i);
            LocalDate scheduledReturnDate = loan.getScheduledReturnDate();
            LocalDate realReturnDate = loan.getRealReturnDate();

            if(realReturnDate != null){
                LocalDate penaltyDate = realReturnDate.plusDays(15);
                LocalDate actualDate = LocalDate.now();
                if (realReturnDate.isAfter(scheduledReturnDate)&&(actualDate.isBefore(penaltyDate))) return false;
            } else {
                return false;
            }

        }
        return true;
    }

    public void createLoan(CreateLoanRequest loanRequest) {
        User user = userRepository.findById(loanRequest.getUserId()).orElseThrow(() -> new UserNotFoundException(loanRequest.getUserId()));
        Book book = bookRepository.findById(loanRequest.getBookId()).orElseThrow(() -> new BookNotFoundException(loanRequest.getBookId()));
        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        LocalDate currentDate = LocalDate.now();
        LocalDate scheduledReturnDate = currentDate.plusDays(7);
        loan.setLoanDate(currentDate);
        loan.setScheduledReturnDate(scheduledReturnDate);
        loanRepository.save(loan);
    }

    public Optional<Loan> registerLoanReturn(UpdateLoanRequest returnDateRequest) {
        Optional<Loan> loanOpt = loanRepository.findById(returnDateRequest.getLoanId());

        if (loanOpt.isPresent()) {
            Loan loan = loanOpt.get();
            loan.setRealReturnDate(LocalDate.now());
            loanRepository.save(loan);
        }

        return loanOpt;
    }

    public Optional<Loan> renewLoan(UpdateLoanRequest returnDateRequest) {
        Optional<Loan> loanOpt = loanRepository.findById(returnDateRequest.getLoanId());

        if (loanOpt.isPresent()) {
            Loan loan = loanOpt.get();
            LocalDate currentDate = LocalDate.now();

            if(loan.getRealReturnDate() == null && currentDate.isBefore(loan.getScheduledReturnDate())) {
                loan.setScheduledReturnDate(currentDate.plusDays(7));
                loanRepository.save(loan);
            }
        }

        return loanOpt;
    }
  
}
