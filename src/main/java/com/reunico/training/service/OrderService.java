package com.reunico.training.service;

import com.reunico.training.model.Customer;
import com.reunico.training.model.Order;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.connect.Connectors;
import org.camunda.connect.httpclient.HttpConnector;
import org.camunda.connect.httpclient.HttpRequest;
import org.camunda.connect.httpclient.HttpResponse;
import org.camunda.spin.Spin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderService {

    private Logger logger = LoggerFactory.getLogger(PublicService.class);

    @Value("${application.orderUrl}")
    private String orderUrl;

    public Order save(Long number, String customer, String dish) {
        Order order = new Order(customer, dish);
        HttpConnector httpConnector = Connectors.getConnector(HttpConnector.ID);
        HttpRequest request = httpConnector.createRequest();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");
        request.setRequestParameter("headers", headers);
        request.setRequestParameter("url", String.format("%s/%s", orderUrl, number));
        request.setRequestParameter("method","PUT");
        request.setRequestParameter("payload", Spin.JSON(order).toString());
        HttpResponse response = request.execute();
        if (response.getStatusCode() == 200) {
            logger.info(String.format("Response: %s", response.getResponse()));
        } else {
            logger.info("Response is: {} {}", response.getStatusCode(), response.getResponse());
            throw new BpmnError("ORDER_SAVE_ERROR", response.getResponse());
        }
        return order;
    }
}
