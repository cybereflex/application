package cc.cybereflex.resources.model.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device {

    private Long id;
    private String uuid;
    private String name;
    private String ip;
    private String port;
    private String username;
    private String password;
    private String vendor;
    private Integer type;
    private Boolean enable;
    private Long version;
    private LocalDateTime createTime;
    private LocalDateTime modifiedTime;
    private Map<String,Object> extraInfo;
}
