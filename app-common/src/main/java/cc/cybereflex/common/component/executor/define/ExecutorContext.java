package cc.cybereflex.common.component.executor.define;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ExecutorContext {
    /**
     * 开始执行时间
     */
    private long startTimeMills;
    /**
     * 是否成功执行完成所有结果
     */
    private boolean execSuccess;
    /**
     * 是否中断执行
     */
    private boolean interrupt;

}
