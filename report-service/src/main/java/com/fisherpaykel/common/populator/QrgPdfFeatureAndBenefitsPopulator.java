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
package com.fisherpaykel.common.populator;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import com.fisherpaykel.common.QrgPdfGenerator;
import com.fisherpaykel.common.util.FPAConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

@Component
@SuppressWarnings("deprecation")
public class QrgPdfFeatureAndBenefitsPopulator extends QrgPdfGenerator {

	public QrgPdfFeatureAndBenefitsPopulator() {

	}

	/**
	 * This method adds Features and Benefits to the QRG.
	 *
	 * @return
	 * @throws IOException
	 */
	@Override
	public void generateQrgPart() throws IOException {

		this.logger.debug("in QrgFeatureAndBenefitsPopulator");
		if (this.product.getFeaturesBenefits() != null && !this.product.getFeaturesBenefits().isEmpty()) {
			final Table featuresBenefitsHeaderTable = new Table(1);
			populateTableWithText(featuresBenefitsHeaderTable, 100f, null, VerticalAlignment.MIDDLE, TextAlignment.LEFT, 12f,
					new Text(FPAConstants.FEATURES_AND_BENEFITS).setFont(this.fontUtil.getGothamBoldFont())
							.setFontColor(Color.BLACK).setFontSize(8.6f));
			this.document.add(featuresBenefitsHeaderTable);
			

			final Table featuresBenefitsTable = new Table(1);
			featuresBenefitsTable.setBorder(Border.NO_BORDER);

			// Add line separator
			addLineSeparator(8f);

			this.product.getFeaturesBenefits().stream().forEach(productFeature -> {
				populateTableWithText(featuresBenefitsTable, 100f, null, VerticalAlignment.MIDDLE, TextAlignment.LEFT, 12f,
						new Text(productFeature.getTitle()).setFont(this.fontUtil.getGothamMediumFont())
								.setFontColor(Color.BLACK).setFontSize(7.5f),
						new Text(Jsoup.parse(productFeature.getDescription()).text()).setFont(this.fontUtil.getGothamBookFont())
								.setFontColor(Color.BLACK).setFontSize(7f));
				populateTableWithText(featuresBenefitsTable, 100f, null, VerticalAlignment.MIDDLE, TextAlignment.LEFT, 6f,
						new Text(" ").setFont(this.fontUtil.getGothamBookFont()).setFontSize(6f));

			});

			this.document.add(featuresBenefitsTable);
		}

		passToSuccessor();

	}

}
