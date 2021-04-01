import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import model.PlaylistBody;
import org.junit.Assert;
import org.testng.annotations.Test;
import spec.ResponseSpec;

import java.util.*;

public class SpotifyApiTest extends BaseTest  {


    @Test
    public void assignmentCase(){

        final String searchKeyword = "Ezhel";

        final Map<String, Object> params = new HashMap<>();
        params.put("market", "TR");
        params.put("q",searchKeyword);
        params.put("type", Constant.Type.ARTIST);

        final Response searchResult = searchService.search(params, ResponseSpec.checkStatusCodeOk());
        final List<JsonObject> artists = searchResult.getBody().jsonPath().getList("artists.items", JsonObject.class);
        final JsonObject selectedArtist = artists.get(0);

        final String selectedArtistName = selectedArtist.get("name").getAsString();
        Assert.assertTrue(selectedArtistName.contains(searchKeyword));

        final String selectedArtistId = selectedArtist.get("id").getAsString();

        params.clear();
        params.put("market", "TR");

        final Response artistTopTracks = artistService.getArtistTopTracks(selectedArtistId, params, ResponseSpec.checkStatusCodeOk());
        final List<JsonObject> topTracks = artistTopTracks.getBody().jsonPath().getList("tracks", JsonObject.class);

        final String randomTrackArtistName = topTracks.get(0).get("artists").getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString();
        Assert.assertEquals(selectedArtistName,randomTrackArtistName);


        final ArrayList<JsonObject> copyList = new ArrayList<>(topTracks);
        final JsonObjectComparator comparator = new JsonObjectComparator("popularity", Constant.ValueType.INTEGER);
        copyList.sort(comparator);

        Collections.reverse(copyList);

        final JsonObject firstPopularTrack = copyList.get(0);
        final JsonObject secondPopularTrack = copyList.get(1);
        final JsonObject thirdPopularTrack = copyList.get(2);

        final Response currentUser = currentUserService.getCurrentUser(ResponseSpec.checkStatusCodeOk());
        final String currentUserId = currentUser.getBody().jsonPath().getString("id");

        final String playlistName = "Sinav playlist";
        final PlaylistBody playlistBody = new PlaylistBody(playlistName, "mock");

        userService.createPlaylist(currentUserId,playlistBody,params,ResponseSpec.checkStatusCodeCreated());

        final JsonObject createdPlaylist = getCurrentUserPlayList();

        final String createdPlaylistName = createdPlaylist.get("name").getAsString();
        Assert.assertEquals(createdPlaylistName,playlistName);

        String id = createdPlaylist.get("id").getAsString();

        final String uri1 = firstPopularTrack.get("uri").getAsString();
        final String uri2 = secondPopularTrack.get("uri").getAsString();
        final String uri3 = thirdPopularTrack.get("uri").getAsString();

        final String uris = StringUtils.mergeStringWithComma(StringUtils.mergeStringWithComma(uri1, uri2), uri3);

        params.clear();
        params.put("uris",uris);
        playlistService.addItemToPlaylist(id,params,ResponseSpec.checkStatusCodeCreated());
    }

}
