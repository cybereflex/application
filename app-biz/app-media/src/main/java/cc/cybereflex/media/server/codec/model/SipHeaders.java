package cc.cybereflex.media.server.codec.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SipHeaders {

    private final Map<String, String> headers;

    public SipHeaders() {
        headers = new HashMap<>();
    }


    public SipHeaders add(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    public SipHeaders remove(String name) {
        this.headers.remove(name);
        return this;
    }

    public String get(String name) {
        return this.headers.get(name);
    }

    public void clear() {
        this.headers.clear();
    }

    public Set<Map.Entry<String, String>> getAll(){
        return this.headers.entrySet();
    }
}
