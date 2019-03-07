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
package com.fisherpaykel.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fisherpaykel.model.qrg.Product;
import com.fisherpaykel.service.ReportService;
import com.fisherpaykel.util.JsonUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportServiceImplIT {

	private static final int SIZE_OF_QRG_PDF_FILE = 66803;
	@Autowired
	private ReportService reportService;

	private Product productUnderTest;
	private byte[] b;

	@Before
	public void setup() throws IOException {
		this.productUnderTest = JsonUtils.jsonFile2Object("sample-json-payload.json", Product.class);
		this.b = new byte[SIZE_OF_QRG_PDF_FILE];
	}

	@Test
	public void testGenerateQRGFromIText() throws IOException {
		// WHEN
		final FileInputStream inputStream = this.reportService.generateQRGFromIText(this.productUnderTest, "nz", false);

		// THEN
		assertThat(inputStream).isNotNull();
		assertThat(inputStream.read(this.b)).isNotNull().isEqualTo(SIZE_OF_QRG_PDF_FILE);
	}

	@Test
	public void testFindQRGBySkuandCountry() throws IOException {
		// WHEN
		final FileInputStream inputStream = this.reportService.findQRGBySkuAndCountry("24995", "nz", false);

		// THEN
		assertThat(inputStream).isNotNull();
		assertThat(inputStream.read(this.b)).isNotNull().isEqualTo(SIZE_OF_QRG_PDF_FILE);
	}

}
