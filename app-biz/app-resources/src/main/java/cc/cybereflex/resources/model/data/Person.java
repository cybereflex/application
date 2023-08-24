package cc.cybereflex.resources.model.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {

    private Long id;
    private String uid;
    private String username;
    private String password;
    private String name;
    private String idCard;
    private Integer age;
    private Boolean enable;
    private Boolean deleted;
    private LocalDateTime createTime;
    private LocalDateTime modifiedTime;
}
