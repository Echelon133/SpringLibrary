package ml.echelon133.entry;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class NewEntryDto {

    @NotNull
    @Positive
    private Long borrowedBookId;

    @NotNull
    private String borrowerUsername;

    public Long getBorrowedBookId() {
        return borrowedBookId;
    }

    public void setBorrowedBookId(Long borrowedBookId) {
        this.borrowedBookId = borrowedBookId;
    }

    public String getBorrowerUsername() {
        return borrowerUsername;
    }

    public void setBorrowerUsername(String borrowerUsername) {
        this.borrowerUsername = borrowerUsername;
    }
}
