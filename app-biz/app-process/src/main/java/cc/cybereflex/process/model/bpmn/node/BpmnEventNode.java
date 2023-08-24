package cc.cybereflex.process.model.bpmn.node;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BpmnEventNode extends BaseBpmnNode {

    /**
     * 事件类型
     */
    @JsonDeserialize(converter = EventTypeConverter.class)
    private EventType eventType;


    /**
     * 事件监听器集合
     */
    private List<EventListener> eventListeners;



    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EventListener{


        /**
         * 事件监听器
         */
        private String listener;

        /**
         * 事件监听器类型
         */
        private EventListenerType listenerType;
    }


    public static class EventTypeConverter implements Converter<String, EventType>{

        @Override
        public EventType convert(String value) {
            return EventType.valueOf(value);
        }

        @Override
        public JavaType getInputType(TypeFactory typeFactory) {
            return typeFactory.constructType(String.class);
        }

        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            return typeFactory.constructType(EventType.class);
        }
    }

    public enum EventType {

        /**
         * 开始事件
         */
        START_EVENT,

        /**
         * 结束事件
         */
        END_EVENT,
    }


    public enum EventListenerType{
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
