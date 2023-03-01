package codingnomads.co.Recipe.API.services;

import codingnomads.co.Recipe.API.exceptions.NoSuchRecipeException;
import codingnomads.co.Recipe.API.models.Recipe;
import codingnomads.co.Recipe.API.models.securitymodels.CustomUserDetails;
import codingnomads.co.Recipe.API.repositories.RecipeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    RecipeRepo recipeRepo;

    @Transactional

    public Recipe createNewRecipe(Recipe recipe) throws IllegalStateException {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        recipe.setUser(userDetails);
        recipe.validate();
        recipe = recipeRepo.save(recipe);
        recipe.generateLocationURI();
        return recipe;
    }

    //TODO Can values be whatever?
    @Cacheable(value = "recipes", key = "#id", sync = true)
    public Recipe getRecipeById(Long id) throws NoSuchRecipeException {
        Optional<Recipe> recipeOptional = recipeRepo.findById(id);

        if (recipeOptional.isEmpty()) {
            throw new NoSuchRecipeException("No recipe with ID " + id + " could be found.");
        }

        Recipe recipe = recipeOptional.get();
        recipe.generateLocationURI();
        return recipe;
    }

    @Cacheable(value = "recipes", key = "#name", sync = true)
    public ArrayList<Recipe> getRecipesByName(String name) throws NoSuchRecipeException {
        ArrayList<Recipe> matchingRecipes = recipeRepo.findByNameContaining(name);

        if (matchingRecipes.isEmpty()) {
            throw new NoSuchRecipeException("No recipes could be found with that name.");
        }

        for (Recipe r : matchingRecipes) {
            r.generateLocationURI();
        }
        return matchingRecipes;
    }

    @Cacheable(value = "recipes", key = "#id", sync = true)
    public ArrayList<Recipe> getAllRecipes() throws NoSuchRecipeException {
        ArrayList<Recipe> recipes = new ArrayList<>(recipeRepo.findAll());

        if (recipes.isEmpty()) {
            throw new NoSuchRecipeException("There are no recipes yet :( feel free to add one though");
        }
        return recipes;
    }

    @Transactional
    @CacheEvict(value = "recipes", allEntries = true)
    public Recipe deleteRecipeById(Long id) throws NoSuchRecipeException {
        try {
            Recipe recipe = getRecipeById(id);
            recipeRepo.deleteById(id);
            return recipe;
        } catch (NoSuchRecipeException e) {
            throw new NoSuchRecipeException(e.getMessage() + " Could not delete.");
        }
    }

    @Transactional
    @CachePut(value = "recipes", key = "#id")
    public Recipe updateRecipe(Recipe recipe, boolean forceIdCheck) throws NoSuchRecipeException {
        try {
            if (forceIdCheck) {
                getRecipeById(recipe.getId());
            }
            recipe.validate();
            Recipe savedRecipe = recipeRepo.save(recipe);
            savedRecipe.generateLocationURI();
            return savedRecipe;
        } catch (NoSuchRecipeException e) {
            throw new NoSuchRecipeException("The recipe you passed in did not have an ID found in the database." +
                                            " Double check that it is correct. Or maybe you meant to POST a recipe not PATCH one.");
        }
    }


    @Cacheable(value = "recipes", key = "#rating")
    public List<Recipe> getRecipesByMinAverage(Double averageRating) throws NoSuchRecipeException {
        List<Recipe> recipes = recipeRepo.findByAverageRatingGreaterThanEqual(averageRating);

        if (recipes.isEmpty()) {
            throw new NoSuchRecipeException("No recipes could be found with or greater than that rating.");
        }
        return recipes;
    }

    @Cacheable(value = "recipes", key = "#name")
    public List<Recipe> getRecipeNameAndAverage(String name, Double minAverageRating) throws NoSuchRecipeException {
        List<Recipe> recipes = recipeRepo.findByNameAndAverageRatingLessThanEqual(name, minAverageRating);

        if (recipes.isEmpty()) {
            throw new NoSuchRecipeException("No recipes could be found with that name.");
        }
        return recipes;
    }

    @Cacheable(value = "recipes", key = "#rating")
    public List<Recipe> getRecipesByRating(Double rating) throws NoSuchRecipeException {
        List<Recipe> recipes = recipeRepo.findByAverageRating(rating);

        if (recipes.isEmpty()) {
            throw new NoSuchRecipeException("No recipes could be found with that rating.");
        }
        return recipes;
    }

    @Cacheable(value = "recipes", key = "#username")
    public List<Recipe> getRecipesByUserName(String username) throws NoSuchRecipeException {
        List<Recipe> recipes = recipeRepo.findAllByUser_Username(username);

        if (recipes.isEmpty()) {
            throw new NoSuchRecipeException("No recipes could be found from that username");
        }
        return recipes;
    }
}