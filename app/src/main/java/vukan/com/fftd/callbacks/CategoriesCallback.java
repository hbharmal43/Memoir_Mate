package vukan.com.fftd.callbacks;

import java.util.List;

import vukan.com.fftd.models.PostCategory;

public interface CategoriesCallback {
    void onCallback(List<PostCategory> categories);
}