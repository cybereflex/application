package cc.cybereflex.media.onvif.common;

import cc.cybereflex.common.enums.ResultEnum;
import cc.cybereflex.common.model.Result;
import org.apache.commons.lang3.RandomStringUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Optional;

public abstract class AbstractOnvifCommand<T> {

    protected static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .version(HttpClient.Version.HTTP_1_1)
            .build();

    public Result<T> execute() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri())
                    .POST(HttpRequest.BodyPublishers.ofString(body()))
                    .build();

            HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
            return processResponse(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String buildWssHeader(String username, String password) {

        try {
            Base64.Encoder encoder = Base64.getEncoder();

            String nonce = System.currentTimeMillis() + RandomStringUtils.randomAlphabetic(5);
            String created = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));

            MessageDigest md = MessageDigest.getInstance("SHA-1");

            md.update(nonce.getBytes());
            md.update(created.getBytes());
            md.update(password.getBytes());
            byte[] digest = md.digest();

            return """
                    <Security xmlns="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd" s:mustUnderstand="1">
                        <UsernameToken>
                            <Username>%s</Username>
                            <Password Type="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest">%s</Password>
                            <Nonce EncodingType="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary">%s</Nonce>
                            <Created xmlns="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd">%s</Created>
                        </UsernameToken>
                    </Security>
                    """
                    .formatted(
                            username,
                            encoder.encodeToString(digest),
                            encoder.encodeToString(nonce.getBytes()),
                            created
                    );
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    protected abstract URI uri();

    protected abstract String body();

    protected Result<T> processResponse(HttpResponse<byte[]> response){
        if (response.statusCode() == 401) {
            return Result.failed(ResultEnum.AUTH_FAILED);
        }

        if (response.statusCode() != 200){
            return Result.failed(ResultEnum.UNKNOWN_FAILED);
        }

        return Result.success(ResultEnum.SUCCESS);
    }
}
