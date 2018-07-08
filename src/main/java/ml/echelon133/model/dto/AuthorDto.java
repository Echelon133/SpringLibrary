package ml.echelon133.model.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class AuthorDto {

    @NotNull
    @Length(min=1, max=100)
    private String name;

    @NotNull
    @Length(min=10, max=3000)
    private String description;

    public AuthorDto() {}
    public AuthorDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
