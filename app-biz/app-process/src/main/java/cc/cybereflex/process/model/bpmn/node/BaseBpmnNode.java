package cc.cybereflex.process.model.bpmn.node;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "nodeType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BpmnEventNode.class, name = "EVENT_TYPE"),
        @JsonSubTypes.Type(value = BpmnGatewayNode.class, name = "GATEWAY_TYPE"),
        @JsonSubTypes.Type(value = BpmnUserTaskNode.class, name = "USER_TASK_TYPE"),
        @JsonSubTypes.Type(value = BpmnServiceTaskNode.class, name = "SERVICE_TASK_TYPE"),
})
public class BaseBpmnNode {
    /**
     * 节点ID
     */
    private String id;

    /**
     * 节点类型
     */
    @JsonDeserialize(converter = BpmnNodeTypeConverter.class)
    private BpmnNodeType nodeType;

    /**
     * 名称
     */
    private String name;

    public static class BpmnNodeTypeConverter implements Converter<String,BpmnNodeType> {

        @Override
        public BpmnNodeType convert(String value) {
            return BpmnNodeType.valueOf(value);
        }

        @Override
        public JavaType getInputType(TypeFactory typeFactory) {
            return typeFactory.constructType(String.class);
        }

        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            return typeFactory.constructType(BpmnNodeType.class);
        }
    }

    public enum BpmnNodeType {
        /**
         * 事件类型
         */
        EVENT_TYPE,
        /**
         * 网关类型
         */
        GATEWAY_TYPE,
        /**
         * 用户任务类型
         */
        USER_TASK_TYPE,

        /**
         * 服务任务类型
         */
        SERVICE_TASK_TYPE,
    }
}
