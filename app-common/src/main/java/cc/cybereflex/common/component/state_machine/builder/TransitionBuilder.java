package cc.cybereflex.common.component.state_machine.builder;

import cc.cybereflex.common.component.state_machine.define.*;
import cc.cybereflex.common.component.state_machine.impl.StateImpl;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
public class TransitionBuilder<C extends Context, S extends Enum<?>, E extends Enum<?>> {

    private State<C, S, E> source;
    private State<C, S, E> target;
    private E event;
    private Condition<C> condition;
    private Action<C, S, E> action;


    private final Map<S, State<C, S, E>> stateMap;


    public TransitionBuilder<C, S, E> from(S stateId) {
        this.source = getState(stateId);
        return this;
    }


    public TransitionBuilder<C, S, E> to(S stateId) {
        this.target = getState(stateId);
        return this;
    }

    public TransitionBuilder<C, S, E> on(E event) {
        this.event = event;
        return this;
    }

    public TransitionBuilder<C, S, E> when(Condition<C> condition) {
        this.condition = condition;
        return this;
    }

    public TransitionBuilder<C, S, E> action(Action<C, S, E> action) {
        this.action = action;
        return this;
    }

    public void build() {
        Transition<C, S, E> transition = this.source.addTransition(this.event, this.target);
        transition.setCondition(condition);
        transition.setAction(action);
    }

    private State<C, S, E> getState(S stateId) {
        State<C, S, E> state = stateMap.get(stateId);
        if (Objects.isNull(state)) {
            state = new StateImpl<>(stateId);
        }
        return state;
    }

}
