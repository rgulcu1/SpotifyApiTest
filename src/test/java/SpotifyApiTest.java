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
    public void shoulgetList() {

        params.put("market", "ES");

        final String playlistId = "3cEYpjA9oz9GiPac4AsH4n";

        final Response playList = playlistService.getPlayList(playlistId, params, ResponseSpec.checkStatusCodeOk());
        final ResponseBody body = playList.getBody();
    }

    @Test
    public void shouldNotContainDeletedAlbum() {

        params.put("market", "ES");

        final Response savedAlbums = currentUserService.getSavedAlbums(params, ResponseSpec.checkStatusCodeOk());
        final List<String> list = savedAlbums.getBody().jsonPath().getList("items.album.id", String.class);

        if (list.isEmpty()) {
            return;
        }
        final String selectedId = list.get(0);

        params.clear();
        params.put("ids",selectedId);

        currentUserService.deleteAlbum(params,ResponseSpec.checkStatusCodeOk());

        final Response containResponse = currentUserService.checkCotainsAlbum(params, ResponseSpec.checkStatusCodeOk());
        final ArrayList<Boolean> responseList = containResponse.getBody().jsonPath().get();
        final Boolean result = responseList.get(0);

        Assert.assertFalse(result);

    }

    @Test
    public void reoreder() {

        final JsonObject foundPlayList = getCurrentUserPlayList();

        if (Objects.isNull(foundPlayList)) {
            return;
        }
        final String selectedId = foundPlayList.get("id").getAsString(); //kişnin sahip olduğu bir playlist bulma

        params.put("market", "TR");

        final Response playlistTracks = playlistService.getPlaylistTracks(selectedId, params, ResponseSpec.checkStatusCodeOk());
        final List<String> trackNames = playlistTracks.getBody().jsonPath().getList("items.track.name", String.class);

        trackNames.forEach(track -> System.out.println(track));
    }

    @Test
    public void searchTest() {

        final String searchKeyword = "Ezhel";
        final String type = StringUtils.mergeStringWithComma(Constant.Type.TRACK,Constant.Type.ALBUM);

        final Map<String, Object> params = new HashMap<>();
        params.put("market", "TR");
        params.put("q",searchKeyword);
        params.put("type", Constant.Type.TRACK);
        params.put("limit",50);

        final Response searchResult = searchService.search(params, ResponseSpec.checkStatusCodeOk());

        List<JsonObject> tracks = searchResult.getBody().jsonPath().getList("tracks.items", JsonObject.class);

        List<JsonObject> copyList = new ArrayList<>(tracks);
        final JsonObjectComparator jsonObjectComparator = new JsonObjectComparator("popularity", Constant.ValueType.INTEGER); // jsona göre compare etme
        copyList.sort(jsonObjectComparator);
        Collections.reverse(copyList);

        final JsonObject selectedTrack = copyList.get(0);
        final String selectedUri = selectedTrack.get("uri").getAsString();

        final JsonObject foundPlayList = getCurrentUserPlayList();

        if (Objects.isNull(foundPlayList)) {
            return;
        }
        final String selectedId = foundPlayList.get("id").getAsString();

        params.clear();
        params.put("uris", selectedUri);

        playlistService.addItemToPlaylist(selectedId,params,ResponseSpec.checkStatusCodeCreated());
    }


    @Test
    public void case1(){

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
