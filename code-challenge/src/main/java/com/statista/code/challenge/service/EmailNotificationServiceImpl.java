package com.statista.code.challenge.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailNotificationServiceImpl implements NotificationService {

    @Override
    public void sendBookingConfirmation(ConfirmationDetails confirmationDetails) {
        String emailAddress = confirmationDetails.emailAddress();
        if (emailAddress.contains("server.foobar")) {
            throw new ServerNotReachableException("The server server.foobar is not reachable");
        }
        if (emailAddress.endsWith("foobar.org")) {
            log.error("Server domain foobar.org is not valid");
            throw new ServerNotSupportedException("Server domain is not valid");
        }

        log.info("Email with body : '" + confirmationDetails.body() + "' was send to email: " + emailAddress);
    }
}
