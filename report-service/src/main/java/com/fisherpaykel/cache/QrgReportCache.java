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
package com.fisherpaykel.cache;

import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;
import org.apache.commons.jcs.access.exception.CacheException;
import org.apache.commons.jcs.engine.behavior.IElementAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fisherpaykel.common.util.FPAConstants;
import com.fisherpaykel.model.qrg.Product;

@Component
public class QrgReportCache {
	private static Logger log = LoggerFactory.getLogger(QrgReportCache.class);

	private CacheAccess<String, Product> cache;

	public QrgReportCache() {
		try {
			this.cache = JCS.getInstance("qrgReportCache");
			final IElementAttributes attributes = this.cache.getDefaultElementAttributes();
			// MAXLIFE is 5 days.
			attributes.setMaxLife(FPAConstants.FIVE_DAYS_IN_SECONDS);
		} catch (final CacheException e) {
			log.error(String.format("Problem initializing cache: %s", e.getMessage()));
		}
	}

	public CacheAccess<String, Product> getCache() {
		return this.cache;
	}
}
