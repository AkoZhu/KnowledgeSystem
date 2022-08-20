package com.Ako.wiki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;



// @SpringBootApplication contains @ComponentScan(), which scan all its sub package.
// You can use @ComponentScan({"com.Ako", "com.xxx"}) when you want to add other packages.
// @ComponentScan({"com.Ako", "com.xxx"})
@SpringBootApplication
public class WikiApplication {

	private static final Logger LOG = LoggerFactory.getLogger(WikiApplication.class);
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(WikiApplication.class);
		Environment env = app.run(args).getEnvironment();
		LOG.info("Successfully start!");
		LOG.info("Address:\thttp://127.0.0.1:{}", env.getProperty("server.port"));
	}

}
