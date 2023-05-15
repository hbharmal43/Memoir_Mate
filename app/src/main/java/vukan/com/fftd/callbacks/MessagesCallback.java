package vukan.com.fftd.callbacks;

import java.util.List;

import vukan.com.fftd.models.Conv;

public interface MessagesCallback {
    void onCallback(List<Conv> conv);
}