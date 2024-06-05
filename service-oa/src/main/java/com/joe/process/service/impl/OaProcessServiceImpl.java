package com.joe.process.service.impl;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joe.auth.service.SysUserService;
import com.joe.model.process.Process;
import com.joe.model.process.ProcessRecord;
import com.joe.model.process.ProcessTemplate;
import com.joe.model.system.SysUser;
import com.joe.process.mapper.OaProcessMapper;
import com.joe.process.service.OaProcessRecordService;
import com.joe.process.service.OaProcessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joe.process.service.OaProcessTemplateService;
import com.joe.security.custom.LoginUserInfoHelper;
import com.joe.vo.process.ApprovalVo;
import com.joe.vo.process.ProcessFormVo;
import com.joe.vo.process.ProcessQueryVo;
import com.joe.vo.process.ProcessVo;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.util.CollectionUtil;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jni.Proc;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author joe
 * @since 2024-06-03
 */
@Service
public class OaProcessServiceImpl extends ServiceImpl<OaProcessMapper, Process> implements OaProcessService {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private OaProcessTemplateService templateService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private OaProcessRecordService recordService;
    @Autowired
    private HistoryService historyService;
    @Override
    public IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo) {
        IPage<ProcessVo> pageModel = baseMapper.selectPage(pageParam, processQueryVo);
        return pageModel;
    }

    @Override
    public void deployByZip(String deployPath) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(deployPath);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deployment = repositoryService.createDeployment().addZipInputStream(zipInputStream).deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    @Override
    public void startUp(ProcessFormVo processFormVo) {
        //根据用户id查询当前用户信息
        SysUser user = userService.getById(LoginUserInfoHelper.getUserId());
        //根据审批模版id把模版信息查询
        ProcessTemplate template = templateService.getById(processFormVo.getProcessTemplateId());
        //保存提交审批信息到业务表 oa_process
        Process process = new Process();
        BeanUtils.copyProperties(processFormVo ,process );
        String workNo = System.currentTimeMillis() + "";
        process.setProcessCode(workNo);
        process.setUserId(LoginUserInfoHelper.getUserId());
        process.setFormValues(processFormVo.getFormValues());
        process.setTitle(user.getName() + "发起" +template.getName() + "申请");
        process.setStatus(1);
        baseMapper.insert(process);
        //启动流程实例 通过RuntimeService , 包含流程定义的key 业务的key ,流程参数form表单json数据，转换map集合
        String processDefinitionKey = template.getProcessDefinitionKey();//流程定义key
        Long businessKey = process.getId();//业务key
        String formValues = processFormVo.getFormValues();
        JSONObject jsonObject = JSON.parseObject(formValues);
        JSONObject formData = jsonObject.getJSONObject("formData");
        HashMap<String, Object> map = new HashMap<>();
        for(Map.Entry<String , Object> entry :formData.entrySet()){
            map.put(entry.getKey() , entry.getValue());
        }
        HashMap<String, Object> variable = new HashMap<>();
        variable.put("data" , map);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, String.valueOf(businessKey), variable);
        //查询到下一个的审批人是谁
        List<String> nameList = new ArrayList<>();
        List<org.activiti.engine.task.Task> taskList = this.getCurrentTaskList(processInstance.getId());
        if(!CollectionUtil.isEmpty(taskList)) {
            for (Task task : taskList) {
                String assignee = task.getAssignee();
                SysUser byUsername = userService.getByUsername(assignee);
                String name = byUsername.getName();
                nameList.add(name);
                //推送消息
            }
        }
       process.setProcessInstanceId(processInstance.getId());
        process.setDescription("等待" + StringUtils.join(nameList.toArray()) +"审批");
        //业务和流程的关联
        baseMapper.updateById(process);
        recordService.record(process.getId() ,  1 , "发起申请");
    }

    @Override
    public IPage<ProcessVo> findPending(Page<Process> processPage) {
        TaskQuery query = taskService.createTaskQuery().taskAssignee(LoginUserInfoHelper.getUsername()).orderByTaskCreateTime().desc();
        List<Task> list = query.listPage((int) ((processPage.getCurrent() - 1) * processPage.getSize()), (int) processPage.getSize());
        long totalCount = query.count();

        List<ProcessVo> processList = new ArrayList<>();
        // 根据流程的业务ID查询实体并关联
        for (Task item : list) {
            String processInstanceId = item.getProcessInstanceId();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            if (processInstance == null) {
                continue;
            }
            // 业务key
            String businessKey = processInstance.getBusinessKey();
            if (businessKey == null) {
                continue;
            }
            Process process = this.getById(Long.parseLong(businessKey));
            ProcessVo processVo = new ProcessVo();
            BeanUtils.copyProperties(process, processVo);
            processVo.setTaskId(item.getId());
            processList.add(processVo);
        }
        IPage<ProcessVo> page = new Page<ProcessVo>(processPage.getCurrent(), processPage.getSize(), totalCount);
        page.setRecords(processList);
        return page;
    }

    @Override
    public Map<String, Object> show(Long id) {
        Process process = baseMapper.selectById(id);
        LambdaQueryWrapper<ProcessRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessRecord::getProcessId ,id);
        List<ProcessRecord> RecordList = recordService.list(wrapper);
        ProcessTemplate template = templateService.getById(process.getProcessTemplateId());
        boolean isApprove = false;
        List<Task> currentTaskList = this.getCurrentTaskList(process.getProcessInstanceId());
        for (Task task : currentTaskList) {
            if(task.getAssignee().equals(LoginUserInfoHelper.getUsername())){
                isApprove = true;
            }
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("process", process);
        map.put("processRecordList", RecordList);
        map.put("processTemplate", template);
        map.put("isApprove", isApprove);
        return  map;
    }
    //审批
    @Override
    public void approve(ApprovalVo approvalVo) {
        //从approvalvo中取任务id，根据id 获取流程变量
        String taskId = approvalVo.getTaskId();
        Map<String, Object> variables = taskService.getVariables(taskId);
        for(Map.Entry entry: variables.entrySet())
        {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
        //判断审批状态值
        //值为1 ,审批通过 //-1 驳回, 流程结束
        if(approvalVo.getStatus() == 1){
            HashMap<String, Object> map = new HashMap<>();
            taskService.complete(taskId , map);
        }else{
            this.endTask(taskId);
        }
        String description = approvalVo.getStatus().intValue()==1?"通过":"驳回";
        //记录审批信息  oa_process_record
        recordService.record(approvalVo.getProcessId() ,approvalVo.getStatus() ,description );
        //下一个审批人
        Process process = baseMapper.selectById(approvalVo.getProcessId());
        List<Task> TaskList = this.getCurrentTaskList(process.getProcessInstanceId());
        if(!CollectionUtil.isEmpty(TaskList)){
            ArrayList<String> assignList = new ArrayList<>();
            for (Task task : TaskList) {
                String assignee = task.getAssignee();
                SysUser user = userService.getByUsername(assignee);
                String name = user.getName();
                assignList.add(name);
                //推送消息
            }
        process.setDescription("等待" + StringUtils.join(assignList.toArray() , ",") + "审批");
         process.setStatus(1);
        }else{
            if(approvalVo.getStatus().intValue() == 1){
            process.setDescription("审批完成");
            process.setStatus(2);
            }else{
                process.setDescription("审批驳回");
                process.setStatus(-1);
            }
        }
        baseMapper.updateById(process);
    }

    @Override
    public IPage<ProcessVo> findProcessed(Page<Process> pageParam) {
        //封装查询条件
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(LoginUserInfoHelper.getUsername())
                .finished()
                .orderByTaskCreateTime().desc();
        //调用方法条件分页查询，返回list集合
        //开始位置， 每页显示记录数
        List<HistoricTaskInstance> list = query.listPage((int) ((pageParam.getCurrent() - 1) * pageParam.getSize()), (int) pageParam.getSize());
        long totalCount = query.count();
        //便利返回list集合， 封装List<ProcessVo>
       List<ProcessVo> processVoList = new ArrayList<>();
        for (HistoricTaskInstance item : list) {
            String processInstanceId = item.getProcessInstanceId();//获取流程实例id
            //根据流程实例id查询获取process信息
            Process process = baseMapper.selectOne(new LambdaQueryWrapper<Process>().eq(Process::getProcessInstanceId, processInstanceId));
            //把proccess对象转为processVo对象
            ProcessVo processVo = new ProcessVo();
            BeanUtils.copyProperties(process , processVo);
            processVoList.add(processVo);
        }
        //Ipage 封装分页查询数据

        IPage<ProcessVo> pageModel = new Page<>(pageParam.getCurrent(), pageParam.getSize() , totalCount);//当前页， 当前页数量， 总数量
        pageModel.setRecords(processVoList);
        return pageModel;
    }

    @Override
    public IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam) {
        ProcessQueryVo processQueryVo = new ProcessQueryVo();
        processQueryVo.setUserId(LoginUserInfoHelper.getUserId());
        IPage<ProcessVo> pageModel = baseMapper.selectPage(pageParam, processQueryVo);
        return pageModel;
    }

    private void endTask(String taskId) {
        //  当前任务
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        List endEventList = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class);
        // 并行任务可能为null
        if(CollectionUtils.isEmpty(endEventList)) {
            return;
        }
        FlowNode endFlowNode = (FlowNode) endEventList.get(0);
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());

        //  临时保存当前活动的原始方向
        List originalSequenceFlowList = new ArrayList<>();
        originalSequenceFlowList.addAll(currentFlowNode.getOutgoingFlows());
        //  清理活动方向
        currentFlowNode.getOutgoingFlows().clear();

        //  建立新方向
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlowId");
        newSequenceFlow.setSourceFlowElement(currentFlowNode);
        newSequenceFlow.setTargetFlowElement(endFlowNode);
        List newSequenceFlowList = new ArrayList<>();
        newSequenceFlowList.add(newSequenceFlow);
        //  当前节点指向新的方向
        currentFlowNode.setOutgoingFlows(newSequenceFlowList);

        //  完成当前任务
        taskService.complete(task.getId());
    }

    //当前人物列表
    private List<org.activiti.engine.task.Task> getCurrentTaskList(String id) {
        return taskService.createTaskQuery().processInstanceId(id).list();
    }
}
