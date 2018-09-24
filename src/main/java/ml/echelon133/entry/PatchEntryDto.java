package ml.echelon133.entry;

import javax.validation.constraints.NotNull;

public class PatchEntryDto {

    @NotNull
    private Boolean returned;

    public Boolean getReturned() {
        return returned;
    }

    public void setReturned(Boolean returned) {
        this.returned = returned;
    }
}
