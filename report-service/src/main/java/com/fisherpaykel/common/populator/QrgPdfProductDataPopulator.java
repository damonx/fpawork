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
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import com.fisherpaykel.common.QrgPdfGenerator;
import com.fisherpaykel.common.util.FPAConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

@Component
@SuppressWarnings("deprecation")
public class QrgPdfProductDataPopulator extends QrgPdfGenerator {

	/**
	 * Default Constructor
	 */
	public QrgPdfProductDataPopulator() {

	}

	/**
	 * This method adds Product Image, Product Model Number, Product Title, Product Style, Product Description and Finish to the QRG.
	 *
	 * @throws IOException
	 */
	@Override
	public void generateQrgPart() throws IOException {

		this.logger.debug("in QrgPdfProductDataPopulator");

		// Product Title
		final Table productHeaderTable = new Table(1);
		populateTableWithText(productHeaderTable, 100f, null, VerticalAlignment.TOP, TextAlignment.LEFT, 23f,
				new Text(this.product.getName()).setFont(this.fontUtil.getGothamLightFont())
						.setFontColor(Color.BLACK).setFontSize(15f));
		this.document.add(productHeaderTable);
		/*populateDocumentWithText(this.document, this.product.getName(), this.fontUtil.getGothamLightFont(), 15f, Color.BLACK,
				TextAlignment.LEFT, 23f);*/

		// Line Separator
		addLineSeparator(9f);

		// Product Style & Series
		populateDocumentWithText(this.document, String.join(FPAConstants.PIPE_SEPARATOR, this.product.getSeries(), this.product.getStyle()),
				this.fontUtil.getGothamBookFont(), 6.5f, Color.BLACK, TextAlignment.LEFT, 12f);

		// Finish
		final StringBuilder finish = new StringBuilder();
		this.product.getVariationAttributes().stream().forEach(e -> {
			finish.append(FPAConstants.PIPE_SEPARATOR).append(e.getValue());
		});
		if (StringUtils.isNotBlank(finish.toString())) {
			populateDocumentWithText(this.document, finish.substring(7), this.fontUtil.getGothamBookFont(), 6.5f,
					Color.BLACK, TextAlignment.LEFT, 12f);
		}

		populateVerticalSpace(8f);

		// Product Image.
		final ImageData data = ImageDataFactory.create(this.product.getImageUrl());
		final Image img = new Image(data);
		final Table imageTable = new Table(1);
		final Cell imageCell = new Cell().setWidthPercent(25f).setBorder(Border.NO_BORDER).setBackgroundColor(FPAConstants.LIGHT_GRAY)
				.add(img.scaleToFit(125f, 250f).setHorizontalAlignment(HorizontalAlignment.CENTER));
		imageTable.addCell(imageCell);
		this.document.add(imageTable);

		populateVerticalSpace(8f);

		// Product Description.
		populateDocumentWithText(this.document, this.product.getShortDescription(), this.fontUtil.getGothamBookFont(), 7f,
				Color.BLACK, TextAlignment.LEFT, 12f);

		// Product key selling points.
		final Image bulletImage = new Image(ImageDataFactory.create(
				IOUtils.toByteArray(this.getClass().getResourceAsStream(this.env.getProperty(FPAConstants.IMAGE_PATH_BULLET)))));
		bulletImage.scaleToFit(10f, 6f);

		final List list = new List();
		list.setListSymbol(bulletImage);
		Jsoup.parse(this.product.getLongDescription()).select("li").stream().forEach(keySellingPoint -> {
			final ListItem li = new ListItem();
			li.setKeepTogether(true);
			li.add(new Paragraph(keySellingPoint.text()).setFont(this.fontUtil.getGothamBookFont())
					.setFontColor(Color.BLACK).setFontSize(7f).setFixedLeading(10f).setVerticalAlignment(VerticalAlignment.BOTTOM));
			li.setPaddingBottom(3f);
			list.add(li);
		});
		this.document.add(list);

		passToSuccessor();
	}

}
