package vukan.com.fftd.callbacks;

import java.util.List;

import vukan.com.fftd.models.Message;

public interface MessageCallback {
    void onCallback(List<Message> message);
}