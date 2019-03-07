/**
 *
 */
package com.fisherpaykel.common.util;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;

/**
 * @author wilsonas
 *
 */
@Component
public class FontUtil {

	private final String gothamUltraPath = "/static/fonts/Gotham-Ultra.ttf";
	private final String gothamBoldPath = "/static/fonts/Gotham-Bold.ttf";
	private final String gothamMediumPath = "/static/fonts/Gotham-Medium.ttf";
	private final String gothamBookPath = "/static/fonts/Gotham-Book.ttf";
	private final String gothamLightPath = "/static/fonts/Gotham-Light.ttf";

	private PdfFont gothamUltraFont, gothamBoldFont, gothamMediumFont, gothamBookFont, gothamLightFont;

	@PostConstruct
	public void loadFonts() throws IOException {
		this.gothamUltraFont = PdfFontFactory.createFont(
				IOUtils.toByteArray(getClass().getResourceAsStream(this.gothamUltraPath)), PdfEncodings.IDENTITY_H,
				true);
		this.gothamBoldFont = PdfFontFactory.createFont(
				IOUtils.toByteArray(getClass().getResourceAsStream(this.gothamBoldPath)), PdfEncodings.IDENTITY_H, true);
		this.gothamMediumFont = PdfFontFactory.createFont(
				IOUtils.toByteArray(getClass().getResourceAsStream(this.gothamMediumPath)), PdfEncodings.IDENTITY_H,
				true);
		this.gothamBookFont = PdfFontFactory.createFont(
				IOUtils.toByteArray(getClass().getResourceAsStream(this.gothamBookPath)), PdfEncodings.IDENTITY_H, true);
		this.gothamLightFont = PdfFontFactory.createFont(
				IOUtils.toByteArray(getClass().getResourceAsStream(this.gothamLightPath)), PdfEncodings.IDENTITY_H, true);
	}

	public void unloadFonts() {
		this.gothamUltraFont = null;
		this.gothamBoldFont = null;
		this.gothamMediumFont = null;
		this.gothamBookFont = null;
		this.gothamLightFont = null;
	}

	public PdfFont getGothamUltraFont() {
		return this.gothamUltraFont;
	}

	public PdfFont getGothamBoldFont() {
		return this.gothamBoldFont;
	}

	public PdfFont getGothamMediumFont() {
		return this.gothamMediumFont;
	}

	public PdfFont getGothamBookFont() {
		return this.gothamBookFont;
	}

	public PdfFont getGothamLightFont() {
		return this.gothamLightFont;
	}
}
