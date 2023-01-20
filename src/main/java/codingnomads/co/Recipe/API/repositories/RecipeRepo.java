package codingnomads.co.Recipe.API.repositories;

import codingnomads.co.Recipe.API.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface RecipeRepo extends JpaRepository<Recipe, Long> {

    ArrayList<Recipe> findByNameContaining(String name);

    ArrayList<Recipe> findByNameAndAverageRatingLessThanEqual(String name, Double averageRating);

    ArrayList<Recipe> findByAverageRating(Double rating);

    List<Recipe> findAllByUserName(String userName);

    List<Recipe> findByAverageRatingGreaterThanEqual(Double averageRating);

}