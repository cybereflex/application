package cc.cybereflex.process.model.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessDefinition {

    private long id;
    private String pid;
    private String name;
    private String definition;
    private LocalDateTime createTime;
    private LocalDateTime modifiedTime;
    private String extraData;
}
