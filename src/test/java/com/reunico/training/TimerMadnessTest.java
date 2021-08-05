package com.reunico.training;

import com.reunico.training.constant.ProcessVariableConstants;
import com.reunico.training.model.Customer;
import com.reunico.training.service.OrderService;
import com.reunico.training.service.PublicService;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.impl.persistence.entity.TimerEntity;
import org.camunda.bpm.engine.management.JobDefinition;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.RequiredHistoryLevel;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.camunda.bpm.spring.boot.starter.test.helper.AbstractProcessEngineRuleTest;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.camunda.bpm.engine.impl.el.DateTimeFunctionMapper.dateTime;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
@Deployment(resources = {"timerMadness.bpmn"})
public class TimerMadnessTest extends AbstractProcessEngineRuleTest {

  @Autowired
  public ProcessEngine processEngine;

  @Autowired
  public RuntimeService runtimeService;

  @Autowired
  ManagementService managementService;

/*
  Документация Camunda BPM Assert
  https://github.com/camunda/camunda-bpm-assert/blob/master/docs/User_Guide_BPMN.md
 */
  /* Нужно для работы Process Test Coverage */
  @Rule
  @ClassRule
  public static ProcessEngineRule rule;

  /* Нужно для работы Process Test Coverage */
  @PostConstruct
  void initRule() {
    rule = TestCoverageProcessEngineRuleBuilder.create(processEngine).build();
  }



  @Test
  @RequiredHistoryLevel(ProcessEngineConfiguration.HISTORY_NONE)
  public void shouldExecuteHappyPath() {
    // given
    String processDefinitionKey = "timerMadness";
    Date remindDate = dateTime().plusDays(1).toDate();
    Date informDate = dateTime().plusDays(5).toDate();;
    Date escalationDate = dateTime().plusDays(10).toDate();;

    // when
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
            processDefinitionKey, withVariables(
                    "remindDate", remindDate,
                   "informDate", informDate,
                    "escalationDate", escalationDate
            ));

    // then
    assertThat(processInstance).isStarted().hasProcessDefinitionKey(processDefinitionKey);
    assertThat(processInstance).isWaitingAt("unhappySalesGuy");

    /*
      assert that all jobs created with expected values
      not possible to get and check timer/job values by timer definition ids (remindDate, informDate, escalationDate)
      so only the way to assert timer values is get all timers and filter it by expected values
      and there can be an issue if i have timers with the same values
    * */

    List<Job> timers = managementService
            .createJobQuery()
            .processInstanceId(processInstance.getId())
            .timers().list();

      for (Job timer : timers) {
        System.out.println(((TimerEntity) timer).getJobHandlerConfigurationRaw());
      }

    TimerEntity escalationTimer =
            timers.stream()
                    .map(job -> (TimerEntity) job)
                    .filter(timer -> timer.getJobHandlerConfigurationRaw()
                    .equals("escalationTimer")).findFirst().orElseThrow();
    System.out.println("Actual:" + escalationTimer.getDuedate());
    System.out.println("Expected:" + escalationDate);
    assertEquals(escalationDate, escalationTimer.getDuedate());

    // complete(task());


  }
}
