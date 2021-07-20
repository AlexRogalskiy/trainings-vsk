package com.reunico.training.accessors;

import com.reunico.training.constant.ProcessVariableConstants;
import com.reunico.training.model.Customer;
import org.camunda.bpm.engine.delegate.VariableScope;

public class ProcessVariableAccessor {

    private VariableScope variableScope;

    public ProcessVariableAccessor(VariableScope variableScope) {
        this.variableScope = variableScope;
    }

    Customer getCustomer() {
        return (Customer) variableScope.getVariable(ProcessVariableConstants.CUSTOMER);
    }

    void setCustomer(Customer customer) {
        variableScope.setVariable(ProcessVariableConstants.CUSTOMER, customer);
    }

    String getDish() {
        return (String) variableScope.getVariable(ProcessVariableConstants.DISH);
    }

    void setDish(String dish) {
        variableScope.setVariable(ProcessVariableConstants.DISH, dish);
    }

    String getCustomerName() {
        return (String) variableScope.getVariable(ProcessVariableConstants.CUSTOMER_NAME);
    }

    void setCustomerName(String customerName) {
        variableScope.setVariable(ProcessVariableConstants.CUSTOMER_NAME, customerName);
    }

    Long getCustomerId() {
        return (Long) variableScope.getVariable(ProcessVariableConstants.CUSTOMER_ID);
    }

    void setCustomerId(Long customerId) {
        variableScope.setVariable(ProcessVariableConstants.CUSTOMER_ID, customerId);
    }

    Long getNumber() {
        return (Long) variableScope.getVariable(ProcessVariableConstants.NUMBER);
    }

    void setNumber(Long number) {
        variableScope.setVariable(ProcessVariableConstants.NUMBER, number);
    }
}
