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
package com.fisherpaykel.model.qrg;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author wilsonas
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpecificationEntry {

	@JsonProperty("AttrName")
	private String attributeName;

	@JsonProperty("Value")
	private Object value;

	/**
	 * Default Constructor.
	 */
	public SpecificationEntry() {
	}

	/**
	 * Parameterized Constructor.
	 *
	 * @param attribute
	 * @param value
	 */
	public SpecificationEntry(final String attributeName, final Object value) {
		this.attributeName = attributeName;
		this.value = value;
	}

	/**
	 * @return the attributeName
	 */
	public String getAttributeName() {
		return this.attributeName;
	}

	/**
	 * @param attributeName the attributeName to set
	 */
	public void setAttributeName(final String attributeName) {
		this.attributeName = attributeName;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return this.value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(final Object value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(final Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
