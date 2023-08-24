package cc.cybereflex.media.onvif.common;

public interface DiscoveryCallback {

    void onDiscoveryStarted();

    void onDeviceFound(String hostname, String response);

    void onDiscoveryFinished(boolean awaitResult);

    void onError(Exception e);
}
