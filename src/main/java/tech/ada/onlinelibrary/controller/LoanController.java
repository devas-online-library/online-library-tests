package tech.ada.onlinelibrary.controller;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.ada.onlinelibrary.advice.exception.UnauthorizedLoanException;
import tech.ada.onlinelibrary.domain.Loan;
import tech.ada.onlinelibrary.dto.CreateLoanRequest;
import tech.ada.onlinelibrary.dto.UpdateLoanRequest;
import tech.ada.onlinelibrary.repository.LoanRepository;
import tech.ada.onlinelibrary.service.LoanService;

import java.util.List;
import java.util.Optional;

@RestController
public class LoanController {

    private LoanService loanService;
    private LoanRepository loanRepository;
    private ModelMapper modelMapper;


    @Autowired
    public LoanController(LoanService loanService, LoanRepository loanRepository, ModelMapper modelMapper){
        this.loanService = loanService;
        this.loanRepository = loanRepository;
        this.modelMapper = modelMapper;
    }


    @PostMapping("/library/loans")
    public ResponseEntity<Loan> newLoan(@RequestBody CreateLoanRequest loanRequest) {
        Long userId = loanRequest.getUserId();

        boolean isAuthorized = loanService.checkLoanAuthorization(userId);

        if (isAuthorized) {
            loanService.createLoan(loanRequest);
            return ResponseEntity.ok().build();
        } else {
            throw new UnauthorizedLoanException(userId);
        }
    }

    @GetMapping("/library/loans")
    public ResponseEntity<List<Loan>> getAllLoans(){
        List<Loan> loans = loanRepository.findAll();
        return ResponseEntity.ok(loans);
    }

    @GetMapping(value = "/library/loans", params = {"userId"})
    public ResponseEntity<List<Loan>> getLoansByUserId(@RequestParam Long userId){
        List<Loan> loans = loanRepository.findByUserId(userId);
        return ResponseEntity.ok(loans);
    }

    @PutMapping("/library/loans/return")
    public ResponseEntity<Loan> registerLoanReturn(@RequestBody UpdateLoanRequest loanRequest) {
        Optional<Loan> loanOpt = loanService.registerLoanReturn(loanRequest);
        if (loanOpt.isPresent()) {
            return ResponseEntity.ok(loanOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/library/loans/renewal")
    public ResponseEntity<Loan> renewLoan(@RequestBody UpdateLoanRequest loanRequest) {
        Optional<Loan> loanOpt = loanService.renewLoan(loanRequest);
        if (loanOpt.isPresent()) {
            return ResponseEntity.ok(loanOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/library/loans/{id}")
    public ResponseEntity<Loan> deleteLoan(@PathVariable Long id){
        Optional<Loan> optionalLoan = loanRepository.findById(id);
        if (optionalLoan.isPresent()){
            loanRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else  {
            return ResponseEntity.notFound().build();

        }
    }


}
