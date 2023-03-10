package com.statista.code.challenge.service;

import com.statista.code.challenge.exceptions.ServerNotReachableException;
import com.statista.code.challenge.exceptions.ServerNotSupportedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailNotificationServiceImpl implements NotificationService {

    @Override
    public void sendBookingConfirmation(ConfirmationDetails confirmationDetails) {
        String emailAddress = confirmationDetails.emailAddress();
        //Here just mocking the case that a server is not reachable
        if (emailAddress.contains("server.foobar")) {
            log.error("The server server.foobar is not reachable");
            throw new ServerNotReachableException("The server server.foobar is not reachable");
        }
        //and here that this domain ist not supported
        if (emailAddress.endsWith("foobar.org")) {
            log.error("Server domain foobar.org is not valid");
            throw new ServerNotSupportedException("Server domain is not valid");
        }
        log.info("Email with body : '" + confirmationDetails.body() + "' was send to email: " + confirmationDetails.emailAddress());
    }
}