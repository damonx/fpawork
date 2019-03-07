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
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.fisherpaykel.common.util.FPAConstants;
import com.fisherpaykel.common.util.FontUtil;
import com.fisherpaykel.model.qrg.Product;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.ColumnDocumentRenderer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

/**
 * The {@code QrgPdfGenerator} abstract class
 *
 * @author Damon Xu (damon.xu@fisherpaykel.com)
 *
 */
@SuppressWarnings("deprecation")
public abstract class QrgPdfGenerator {

	@Autowired
	protected Environment env;

	@Autowired
	protected FontUtil fontUtil;

	protected QrgPdfGenerator successor;

	protected Product product;

	protected Document document;

	/**
	 * Abstract method generates a specific part of the QRG pdf file based on the actual implementations. *
	 */
	public abstract void generateQrgPart() throws IOException;

	public void setProduct(final Product product) {
		this.product = product;
	}

	public void setDocument(final Document document) {
		this.document = document;
	}

	protected final Logger logger = Logger.getLogger(QrgPdfGenerator.class);

	/**
	 * This method creates the PDF Document needed to add the Product Information.
	 *
	 * @param productToAdd
	 * @param dest
	 * @return
	 * @throws IOException
	 */
	protected void createPdfLayout(final Product productToAdd, final String dest) throws IOException {

		this.logger.debug("in Method createPdfLayout");
		this.product = productToAdd;
		this.fontUtil.loadFonts();

		// This method will create a header handler
		final PdfHeaderHandler headerHandler = new PdfHeaderHandler();
		headerHandler.setProductModelNumber(productToAdd.getModelNumber());
		headerHandler.setProductTitle(productToAdd.getName());
		headerHandler.setGothamBoldFont(this.fontUtil.getGothamBoldFont());
		headerHandler.setGothamBookFont(this.fontUtil.getGothamBookFont());
		headerHandler.setGothamMediumFont(this.fontUtil.getGothamMediumFont());

		// This method will create a footer handler.
		final PdfFooterHandler footerHandler = new PdfFooterHandler();
		footerHandler.setFooterImagePath(this.env.getProperty(FPAConstants.IMAGE_PATH_FOOTER));

		// This method will add a header to the document.
		final PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
		pdf.setTagged();
		pdf.addEventHandler(PdfDocumentEvent.START_PAGE, headerHandler);

		// This method will define the page layout.
		createPageLayout(pdf);

		// This method will add a footer to the document.
		pdf.addEventHandler(PdfDocumentEvent.END_PAGE, footerHandler);
	}

	/**
	 * This method is used to generate and set the page layout of the pdf document.
	 *
	 * @param pdf
	 */
	private void createPageLayout(final PdfDocument pdf) {
		final PageSize ps = PageSize.A4.rotate();
		// Set column parameters
		final float x_offSet = 36f;
		final float y_offSet = 58f;
		final float columnWidth = 232f;
		final float columnHeight = 490f;

		// Define column areas
		final Rectangle[] columns = {
				new Rectangle(x_offSet, y_offSet, columnWidth, columnHeight),
				new Rectangle(x_offSet * 2 + columnWidth, y_offSet, columnWidth, columnHeight),
				new Rectangle(x_offSet * 3 + columnWidth * 2, y_offSet, columnWidth, columnHeight) };

		this.document = new Document(pdf, ps);
		this.document.setRenderer(new ColumnDocumentRenderer(this.document, columns));
	}

	/**
	 * This method sets the next successor in the chain.
	 *
	 * @param successor
	 */
	public QrgPdfGenerator setSuccessor(final QrgPdfGenerator successor) {
		this.successor = successor;
		Optional.ofNullable(this.successor).ifPresent(s -> {
			this.successor.setProduct(this.product);
			this.successor.setDocument(this.document);
		});
		return this.successor;
	}

