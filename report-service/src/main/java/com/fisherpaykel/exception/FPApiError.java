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
package com.fisherpaykel.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FPApiError {
	private HttpStatus status;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private final LocalDateTime timestamp;
	private String message;
	private String debugMessage;
	private List<FPApiSubError> subErrors;

	private FPApiError() {
		this.timestamp = LocalDateTime.now();
	}

	public FPApiError(final HttpStatus status) {
		this();
		this.status = status;
	}

	public FPApiError(final HttpStatus status, final Throwable ex) {
		this();
		this.status = status;
		this.message = "Unexpected error";
		this.debugMessage = ex.getLocalizedMessage();
	}

	public FPApiError(final HttpStatus status, final String message, final Throwable ex) {
		this();
		this.status = status;
		this.message = message;
		this.debugMessage = ex.getLocalizedMessage();
	}

	public HttpStatus getStatus() {
		return this.status;
	}

	public void setStatus(final HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public String getDebugMessage() {
		return this.debugMessage;
	}

	public void setDebugMessage(final String debugMessage) {
		this.debugMessage = debugMessage;
	}

	public List<FPApiSubError> getSubErrors() {
		return this.subErrors;
	}

	public void setSubErrors(final List<FPApiSubError> subErrors) {
		this.subErrors = subErrors;
	}

	public LocalDateTime getTimestamp() {
		return this.timestamp;
	}

}
