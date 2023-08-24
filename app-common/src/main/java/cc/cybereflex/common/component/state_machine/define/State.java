package cc.cybereflex.common.component.state_machine.define;

import java.util.List;

public interface State<C extends Context, S extends Enum<?>, E extends Enum<?>> {

    S getId();

    Transition<C, S, E> addTransition(E event, State<C, S, E> target);

    List<Transition<C, S, E>> getTransitionsByEvent(E event);

    List<Transition<C, S, E>> getAllTransition();
}
