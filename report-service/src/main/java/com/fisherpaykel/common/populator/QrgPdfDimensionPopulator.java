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

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.fisherpaykel.common.QrgPdfGenerator;
import com.fisherpaykel.common.util.FPAConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

@Component
@SuppressWarnings("deprecation")
public class QrgPdfDimensionPopulator extends QrgPdfGenerator {

	public QrgPdfDimensionPopulator() {

	}

	/**
	 * This method adds Dimensions to the QRG.
	 *
	 * @return
	 * @throws IOException
	 */
	@Override
	public void generateQrgPart() throws IOException {
		this.logger.debug("in Method withDimensions");

		// Move to the next column.
		this.document.add(new AreaBreak());

		if (this.product.getDimensions() != null && !this.product.getDimensions().isEmpty()) {
			final Table dimensionHeaderTable = new Table(1);
			populateTableWithText(dimensionHeaderTable, 100f, null, VerticalAlignment.MIDDLE, TextAlignment.LEFT, 12f,
					new Text(FPAConstants.DIMENSIONS).setFont(this.fontUtil.getGothamBoldFont())
							.setFontColor(Color.BLACK).setFontSize(8.6f));
			this.document.add(dimensionHeaderTable);
			

			// Add line separator
			addLineSeparator(8f);

			final Table dimensionsTable = new Table(2);
			this.product.getDimensions().stream().forEach(dimension -> {
				populateTableWithText(dimensionsTable, 50f, 15f, VerticalAlignment.BOTTOM, TextAlignment.LEFT, 12f,
						new Text(dimension.getName()).setFont(this.fontUtil.getGothamMediumFont()).setFontColor(Color.BLACK)
								.setFontSize(7f));
				populateTableWithText(dimensionsTable, 50f, 15f, VerticalAlignment.BOTTOM, TextAlignment.RIGHT, 12f,
						new Text(dimension.getValue().concat(FPAConstants.SPACE).concat(this.product.getUnit()))
								.setFont(this.fontUtil.getGothamMediumFont())
								.setFontColor(Color.BLACK).setFontSize(7f));
			});

			this.document.add(dimensionsTable);
		}

		// Add Blank
		populateBlanks(1);

		// Dimensions Image
		if (StringUtils.isNotBlank(this.product.getDimensionImageUrl())) {
			final ImageData data = ImageDataFactory.create(this.product.getDimensionImageUrl());
			final Image img = new Image(data);
			this.document.add(img.scaleToFit(125f, 250f).setHorizontalAlignment(HorizontalAlignment.CENTER));
		}

		// Add Blank
		populateBlanks(1);

		passToSuccessor();
	}

}
