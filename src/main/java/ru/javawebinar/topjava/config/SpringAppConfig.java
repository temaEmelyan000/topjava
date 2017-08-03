package ru.javawebinar.topjava.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan({"ru.**.service", "ru.**.web"})
@Configuration
public class SpringAppConfig {
}
