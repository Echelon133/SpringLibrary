package ml.echelon133.model.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class NewEntryDto {

    @NotNull
    @PositiveOrZero
    private Long borrowedBookId;

    @NotNull
    private String borrowingUserUsername;

    public Long getBorrowedBookId() {
        return borrowedBookId;
    }

    public void setBorrowedBookId(Long borrowedBookId) {
        this.borrowedBookId = borrowedBookId;
    }

    public String getBorrowingUserUsername() {
        return borrowingUserUsername;
    }

    public void setBorrowingUserUsername(String borrowingUserUsername) {
        this.borrowingUserUsername = borrowingUserUsername;
    }
}
