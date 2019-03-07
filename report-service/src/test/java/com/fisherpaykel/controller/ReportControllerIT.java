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
package com.fisherpaykel.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.fisherpaykel.FpaMicroService;
import com.fisherpaykel.model.qrg.Product;
import com.fisherpaykel.util.JsonUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FpaMicroService.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReportControllerIT {

	@LocalServerPort
	private int randomServerPort;

	private final TestRestTemplate restTemplate = new TestRestTemplate();

	private final HttpHeaders headers = new HttpHeaders();
	private Product productUnderTest;
	private String baseUrl;
	private static final File exisitingReportsUnderTest = new File("src/test/resources/nz");
	private static final File exisitingReportsUnderMvn = new File("./nz");

	@BeforeClass
	public static void tidyUpFiles() {
		tidyUpExistingPdfFiles();
	}

	@AfterClass
	public static void tearDown() {
		tidyUpExistingPdfFiles();
	}

	private static void tidyUpExistingPdfFiles() {
		if (exisitingReportsUnderTest.exists()) {
			Arrays.stream(exisitingReportsUnderTest.listFiles()).forEach(File::delete);
		}
		if (exisitingReportsUnderMvn.exists()) {
			Arrays.stream(exisitingReportsUnderMvn.listFiles()).forEach(File::delete);
		}
		exisitingReportsUnderTest.delete();
		exisitingReportsUnderMvn.delete();
	}

	@Before
	public void before() throws IOException {

		javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
				(hostname, sslSession) -> {
					if (hostname.equals("localhost")) {
						return true;
					}
					return false;
				});

		this.headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		this.headers.setContentType(MediaType.APPLICATION_JSON);
		this.productUnderTest = JsonUtils.jsonFile2Object("sample-json-payload.json", Product.class);
		this.baseUrl = "https://localhost:" + this.randomServerPort + "/report";

	}

	@Test
	public void testGenerateQRG() throws IOException {
		final String productJsonPayload = JsonUtils.object2Json(this.productUnderTest);
		final HttpEntity<String> entity = new HttpEntity<>(productJsonPayload, this.headers);

		final ResponseEntity<String> resultResponse = this.restTemplate.postForEntity(this.baseUrl + "/itext/qrg/v1?country=nz", entity,
				String.class);

		assertResultResponse(resultResponse);

	}

	@Test
	public void testGenerateQRG_encoded() throws IOException {
		final String productJsonPayload = JsonUtils.object2Json(this.productUnderTest);
		final HttpEntity<String> entity = new HttpEntity<>(productJsonPayload, this.headers);

		final ResponseEntity<String> resultResponse = this.restTemplate.postForEntity(
				this.baseUrl + "/itext/qrg/v1?country=nz&encoded=true", entity,
				String.class);

		assertResultResponseEncoded(resultResponse);
	}

	@Test
	public void testGetExistingQRG() {
		final HttpEntity<String> entity = new HttpEntity<>(this.headers);
		final String uri = this.baseUrl + "/itext/qrg/b2b/v1?country=nz&sku=24995";

		final ResponseEntity<String> resultResponse = this.restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

		assertResultResponse(resultResponse);

	}

	@Test
	public void testGetExistingQRG_encoded() {
		final HttpEntity<String> entity = new HttpEntity<>(this.headers);
		final String uri = this.baseUrl + "/itext/qrg/b2b/v1?country=nz&sku=24995&encoded=true";

		final ResponseEntity<String> resultResponse = this.restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

		assertResultResponseEncoded(resultResponse);

	}

	private void assertResultResponseEncoded(final ResponseEntity<String> resultResponse) {
		assertThat(resultResponse).isNotNull();
		assertThat(resultResponse.getStatusCodeValue()).isNotNull().isEqualTo(200);
		assertThat(resultResponse.getBody()).isNotNull();
	}

	private void assertResultResponse(final ResponseEntity<String> resultResponse) {
		assertThat(resultResponse).isNotNull();
		assertThat(resultResponse.getStatusCodeValue()).isNotNull().isEqualTo(200);
		assertThat(resultResponse.getBody()).isNotNull().startsWith("%PDF-1.7");
	}

}
