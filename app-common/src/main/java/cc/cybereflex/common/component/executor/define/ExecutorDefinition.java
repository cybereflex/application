package cc.cybereflex.common.component.executor.define;

import java.util.List;

public interface ExecutorDefinition<Ctx extends ExecutorContext, S extends Stage<Ctx>> {

    List<S> buildStages();
}
