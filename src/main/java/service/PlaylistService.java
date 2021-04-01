package service;

import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import spec.RequestSpec;

import java.util.HashMap;
import java.util.Map;

public class PlaylistService extends RequestSpec {

    public PlaylistService() {
        super("https://api.spotify.com/v1/playlists");
    }

    public Response getPlayList(String playlistId, Map<String, Object> params, ResponseSpecification responseSpecification) {

        final Map<String, Object> pathVariables = new HashMap<>();
        pathVariables.put("playlist_id", playlistId);

        final String endpoint = "/{playlist_id}";
        return getRequestWithPathVariable(params, pathVariables, responseSpecification, endpoint);
    }

    public Response getPlaylistTracks(String playlistId, Map<String, Object> params, ResponseSpecification responseSpecification) {

        final Map<String, Object> pathVariables = new HashMap<>();
        pathVariables.put("playlist_id", playlistId);

        return getRequestWithPathVariable(params,pathVariables,responseSpecification, "/{playlist_id}/tracks");
    }

    public void addItemToPlaylist(String playlistId, Map<String, Object> params, ResponseSpecification responseSpecification) {

        final Map<String, Object> pathVariables = new HashMap<>();
        pathVariables.put("playlist_id", playlistId);
        final JsonObject body = new JsonObject();

        postRequestWithPathVariable(params,body,pathVariables,responseSpecification,"/{playlist_id}/tracks");

    }


}
