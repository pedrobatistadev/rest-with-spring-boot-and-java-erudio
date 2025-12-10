package br.com.erudio.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TestLogService {

    private Logger log = LoggerFactory.getLogger(TestLogService.class.getName());

    public String testLog() {
        log.debug("This is DEBUG logger");
        log.info("This is INFO logger");
        log.warn("This is WARN logger");
        log.error("This is ERROR logger");

        return "Logger generated successfully!";
    }
}
