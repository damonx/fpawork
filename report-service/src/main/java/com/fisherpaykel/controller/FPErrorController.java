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
package com.fisherpaykel.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Error controller for all the errors happened in controller.
 * 
 * @author damonx
 *
 */

@RestController
public class FPErrorController extends AbstractErrorController {

	private static final String PATH = "/error";

	@Autowired
	public FPErrorController(final ErrorAttributes errorAttributes) {
		super(errorAttributes);
	}

	@RequestMapping(value = PATH)
	public ResponseEntity<Map<String, Object>> error(final HttpServletRequest request) {
		final Map<String, Object> body = getErrorAttributes(request, false);
		final HttpStatus status = getStatus(request);
		return new ResponseEntity<Map<String, Object>>(body, status);
	}

	@Override
	public String getErrorPath() {
		return PATH;
	}

}
