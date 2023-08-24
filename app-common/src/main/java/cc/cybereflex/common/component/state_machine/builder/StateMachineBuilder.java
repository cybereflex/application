package cc.cybereflex.common.component.state_machine.builder;

import cc.cybereflex.common.component.state_machine.StateMachineFactory;
import cc.cybereflex.common.component.state_machine.impl.StateMachineImpl;
import cc.cybereflex.common.component.state_machine.define.Context;
import cc.cybereflex.common.component.state_machine.define.State;
import cc.cybereflex.common.component.state_machine.define.StateMachine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StateMachineBuilder<C extends Context, S extends Enum<?>, E extends Enum<?>> {

    private final Map<S, State<C, S, E>> stateMap = new ConcurrentHashMap<>();
    private final StateMachineImpl<C, S, E> stateMachine = new StateMachineImpl<>(stateMap);

    public static <C extends Context, S extends Enum<?>, E extends Enum<?>> StateMachineBuilder<C, S, E> builder() {
        return new StateMachineBuilder<>();
    }

    public TransitionBuilder<C, S, E> transition() {
        return new TransitionBuilder<>(stateMap);
    }


    public StateMachine<C, S, E> build(String id) {
        stateMachine.setId(id);
        StateMachineFactory.register(stateMachine);
        return stateMachine;
    }

}
