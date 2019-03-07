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
package com.fisherpaykel.service;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import com.fisherpaykel.model.qrg.Product;

import net.sf.jasperreports.engine.JRException;

public interface ReportService {

	/**
	 * Generates a sample pdf.
	 *
	 * @return a sample PDF pay-load in ByteArrayInputStream
	 * @throws JRException
	 */
	ByteArrayInputStream generateSamplePdf() throws JRException;

	/**
	 * Generates a quick reference guide in pdf format using Product data and Jasper report template.
	 *
	 * @param product
	 * @param jrxmlPath
	 * @return a PDF pay-load in ByteArrayInputStream
	 * @throws JRException
	 */
	ByteArrayInputStream generateQRGFromJRXML(final Product product, final String jrxmlPath) throws JRException;

	/**
	 * If the product of a particular country exists in the cache, fetches the old QRG file in the file system, otherwise generates a quick
	 * reference guide in pdf format using Product data and iText API.
	 *
	 * @param dest
	 * @param product
	 * @param country
	 * @param encoded Base64 encoded or not
	 * @return PDF pay-load in InputStream
	 * @throws IOException
	 */
	FileInputStream generateQRGFromIText(final Product product, final String country, final boolean encoded)
			throws IOException;

	/**
	 * This method simply reads the file from the filesystem.
	 *
	 * @param sku
	 * @param country
	 * @param encoded
	 * @return
	 * @throws IOException
	 */
	FileInputStream findQRGBySkuAndCountry(String sku, String country, boolean encoded) throws IOException;
}
