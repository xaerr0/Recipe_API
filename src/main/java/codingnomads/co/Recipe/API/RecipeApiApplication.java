package codingnomads.co.Recipe.API;

import codingnomads.co.Recipe.API.models.Ingredient;
import codingnomads.co.Recipe.API.models.Recipe;
import codingnomads.co.Recipe.API.models.Step;
import codingnomads.co.Recipe.API.models.UserMeta;
import codingnomads.co.Recipe.API.models.securitymodels.CustomUserDetails;
import codingnomads.co.Recipe.API.models.securitymodels.Role;
import codingnomads.co.Recipe.API.repositories.RecipeRepo;
import codingnomads.co.Recipe.API.repositories.ReviewRepo;
import codingnomads.co.Recipe.API.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Set;

@SpringBootApplication
@EnableCaching
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
            UserMeta userMeta2 = UserMeta.builder().name("test user 3").email("email3@email.com").build();
            CustomUserDetails userDetails2
                    = new CustomUserDetails("USER3",
                    encoder.encode("user3"),
                    Arrays.asList(
                            new Role(Role.Roles.ROLE_USER),
                            new Role(Role.Roles.ROLE_ADMIN)),
                    userMeta2);

            userDetails2.setAccountNonExpired(true);
            userDetails2.setAccountNonLocked(true);
            userDetails2.setCredentialsNonExpired(true);
            userDetails2.setEnabled(true);
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