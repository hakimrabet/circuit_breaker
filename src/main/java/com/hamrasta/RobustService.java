package com.hamrasta;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
class RobustService {

    private static Logger logger = LoggerFactory.getLogger(RobustService.class);
    private static final int MAX_ATTEMPT = 3;
    private static final long OPEN_TIME_OUT = 15000L;
    private static final long REST_TIME_OUT = 30000l;

    private final RetryTemplate retryTemplate;

    public RobustService(RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
    }

    public String getExternalCustomerName() {
        ResponseEntity<String> result = new RestTemplate().exchange("http://localhost:8080/api/customer/name",
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        return result.getBody();
    }

    @CircuitBreaker(maxAttempts = MAX_ATTEMPT, openTimeout = OPEN_TIME_OUT, resetTimeout = REST_TIME_OUT)
    public String resilientCustomerName() {
        return retryTemplate.execute(new RetryCallback<String, RuntimeException>() {
            @Override
            public String doWithRetry(RetryContext context) {
                logger.info(String.format("Retry count %d", context.getRetryCount()));
                return getExternalCustomerName();
            }
        });
    }

    @Recover // work with AOP
    public String fallback(Throwable e) {
        logger.info("returning name from fallback method");
        return "Mini";
    }

}