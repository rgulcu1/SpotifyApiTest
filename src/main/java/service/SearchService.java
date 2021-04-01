package service;

import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import spec.RequestSpec;

import java.util.Map;

public class SearchService extends RequestSpec {

    public SearchService() { super("https://api.spotify.com/v1/search"); }

    public Response search(Map<String, Object> params, ResponseSpecification responseSpecification) {

        return getRequest(params,responseSpecification,"");
    }
}
