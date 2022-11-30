package Lesson3;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class MealPlanTest {
    String id;

    @Test
    void addMealToMealPlan() {
        id = given()
                .queryParam("hash", "6a0265e22571961b3da37d3d9ce52c58a06cc7fd")
                .queryParam("apiKey", "62e8122bf46941569505d78f6d632a72")
                .body("{\n"
                        + " \"date\": 1644881179,\n"
                        + " \"slot\": 1,\n"
                        + " \"position\": 0,\n"
                        + " \"type\": \"INGREDIENTS\",\n"
                        + " \"value\": {\n"
                        + " \"ingredients\": [\n"
                        + " {\n"
                        + " \"name\": \"1 bullet for my head\"\n"
                        + " }\n"
                        + " ]\n"
                        + " }\n"
                        + "}")
                .expect()
                .body("status", equalTo("success"))
                .when()
                .post("https://api.spoonacular.com/mealplanner/your-users-name599/items")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id")
                .toString();
    }
    @AfterEach
    void tearDown() {
        given()
                .queryParam("hash", "6a0265e22571961b3da37d3d9ce52c58a06cc7fd")
                .queryParam("apiKey", "62e8122bf46941569505d78f6d632a72")
                .delete("https://api.spoonacular.com/mealplanner/your-users-name599/items/"+id)
                .then()
                .statusCode(200);
    }
}
