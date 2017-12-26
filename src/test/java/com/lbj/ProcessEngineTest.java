package com.lbj;

import org.activiti.engine.*;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessEngineTest {
    //repositoryService的管理流程定义

    RepositoryService repositoryService;
    RuntimeService runtimeService;
    TaskService  taskService;

    @Before
    public  void openProcessEngineTest(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        repositoryService=defaultProcessEngine.getRepositoryService();
        runtimeService=defaultProcessEngine.getRuntimeService();
        taskService=defaultProcessEngine.getTaskService();
    }

    //加载部署仓库
    /**
     * 是Activiti的仓库服务类。所谓的仓库指流程定义文档的两个文件：bpmn文件和流程图片。
     */
    @Test
    public  void  doRepositoryService(){
        DeploymentBuilder deployment = repositoryService.createDeployment();
        deployment.addClasspathResource("diagrams/activiti_leave.bpmn")
                   .addClasspathResource("diagrams/activiti_leave.png")
                   .deploy();
    }
    /**
     * 删除流程部署
     */
    @Test
    public void deleteActivitiDeploy() {
        String deploymentId = "1";
        //如果流程已经启动删除会报错
        //repositoryService.deleteDeployment(deploymentId);
        //如果第二个参数是true级联删除，即使有正在运行的数据也会被删除，如果是false非级联删除如果有数据在运行，会报错
        repositoryService.deleteDeployment(deploymentId, true);
    }

    //是activiti的流程执行服务类。可以从这个服务类中获取很多关于流程执行相关的信息。
    @Test
    public  void  doRunService(){
        this.runtimeService.startProcessInstanceByKey("activiti_leave");
    }


    /**
     * 根据Key来查询流程定义对象
     */
    @Test
    public void queryProcessDefinitionByKey() {
        String processDefinitionKey = "activiti_leave";

        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .list();
        for(ProcessDefinition processDefinition : processDefinitionList){
            System.out.println("流程定义的Id："+processDefinition.getId());
            System.out.println("流程定义的key："+processDefinition.getKey());
            System.out.println("流程定义的名字："+processDefinition.getName());
            System.out.println("流程定义的版本："+processDefinition.getVersion());
            System.out.println("流程定义的资源文件名字："+processDefinition.getResourceName());
        }

    }

    /**
     * 常见流程实例对象并查询
     */
    @Test
    public  void  queryProcessInstance(){
        //
        String processDefinitionKey = "activiti_leave";
        //创建流程实例查询对象
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();

       /* List<ProcessInstance> list = this.runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .orderByProcessDefinitionKey()
                .desc()
                .list();
        for (ProcessInstance p:list) {
            System.out.println("流程定义的id："+p.getProcessDefinitionId());
            System.out.println("正在活动的节点："+p.getActivityId());
            System.out.println("流程实例的id："+p.getId());
        }*/

        ProcessInstance processInstance = this.runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .singleResult();
        System.out.println("流程定义的id："+processInstance.getProcessDefinitionId());
        System.out.println("正在活动的节点："+processInstance.getActivityId());
        System.out.println("流程实例的id："+processInstance.getId());
    }
    /**
     *
     * 当办理任务（流程没有结束时）
     * select * from act_ru_execution; -- 正在活动的ACT_ID_指到下一个节点
     select * from act_hi_procinst; -- 如果不是最后一个节点没有变化
     select * from act_ru_task; -- 删除当前实例上一步的任务，产生当前的任务
     select * from act_hi_taskinst; -- 增加一条当前任务的数据
     select * from act_hi_actinst; -- 增加一条当前任务的数据

     当办理任务（流程结束时）
     * select * from act_ru_execution; -- 正在运行的流程对象消失
     select * from act_hi_procinst; -- 历史的流程实例完成，endtime出现
     select * from act_ru_task; -- 当前实例的任务消失
     select * from act_hi_taskinst; -- 没有变化
     select * from act_hi_actinst; -- 增加一个结束的活动endevent
     */

    @Test
    public void  completeTask(){
        String assignee = "employeer";
        List<Task> tasks = this.taskService.createTaskQuery()
                .processDefinitionKey("activiti_leave")//根据流程定义的key来查询
                .taskAssignee(assignee)//根据办理人来查询
                .orderByTaskCreateTime()
                .desc()
                .list();
        Map<String ,Object> map =new HashMap<String, Object>();
        map.put("reason","结婚");
        map.put("days",10);
        map.put("startTime",new Date());
        this.taskService.complete(tasks.get(0).getId(),map);
    }




}
