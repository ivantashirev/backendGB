package Lesson3;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

public class RecipeTests {
    @BeforeAll
    static void setUP(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void recepiesComplexSearch() {
         JsonPath response = given()
                .queryParam("apiKey", "62e8122bf46941569505d78f6d632a72")
                .queryParam("query", "burger")
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch")
                .body()
                 .jsonPath();
         assertThat(response.get("totalResults"), equalTo(54));
         assertThat(response.get("results[0].id"), equalTo(642539));
         assertThat(response.get("results[1].title"), equalTo("$50,000 Burger"));
         assertThat(response.get("results[2].imageType"), equalTo("jpg"));
    }

    @Test
    void cuisineTestCase() {
        given()
                .queryParam("apiKey", "62e8122bf46941569505d78f6d632a72")
                .contentType("application/x-www-form-urlencoded")
                .param("title", "sushi")
                .expect()
                .body("cuisine", equalTo("Japanese"))
                .body("cuisines", contains("Japanese", "Asian"))
                .body("confidence", equalTo(0.85F))
                .when()
                .post("https://api.spoonacular.com/recipes/cuisine")
                //.prettyPeek()
                .then()
                .statusCode(200);
    }



}
