package Lesson3;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

public class RecipeTests {
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
                .log(LogDetail.ALL)
                .build();
    }
    @BeforeEach
    void setUP(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void recipesComplexSearch() {
         given()
                 .spec(requestSpecification)
                 .queryParam("query", "burger")
                 .expect()
                 .body("totalResults", equalTo(54))
                 .body("results[0].id", equalTo(642539))
                 .body("results[1].title", equalTo("$50,000 Burger"))
                 .body("results[2].imageType", equalTo("jpg"))
                 .when()
                 .get("https://api.spoonacular.com/recipes/complexSearch")
                 .then()
                 .spec(responseSpecification);
    }

    @Test
    void cuisineTestCase() {
        given()
                .spec(requestSpecification)
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
                .spec(responseSpecification);
    }

}
