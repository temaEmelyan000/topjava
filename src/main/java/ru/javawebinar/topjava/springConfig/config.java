package ru.javawebinar.topjava.springConfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"ru.javawebinar.topjava.repository", "ru.javawebinar.topjava.service", "ru.javawebinar.topjava.web"})
public class config {

}
