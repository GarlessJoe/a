package com.joe.activiti;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class processtest {
    @Autowired
    private RepositoryService service;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    //单个文件部署
    @Test
    public void deployProcess(){
        //流程部署
        Deployment process = service.createDeployment()
                .addClasspathResource("process/qingjia.bpmn20.xml")
                .addClasspathResource("process/diagram.png")
                .name("请假申请流程")
                .deploy();
        System.out.println(process.getId());
        System.out.println(process.getName());
    }
    //启动流程实例
    @Test
    public void Runtime(){
        ProcessInstance process = runtimeService.startProcessInstanceByKey("qingjia");
        System.out.println("流程定义id" + process.getProcessDefinitionId());
        System.out.println("流程实例id" + process.getId());
        System.out.println("流程活动id" + process.getActivityId());

    }
    @Test
    public void querry(){
        List<Task> list = taskService.createTaskQuery()
                .taskAssigneeLike("张三").list();
    }
}
