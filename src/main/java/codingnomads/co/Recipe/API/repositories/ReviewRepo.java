package codingnomads.co.Recipe.API.repositories;

import codingnomads.co.Recipe.API.models.Recipe;
import codingnomads.co.Recipe.API.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {

    ArrayList<Review> findByUsername(String username);
}