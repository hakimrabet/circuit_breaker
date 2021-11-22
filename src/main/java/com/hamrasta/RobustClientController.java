package com.hamrasta;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/client")
class RobustClientController {

    private final RobustService robustService;

    public RobustClientController(RobustService robustService) {
        this.robustService = robustService;
    }

    @GetMapping("/customer/name")
    public ResponseEntity<String> customerName() throws RuntimeException {
        try {
            //return robustService.getExternalCustomerName();
            return ResponseEntity.ok(robustService.resilientCustomerName());
        } catch (RuntimeException e) {
            throw new RuntimeException("ShakyExternalService is down");
        }
    }

}