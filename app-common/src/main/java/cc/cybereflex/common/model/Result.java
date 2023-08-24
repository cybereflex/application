package cc.cybereflex.common.model;

import cc.cybereflex.common.enums.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unused")
public class Result<T> {

    /**
     * 业务处理状态码
     */
    private Integer code;
    /**
     * 请求是否成功
     */
    private boolean success;
    /**
     * 提示信息
     */
    private String message;
    /**
     * 返回数据
     */
    private T data;
    /**
     * 扩展数据
     */
    private Map<String, Object> extraData;


    public static <T> Result<T> success(ResultEnum e) {
        return Result.<T>builder()
                .code(e.getCode())
                .message(e.getMessage())
                .success(true)
                .build();
    }

    public static <T> Result<T> success(ResultEnum e, T data) {
        return Result.<T>builder()
                .code(e.getCode())
                .message(e.getMessage())
                .data(data)
                .success(true)
                .build();
    }

    public static <T> Result<T> success(Integer code, String message) {
        return Result.<T>builder()
                .code(code)
                .message(message)
                .success(true)
                .build();
    }

    public static <T> Result<T> success(Integer code, String message, T data) {
        return Result.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .success(true)
                .build();
    }

    public static <T> Result<T> failed(ResultEnum e) {
        return Result.<T>builder()
                .code(e.getCode())
                .message(e.getMessage())
                .success(false)
                .build();
    }

    public static <T> Result<T> failed(ResultEnum e, T data) {
        return Result.<T>builder()
                .code(e.getCode())
                .message(e.getMessage())
                .data(data)
                .success(false)
                .build();
    }

    public static Result<Void> failed(Integer code, String message) {
        return Result.<Void>builder()
                .code(code)
                .message(message)
                .success(false)
                .build();
    }

    public static <T> Result<T> failed(Integer code, String message, T data) {
        return Result.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .success(false)
                .build();
    }

    public Result<T> addExtraData(String key, Object value) {
        if (MapUtils.isEmpty(this.extraData)) {
            this.extraData = new HashMap<>();
        }
        this.extraData.put(key, value);
        return this;
    }
    public Result<T> addExtraData(Map<String,Object> map) {
        if (MapUtils.isEmpty(this.extraData)) {
            this.extraData = new HashMap<>();
        }
        this.extraData.putAll(map);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <D> D getExtraDataValue(String key, D defaultValue) {
        if (StringUtils.isBlank(key)) return defaultValue;
        if (MapUtils.isEmpty(this.extraData)) return defaultValue;
        Object value = this.extraData.get(key);
        return Objects.isNull(value) ? defaultValue : ((D) value);
    }

}
