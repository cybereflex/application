package cc.cybereflex.process.model.bpmn;

import cc.cybereflex.process.model.bpmn.node.BaseBpmnNode;
import cc.cybereflex.process.model.bpmn.node.BpmnServiceTaskNode;
import cc.cybereflex.process.model.bpmn.node.BpmnUserTaskNode;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BpmnModel {

    /**
     * id
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 自定义参数定义
     */
    private List<BpmnArgument> arguments;
    /**
     * 节点定义
     */
    private List<? extends BaseBpmnNode> nodes;

    /**
     * 连线定义
     */
    private List<BpmnLine> lines;
}
