/*
 * Copyright (c) Fisher and Paykel Appliances.
 *
 * This document is copyright. Except for the purpose of fair reviewing, no part
 * of this publication may be reproduced or transmitted in any form or by any
 * means, electronic or mechanical, including photocopying, recording, or any
 * information storage and retrieval system, without permission in writing from
 * the publisher. Infringers of copyright render themselves liable for
 * prosecution.
 */
package com.fisherpaykel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class FpaMicroService {


	@Autowired
	protected Environment env;
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(final CorsRegistry registry) {
				registry.addMapping("/*").allowedOrigins(System.getProperty("allowed.origins"));
			}
		};
	}

	public static void main(final String[] args) throws IOException {

		if (args != null && args.length>0) {
			final InputStream input = new FileInputStream(args[0]);
			final Properties props = new Properties();
			props.load(input);
			props.entrySet().forEach(entry -> System.setProperty(entry.getKey().toString(), entry.getValue().toString()));
		}
		SpringApplication.run(FpaMicroService.class, args);
	}
}
