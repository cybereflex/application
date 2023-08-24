package cc.cybereflex.process.model.bpmn.node;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BpmnGatewayNode extends BaseBpmnNode {

    /**
     * 网关类型
     */
    private GatewayType gatewayType;

    /**
     * 网关监听器集合
     */
    private List<GatewayListener> gatewayListeners;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GatewayListener{


        /**
         * 网关监听器实现类
         */
        private String listener;

        /**
         * 网关监听器类型
         */
        private GatewayListenerType listenerType;
    }


    public enum GatewayType {
        EXCLUSIVE_GATEWAY,
        PARALLEL_GATEWAY,
        COMPLEX_GATEWAY,
    }

    public enum GatewayListenerType{
        /**
         * 执行前
         */
        BEFORE,
        /**
         * 执行后
         */
        AFTER,
    }
}
