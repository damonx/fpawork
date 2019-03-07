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

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import com.fisherpaykel.common.QrgPdfGenerator;
import com.fisherpaykel.common.util.FPAConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

@Component
public class QrgPdfAssetPopulator extends QrgPdfGenerator {

	public QrgPdfAssetPopulator() {

	}

	/**
	 * This method adds Features and Benefits to the QRG.
	 *
	 * @return
	 * @throws IOException
	 */
	@Override
	public void generateQrgPart() throws IOException {

		this.logger.debug("in QrgPdfAssetPopulator");

		// Move to the next column.
		this.document.add(new AreaBreak());

		if (!this.product.getAssets().isEmpty()) {

			// Add line separator
			addLineSeparator(8f);
			final Table assetssHeaderTable = new Table(1);
			populateTableWithText(assetssHeaderTable, 100f, null, VerticalAlignment.MIDDLE, TextAlignment.LEFT, 10f,
					new Text(FPAConstants.OTHER_DOWNLOADS).setFont(this.fontUtil.getGothamMediumFont())
							.setFontColor(Color.BLACK).setFontSize(7.5f));
			this.document.add(assetssHeaderTable);
			

			final Image downloadImage = new Image(ImageDataFactory.create(
					IOUtils.toByteArray(this.getClass().getResourceAsStream(this.env.getProperty(FPAConstants.IMAGE_PATH_DOWNLOADS)))));
			downloadImage.scaleToFit(25f, 18f);

			final List list = new List();
			list.setListSymbol(downloadImage);
			this.product.getAssets().stream().forEach(asset -> {
				final ListItem li = new ListItem();
				li.setKeepTogether(true);
				li.add(new Paragraph(asset.getTitle()).setFont(this.fontUtil.getGothamBookFont())
						.setFontColor(Color.BLACK).setFontSize(5.5f).setFixedLeading(10f).setVerticalAlignment(VerticalAlignment.BOTTOM));
				li.setAction(PdfAction.createURI(asset.getUrl()));
				list.add(li);
			});

			this.document.add(list);
		}

		passToSuccessor();

	}

}
