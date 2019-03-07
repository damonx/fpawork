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
import com.itextpdf.layout.property.TextAlignment;

@Component
public class QrgPdfDisclaimerPopulator extends QrgPdfGenerator {

	/**
	 * Default Constructor.
	 */
	public QrgPdfDisclaimerPopulator() {

	}

	/**
	 * This method adds Disclaimer to the QRG.
	 *
	 * @return
	 * @throws IOException
	 */
	@Override
	public void generateQrgPart() throws IOException {

		this.logger.debug("in Method withDisclaimer");

		// Add line separator
		addLineSeparator(8f);

		populateDocumentWithText(this.document, this.env.getProperty(FPAConstants.QRG_DISCLAIMER), this.fontUtil.getGothamBookFont(),
				5.5f, Color.BLACK, TextAlignment.LEFT, 10f);

		passToSuccessor();

	}

}
