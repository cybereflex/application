package cc.cybereflex.common.component.state_machine.define;

public interface Condition<C extends Context> {

    boolean isSatisfy(C ctx);
}
