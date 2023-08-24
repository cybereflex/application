package cc.cybereflex.common.component.state_machine;

import cc.cybereflex.common.component.state_machine.define.Context;
import cc.cybereflex.common.component.state_machine.define.StateMachine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StateMachineFactory {

    private static final Map<String, StateMachine<?, ?, ?>> stateMachineMap = new ConcurrentHashMap<>();


    public static <C extends Context, S extends Enum<?>, E extends Enum<?>> void register(StateMachine<C, S, E> stateMachine) {
        if (stateMachineMap.containsKey(stateMachine.getId())) {
            return;
        }

        stateMachineMap.put(stateMachine.getId(), stateMachine);
    }


    @SuppressWarnings("unchecked")
    public static <C extends Context, S extends Enum<?>, E extends Enum<?>> StateMachine<C, S, E> get(String id) {
        return (StateMachine<C, S, E>) stateMachineMap.get(id);
    }


}
