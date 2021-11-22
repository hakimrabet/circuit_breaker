package com.hamrasta;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController()
@RequestMapping("/api")
class ShakyExternalController {

    private static Logger logger = LoggerFactory.getLogger(ShakyExternalController.class);

    @GetMapping("/customer/name")
    public String customerName() {
        logger.info("called ShakyExternalService api/customer/name");
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(2);
        if (randomInt < 2)
            throw new ShakyServiceException("Service is unavailable");

        return "Mickey";
    }

}