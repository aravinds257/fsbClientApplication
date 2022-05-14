package com.fsbtech.interviews;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        SpringApplication.run(MainApplication.class, args);

    }
}
