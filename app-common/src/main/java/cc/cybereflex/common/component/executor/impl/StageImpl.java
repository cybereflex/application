package cc.cybereflex.common.component.executor.impl;

import cc.cybereflex.common.component.executor.define.Stage;
import cc.cybereflex.common.component.executor.exception.ExecuteFailedException;
import cc.cybereflex.common.component.executor.exception.ExecuteInterruptException;
import cc.cybereflex.common.component.executor.define.ExecutorContext;
import cc.cybereflex.common.component.executor.define.StageCallback;
import cc.cybereflex.common.component.executor.define.Step;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class StageImpl implements Stage<ExecutorContext> {

    protected static final Logger logger = LoggerFactory.getLogger(StageImpl.class);

    @Setter
    private List<Step<ExecutorContext>> steps;

    @Setter
    private StageCallback<ExecutorContext> callback;

    public StageImpl(@Nullable StageCallback<ExecutorContext> callback) {
        if (Objects.isNull(callback)) {
            this.callback = new StageCallback<>() {
                @Override
                public void onFailed(Step<ExecutorContext> step, ExecutorContext executorContext) {

                }

                @Override
                public void onInterrupt(Step<ExecutorContext> step, ExecutorContext executorContext) {

                }

                @Override
                public void onFinally(ExecutorContext executorContext) {

                }
            };
        } else {
            this.callback = callback;
        }
    }

    @Override
    public void execute(ExecutorContext ctx) {
        try {
            this.beforeExecute(ctx);
            for (Step<ExecutorContext> step : this.steps) {

                if (ctx.isInterrupt()) {
                    this.callback.onInterrupt(step, ctx);
                    String interruptMessage = String.format("before execute step [%s] is interrupt",step.getClass().getName());
                    throw new ExecuteInterruptException(interruptMessage);
                }

                boolean result = step.execute(ctx);

                if (!result) {
                    this.callback.onFailed(step, ctx);
                    String errorMessage = String.format("step [%s] execute failed", step.getClass().getName());
                    throw new ExecuteFailedException(errorMessage);
                }
            }
            this.afterExecute(ctx);
        } finally {
            callback.onFinally(ctx);
        }
    }
}
