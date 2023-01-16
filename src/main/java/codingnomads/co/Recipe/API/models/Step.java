package codingnomads.co.Recipe.API.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Step {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private int stepNumber;

    @NotNull
    private String description;
}