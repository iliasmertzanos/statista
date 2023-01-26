package com.statista.code.challenge.service;

import com.statista.code.challenge.exceptions.ServerNotReachableException;
import com.statista.code.challenge.exceptions.ServerNotSupportedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailNotificationServiceImpl implements NotificationService {

    private final EmailSender emailSender;

    public EmailNotificationServiceImpl(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

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
        emailSender.sendEmail(confirmationDetails);
    }
}
