package codingnomads.co.Recipe.API;

import codingnomads.co.Recipe.API.models.*;
import codingnomads.co.Recipe.API.models.securitymodels.CustomUserDetails;
import codingnomads.co.Recipe.API.models.securitymodels.Role;
import codingnomads.co.Recipe.API.repositories.RecipeRepo;
import codingnomads.co.Recipe.API.repositories.ReviewRepo;
import codingnomads.co.Recipe.API.repositories.UserRepo;
import codingnomads.co.Recipe.API.security.userservices.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static codingnomads.co.Recipe.API.models.securitymodels.Role.Roles.ROLE_ADMIN;
import static codingnomads.co.Recipe.API.models.securitymodels.Role.Roles.ROLE_USER;

@SpringBootApplication
public class RecipeApiApplication {

    @Autowired
    RecipeRepo recipeRepo;

    @Autowired
    ReviewRepo reviewRepo;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserRepo userRepo;

    public static void main(String[] args) {
        SpringApplication.run(RecipeApiApplication.class, args);

    }

    @Bean
    public CommandLineRunner loadInitialData() {
        return (args) -> {
            UserMeta userMeta2 = UserMeta.builder().name("test user 9").email("email9@email.com").build();
            CustomUserDetails userDetails2
                    = new CustomUserDetails("USER9",
                    encoder.encode("user9"),
                    Arrays.asList(
                            new Role(Role.Roles.ROLE_USER),
                            new Role(Role.Roles.ROLE_ADMIN)),
                    userMeta2);

            userRepo.save(userDetails2);

            Recipe recipe2 = Recipe.builder()
                    .name("chocolate and potato chips")
                    .difficultyRating(10)
                    .minutesToMake(1)
                    .user(userDetails2)
                    .ingredients(Set.of(
                            Ingredient.builder().name("potato chips").amount("1 bag").build(),
                            Ingredient.builder().name("chocolate").amount("1 bar").build()))
                    .steps(Set.of(
                            Step.builder().stepNumber(1).description("eat both items together").build()))
                    .build();

            recipeRepo.save(recipe2);
//            reviewRepo.save(Review.builder().user(userDetails2).recipe(recipe2).rating(10).description("this stuff is so good").build());
        };
    }
}