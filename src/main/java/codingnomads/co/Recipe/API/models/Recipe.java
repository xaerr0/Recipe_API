package codingnomads.co.Recipe.API.models;


import codingnomads.co.Recipe.API.exceptions.CmonBroException;
import codingnomads.co.Recipe.API.models.securitymodels.CustomUserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@Builder
@AllArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotNull
//    @JoinColumn(name = "user_name_id")
//    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer minutesToMake;

    @Column(nullable = false)
    private Integer difficultyRating;

    @ManyToOne(optional = false)
    @JoinColumn
    @JsonIgnore
    private CustomUserDetails user;

    @Column
    private double averageRating;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "recipeId",
            nullable = false
    )
    private Collection<Ingredient> ingredients;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "recipeId",
            nullable = false,
            foreignKey = @ForeignKey
    )
    private Collection<Step> steps;

    @OneToMany(mappedBy = "recipe")
    private Collection<Review> reviews;

    @Transient
    @JsonIgnore
    private URI locationURI;

    public Recipe() {
        ingredients = new ArrayList<>();
        steps = new ArrayList<>();
    }

    public void setDifficultyRating(int difficultyRating) {

        if(difficultyRating < 0 || difficultyRating > 10) {
            throw new IllegalStateException("difficulty rating must be between 0 and 10");
        }

        this.difficultyRating = difficultyRating;
    }

    public void validate() throws IllegalStateException {
        if(ingredients.size() == 0) {
            throw new IllegalStateException("You have to have at least one ingredient for you recipe!");
        }else if(steps.size() == 0) {
            throw new IllegalStateException("You have to include at least one step for your recipe!");
        }
    }

    public void generateLocationURI() {
        try {
            locationURI = new URI(
                    ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/recipes/")
                            .path(String.valueOf(id))
                            .toUriString());
        }catch (URISyntaxException e) {
            //Exception should stop here.
        }
    }

    public void calculateAverageRating() {
        averageRating = 0.0;
        if (reviews != null && !reviews.isEmpty()) {
            for (Review review : reviews) {
                averageRating += review.getRating();
            }
            averageRating /= reviews.size();
        }
    }

    public String getAuthor() {
        return user.getUsername();
    }
}