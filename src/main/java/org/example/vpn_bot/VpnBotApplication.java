package org.example.vpn_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VpnBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(VpnBotApplication.class, args);
    }

}
