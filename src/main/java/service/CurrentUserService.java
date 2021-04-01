package service;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import spec.RequestSpec;

import java.util.HashMap;
import java.util.Map;

public class CurrentUserService extends RequestSpec {

    public CurrentUserService() {
        super("https://api.spotify.com/v1/me");
    }

    public Response getSavedAlbums(Map<String, Object> params, ResponseSpecification responseSpecification) {

        return getRequest(params, responseSpecification, "/albums");
    }

    public void deleteAlbum(Map<String, Object> params, ResponseSpecification responseSpecification) {

        super.deleteRequest(params,responseSpecification,"/albums");
    }

    public Response checkCotainsAlbum(Map<String, Object> params, ResponseSpecification responseSpecification) {

        return super.getRequest(params,responseSpecification,"/albums/contains");
    }

    public Response getPlaylists(ResponseSpecification responseSpecification) {

        return super.getRequest(new HashMap<>(),responseSpecification,"/playlists");
    }

    public Response getCurrentUser(ResponseSpecification responseSpecification) {

        return super.getRequest(new HashMap<>(), responseSpecification, "");
    }
}
