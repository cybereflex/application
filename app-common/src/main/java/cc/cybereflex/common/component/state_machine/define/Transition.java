package cc.cybereflex.common.component.state_machine.define;

public interface Transition<C extends Context, S extends Enum<?>, E extends Enum<?>> {

    State<C, S, E> getSource();

    void setSource(State<C, S, E> source);

    E getEvent();

    void setEvent(E event);

    State<C, S, E> getTarget();

    void setTarget(State<C, S, E> target);

    Condition<C> getCondition();

    void setCondition(Condition<C> condition);

    Action<C, S, E> getAction();

    void setAction(Action<C, S, E> action);

    State<C, S, E> transit(C ctx, boolean checkCondition);
}
