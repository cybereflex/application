package cc.cybereflex.process.helper;

import cc.cybereflex.process.model.bpmn.BpmnLine;
import cc.cybereflex.process.model.bpmn.BpmnModel;
import cc.cybereflex.process.model.bpmn.node.*;
import org.apache.commons.collections4.CollectionUtils;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BpmnHelper {
    private static final Logger logger = LoggerFactory.getLogger(BpmnHelper.class);

    public static Optional<String> convert(BpmnModel model) {
        try {
            BpmnModelInstance instance = Bpmn.createExecutableProcess(model.getId())
                    .name(model.getName())
                    .done();

            List<? extends BaseBpmnNode> nodes = model.getNodes();
            List<BpmnLine> lines = model.getLines();

            Assert.isTrue(CollectionUtils.isNotEmpty(nodes), "bpmn nodes can not be empty");
            Assert.isTrue(CollectionUtils.isNotEmpty(lines), "bpmn lines can not be empty");

            Process process = instance.getModelElementById(model.getId());

            Map<String, FlowNode> nodeMap = nodes.stream()
                    .map(it -> buildFlowNode(instance, it))
                    .peek(process::addChildElement)
                    .collect(
                            Collectors.toMap(
                                    BaseElement::getId,
                                    Function.identity()
                            )
                    );

            lines.stream()
                    .map(it -> buildSequenceFlow(instance, it, nodeMap))
                    .forEach(process::addChildElement);

            Bpmn.validateModel(instance);
            return Optional.of(Bpmn.convertToString(instance));
        } catch (Exception e) {
            logger.error("BpmnModel convert to Bpmn xml failed", e);
        }

        return Optional.empty();
    }


    private static SequenceFlow buildSequenceFlow(BpmnModelInstance instance, BpmnLine line, Map<String, FlowNode> nodeMap) {
        SequenceFlow sequenceFlow = instance.newInstance(SequenceFlow.class);
        sequenceFlow.setId(line.getId());
        sequenceFlow.setSource(nodeMap.get(line.getSourceNodeId()));
        sequenceFlow.setTarget(nodeMap.get(line.getTargetNodeId()));
        if (Objects.nonNull(line.getCondition())) {
            ConditionExpression conditionExpression = instance.newInstance(ConditionExpression.class);
            conditionExpression.setTextContent(line.getCondition());
            sequenceFlow.setConditionExpression(conditionExpression);
        }
        return sequenceFlow;
    }


    private static <T extends BaseBpmnNode> FlowNode buildFlowNode(BpmnModelInstance instance, T node) {
        BaseBpmnNode.BpmnNodeType nodeType = node.getNodeType();
        return switch (nodeType) {
            case EVENT_TYPE -> {
                if (node instanceof BpmnEventNode eventNode) {
                    yield buildEventNode(instance, eventNode);
                }
                throw new IllegalArgumentException("illegal node:" + node.getName());
            }
            case USER_TASK_TYPE, SERVICE_TASK_TYPE -> buildTaskNode(instance, node);
            case GATEWAY_TYPE -> {
                if (node instanceof BpmnGatewayNode gatewayNode) {
                    yield buildGatewayNode(instance, gatewayNode);
                }
                throw new IllegalArgumentException("illegal node:" + node.getName());
            }
        };
    }


    private static FlowNode buildTaskNode(BpmnModelInstance instance, BaseBpmnNode taskNode) {
        return switch (taskNode.getNodeType()) {
            case USER_TASK_TYPE -> {
                if (taskNode instanceof BpmnUserTaskNode userTaskNode) {
                    UserTask userTask = instance.newInstance(UserTask.class);
                    userTask.setId(userTaskNode.getId());
                    userTask.setCamundaDueDate(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(userTaskNode.getDueDate()));
                    userTask.setName(userTaskNode.getName());

                    ExtensionElements extensionElements = instance.newInstance(ExtensionElements.class);
                    userTask.setExtensionElements(extensionElements);

                    List<BpmnUserTaskNode.TaskListener> taskListeners = userTaskNode.getTaskListeners();
                    if (CollectionUtils.isNotEmpty(taskListeners)) {
                        taskListeners.forEach(it -> {
                            CamundaExecutionListener camundaExecutionListener = extensionElements.addExtensionElement(CamundaExecutionListener.class);
                            camundaExecutionListener.setCamundaClass(it.getListener());
                            camundaExecutionListener.setCamundaEvent(it.getListenerType().name());
                        });
                    }

                    switch (userTaskNode.getApproveType()) {
                        case SINGLE ->
                                userTask.setCamundaAssignee(String.format("${%s}", userTaskNode.getAssigneeArgName()));
                        case ALL -> {
                            userTask.setCamundaAssignee("${assignee}");
                            MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = instance.newInstance(MultiInstanceLoopCharacteristics.class);
                            multiInstanceLoopCharacteristics.setSequential(false);
                            CompletionCondition completionCondition = instance.newInstance(CompletionCondition.class);
                            completionCondition.setTextContent("${nrOfCompletedInstances == nrOfInstances}");
                            multiInstanceLoopCharacteristics.setCompletionCondition(completionCondition);
                            multiInstanceLoopCharacteristics.setCamundaCollection(userTaskNode.getAssigneeArgName());
                            multiInstanceLoopCharacteristics.setCamundaElementVariable("assignee");
                            userTask.addChildElement(multiInstanceLoopCharacteristics);
                        }
                        case ONE -> {
                            userTask.setCamundaAssignee("${assignee}");
                            MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = instance.newInstance(MultiInstanceLoopCharacteristics.class);
                            multiInstanceLoopCharacteristics.setSequential(false);
                            CompletionCondition completionCondition = instance.newInstance(CompletionCondition.class);
                            completionCondition.setTextContent("${nrOfCompletedInstances == 1 && approve}");
                            multiInstanceLoopCharacteristics.setCompletionCondition(completionCondition);
                            multiInstanceLoopCharacteristics.setCamundaCollection(userTaskNode.getAssigneeArgName());
                            multiInstanceLoopCharacteristics.setCamundaElementVariable("assignee");
                            userTask.addChildElement(multiInstanceLoopCharacteristics);
                        }
                        case SEQUENCE -> {
                            userTask.setCamundaAssignee("${assignee}");
                            MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = instance.newInstance(MultiInstanceLoopCharacteristics.class);
                            multiInstanceLoopCharacteristics.setSequential(true);
                            CompletionCondition completionCondition = instance.newInstance(CompletionCondition.class);
                            completionCondition.setTextContent("${nrOfCompletedInstances == nrOfInstances}");
                            multiInstanceLoopCharacteristics.setCompletionCondition(completionCondition);
                            multiInstanceLoopCharacteristics.setCamundaCollection(userTaskNode.getAssigneeArgName());
                            multiInstanceLoopCharacteristics.setCamundaElementVariable("assignee");
                            userTask.addChildElement(multiInstanceLoopCharacteristics);
                        }
                    }

                    yield userTask;
                }
                throw new IllegalArgumentException("illegal task node:" + taskNode.getName());
            }
            case SERVICE_TASK_TYPE -> {
                if (taskNode instanceof BpmnServiceTaskNode serviceTaskNode) {
                    ServiceTask serviceTask = instance.newInstance(ServiceTask.class);
                    serviceTask.setId(serviceTaskNode.getId());
                    serviceTask.setName(serviceTaskNode.getName());
                    serviceTask.setCamundaClass(serviceTaskNode.getImplementation());

                    ExtensionElements extensionElements = instance.newInstance(ExtensionElements.class);
                    serviceTask.setExtensionElements(extensionElements);

                    List<BpmnServiceTaskNode.TaskListener> taskListeners = serviceTaskNode.getTaskListeners();
                    if (CollectionUtils.isNotEmpty(taskListeners)) {
                        taskListeners.forEach(it -> {
                            CamundaExecutionListener camundaExecutionListener = extensionElements.addExtensionElement(CamundaExecutionListener.class);
                            camundaExecutionListener.setCamundaClass(it.getListener());
                            camundaExecutionListener.setCamundaEvent(it.getListenerType().name());
                        });
                    }

                    yield serviceTask;
                }
                throw new IllegalArgumentException("illegal task node:" + taskNode.getName());
            }
            default -> throw new IllegalArgumentException("illegal task node:" + taskNode.getName());
        };
    }


    private static FlowNode buildGatewayNode(BpmnModelInstance instance, BpmnGatewayNode gatewayNode) {
        return switch (gatewayNode.getGatewayType()) {
            case PARALLEL_GATEWAY -> {
                ParallelGateway parallelGateway = instance.newInstance(ParallelGateway.class);
                yield commonGatewayBuild(instance, parallelGateway, gatewayNode);
            }
            case EXCLUSIVE_GATEWAY -> {
                ExclusiveGateway exclusiveGateway = instance.newInstance(ExclusiveGateway.class);
                yield commonGatewayBuild(instance, exclusiveGateway, gatewayNode);
            }
            case COMPLEX_GATEWAY -> {
                ComplexGateway complexGateway = instance.newInstance(ComplexGateway.class);
                yield commonGatewayBuild(instance, complexGateway, gatewayNode);
            }
        };
    }

    private static <G extends Gateway, N extends BpmnGatewayNode> G commonGatewayBuild(BpmnModelInstance instance, G gateway, N node) {
        gateway.setId(node.getId());
        gateway.setName(node.getName());

        ExtensionElements extensionElements = instance.newInstance(ExtensionElements.class);
        gateway.setExtensionElements(extensionElements);

        List<BpmnGatewayNode.GatewayListener> gatewayListeners = node.getGatewayListeners();
        if (CollectionUtils.isNotEmpty(gatewayListeners)) {
            gatewayListeners.forEach(it -> {
                CamundaExecutionListener camundaExecutionListener = extensionElements.addExtensionElement(CamundaExecutionListener.class);
                camundaExecutionListener.setCamundaClass(it.getListener());
                camundaExecutionListener.setCamundaEvent(it.getListenerType().name());
            });
        }
        return gateway;
    }


    private static FlowNode buildEventNode(BpmnModelInstance instance, BpmnEventNode eventNode) {

        return switch (eventNode.getEventType()) {
            case START_EVENT -> {
                StartEvent startEvent = instance.newInstance(StartEvent.class);
                yield commonEventBuild(instance, startEvent, eventNode);
            }
            case END_EVENT -> {
                EndEvent endEvent = instance.newInstance(EndEvent.class);
                yield commonEventBuild(instance, endEvent, eventNode);
            }
        };
    }


    private static <E extends Event, N extends BpmnEventNode> E commonEventBuild(BpmnModelInstance instance, E event, N node) {
        event.setId(node.getId());
        event.setName(node.getName());
        ExtensionElements extensionElements = instance.newInstance(ExtensionElements.class);
        event.setExtensionElements(extensionElements);

        List<BpmnEventNode.EventListener> eventListeners = node.getEventListeners();
        if (CollectionUtils.isNotEmpty(eventListeners)) {
            eventListeners.forEach(it -> {
                CamundaExecutionListener camundaExecutionListener = extensionElements.addExtensionElement(CamundaExecutionListener.class);
                camundaExecutionListener.setCamundaClass(it.getListener());
                camundaExecutionListener.setCamundaEvent(it.getListenerType().name());
            });
        }
        return event;
    }


}
