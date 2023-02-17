package codingnomads.co.Recipe.API.services;

import codingnomads.co.Recipe.API.exceptions.CmonBroException;
import codingnomads.co.Recipe.API.exceptions.NoSuchRecipeException;
import codingnomads.co.Recipe.API.exceptions.NoSuchReviewException;
import codingnomads.co.Recipe.API.models.Recipe;
import codingnomads.co.Recipe.API.models.Review;
import codingnomads.co.Recipe.API.repositories.ReviewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    ReviewRepo reviewRepo;

    @Autowired
    RecipeService recipeService;

    public Review getReviewById(Long id) throws NoSuchReviewException {
        Optional<Review> review = reviewRepo.findById(id);

        if (review.isEmpty()) {
            throw new NoSuchReviewException("The review with ID " + id + " could not be found.");
        }
        return review.get();
    }

    public ArrayList<Review> getReviewByRecipeId(Long recipeId) throws NoSuchRecipeException, NoSuchReviewException {
        Recipe recipe = recipeService.getRecipeById(recipeId);

        ArrayList<Review> reviews = new ArrayList<>(recipe.getReviews());

        if (reviews.isEmpty()) {
            throw new NoSuchReviewException("There are no reviews for this recipe.");
        }
        return reviews;
    }

    //TODO Fix this
    public ArrayList<Review> getReviewByUsername(String username) throws NoSuchReviewException {
        ArrayList<Review> reviews = reviewRepo.findByUser_Username(username);
        if (reviews.isEmpty()) {
            throw new NoSuchReviewException("No reviews could be found for username " + username);
        }
        return reviews;
    }

    public Recipe postNewReview(Review review, Long recipeId) throws CmonBroException, NoSuchRecipeException {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        recipe.getReviews().add(review);
        recipe.calculateAverageRating();
        if (recipe.getAuthor().equals(review.getAuthor())) {
            throw new CmonBroException("C'mon Bro we all know you wrote this recipe.");
        } else {
            recipeService.updateRecipe(recipe, false);
        }
        return recipe;
    }

    public Review deleteReviewById(Long id) throws NoSuchReviewException {
        Review review = getReviewById(id);

        if (null == review) {
            throw new NoSuchReviewException("The review you are trying to delete does not exist.");
        }
        reviewRepo.deleteById(id);
        return review;
    }

    public Review updateReviewById(Review reviewToUpdate) throws NoSuchReviewException {
        try {
            Review review = getReviewById(reviewToUpdate.getId());
        } catch (NoSuchReviewException e) {
            throw new NoSuchReviewException("The review you are trying to update. Maybe you meant to create one? If not," +
                                            "please double check the ID you passed in.");
        }
        reviewRepo.save(reviewToUpdate);
        return reviewToUpdate;
    }
}