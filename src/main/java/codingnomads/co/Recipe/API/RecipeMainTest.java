package codingnomads.co.Recipe.API;

import codingnomads.co.Recipe.API.models.Ingredient;
import codingnomads.co.Recipe.API.models.Recipe;
import codingnomads.co.Recipe.API.models.Review;
import codingnomads.co.Recipe.API.models.Step;
import codingnomads.co.Recipe.API.models.securitymodels.CustomUserDetails;
import codingnomads.co.Recipe.API.repositories.RecipeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import java.util.Set;

@SpringBootApplication
@Profile("test")
public class RecipeMainTest implements CommandLineRunner {

    @Autowired
    RecipeRepo recipeRepo;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("STARTING WITH TEST DATABASE SETUP");
        if (recipeRepo.findAll().isEmpty()) {

            Ingredient ingredient = Ingredient.builder().name("flour").state("dry").amount("2 cups").build();
            Step step1 = Step.builder().description("put flour in bowl").stepNumber(1).build();
            Step step2 = Step.builder().description("eat it?").stepNumber(2).build();

            Review review = Review.builder().description("tasted pretty bad").rating(2).user(CustomUserDetails.builder()
                    .username("idfk").build()).build();

            Recipe recipe1 = Recipe.builder()
                    .name("test recipe")
                    .difficultyRating(10)
                    .minutesToMake(2)
                    .ingredients(Set.of(ingredient))
                    .steps(Set.of(step1, step2))
                    .reviews(Set.of(review))
                    .user(CustomUserDetails.builder().username("Bilbert").build())
                    .build();

            recipeRepo.save(recipe1);

            ingredient.setId(null);
            Recipe recipe2 = Recipe.builder()
                    .steps(Set.of(Step.builder().description("test").build()))
                    .ingredients(Set.of(Ingredient.builder().name("test ing").amount("1").state("dry").build()))
                    .name("another test recipe")
                    .difficultyRating(10)
                    .minutesToMake(2)
                    .user(CustomUserDetails.builder().username("Rupbert").build())
                    .build();
            recipeRepo.save(recipe2);

            Recipe recipe3 = Recipe.builder()
                    .steps(Set.of(Step.builder().description("test 2").build()))
                    .ingredients(Set.of(Ingredient.builder().name("test ing 2").amount("2").state("wet").build()))
                    .name("another another test recipe")
                    .difficultyRating(5)
                    .minutesToMake(2)
                    .user(CustomUserDetails.builder().username("Johnnifer").build())
                    .build();

            recipeRepo.save(recipe3);

            Recipe recipe4 = Recipe.builder()
                    .user(CustomUserDetails.builder().username("Stevie").build())
                    .name("chocolate and potato chips")
                    .difficultyRating(10)
                    .minutesToMake(1)
                    .ingredients(Set.of(
                            Ingredient.builder().name("potato chips").amount("1 bag").build(),
                            Ingredient.builder().name("chocolate").amount("1 bar").build()))
                    .steps(Set.of(
                            Step.builder().stepNumber(1).description("eat both items together").build()))
                    .reviews(Set.of(
                            Review.builder().user(CustomUserDetails.builder().
                                    username("ben").build()).rating(10).description("this stuff is so good").build()
                    ))
                    .build();

            recipeRepo.save(recipe4);
            System.out.println("FINISHED TEST DATABASE SETUP");
        }
    }
}