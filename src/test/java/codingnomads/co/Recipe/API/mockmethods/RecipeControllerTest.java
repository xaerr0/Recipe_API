package codingnomads.co.Recipe.API.mockmethods;

import codingnomads.co.Recipe.API.RecipeApiApplication;
import codingnomads.co.Recipe.API.controllers.RecipeController;
import codingnomads.co.Recipe.API.exceptions.NoSuchRecipeException;
import codingnomads.co.Recipe.API.models.Ingredient;
import codingnomads.co.Recipe.API.models.Recipe;
import codingnomads.co.Recipe.API.models.Step;
import codingnomads.co.Recipe.API.services.RecipeService;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(RecipeController.class)
@ContextConfiguration(classes = RecipeApiApplication.class)
@ActiveProfiles("test")
public class RecipeControllerTest {

    @MockBean
    RecipeService recipeService;

    @Autowired
    MockMvc mockMvc;




    @Test
    public void testGetRecipeByIdAndDeleteSuccess() throws Exception {

        Recipe recipe = Recipe.builder()
                .name("Full Proof Water")
                .difficultyRating(5)
                .minutesToMake(5)
                .ingredients(
                        Set.of(Ingredient.builder().amount("1 lb").name("Cold Water").build(),
                                Ingredient.builder().amount("1 lb").name("Hot Water").build())
                )
                .steps(Set.of(Step.builder()
                        .description("Boil cold water").stepNumber(1)
                        .description("Chill hot water").stepNumber(2)
                        .description("Layer water on baking tray").stepNumber(3)
                        .description("Bake water at 350 degrees for 1 hour").stepNumber(4)
                        .description("Chill water in fridge for 7 hours before serving")
                        .build()))
                .build();

        when(recipeService.getRecipeById(anyLong()))
                .thenReturn(recipe);

        mockMvc.perform(get("/recipes/1"))

                //print response
                .andDo(print())
                //expect status 200 OK
                .andExpect(status().isOk())
                //expect return Content-Type header as application/json
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));

        // delete recipe
        recipeService.deleteRecipeById(anyLong());
    }

    @Test
    public void testGetRecipeByIdFailure() throws Exception{
        when(recipeService.getRecipeById(anyLong()))
                .thenThrow(new NoSuchRecipeException("No recipe was found!"));

        mockMvc.perform(get("/recipes/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("No recipe was found!")));
    }

    @Test
    public void testGetRecipesByNameSuccess() throws Exception {
        when(recipeService.getRecipesByName(anyString())).thenReturn(
                new ArrayList<>(Arrays.asList(
                        Recipe.builder()
                                .id(1L)
                                .name("Baked Potato")
                                .difficultyRating(1)
                                .minutesToMake(5)
                                .ingredients(Set.of(Ingredient.builder().amount("1 jar").name("Potato").build()))
                                .steps(Set.of(
                                        Step.builder().description("Grab potato").stepNumber(1).build(),
                                        Step.builder().description("Microwave Potato for 1200 seconds").stepNumber(2).build(),
                                        Step.builder().description("Eat immediately. Don't allow to cool").stepNumber(3).build()
                                ))
                                .build(),

                        Recipe.builder()
                                .id(2L)
                                .name("Raw Potato")
                                .difficultyRating(10)
                                .minutesToMake(10)
                                .ingredients(Set.of(Ingredient.builder().amount("1 jar").name("Potato").build()))
                                .steps(Set.of(
                                                Step.builder().description("Crack open potato").stepNumber(1).build(),
                                                Step.builder().description("Guzzle potato").stepNumber(2).build()
                                ))
                                .build()
                ))
        );

        mockMvc.perform(get("/recipes/search/searched"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(Matchers.containsStringIgnoringCase("potato")))
                .andExpect(jsonPath("$[1].name").value(Matchers.containsStringIgnoringCase("potato")));
    }
}