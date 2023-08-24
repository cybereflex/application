package cc.cybereflex.common.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("unused")
public class Json {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(Json.class);

    static {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));

        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));

        objectMapper.registerModule(javaTimeModule);
    }

    public static <T> Optional<String> writeValueAsString(@NonNull T obj) {
        try {
            return Optional.of(objectMapper.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            logger.error("object write to json failed", e);
        }
        return Optional.empty();
    }

    public static <T> Optional<byte[]> writeValueAsByte(@NonNull T obj) {
        try {
            return Optional.of(objectMapper.writeValueAsBytes(obj));
        } catch (JsonProcessingException e) {
            logger.error("object write to json failed", e);
        }
        return Optional.empty();
    }

    public static <T> Optional<T> parse(@NonNull String text,@NonNull Class<T> clazz) {
        try {
            return Optional.of(objectMapper.readValue(text, clazz));
        } catch (JsonProcessingException e) {
            logger.error("json read to object failed", e);
        }
        return Optional.empty();
    }

    public static <T> Optional<T> parse(@NonNull String text,@NonNull TypeReference<T> typeReference) {
        try {
            return Optional.of(objectMapper.readValue(text, typeReference));
        } catch (JsonProcessingException e) {
            logger.error("json read to object failed", e);
        }
        return Optional.empty();
    }

    public static <T> Optional<List<T>> parseList(@NonNull String text) {
        try {
            return Optional.of(objectMapper.readValue(text, new TypeReference<>() {
            }));
        } catch (JsonProcessingException e) {
            logger.error("json read to object failed", e);
        }
        return Optional.empty();
    }

    public static <T> Optional<Set<T>> parseSet(@NonNull String text) {
        try {
            return Optional.of(objectMapper.readValue(text, new TypeReference<>() {
            }));
        } catch (JsonProcessingException e) {
            logger.error("json read to object failed", e);
        }
        return Optional.empty();
    }


}
