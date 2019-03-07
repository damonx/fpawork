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
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

@Component
@SuppressWarnings("deprecation")
public class QrgPdf24x7StaticInfoPopulator extends QrgPdfGenerator {

	public QrgPdf24x7StaticInfoPopulator() {

	}

	/**
	 * This method adds 24 x 7 support related static information to the QRG.
	 *
	 * @return
	 * @throws IOException
	 */
	@Override
	public void generateQrgPart() throws IOException {

		this.logger.debug("in QrgPdf24x7StaticInfoPopulator");
		
		// Add Vertical Space
		populateVerticalSpace(45f);
		
		final Table top24x7Table = new Table(2);
		top24x7Table.setWidthPercent(100f);
		top24x7Table.setHeight(50f);
		top24x7Table.setBorder(Border.NO_BORDER);

		final Image top24x7Image = new Image(ImageDataFactory.create(
				IOUtils.toByteArray(this.getClass().getResourceAsStream(this.env.getProperty(FPAConstants.IMAGE_PATH_24x7)))));
		final Cell top24x7ImageCell = new Cell(3, 1).setWidthPercent(20f).setBorder(Border.NO_BORDER)
				.add(top24x7Image.scaleToFit(40f, 40f).setHorizontalAlignment(HorizontalAlignment.CENTER));
		top24x7Table.addCell(top24x7ImageCell);

		populateTableWithText(top24x7Table, 80f, 10f, VerticalAlignment.BOTTOM, TextAlignment.LEFT, 10f,
				new Text(FPAConstants.STATIC_24x7_TEXT_1).setFont(this.fontUtil.getGothamBoldFont()).setFontColor(Color.BLACK)
						.setFontSize(7f));
		populateTableWithText(top24x7Table, 80f, 10f, VerticalAlignment.MIDDLE, TextAlignment.LEFT, 10f,
				new Text(FPAConstants.STATIC_24x7_TEXT_2).setFont(this.fontUtil.getGothamMediumFont())
						.setFontColor(Color.BLACK).setFontSize(7f));
		populateTableWithParagraph(top24x7Table, 80f, 40f, VerticalAlignment.TOP, TextAlignment.LEFT,
				new Paragraph().add(new Text(FPAConstants.T).setFont(this.fontUtil.getGothamBoldFont()).setFontSize(7f))
								.add(new Text(FPAConstants.STATIC_24x7_TEXT_PHONE_NUMBER).setFont(this.fontUtil.getGothamBookFont()).setFontSize(7f))
								.add(new Text(FPAConstants.W).setFont(this.fontUtil.getGothamBoldFont()).setFontSize(7f))
								.add(new Text(FPAConstants.STATIC_24x7_TEXT_WEBSITE).setFont(this.fontUtil.getGothamBookFont()).setFontSize(7f)));

		this.document.add(top24x7Table);
		passToSuccessor();

	}

}
