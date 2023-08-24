package cc.cybereflex.common.component.executor.define;

public interface Stage<Ctx extends ExecutorContext> extends Executor<Ctx>{

    @Override
    default void afterExecute(Ctx ctx) {

    }


    @Override
    default void beforeExecute(Ctx ctx) {

    }
}
