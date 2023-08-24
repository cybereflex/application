package cc.cybereflex.process.model.bpmn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BpmnArgument {
    /**
     * 变量名
     */
    private String name;
    /**
     * 变量类型
     */
    private ArgumentType type;
    /**
     * 默认值
     */
    private Object defaultValue;

    /**
     * 在某个节点隐藏
     */
    private List<String> hiddenNode;


    public enum ArgumentType{
        STRING,
        BOOLEAN,
        NUMBER,
        DATE,
    }
}
