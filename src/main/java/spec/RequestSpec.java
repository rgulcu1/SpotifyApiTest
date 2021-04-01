package spec;

import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.Getter;

import java.net.URI;
import java.net.URL;
import java.util.Map;

@Getter
public class RequestSpec {

    private final RequestSpecification requestSpecification;

    private final String token = "BQDrfIOfi_lZEYiO7OjYNpg7WGHEIA0IakJQVhSbVveD28nI_H8cZ1l_6VkEwdxnvKlp81zOMWVnsdSx7pccdST3V0vKFKftTB456t12JDAKOxF07SB0DTlflMMzletLXMZj9zc1ygy0ueiCo0D6GqFhRnnqUv-iyGtLR49TjVR8nunA2NWN9KWORMZuQ68ySlLvBlGFy_juGUNaPJ1hOMEbEjefF5FHylLEwfpvtdfr8edoiVTD6NsleRl7dh0pldlRPmFznYa49sSqF8APSHpuRW5v_0l1SuD1On2G";

    public RequestSpec(String baseUrl) {

        this.requestSpecification = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType("application/json")
                .setAccept("application/json")
                .addHeader("Authorization","Bearer "+token)
                .build();
    }

    public Response getRequest(Map<String, Object> params, ResponseSpecification responseSpecification, String endpoint) {

        return RestAssured.given()
                .spec(requestSpecification)
                .queryParams(params)
                .get(endpoint)
                .then()
                .spec(responseSpecification)
                .extract()
                .response();
    }

    public void postRequest(Map<String, Object> params, ResponseSpecification responseSpecification, String endpoint) {

         RestAssured.given()
                .spec(requestSpecification)
                .queryParams(params)
                .post(endpoint)
                .then()
                .spec(responseSpecification);
    }

    public Response getRequestWithPathVariable(Map<String, Object> params, Map<String, Object> pathVariables, ResponseSpecification responseSpecification, String endpoint) {

        return RestAssured.given()
                .spec(requestSpecification)
                .queryParams(params)
                .get(endpoint,pathVariables)
                .then()
                .spec(responseSpecification)
                .extract()
                .response();
    }


    public void postRequestWithPathVariable(Map<String, Object> params, Object body, Map<String, Object> pathVariables, ResponseSpecification responseSpecification, String endpoint) {

         RestAssured.given()
                .spec(requestSpecification)
                .queryParams(params)
                 .body(body)
                .post(endpoint,pathVariables)
                .then()
                .spec(responseSpecification);
    }

    public void deleteRequest(Map<String, Object> params, ResponseSpecification responseSpecification, String endpoint) {

         RestAssured.given()
                .spec(requestSpecification)
                .queryParams(params)
                .delete(endpoint)
                .then()
                .spec(responseSpecification);
    }
}
