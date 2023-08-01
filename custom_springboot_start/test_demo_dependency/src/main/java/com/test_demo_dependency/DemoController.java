package com.test_demo_dependency;

import com.demo.DemoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    private DemoService demoService;

    @GetMapping("demo")
    public ResponseEntity demo(){
        String service = demoService.getService();
        return new ResponseEntity(service, HttpStatus.OK);
    }

    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }
}

