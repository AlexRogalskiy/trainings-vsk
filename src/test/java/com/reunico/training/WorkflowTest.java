package com.reunico.training;

import com.reunico.training.constant.ProcessVariableConstants;
import com.reunico.training.model.Customer;
import com.reunico.training.service.OrderService;
import com.reunico.training.service.PublicService;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RuntimeService;
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
import java.util.List;

import static org.camunda.bpm.engine.impl.el.DateTimeFunctionMapper.dateTime;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
@Deployment(resources = {"process.bpmn", "selectOffer.dmn"})
public class WorkflowTest extends AbstractProcessEngineRuleTest {

  @Autowired
  public ProcessEngine processEngine;

  @Autowired
  public RuntimeService runtimeService;

  @MockBean
  public OrderService orderService;

  @MockBean
  public PublicService publicService;



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

  @Before
  public void setup() {
    Mockito.when(publicService.getCustomer(123L)).thenReturn(getPoorCustomer());
    Mockito.when(publicService.getCustomer(321L)).thenReturn(getRichCustomer());
  }

  @Test
  @RequiredHistoryLevel(ProcessEngineConfiguration.HISTORY_NONE)
  public void shouldExecuteHappyPath() {
    // given
    String processDefinitionKey = "vsk-training-process";

    // when
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
            processDefinitionKey, withVariables(ProcessVariableConstants.CUSTOMER_ID, 123L)
    );

    // then
    assertThat(processInstance).isStarted().hasProcessDefinitionKey("vsk-training-process");
    assertThat(processInstance).isWaitingAt("GetCustomer");
    execute(job());
    assertThat(processInstance).isWaitingAt("CheckCustomer");

    /* Примеры тестирования таймеров - тк нет возможности получить экземпляры таймеров по их описанию ID или имени,
    * необходимы костыли - проверять таймеры по значению или по activity к которому таймер привязан */

    List<Job> timers = managementService
            .createJobQuery()
            .processInstanceId(processInstance.getId())
            .timers().list();


    Job firstNotification = managementService
            .createJobQuery()
            .processInstanceId(processInstance.getId())
            .timers()
            .list()
            .stream()
            .filter(
                    timer -> timer.getDuedate().equals(
                            dateTime().withMillis( Long.MAX_VALUE ).minusDays(3).toDate()
                    )
            )
            .findFirst()
            .orElseThrow();
    execute(firstNotification);

    assertTrue(timers.stream().anyMatch(timer -> timer.getDuedate().equals(
            dateTime().withMillis( Long.MAX_VALUE ).minusDays(5).toDate()
    )));

  }

  private Customer getRichCustomer() {
    return new Customer((double) 600L, "RU");
  }

  private Customer getPoorCustomer() {
    return new Customer((double) 100L, "UZ");
  }

}
