package service;

import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import spec.RequestSpec;

import java.util.HashMap;
import java.util.Map;

public class ArtistService extends RequestSpec {

    public ArtistService() {
        super("https://api.spotify.com/v1/artists");
    }


    public Response getArtistTopTracks(String artistId, Map<String, Object> params, ResponseSpecification responseSpecification) {

        final Map<String, Object> pathVariables = new HashMap<>();
        pathVariables.put("id", artistId);

        return getRequestWithPathVariable(params,pathVariables,responseSpecification,"/{id}/top-tracks");
    }

}
