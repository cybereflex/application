package cc.cybereflex.common.component.executor.define;

public interface Executor<Ctx extends ExecutorContext> {

    void beforeExecute(Ctx ctx);

    void execute(Ctx ctx);

    void afterExecute(Ctx ctx);
}
