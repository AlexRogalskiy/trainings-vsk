package com.reunico.training.service;

import java.util.HashMap;
import java.util.Map;

import com.reunico.training.model.Customer;
import org.camunda.connect.Connectors;
import org.camunda.connect.httpclient.HttpConnector;

import org.camunda.connect.httpclient.HttpRequest;
import org.camunda.connect.httpclient.HttpResponse;
import org.camunda.spin.Spin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PublicService {
    @Value("${application.url}")
    private String url;

    private Logger logger = LoggerFactory.getLogger(PublicService.class);

    public Customer getCustomer(Long number) {
        logger.info(String.format("URL: %s", url));
        Customer customer = null;
        HttpConnector httpConnector = Connectors.getConnector(HttpConnector.ID);
        HttpRequest request = httpConnector.createRequest();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");
        request.setRequestParameter("headers", headers);
        request.setRequestParameter("url", url);
        request.setRequestParameter("method","GET");
        HttpResponse response = request.execute();
        if (response.getStatusCode() == 200 && !response.getResponse().isBlank()) {
            customer = Spin.JSON(response.getResponse()).mapTo(Customer.class);
            logger.info(String.format("Customer: %s", customer));
        }
        return customer;
    }
}
