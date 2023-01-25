package com.statista.code.challenge.domainobjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Email {
    private static final String REGEX_EMAIL_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@" + "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$";
    @NotNull
    private String emailAddress;

    public boolean isValid() {
        return Pattern.compile(REGEX_EMAIL_PATTERN).matcher(emailAddress).matches();
    }
}
