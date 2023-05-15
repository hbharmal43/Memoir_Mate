package vukan.com.fftd.callbacks;

import java.util.List;

import vukan.com.fftd.models.PostImage;

public interface PostImagesCallback {
    void onCallback(List<PostImage> postImages);
}