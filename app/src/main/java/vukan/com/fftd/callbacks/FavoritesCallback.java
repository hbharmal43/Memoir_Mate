package vukan.com.fftd.callbacks;

import java.util.List;

import vukan.com.fftd.models.FavoritePost;

public interface FavoritesCallback {
    void onCallback(List<FavoritePost> posts);
}