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

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fisherpaykel.common.util.FPAConstants;
import com.fisherpaykel.common.util.FPHttpUtils;
import com.fisherpaykel.exception.FPBadRequestException;
import com.fisherpaykel.exception.FPNotFoundException;
import com.fisherpaykel.exception.FPServerInternalException;
import com.fisherpaykel.model.qrg.Product;
import com.fisherpaykel.service.ReportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/report")
@Api(value = "QRG report", description = "FPA QRG Report service endpoints")
public class ReportController {

	@Autowired
	private Environment env;

	@Autowired
	private ReportService reportService;

	private final ObjectMapper mapper = new ObjectMapper();

	@ApiOperation(value = "Test Endpoint", response = String.class)
	@RequestMapping(path = "/test", method = RequestMethod.GET)
	public String index() {
		return FPAConstants.FPA_QRG_MICROSERVICE;
	}

	@ApiOperation(value = "Get a sample QRG", response = ResponseEntity.class)
	@RequestMapping(path = "/sample", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> getSample() throws JRException {
		final ByteArrayInputStream byteArrayInputStream = this.reportService.generateSamplePdf();
		return FPHttpUtils.generatePayload(new InputStreamResource(byteArrayInputStream), "OB30DTEPX3_N",
				MediaType.APPLICATION_PDF);
	}

	@ApiOperation(value = "Create QRG report in JASPER report", response = ResponseEntity.class)
	@RequestMapping(path = "/jasper/qrg/v1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<InputStreamResource> getQuickReferenceGuide(@RequestBody final String json,
			@SuppressWarnings("unused") @RequestParam(name = "country", required = false) final String country)
			throws JRException, JsonParseException, JsonMappingException, IOException {

		final Product product = this.mapper.readValue(json, Product.class);
		final String jrxmlPath = this.env.getProperty(FPAConstants.JRXML_PATH);

		final ByteArrayInputStream byteArrayInputStream = this.reportService.generateQRGFromJRXML(product, jrxmlPath);

		return FPHttpUtils.generatePayload(new InputStreamResource(byteArrayInputStream), product.getModelNumber(),
				MediaType.APPLICATION_PDF);

	}

	@ApiOperation(value = "Create QRG report in iText report", response = ResponseEntity.class)
	@RequestMapping(path = "/itext/qrg/v1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully created a QRG report"),
			@ApiResponse(code = 400, message = "Bad request, country code missing"),
			@ApiResponse(code = 500, message = "Internal Server Errors")
	})
	public ResponseEntity<InputStreamResource> generateQRG(@RequestBody final String json,
			@RequestParam(name = "country", required = true) final String country,
			@RequestParam(name = "encoded", required = false) final String encoded)
			throws FPBadRequestException, FPServerInternalException {

		if (StringUtils.isEmpty(json)
				|| !FPAConstants.ACCEPTED_COUNTRIES.stream().anyMatch(e -> StringUtils.equalsIgnoreCase(e, country))) {
			throw new FPBadRequestException("The product json payload is empty or the country is not supported.");
		}

		Product product = null;
		try {
			product = this.mapper.readValue(json, Product.class);
		} catch (JsonParseException | JsonMappingException e) {
			throw new FPBadRequestException("The product json payload is malformed");
		} catch (final IOException ioe) {
			throw new FPServerInternalException("Cannot serialize the JSON");
		}

		try {
			final FileInputStream inputStream = this.reportService.generateQRGFromIText(product, country,
					BooleanUtils.toBoolean(encoded));
			return FPHttpUtils.generatePayload(new InputStreamResource(inputStream), product.getSku(),
					MediaType.APPLICATION_PDF);
		} catch (final IOException ioe) {
			throw new FPServerInternalException("Cannot generate PDF file");
		}
	}

	@ApiOperation(value = "Get an existing QRG report based on sku and country", response = ResponseEntity.class)
	@RequestMapping(path = "/itext/qrg/b2b/v1", method = RequestMethod.GET)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully got a QRG report"),
			@ApiResponse(code = 400, message = "Bad request, country code or sku missing"),
			@ApiResponse(code = 500, message = "Internal Server Errors")
	})
	public ResponseEntity<InputStreamResource> fetchExistingQRG(
			@RequestParam(name = "country", required = true) final String country,
			@RequestParam(name = "encoded", required = false) final String encoded,
			@RequestParam(name = "sku", required = true) final String sku)
			throws FPBadRequestException, FPNotFoundException {

		if (!FPAConstants.ACCEPTED_COUNTRIES.stream().anyMatch(e -> StringUtils.equalsIgnoreCase(e, country))) {
			throw new FPBadRequestException("The product json payload is empty or the country is not supported.");
		}

		try {
			final FileInputStream inputStream = this.reportService.findQRGBySkuAndCountry(sku, country, BooleanUtils.toBoolean(encoded));
			return FPHttpUtils.generatePayload(new InputStreamResource(inputStream), sku,
					MediaType.APPLICATION_PDF);
		} catch (final IOException ioe) {
			throw new FPNotFoundException(String.format("The QRG file with sku %s does not exist.", sku));
		}
	}

}