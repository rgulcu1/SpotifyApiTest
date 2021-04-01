import com.google.gson.JsonObject;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import spec.ResponseSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseTest implements BaseServiceTest{
    final Map<String, Object> params = new HashMap<>();

    @AfterMethod
    public void afterMethod(){
        params.clear();
    }


    public JsonObject getCurrentUserPlayList(){

        final Response playlistsResponse = currentUserService.getPlaylists(ResponseSpec.checkStatusCodeOk());
        final List<JsonObject> playLists = playlistsResponse.getBody().jsonPath().getList("items", JsonObject.class);

        if(playLists.isEmpty()) {
            return null;
        }

        final Response currentUser = currentUserService.getCurrentUser(ResponseSpec.checkStatusCodeOk());
        final String currentUserId = currentUser.getBody().jsonPath().getString("id");
        final JsonObject foundPlayList = playLists.stream().filter(playlist -> playlist.getAsJsonObject("owner").get("id").getAsString().equals(currentUserId)).findFirst().orElse(null);

        return foundPlayList;
    }
}
