package io.jee.alaska.sso.server.jpa;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("io.jee.alaska.sso.server.jpa")
@EnableJpaRepositories("io.jee.alaska.sso.server.jpa")
public class SSOTicketServiceJpaConfig {

}
