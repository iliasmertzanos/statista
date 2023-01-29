package com.statista.code.challenge.domainobjects;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailTest {

    @Test
    void test(){
        Email email = new Email("ilias@gmail.com");
        assertThat(email.isValid()).isTrue();
        email = new Email("ilias.gmail.com");
        assertThat(email.isValid()).isFalse();
        email = new Email("ilias mertzanidis");
        assertThat(email.isValid()).isFalse();
        email = new Email("ilias_gmail_comn");
        assertThat(email.isValid()).isFalse();
    }
}
