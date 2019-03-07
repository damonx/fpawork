package com.fisherpaykel.common;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.HorizontalAlignment;

/**
 * @author wilsonas
 *
 */
public class PdfFooterHandler implements IEventHandler {

	private String footerImagePath;

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
			constructFooter(canvas);
		} catch (final Exception e) {
			this.logger.error(String.format("Error while rendering Footer: %s", e.getMessage()));
		} finally {
			canvas.close();
		}
	}

	/**
	 * This method will add a footer to the document.
	 *
	 * @param pageSize
	 * @param canvas
	 * @throws IOException
	 */
	private void constructFooter(final Canvas canvas) throws IOException {
		// Product Image
		final Image image = new Image(
				ImageDataFactory.create(IOUtils.toByteArray(this.getClass().getResourceAsStream(this.footerImagePath))));
		canvas.add(new Paragraph().setPaddingTop(530f));
		canvas.add(image.scaleToFit(768f, 42f).setHorizontalAlignment(HorizontalAlignment.CENTER));
	}

	/**
	 * @return the footerImagePath
	 */
	public String getFooterImagePath() {
		return this.footerImagePath;
	}

	/**
	 * @param footerImagePath the footerImagePath to set
	 */
	public void setFooterImagePath(final String footerImagePath) {
		this.footerImagePath = footerImagePath;
	}
}
