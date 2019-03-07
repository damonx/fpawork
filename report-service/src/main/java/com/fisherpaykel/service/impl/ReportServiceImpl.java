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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.jcs.access.CacheAccess;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fisherpaykel.cache.QrgReportCache;
import com.fisherpaykel.common.QRGFactory;
import com.fisherpaykel.common.util.FPAConstants;
import com.fisherpaykel.common.util.FPFileCodecUtils;
import com.fisherpaykel.model.qrg.Product;
import com.fisherpaykel.model.qrg.VariationAttribute;
import com.fisherpaykel.service.ReportService;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReportServiceImpl implements ReportService {

	private final Logger logger = Logger.getLogger(ReportServiceImpl.class);

	@Autowired
	private Environment env;

	@Autowired
	private QrgReportCache qrgReportCache;

	@Autowired
	private QRGFactory qrgFactory;

	/**
	 * @see com.fisherpaykel.service.ReportService#generateSamplePdf()
	 */
	@Override
	public ByteArrayInputStream generateSamplePdf() throws JRException {
		this.logger.debug("in Method generateSamplePdf");
		final JasperReport jasperReport = JasperCompileManager
				.compileReport(this.getClass().getResourceAsStream("/jrxml/sample.jrxml"));
		final JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, new JREmptyDataSource());

		return new ByteArrayInputStream(JasperExportManager.exportReportToPdf(jasperPrint));
	}

	/**
	 * @see com.fisherpaykel.service.ReportService#generateQRGFromJRXML(Product)
	 */
	@Override
	public ByteArrayInputStream generateQRGFromJRXML(final Product product, final String jrxmlPath) throws JRException {

		this.logger.debug("in Method generateQRGFromJRXML");

		final JasperReport jasperReport = JasperCompileManager
				.compileReport(this.getClass().getResourceAsStream(jrxmlPath));

		final List<Product> products = new ArrayList<>();
		products.add(product);

		final Map<String, Object> parameters = new HashMap<>();
		parameters.put("currentDate", new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
		parameters.put("dimensionsDataSource", new JRBeanCollectionDataSource(product.getDimensions()));
		parameters.put("featuresDataSource", new JRBeanCollectionDataSource(product.getFeaturesBenefits()));
		parameters.put("specificationsDataSource", new JRBeanCollectionDataSource(product.getSpecifications()));

		for (final VariationAttribute variationAttribute : product.getVariationAttributes()) {
			if (StringUtils.equals("finish", variationAttribute.getAttribute())) {
				parameters.put("finish", variationAttribute.getValue());
			}
		}

		final JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
				new JRBeanCollectionDataSource(products));

		return new ByteArrayInputStream(JasperExportManager.exportReportToPdf(jasperPrint));
	}

	@Override
	public FileInputStream generateQRGFromIText(final Product product, final String country, final boolean encoded)
			throws IOException {

		this.logger.debug("in Method generateQRGFromIText");

		final String cacheKey = String.join(FPAConstants.HYPHEN, StringUtils.lowerCase(country), product.getSku());
		final CacheAccess<String, Product> productCache = this.qrgReportCache.getCache();

		final Product cachedProduct = productCache.get(cacheKey);
		final String destination = buildFilePath(product.getSku(), country);
		final File file = new File(destination);

		if (cachedProduct == null || !cachedProduct.equals(product)) {
			// put the product into the cache
			productCache.put(cacheKey, product);
			// generate the new QRG file;
			generateQRG(product, destination, file);
		} else if (cachedProduct.equals(product)) {
			// directly fetches the QRG PDF file from the destination if it exists and
			// returns the base64 encoded file
			if (file.exists()) {
				return fetchExistingQrgFile(encoded, destination);

			} else {
				// In case of the file has been deleted somehow in the file system
				generateQRG(product, destination, file);
			}
		}
		return buildFileInputStream(encoded, destination);
	}

	@Override
	public FileInputStream findQRGBySkuAndCountry(final String sku, final String country,
			final boolean encoded) throws IOException {

		this.logger.debug("in Method readQRG");

		final String destination = buildFilePath(sku, country);
		final File file = new File(destination);

		if (file.exists()) {
			return fetchExistingQrgFile(encoded, destination);
		}

		return buildFileInputStream(encoded, destination);
	}

	private FileInputStream buildFileInputStream(final boolean encoded, final String destination)
			throws FileNotFoundException, IOException {
		return encoded == true ? new FileInputStream(FPFileCodecUtils.encodeFile(destination))
				: new FileInputStream(new File(destination));
	}

	private String buildFilePath(final String sku, final String country) {
		return this.env.getProperty(FPAConstants.QRG_DOC_PATH) + country + "/" + sku + ".pdf";
	}

	private FileInputStream fetchExistingQrgFile(final boolean encoded, final String destination)
			throws FileNotFoundException, IOException {
		if (!encoded) {
			return new FileInputStream(new File(destination));
		}

		if (new File(destination.concat("-base64encoded")).exists()) {
			return new FileInputStream(destination.concat("-base64encoded"));
		} else {
			return new FileInputStream(FPFileCodecUtils.encodeFile(destination));
		}
	}

	private void generateQRG(final Product product, final String destination, final File file) throws IOException {
		file.getParentFile().mkdirs();
		// this.qrgFactory.addQrgBuilder(this.qrgBuilder.addProduct(product, destination)).buildAndPersistQRGFile();
		this.qrgFactory.buildAndStoreQRGFile(product, destination);
	}
}
