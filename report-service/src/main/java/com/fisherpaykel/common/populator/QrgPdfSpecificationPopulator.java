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

import org.springframework.stereotype.Component;

import com.fisherpaykel.common.QrgPdfGenerator;
import com.fisherpaykel.common.util.FPAConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

@Component
@SuppressWarnings("deprecation")
public class QrgPdfSpecificationPopulator extends QrgPdfGenerator {

	public QrgPdfSpecificationPopulator() {

	}

	/**
	 * This method adds Specifications to the QRG.
	 *
	 * @return
	 * @throws IOException
	 */
	@Override
	public void generateQrgPart() throws IOException {

		this.logger.debug("in QrgPdfSpecificationPopulator");
		if (this.product.getSpecifications() != null && !this.product.getSpecifications().isEmpty()) {
			final Table specificationsHeaderTable = new Table(1);
			populateTableWithText(specificationsHeaderTable, 100f, null, VerticalAlignment.MIDDLE, TextAlignment.LEFT, 12f,
					new Text(FPAConstants.SPECIFICATIONS).setFont(this.fontUtil.getGothamBoldFont())
							.setFontColor(Color.BLACK).setFontSize(8.6f));
			this.document.add(specificationsHeaderTable);
			

			final Table specificationsTable = new Table(1);
			this.product.getSpecifications().stream().forEach(specification -> {

				// Add line separator
				final SolidLine solidline = new SolidLine(0.5f);
				solidline.setColor(FPAConstants.GRAY);
				final LineSeparator ls = new LineSeparator(solidline).setWidthPercent(100f);
				final Cell lineCell = new Cell().add(ls);
				lineCell.setBorder(Border.NO_BORDER);
				lineCell.setPaddingTop(8f);
				lineCell.setPaddingBottom(8f);
				specificationsTable.addCell(lineCell);

				populateTableWithText(specificationsTable, 100f, null, VerticalAlignment.MIDDLE, TextAlignment.LEFT, 9f,
						new Text(specification.getGroupDisplayName()).setFont(this.fontUtil.getGothamMediumFont())
								.setFontColor(Color.BLACK).setFontSize(7.5f));

				final Table specificationEntriesTable = new Table(2);

				specification.getSpecificationEntries().stream().forEach(specificationEntry -> {

					populateTableWithText(specificationEntriesTable, 50f, 12f, VerticalAlignment.MIDDLE, TextAlignment.LEFT, 9f,
							new Text(specificationEntry.getAttributeName()).setFont(this.fontUtil.getGothamBookFont())
									.setFontColor(Color.BLACK).setFontSize(7f));

					if (specificationEntry.getValue() instanceof Boolean) {
						populateTableWithText(specificationEntriesTable, 50f, 12f, VerticalAlignment.MIDDLE, TextAlignment.RIGHT, 9f,
								new Text(((Boolean) specificationEntry.getValue()) ? FPAConstants.BULL : FPAConstants.DEGREE)
										.setFont(this.fontUtil.getGothamMediumFont()).setFontColor(Color.BLACK)
										.setFontSize(7f));
					} else {
						populateTableWithText(specificationEntriesTable, 50f, 12f, VerticalAlignment.MIDDLE, TextAlignment.RIGHT, 9f,
								new Text(specificationEntry.getValue().toString()).setFont(this.fontUtil.getGothamMediumFont())
										.setFontColor(Color.BLACK).setFontSize(7f));
					}

				});

				final Cell nestedTableCell = new Cell().add(specificationEntriesTable);
				nestedTableCell.setBorder(Border.NO_BORDER);
				specificationsTable.addCell(nestedTableCell);

			});

			this.document.add(specificationsTable);
		}

		passToSuccessor();

	}

}
