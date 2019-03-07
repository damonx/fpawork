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
package com.fisherpaykel.common.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * @author damonx
 *
 *         Utility class manipulates HTTP headers and payloads.
 */
public class FPHttpUtils {
	private FPHttpUtils() {
		throw new Error("Don't instantiate me!");
	}

	public static <T> ResponseEntity<T> generatePayload(final T stream, final String headerContent, final MediaType mediaType) {
		return ResponseEntity.ok().headers(createHeaders(headerContent)).contentType(mediaType)
				.body(stream);
	}

	/**
	 * This method is used to create headers.
	 *
	 * @param product
	 * @return
	 */
	private static HttpHeaders createHeaders(@SuppressWarnings("unused") final String modelNumber) {
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		// headers.add("Content-Disposition", "filename=" + modelNumber + ".pdf");
		return headers;
	}
}
