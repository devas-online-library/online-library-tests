package tech.ada.onlinelibrary.dto;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UpdateLoanRequest {

    private Long loanId;

    public UpdateLoanRequest(Long loanId) {
        this.loanId = loanId;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

}
