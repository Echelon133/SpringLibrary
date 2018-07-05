package ml.echelon133.model.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class GenreDto {

    @NotNull
    @Length(min=1, max=50)
    private String name;

    @NotNull
    @Length(min=10, max=1500)
    private String description;

    public GenreDto() {}
    public GenreDto(String name, String description) {
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
