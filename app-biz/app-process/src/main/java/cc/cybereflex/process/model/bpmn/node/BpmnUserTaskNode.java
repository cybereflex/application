package cc.cybereflex.process.model.bpmn.node;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class BpmnUserTaskNode extends BaseBpmnNode{

    /**
     * 审批类型
     */
    private ApproveType approveType;

    /**
     * 审批人员变量名, 会签，或签，依次审批对应的就是审批人员集合的变量名称
     */
    public String assigneeArgName;

    /**
     * 到期时间
     */
    public LocalDateTime dueDate;

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
         * 创建时
         */
        CREATE,
        /**
         * 审批时
         */
        ASSIGNEE,

        /**
         * 完成时
         */
        COMPLETE,
        /**
         * 超时时
         */
        TIMEOUT,
    }


    public enum ApproveType{
        /**
         * 单人审批
         */
        SINGLE,
        /**
         * 会签
         */
        ALL,
        /**
         * 或签
         */
        ONE,

        /**
         * 依次审批
         */
        SEQUENCE,

    }

}