	/**
	 * This method is used to populate text in a data table.
	 *
	 * @param table
	 * @param widthPercent
	 * @param height
	 * @param verticalAlignment
	 * @param data
	 */
	protected void populateTableWithText(final Table table, final Float widthPercent, final Float height,
			final VerticalAlignment verticalAlignment, final TextAlignment textAlignment, final Float leading, final Text... data) {
		this.logger.debug("in Method populateTableWithText");

		final Cell cell = new Cell().setWidthPercent(widthPercent).setVerticalAlignment(verticalAlignment).setTextAlignment(textAlignment);

		Stream.of(data).forEach(text -> {
			final Paragraph paragraph = new Paragraph().setWidthPercent(100f).setFixedLeading(leading);
			paragraph.add(text);
			cell.add(paragraph);
		});

		if (height != null) {
			cell.setHeight(height);
		}
		cell.setBorder(Border.NO_BORDER);
		table.addCell(cell);
	}

	/**
	 * This method is used to populate paragraph in a data table.
	 *
	 * @param table
	 * @param widthPercent
	 * @param height
	 * @param verticalAlignment
	 * @param data
	 */
	protected void populateTableWithParagraph(final Table table, final Float widthPercent, final Float height,
			final VerticalAlignment verticalAlignment, final TextAlignment textAlignment, final Paragraph paragraph) {
		this.logger.debug("in Method populateTableWithText");

		final Cell cell = new Cell().setWidthPercent(widthPercent).setVerticalAlignment(verticalAlignment).setTextAlignment(textAlignment);

		cell.add(paragraph);

		if (height != null) {
			cell.setHeight(height);
		}
		cell.setBorder(Border.NO_BORDER);
		table.addCell(cell);
	}

	/**
	 * This method is used to populate data in the document.
	 *
	 * @param table
	 * @param data
	 * @param font
	 * @param fontSize
	 * @param fontColor
	 */
	protected void populateDocumentWithText(final Document documentToChange, final String data, final PdfFont font, final float fontSize,
			final Color fontColor, final TextAlignment textAlignment, final float leading) {
		this.logger.debug("in Method populateDocumentWithText");
		final Text text = new Text(data);
		final Paragraph paragraph = new Paragraph().setFont(font).setFontColor(fontColor).setFixedLeading(leading).setFontSize(fontSize)
				.add(text);
		documentToChange.add(paragraph.setTextAlignment(textAlignment));
	}

	/**
	 * This method helps to add blank lines to the document
	 *
	 * @param times
	 */
	protected void populateBlanks(final int times) {
		IntStream.rangeClosed(1, times).forEach(t -> {
			populateDocumentWithText(this.document, "  ", this.fontUtil.getGothamMediumFont(), 7.5f, FPAConstants.GRAY,
					TextAlignment.CENTER, 10f);
		});
	}

	/**
	 * This method helps to add height controlled blank spaces to the document
	 *
	 * @param height
	 */
	protected void populateVerticalSpace(final Float height) {
		this.logger.debug("in Method populateVerticalSpaces");
		final Table blankTable = new Table(1);
		final Cell cell = new Cell().add(new Paragraph("").setFontSize(1f)).setWidthPercent(100f);
		cell.setHeight(height);
		cell.setBorder(Border.NO_BORDER);
		blankTable.addCell(cell);
		this.document.add(blankTable);
	}

	/**
	 * This method is used for adding line separators with appropriate height padded on top and below it.
	 *
	 * @param height
	 */
	protected void addLineSeparator(final Float height) {
		final Table table = new Table(1);
		final SolidLine solidline = new SolidLine(0.5f);
		solidline.setColor(FPAConstants.GRAY);
		final LineSeparator ls = new LineSeparator(solidline).setWidthPercent(100f);
		final Cell lineCell = new Cell().add(ls);
		lineCell.setBorder(Border.NO_BORDER);
		lineCell.setPaddingTop(height);
		lineCell.setPaddingBottom(height);
		table.addCell(lineCell);
		this.document.add(table);
	}

	/**
	 * This method is used for passing responsibility from one job to its successor.
	 */
	protected void passToSuccessor() {
		Optional.ofNullable(this.successor).ifPresent(s -> {
			try {
				s.generateQrgPart();
			} catch (final IOException e) {
				this.logger.error("Error when generating the QRG PDF");
			}
		});
	}

	/**
	 * This method builds the PDF document and commits the stream on the file system.
	 *
	 * @return
	 */
	protected void finalise() {
		this.logger.debug("in Method build");
		this.document.close();
		this.fontUtil.unloadFonts();
	}
}
