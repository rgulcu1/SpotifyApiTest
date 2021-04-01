import service.*;

public interface BaseServiceTest {

    final PlaylistService playlistService = new PlaylistService();
    final CurrentUserService currentUserService = new CurrentUserService();
    final SearchService searchService = new SearchService();
    final ArtistService artistService = new ArtistService();
    final UserService userService = new UserService();
}
