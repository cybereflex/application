package cc.cybereflex.common.component.executor.define;

public interface Step<Ctx extends ExecutorContext>{

    boolean execute(Ctx ctx);
}
