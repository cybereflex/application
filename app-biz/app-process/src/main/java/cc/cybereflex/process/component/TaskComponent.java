package cc.cybereflex.process.component;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskComponent {

    private final TaskService taskService;
    private final RuntimeService runtimeService;

    public List<Task> queryTask(String processInstanceId, String assignee) {
        return taskService.createTaskQuery()
                .taskAssignee(assignee)
                .processInstanceId(processInstanceId)
                .active()
                .list();
    }

    public Map<String, Object> queryCurrentTaskVariables(String taskId) {
        return taskService.getVariables(taskId);
    }

    public void updateVariables(String taskId, Map<String, Object> variables) {
        taskService.setVariables(taskId, variables);
    }

    public void completeTask(String taskId, Map<String, Object> variables) {
        taskService.complete(taskId, variables);
    }

    public void jumpTask(String currentTaskId, String processInstanceId, String taskActivityId) {
        Set<String> activityIds = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .active()
                .list()
                .stream()
                .map(Task::getTaskDefinitionKey)
                .collect(Collectors.toSet());

        if (CollectionUtils.isNotEmpty(activityIds)) {
            Map<String, Object> variables = taskService.getVariables(currentTaskId);

            activityIds.forEach(it ->
                    runtimeService.createProcessInstanceModification(processInstanceId)
                            .cancelAllForActivity(it)
                            .execute()
            );

            runtimeService.createProcessInstanceModification(processInstanceId)
                    .startBeforeActivity(taskActivityId)
                    .setVariables(variables)
                    .execute();
        }
    }

    public void delegateTask(String taskId, String userId) {
        taskService.delegateTask(taskId, userId);
    }

    public void resolveTask(String taskId) {
        taskService.resolveTask(taskId);
    }
}
