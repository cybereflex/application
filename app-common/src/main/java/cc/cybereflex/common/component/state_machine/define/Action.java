package cc.cybereflex.common.component.state_machine.define;

public interface Action<C extends Context, S extends Enum<?>, E extends Enum<?>> {

    void execute(C ctx, S sourceStateId, S targetStateId, E event);
}
