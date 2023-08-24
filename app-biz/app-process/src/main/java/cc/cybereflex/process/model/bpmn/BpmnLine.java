package cc.cybereflex.process.model.bpmn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BpmnLine {

    /**
     * id
     */
    private String id;
    /**
     * 条件表达式
     */
    private String condition;
    /**
     * 来源节点id
     */
    private String sourceNodeId;
    /**
     * 目标节点id
     */
    private String targetNodeId;
}
