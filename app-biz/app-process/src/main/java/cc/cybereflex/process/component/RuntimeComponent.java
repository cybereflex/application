package cc.cybereflex.process.component;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RuntimeComponent {

    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;


    public void deploy(String resourceName, String bpmnXML) {
        repositoryService.createDeployment()
                .addString(resourceName, bpmnXML)
                .deploy();
    }

    public List<Deployment> queryDeployments() {
        return repositoryService.createDeploymentQuery()
                .orderByDeploymentTime()
                .desc()
                .list();
    }

    public void deleteDeployment(String deploymentId) {
        repositoryService.deleteDeployment(deploymentId);
    }

    public ProcessDefinition queryProcessDefinition(String deploymentId) {
        return repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploymentId)
                .singleResult();
    }

    public List<ProcessDefinition> queryProcessDefinitions() {
        return repositoryService.createProcessDefinitionQuery()
                .orderByDeploymentTime()
                .desc()
                .list();
    }

    public void createProcessInstance(String processDefinitionId, Map<String, Object> variables) {
        runtimeService.createProcessInstanceById(processDefinitionId)
                .setVariables(variables)
                .execute();
    }

    public List<ProcessInstance> queryProcessInstance(String processDefinitionId) {
        return runtimeService.createProcessInstanceQuery()
                .processDefinitionId(processDefinitionId)
                .list();
    }

    public void deleteProcessInstance(String processInstanceId, String reason) {
        runtimeService.deleteProcessInstance(processInstanceId, reason);
    }
}
