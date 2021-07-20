package com.reunico.training.dmn;

import com.reunico.training.constant.ProcessVariableConstants;
import com.reunico.training.model.Customer;
import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.dmn.engine.DmnDecisionRuleResult;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.engine.ProcessEngine;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SelectOfferTest {


    /*

    Тестирование DMN таблиц

     */



    @Autowired
    ProcessEngine processEngine;

    @Test
    public void testPersonalTrue() {
        Double amount = (double) 600L;
        String countryCode = null;

        DmnDecisionRuleResult result = runDmn(countryCode, amount);
        Assert.assertTrue((Boolean) result.get("isPersonal"));
    }

    @Test
    public void testPersonalFalseAndCountryCodeUnknown() {
        Double amount = (double) 1L;
        String countryCode = "XX";

        DmnDecisionRuleResult result = runDmn(countryCode, amount);
        Assert.assertFalse((Boolean) result.get("isPersonal"));
        Assert.assertEquals((String) result.get("dish"), "sandwich");
    }

    @Test
    public void testPersonalFalseAndCountryCodeExists() {
        Double amount = (double) 1L;
        String countryCode = "RU";

        DmnDecisionRuleResult result = runDmn(countryCode, amount);
        Assert.assertFalse((Boolean) result.get("isPersonal"));
        Assert.assertEquals((String) result.get("dish"), "soup");
    }

    private DmnDecisionRuleResult runDmn(String countryCode, Double amount) {
        Customer customer = new Customer(amount, countryCode);
        Map<String, Object> variables = new HashMap<>();
        variables.put("customer", customer);
        DmnDecisionTableResult result = processEngine
                .getDecisionService().evaluateDecisionTableByKey("selectOffer", variables);
        return result.getSingleResult();
    }
}
