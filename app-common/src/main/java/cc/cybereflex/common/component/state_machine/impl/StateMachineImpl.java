package cc.cybereflex.common.component.state_machine.impl;

import cc.cybereflex.common.component.state_machine.define.Context;
import cc.cybereflex.common.component.state_machine.define.State;
import cc.cybereflex.common.component.state_machine.define.StateMachine;
import cc.cybereflex.common.component.state_machine.define.Transition;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class StateMachineImpl<C extends Context, S extends Enum<?>, E extends Enum<?>> implements StateMachine<C, S, E> {

    protected static final Logger logger = LoggerFactory.getLogger(StateMachineImpl.class);
    private String id;

    private final Map<S, State<C, S, E>> stateMap;

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public S push(S sourceStateId, E event, C ctx) {
        State<C, S, E> state = stateMap.get(sourceStateId);
        Assert.notNull(state, "unsupported state id:%s".formatted(sourceStateId));

        List<Transition<C, S, E>> transitions = state.getTransitionsByEvent(event);
        Assert.isTrue(CollectionUtils.isNotEmpty(transitions), "can't find event transitions, current state:%s,event:%s".formatted(sourceStateId, event));

        Optional<Transition<C, S, E>> transition = transitions.stream()
                .filter(it -> {
                    if (Objects.isNull(it.getCondition())) return true;
                    return it.getCondition().isSatisfy(ctx);
                })
                .findFirst();

        if (transition.isPresent()) {
            return transition.get().transit(ctx, true).getId();
        } else {
            logger.warn("can't find next transition,state: {}, event: {}, ctx: {}", sourceStateId, event, ctx);
        }

        return sourceStateId;
    }
}
