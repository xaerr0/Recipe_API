package codingnomads.co.Recipe.API.models;

import codingnomads.co.Recipe.API.models.securitymodels.CustomUserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "recipe")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn
    @JsonIgnore
    private CustomUserDetails user;

    @Column(nullable = false)
    private Integer rating;

    private String description;

    @ManyToOne
    @JoinColumn(
            name = "recipeId",
            nullable = false,
            foreignKey = @ForeignKey
    )
    @JsonIgnore
    private Recipe recipe;

    public void setRating(int rating) {
        if(rating <= 0 || rating > 10) {
            throw new IllegalStateException("Rating must be between 0 and 10");
        }
        this.rating = rating;
    }

    public void validateRating() {
        if (rating == null) {
            throw new IllegalStateException("You must include a rating with your review.");
        }
        if (rating <= 0 || rating > 10) {
            throw new IllegalStateException("Rating must be between 0 and 10.");
        }
    }

    public String getAuthor() {
        return user.getUsername();
    }
}