/**
 *
 */
package com.fisherpaykel.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.fisherpaykel.common.util.FPAConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

/**
 * @author wilsonas
 *
 */
public class PdfHeaderHandler implements IEventHandler {


	private String productModelNumber;

	private String productTitle;

	private PdfFont gothamBoldFont;

	private PdfFont gothamMediumFont;
	
	private PdfFont gothamBookFont;

	private final Logger logger = Logger.getLogger(getClass());

	@Override
	public void handleEvent(final Event event) {

		final PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
		final PdfPage page = docEvent.getPage();
		final Rectangle pageSize = page.getPageSize();
		final PdfDocument pdfDoc = ((PdfDocumentEvent) event).getDocument();
		final PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
		final Canvas canvas = new Canvas(pdfCanvas, pdfDoc, pageSize);

		try {
			constructHeader(page, pageSize, pdfDoc, canvas);
		} catch (final Exception e) {
			this.logger.error(String.format("Error while rendering Header: %s", e.getMessage()));
		} finally {
			canvas.close();
		}
	}

	/**
	 * This method will add header to the document.
	 * @param page
	 * @param pageSize
	 * @param pdfDoc
	 * @param canvas
	 */
	private void constructHeader(final PdfPage page, final Rectangle pageSize, final PdfDocument pdfDoc, final Canvas canvas) {
		final Text headerText = new Text(String.join(FPAConstants.EMPTY_STRING, FPAConstants.HEADER_QUICK_REFERENCE_GUIDE, FPAConstants.SPACE, FPAConstants.GREATER_THAN, FPAConstants.SPACE)).
				setFont(this.gothamBoldFont).setFontSize(7.5f).setFontColor(Color.BLACK);
		final Text headerProductModelNumber = new Text(this.productModelNumber).setFont(this.gothamBookFont)
				.setFontSize(7.5f).setFontColor(FPAConstants.GRAY);		
		final Paragraph headerParagraph = new Paragraph().add(headerText).add(headerProductModelNumber);

		canvas.showTextAligned(headerParagraph, 36, pageSize.getTop() - 30, TextAlignment.LEFT,
				VerticalAlignment.MIDDLE);

		final Text dateText = new Text(String.join(FPAConstants.EMPTY_STRING, FPAConstants.HEADER_DATE,FPAConstants.COLON,FPAConstants.SPACE) + new SimpleDateFormat(FPAConstants.HEADER_DATE_FORMAT).format(new Date()))
				.setFont(this.gothamBookFont).setFontSize(7.5f).setFontColor(FPAConstants.GRAY);
		final Text pageNumberText = new Text(String.join(FPAConstants.EMPTY_STRING, FPAConstants.SPACE,FPAConstants.GREATER_THAN,FPAConstants.SPACE,FPAConstants.SPACE, Integer.toString(pdfDoc.getPageNumber(page))))
				.setFont(this.gothamBookFont).setFontSize(7.5f).setFontColor(FPAConstants.GRAY);
		final Paragraph pageNumberParagraph = new Paragraph().add(dateText).add(pageNumberText);

		canvas.showTextAligned(pageNumberParagraph, pageSize.getWidth() - 36, pageSize.getTop() - 30,
				TextAlignment.RIGHT, VerticalAlignment.MIDDLE);
		
	}

	/**
	 * @return the productModelNumber
	 */
	public String getProductModelNumber() {
		return this.productModelNumber;
	}

	/**
	 * @param productModelNumber the productModelNumber to set
	 */
	public void setProductModelNumber(final String productModelNumber) {
		this.productModelNumber = productModelNumber;
	}

	/**
	 * @return the productTitle
	 */
	public String getProductTitle() {
		return this.productTitle;
	}

	/**
	 * @param productTitle the productTitle to set
	 */
	public void setProductTitle(final String productTitle) {
		this.productTitle = productTitle;
	}

	/**
	 * @return the gothamBoldFont
	 */
	public PdfFont getGothamBoldFont() {
		return this.gothamBoldFont;
	}

	/**
	 * @param gothamBoldFont the gothamBoldFont to set
	 */
	public void setGothamBoldFont(final PdfFont gothamBoldFont) {
		this.gothamBoldFont = gothamBoldFont;
	}

	/**
	 * @return the gothamMediumFont
	 */
	public PdfFont getGothamMediumFont() {
		return this.gothamMediumFont;
	}

	/**
	 * @param gothamMediumFont the gothamMediumFont to set
	 */
	public void setGothamMediumFont(final PdfFont gothamMediumFont) {
		this.gothamMediumFont = gothamMediumFont;
	}

	/**
	 * @return the gothamBookFont
	 */
	public PdfFont getGothamBookFont() {
		return gothamBookFont;
	}

	/**
	 * @param gothamBookFont the gothamBookFont to set
	 */
	public void setGothamBookFont(PdfFont gothamBookFont) {
		this.gothamBookFont = gothamBookFont;
	}

}
