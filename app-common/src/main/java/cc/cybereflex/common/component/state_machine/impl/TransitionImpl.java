package cc.cybereflex.common.component.state_machine.impl;

import cc.cybereflex.common.component.state_machine.define.*;

import java.util.Objects;

public class TransitionImpl<C extends Context, S extends Enum<?>, E extends Enum<?>> implements Transition<C, S, E> {

    private State<C, S, E> source;
    private State<C, S, E> target;
    private E event;
    private Condition<C> condition;
    private Action<C, S, E> action;


    @Override
    public State<C, S, E> getSource() {
        return this.source;
    }

    @Override
    public void setSource(State<C, S, E> source) {
        this.source = source;
    }

    @Override
    public E getEvent() {
        return this.event;
    }

    @Override
    public void setEvent(E event) {
        this.event = event;
    }

    @Override
    public State<C, S, E> getTarget() {
        return this.target;
    }

    @Override
    public void setTarget(State<C, S, E> target) {
        this.target = target;
    }

    @Override
    public Condition<C> getCondition() {
        return this.condition;
    }

    @Override
    public void setCondition(Condition<C> condition) {
        this.condition = condition;
    }

    @Override
    public Action<C, S, E> getAction() {
        return this.action;
    }

    @Override
    public void setAction(Action<C, S, E> action) {
        this.action = action;
    }

    @Override
    public State<C, S, E> transit(C ctx, boolean checkCondition) {
        if (!checkCondition || Objects.isNull(condition) || condition.isSatisfy(ctx)) {
            if (Objects.nonNull(action)) {
                action.execute(ctx, source.getId(), target.getId(), event);
            }
            return target;
        }

        return source;
    }
}
