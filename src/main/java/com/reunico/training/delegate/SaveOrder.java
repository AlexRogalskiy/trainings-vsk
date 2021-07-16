package com.reunico.training.delegate;

import com.reunico.training.constant.ProcessVariables;
import com.reunico.training.model.Customer;
import com.reunico.training.service.OrderService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class SaveOrder implements JavaDelegate {

    private final OrderService orderService;

    public SaveOrder(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String dish = (String) delegateExecution.getVariable(ProcessVariables.DISH);
        String customer = (String) delegateExecution.getVariable(ProcessVariables.CUSTOMER_NAME);
        Long number = (Long) delegateExecution.getVariable(ProcessVariables.NUMBER);
        orderService.save(number, customer, dish);
    }
}
