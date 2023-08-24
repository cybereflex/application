package cc.cybereflex.process.model.bpmn.node;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class BpmnServiceTaskNode extends BaseBpmnNode{

    /**
     * 实现java class
     */
    private String implementation;

    /**
     * 任务监听器集合
     */
    private List<TaskListener> taskListeners;



    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TaskListener{


        /**
         * 任务监听器
         */
        private String listener;

        /**
         * 任务监听器类型
         */
        private TaskListenerType listenerType;
    }


    public enum TaskListenerType{
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
