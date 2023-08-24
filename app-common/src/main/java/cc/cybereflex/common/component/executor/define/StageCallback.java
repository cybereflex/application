package cc.cybereflex.common.component.executor.define;

public interface StageCallback<Ctx extends ExecutorContext> {

    void onFailed(Step<Ctx> step, Ctx ctx);


    void onInterrupt(Step<Ctx> step, Ctx ctx);


    void onFinally(Ctx ctx);
}
