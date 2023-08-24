package cc.cybereflex.common.component;

import org.apache.commons.collections4.MapUtils;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommonHttpClient {
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .version(HttpClient.Version.HTTP_1_1)
            .build();

    public static <T> HttpResponse<T> doGet(String url, HttpResponse.BodyHandler<T> bodyHandler) {
        return doGet(url, null, bodyHandler);
    }

    public static <T, P> HttpResponse<T> doGet(String url, P params, HttpResponse.BodyHandler<T> bodyHandler) {
        return doGet(url, null, params, bodyHandler);
    }

    public static <T, P> HttpResponse<T> doGet(String url, Map<String, String> headers, P params, HttpResponse.BodyHandler<T> bodyHandler) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .GET()
                    .timeout(Duration.ofSeconds(3));

            if (MapUtils.isNotEmpty(headers)) {
                headers.forEach(builder::setHeader);
            }

            if (Objects.nonNull(params)) {
                StringBuilder sb = new StringBuilder(url);
                if (!url.endsWith("?")) {
                    sb.append("?");
                }

                String queryString = String.join(
                        "&",
                        toMap(params).entrySet()
                                .stream()
                                .map(it -> it.getKey() + "=" + it.getValue())
                                .toList()
                );
                builder.uri(URI.create(sb.append(queryString).toString()));
            }

            return httpClient.send(builder.build(), bodyHandler);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        }
    }

    public static <T> HttpResponse<T> doPost(String url, HttpRequest.BodyPublisher bodyPublisher, HttpResponse.BodyHandler<T> bodyHandler) {
        return doPost(url, null, bodyPublisher, bodyHandler);
    }

    public static <T> HttpResponse<T> doPost(String url, Map<String, String> headers, HttpRequest.BodyPublisher bodyPublisher, HttpResponse.BodyHandler<T> bodyHandler) {

        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .POST(bodyPublisher)
                    .uri(URI.create(url));

            if (MapUtils.isNotEmpty(headers)) {
                headers.forEach(builder::setHeader);
            }

            return httpClient.send(builder.build(), bodyHandler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static Map<String, Object> toMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }


}
