package codingnomads.co.Recipe.API.controllers;

import codingnomads.co.Recipe.API.exceptions.CmonBroException;
import codingnomads.co.Recipe.API.exceptions.NoSuchRecipeException;
import codingnomads.co.Recipe.API.exceptions.NoSuchReviewException;
import codingnomads.co.Recipe.API.models.Recipe;
import codingnomads.co.Recipe.API.models.Review;
import codingnomads.co.Recipe.API.models.securitymodels.CustomUserDetails;
import codingnomads.co.Recipe.API.services.ReviewService;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable("id") Long id) {
        try {
            Review
                    retrievedReview = reviewService.getReviewById(id);
            return ResponseEntity.ok(retrievedReview);
        } catch (IllegalStateException | NoSuchReviewException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/recipe/{recipeId}")
    @Cacheable(value = "review")
    public ResponseEntity<?> getReviewByRecipeId(@PathVariable("recipeId") Long recipeId) {
        try {
            ArrayList<Review> reviews = reviewService.getReviewByRecipeId(recipeId);
            return ResponseEntity.ok(reviews);
        } catch (NoSuchRecipeException | NoSuchReviewException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{username}")
    @CachePut(value = "reviews", key = "#username")
    public ResponseEntity<?> getReviewByUsername(@PathVariable("username") String username) {
        try {
            ArrayList<Review> reviews = reviewService.getReviewByUsername(username);
            return ResponseEntity.ok(reviews);
        } catch (NoSuchReviewException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{recipeId}")
    @CachePut(value = "reviews", key = "#id")
    public ResponseEntity<?> postNewReview(@RequestBody Review review,
                                           @PathVariable("recipeId") Long recipeId, Authentication authentication) {
        try {
            review.validateRating();
            review.setUser((CustomUserDetails) authentication.getPrincipal());
            Recipe insertedRecipe = reviewService.postNewReview(review, recipeId);
            return ResponseEntity.created(insertedRecipe.getLocationURI()).body(insertedRecipe);
        } catch (NoSuchRecipeException | CmonBroException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(#id, 'Review', 'delete')")
    @CacheEvict(value = "reviews", key = "#id", allEntries = true)
    public ResponseEntity<?> deleteReviewById(@PathVariable("id") Long id) {
        try {
            Review review = reviewService.deleteReviewById(id);
            return ResponseEntity.ok(review);
        } catch (NoSuchReviewException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping
    @PreAuthorize("hasPermission(#reviewToUpdate.id, 'Review', 'edit')")
    @CachePut(value = "reviews", key = "#id")
    public ResponseEntity<?> updateReviewById(@RequestBody Review reviewToUpdate) {
        try {
            Review review = reviewService.updateReviewById(reviewToUpdate);
            return ResponseEntity.ok(review);
        } catch (NoSuchReviewException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}