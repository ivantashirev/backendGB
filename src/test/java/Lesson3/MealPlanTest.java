package Lesson3;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class MealPlanTest {
    String id;
    ResponseSpecification responseSpecification = null;
    RequestSpecification requestSpecification = null;
    @BeforeEach
    void beforeTest() {
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(5000L))
                .build();
    }
    @BeforeEach
    void beforeRequest(){
        requestSpecification = new RequestSpecBuilder()
                .addQueryParam("apiKey", "62e8122bf46941569505d78f6d632a72")
                .addQueryParam("hash", "6a0265e22571961b3da37d3d9ce52c58a06cc7fd")
                .log(LogDetail.ALL)
                .build();
    }
    @BeforeEach
    void setUP(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
    @Test
    void addMealToMealPlan() {
        id = given()
                .spec(requestSpecification)
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
                .spec(requestSpecification
                )
                .delete("https://api.spoonacular.com/mealplanner/your-users-name599/items/"+id)
                .then()
                .statusCode(200);
    }
}
