package com.reunico.training.delegate;

import com.reunico.training.accessors.ProcessVariableAccessor;
import com.reunico.training.constant.ProcessVariableConstants;
import com.reunico.training.model.Customer;
import com.reunico.training.service.PublicService;
import com.reunico.training.util.VariableUtil;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class GetCustomer implements JavaDelegate {

    private final PublicService publicService;

    public GetCustomer(PublicService publicService) {
        this.publicService = publicService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long customerId = (Long) delegateExecution.getVariable(ProcessVariableConstants.CUSTOMER_ID);
        Customer customer = publicService.getCustomer(customerId);
        VariableUtil.setObjectVariable(delegateExecution, ProcessVariableConstants.CUSTOMER, customer);
        VariableUtil.setObjectVariable(delegateExecution, ProcessVariableConstants.NUMBER, customerId);
        VariableUtil.setObjectVariable(delegateExecution, ProcessVariableConstants.CUSTOMER_NAME, customer.getFullName());
    }
}
