package com.myblogbackend.blog.strategyPattern;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class MailFactory {
    private final Logger log = LogManager.getLogger(MailFactory.class);

    private final Environment environment;
    private final GmailStrategy gmailStrategy;

    public MailStrategy createStrategy() {
        String[] activeProfiles = environment.getActiveProfiles();
        log.info("Active profiles '{}'", Arrays.toString(activeProfiles));

        //Check if Active profiles contains "local" or "test"
        if (Arrays.stream(environment.getActiveProfiles()).anyMatch(
                env -> (env.equalsIgnoreCase(Constant.getDevProfile())))) {
            return this.gmailStrategy;
        } else {
            return this.gmailStrategy;
        }
    }
}