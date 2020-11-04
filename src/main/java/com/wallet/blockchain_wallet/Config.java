package com.wallet.blockchain_wallet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class Config {

    @Bean
    public Random getRandom() {
        return new Random();
    }
}
