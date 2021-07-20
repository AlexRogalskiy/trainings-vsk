package com.reunico.training.delegate;

import com.reunico.training.accessors.ProcessVariableAccessor;
import com.reunico.training.constant.ProcessVariableConstants;
import com.reunico.training.service.OrderService;
import com.reunico.training.util.VariableUtil;
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
        String dish = VariableUtil.getObjectVariable(delegateExecution, ProcessVariableConstants.DISH, String.class);
        String customer = VariableUtil.getObjectVariable(delegateExecution, ProcessVariableConstants.CUSTOMER_NAME, String.class);
        Long number = VariableUtil.getObjectVariable(delegateExecution, ProcessVariableConstants.NUMBER, Long.class);
        orderService.save(number, customer, dish);
    }
}
