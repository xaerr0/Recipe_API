package codingnomads.co.Recipe.API.controllers;

import codingnomads.co.Recipe.API.exceptions.NoSuchRecipeException;
import codingnomads.co.Recipe.API.models.Recipe;
import codingnomads.co.Recipe.API.models.securitymodels.CustomUserDetails;
import codingnomads.co.Recipe.API.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    RecipeService recipeService;

    @PostMapping
    public ResponseEntity<?> createNewRecipe(@RequestBody Recipe recipe, Authentication authentication) {
        try {
            recipe.setUser((CustomUserDetails) authentication.getPrincipal());
            Recipe insertedRecipe = recipeService.createNewRecipe(recipe);
            return ResponseEntity.created(insertedRecipe.getLocationURI()).body(insertedRecipe);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecipeById(@PathVariable("id") Long id) {
        try {
            Recipe recipe = recipeService.getRecipeById(id);
            return ResponseEntity.ok(recipe);
        } catch (NoSuchRecipeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllRecipes() {
        try {
            return ResponseEntity.ok(recipeService.getAllRecipes());
        } catch (NoSuchRecipeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<?> getRecipesByName(@PathVariable("name") String name) {
        try {
            ArrayList<Recipe> matchingRecipes = recipeService.getRecipesByName(name);
            return ResponseEntity.ok(matchingRecipes);
        } catch (NoSuchRecipeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    //make sure that a user is either an admin or the owner of the recipe before they are allowed to delete
    @PreAuthorize("hasPermission(#id, 'Recipe', 'delete')")
    public ResponseEntity<?> deleteRecipeById(@PathVariable("id") Long id) {
        try {
            Recipe deletedRecipe = recipeService.deleteRecipeById(id);
            return ResponseEntity.ok("The recipe with ID " + deletedRecipe.getId() +
                                     " and name " + deletedRecipe.getName() + " was deleted.");
        } catch (NoSuchRecipeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    //make sure that a user is either an admin or the owner of the recipe before they are allowed to update
    @PreAuthorize("hasPermission(#updatedRecipe.id, 'Recipe', 'edit')")
    public ResponseEntity<?> updateRecipe(@RequestBody Recipe updatedRecipe) {
        try {
            Recipe returnedUpdatedRecipe = recipeService.updateRecipe(updatedRecipe, true);
            return ResponseEntity.ok(returnedUpdatedRecipe);
        } catch (NoSuchRecipeException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/search/rating/{rating}")
    public ResponseEntity<?> getRecipesByMinAvgRating(@PathVariable("rating)") Double rating) {
        try {
            return ResponseEntity.ok(recipeService.getRecipesByMinAverage(rating));
        } catch (NoSuchRecipeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

//    @GetMapping("/search/{rating}")
//    public ResponseEntity<?> getRecipesByRating(@PathVariable("rating") Double rating) {
//        try {
//            return ResponseEntity.ok("The recipes with a rating of " + rating + "are " +
//                                     recipeService.getRecipesByRating(rating));
//        } catch (NoSuchRecipeException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//    }

    @GetMapping("/search/username/{username}")
    public ResponseEntity<?> getRecipesByUserName(@PathVariable("username") String username) {
        try {
            return ResponseEntity.ok(recipeService.getRecipesByUserName(username));
        } catch (NoSuchRecipeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}