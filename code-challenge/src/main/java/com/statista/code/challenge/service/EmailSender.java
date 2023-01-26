package com.statista.code.challenge.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailSender {
    public void sendEmail(ConfirmationDetails confirmationDetails) {
        log.info("Email with body : '" + confirmationDetails.body() + "' was send to email: " + confirmationDetails.emailAddress());
    }
}
