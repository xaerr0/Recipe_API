package codingnomads.co.Recipe.API.mockmethods;

import codingnomads.co.Recipe.API.RecipeApiApplication;
import codingnomads.co.Recipe.API.controllers.RecipeController;
import codingnomads.co.Recipe.API.exceptions.NoSuchRecipeException;
import codingnomads.co.Recipe.API.models.Ingredient;
import codingnomads.co.Recipe.API.models.Recipe;
import codingnomads.co.Recipe.API.models.Step;
import codingnomads.co.Recipe.API.services.RecipeService;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
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

}