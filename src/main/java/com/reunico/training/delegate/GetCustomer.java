package com.reunico.training.delegate;

import com.reunico.training.constant.ProcessVariables;
import com.reunico.training.model.Customer;
import com.reunico.training.service.PublicService;
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
        Long customerId = (Long) delegateExecution.getVariable(ProcessVariables.CUSTOMER_ID);
        Customer customer =publicService.getCustomer(customerId);
        delegateExecution.setVariable(ProcessVariables.CUSTOMER, customer);
        delegateExecution.setVariable(ProcessVariables.NUMBER, customerId);
        delegateExecution.setVariable(ProcessVariables.CUSTOMER_NAME, customer.getFullName());
    }
}
