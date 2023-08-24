package cc.cybereflex.common.component.state_machine.impl;

import cc.cybereflex.common.component.state_machine.define.Context;
import cc.cybereflex.common.component.state_machine.define.State;
import cc.cybereflex.common.component.state_machine.define.Transition;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

public class StateImpl<C extends Context, S extends Enum<?>, E extends Enum<?>> implements State<C, S, E> {

    protected final S stateId;

    private final Map<E, List<Transition<C, S, E>>> transitionMap = new HashMap<>();

    public StateImpl(S stateId) {
        this.stateId = stateId;
    }

    @Override
    public S getId() {
        return this.stateId;
    }

    @Override
    public Transition<C, S, E> addTransition(E event, State<C, S, E> target) {
        TransitionImpl<C, S, E> transition = new TransitionImpl<>();
        transition.setSource(this);
        transition.setTarget(target);
        transition.setEvent(event);

        List<Transition<C, S, E>> transitions = transitionMap.get(event);
        if (CollectionUtils.isEmpty(transitions)) {
            transitions = new ArrayList<>();
        }
        transitions.add(transition);
        transitionMap.put(event, transitions);

        return transition;
    }

    @Override
    public List<Transition<C, S, E>> getTransitionsByEvent(E event) {
        return this.transitionMap.get(event);
    }

    @Override
    public List<Transition<C, S, E>> getAllTransition() {
        return this.transitionMap.values()
                .stream()
                .flatMap(Collection::stream)
                .toList();
    }
}
