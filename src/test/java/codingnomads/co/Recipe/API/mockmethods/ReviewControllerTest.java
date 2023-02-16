//package codingnomads.co.Recipe.API.mockmethods;
//
//
//import codingnomads.co.Recipe.API.RecipeApiApplication;
//import codingnomads.co.Recipe.API.controllers.ReviewController;
//import codingnomads.co.Recipe.API.models.Review;
//import codingnomads.co.Recipe.API.services.ReviewService;
//import org.hamcrest.Matchers;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//@WebMvcTest(ReviewController.class)
//@ContextConfiguration(classes = RecipeApiApplication.class)
//@ActiveProfiles("test")
//public class ReviewControllerTest {
//
//    @MockBean
//    ReviewService reviewService;
//
//    @Autowired
//    MockMvc mockMvc;
//
//
//    @Test
//    public void testGetReviewById() throws Exception {
//
//        Review review = Review.builder()
//                .username("Bobbins")
//                .rating(1)
//                .description("Everytime I tried this recipe, I burnt the water.")
//                .build();
//
//        when(reviewService.getReviewById(anyLong()))
//                .thenReturn(review);
//
//        mockMvc.perform(get("/review/2"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
//    }
//
//    @Test
//    public void testGetReviewsByUsername() throws Exception {
//        when(reviewService.getReviewByUsername(anyString())).thenReturn(
//                new ArrayList<>(Arrays.asList(
//                        Review.builder()
//                                .id(1L)
//                                .username("JankyWilliams")
//                                .rating(9)
//                                .description("So good! lol")
//                                .build(),
//                        Review.builder()
//                                .id(2L)
//                                .username("JankyWilliams")
//                                .rating(3)
//                                .description("The best yet!")
//                                .build()
//                ))
//        );
//
//        mockMvc.perform(get("/review/user/JankyWilliams"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json"))
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].username").value(Matchers.containsStringIgnoringCase("janky")))
//                .andExpect(jsonPath("$[1].username").value(Matchers.containsStringIgnoringCase("janky")));
//    }
//}