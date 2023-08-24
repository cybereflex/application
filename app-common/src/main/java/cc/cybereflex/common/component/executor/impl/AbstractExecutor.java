package cc.cybereflex.common.component.executor.impl;

import cc.cybereflex.common.component.executor.define.Executor;
import cc.cybereflex.common.component.executor.define.ExecutorContext;
import cc.cybereflex.common.component.executor.define.ExecutorDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractExecutor implements Executor<ExecutorContext>, ExecutorDefinition<ExecutorContext, StageImpl> {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractExecutor.class);

    protected List<StageImpl> stages = new ArrayList<>();

    public abstract String getName();

    public AbstractExecutor() {
        this.stages.addAll(buildStages());
    }

    @Override
    public void beforeExecute(ExecutorContext executorContext) {
        executorContext.setStartTimeMills(System.currentTimeMillis());
        executorContext.setExecSuccess(false);
    }

    @Override
    public void execute(ExecutorContext executorContext) {
        try {
            beforeExecute(executorContext);
            for (StageImpl stage : this.stages) {
                stage.execute(executorContext);
            }
            afterExecute(executorContext);
        } catch (Exception e) {
            logger.error("executor execute failed", e);
        }
    }

    @Override
    public void afterExecute(ExecutorContext executorContext) {
        executorContext.setExecSuccess(true);
        long execTime = System.currentTimeMillis() - executorContext.getStartTimeMills();
        logger.info(getName() + "total execute time is:" + execTime);
    }
}
