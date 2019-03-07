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
package com.fisherpaykel.common;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fisherpaykel.common.populator.QrgPdf24x7StaticInfoPopulator;
import com.fisherpaykel.common.populator.QrgPdfAssetPopulator;
import com.fisherpaykel.common.populator.QrgPdfDimensionPopulator;
import com.fisherpaykel.common.populator.QrgPdfDisclaimerPopulator;
import com.fisherpaykel.common.populator.QrgPdfFeatureAndBenefitsPopulator;
import com.fisherpaykel.common.populator.QrgPdfProductDataPopulator;
import com.fisherpaykel.common.populator.QrgPdfSkuPopulator;
import com.fisherpaykel.common.populator.QrgPdfSpecificationPopulator;
import com.fisherpaykel.model.qrg.Product;

@Component
public class QRGFactory {

	@Autowired
	private QrgPdfProductDataPopulator productDataPopulator;
	@Autowired
	private QrgPdf24x7StaticInfoPopulator static24x7infoPopulator;
	@Autowired
	private QrgPdfDimensionPopulator dimensionPopulator;
	@Autowired
	private QrgPdfFeatureAndBenefitsPopulator featureBenefitsPopulator;
	@Autowired
	private QrgPdfSpecificationPopulator specificationPopulator;
	@Autowired
	private QrgPdfSkuPopulator skuPopulator;
	@Autowired
	private QrgPdfDisclaimerPopulator disclaimerPopulator;
	@Autowired
	private QrgPdfAssetPopulator assetPopulator;

	public QRGFactory() {

	}

	public void buildAndStoreQRGFile(final Product product, final String destination) throws IOException {

		// This method adds header information in the pdf.
		this.productDataPopulator.createPdfLayout(product, destination);

		// The dimensionPopulator successor is responsible for adding product dimensions in the pdf generation chain.
		this.productDataPopulator.setSuccessor(this.dimensionPopulator)
				.setSuccessor(this.featureBenefitsPopulator)
				.setSuccessor(this.specificationPopulator)
				.setSuccessor(this.skuPopulator)
				.setSuccessor(this.disclaimerPopulator)
				.setSuccessor(this.assetPopulator)
				.setSuccessor(this.static24x7infoPopulator);

		// This method is responsible for generating the pdf using the responsibility chain.
		this.productDataPopulator.generateQrgPart();

		// This method ensures flushing open resources.
		this.static24x7infoPopulator.finalise();

	}
}
