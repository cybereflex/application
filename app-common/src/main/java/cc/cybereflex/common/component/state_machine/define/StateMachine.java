package cc.cybereflex.common.component.state_machine.define;

public interface StateMachine<C extends Context, S extends Enum<?>, E extends Enum<?>> {

    String getId();

    S push(S sourceStateId, E event, C ctx);

}
