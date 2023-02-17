package codingnomads.co.Recipe.API.repositories;

import codingnomads.co.Recipe.API.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {

    ArrayList<Review> findByUser_Username(String username);
    Optional<Review> findById(Long id);
}