/**
 * 
 */
package com.open.capacity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableDiscoveryClient
@SpringBootApplication
public class AuthClientApp {
	public static void main(String[] args) {
		SpringApplication.run(AuthClientApp.class, args);
	}

}
