package br.com.erudio.controllers;

import br.com.erudio.services.TestLogService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestLogController {

    @Autowired
    TestLogService log;

    @Operation(summary = "Test Logg", description = "Teste Logg", tags = {"Logger"})
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String testLog() {
        return log.testLog();
    }
}
