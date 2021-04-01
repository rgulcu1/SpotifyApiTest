package service;

import com.google.gson.JsonObject;
import io.restassured.specification.ResponseSpecification;
import spec.RequestSpec;

import java.util.HashMap;
import java.util.Map;

public class UserService extends RequestSpec {

    public UserService() { super("https://api.spotify.com/v1/users"); }


    public void createPlaylist(String userId, Object body, Map<String, Object> params, ResponseSpecification responseSpecification) {

        final Map<String, Object> pathVariables = new HashMap<>();
        pathVariables.put("user_id", userId);

        postRequestWithPathVariable(params,body,pathVariables,responseSpecification,"/{user_id}/playlists");
    }
}
