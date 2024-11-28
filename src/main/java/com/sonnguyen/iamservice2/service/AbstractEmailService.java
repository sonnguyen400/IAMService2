package com.sonnguyen.iamservice2.service;

import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractEmailService {
    @Value("${service.mail.from}")
    protected String from;

    abstract void sendEmail(String to, String subject, String body);
}
