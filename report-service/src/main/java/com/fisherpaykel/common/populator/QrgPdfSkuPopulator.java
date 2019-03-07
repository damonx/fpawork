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
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

@Component
@SuppressWarnings("deprecation")
public class QrgPdfSkuPopulator extends QrgPdfGenerator {

	public QrgPdfSkuPopulator() {

	}

	/**
	 * This method adds SKU to the QRG.
	 *
	 * @return
	 * @throws IOException
	 */
	@Override
	public void generateQrgPart() throws IOException {

		this.logger.debug("in Method with SKU");
		
		// Add line separator
		addLineSeparator(8f);
		
		final Table skuTable = new Table(2);
		populateTableWithText(skuTable, 50f, 15f, VerticalAlignment.BOTTOM, TextAlignment.LEFT, 12f,
				new Text(FPAConstants.SKU).setFont(this.fontUtil.getGothamBookFont()).setFontColor(Color.BLACK).setFontSize(7f));
		populateTableWithText(skuTable, 50f, 15f, VerticalAlignment.BOTTOM, TextAlignment.RIGHT, 12f,
				new Text(this.product.getSku()).setFont(this.fontUtil.getGothamBookFont()).setFontColor(Color.BLACK)
						.setFontSize(7f));
		this.document.add(skuTable);

		passToSuccessor();

	}

}
