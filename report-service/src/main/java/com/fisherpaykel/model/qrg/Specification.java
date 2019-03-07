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

import java.util.List;

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
public class Specification {

	@JsonProperty("GroupId")
	private String groupId;

	@JsonProperty("GroupDisplayName")
	private String groupDisplayName;

	@JsonProperty("SpecificationEntries")
	private List<SpecificationEntry> specificationEntries;

	/**
	 * Default Constructor.
	 */
	public Specification() {
	}

	/**
	 * Parameterized Constructor.
	 *
	 * @param groupId
	 * @param groupDisplayName
	 * @param specificationEntries
	 */
	public Specification(final String groupId, final String groupDisplayName, final List<SpecificationEntry> specificationEntries) {
		this.groupId = groupId;
		this.groupDisplayName = groupDisplayName;
		this.specificationEntries = specificationEntries;
	}

	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return this.groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(final String groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the groupDisplayName
	 */
	public String getGroupDisplayName() {
		return this.groupDisplayName;
	}

	/**
	 * @param groupDisplayName the groupDisplayName to set
	 */
	public void setGroupDisplayName(final String groupDisplayName) {
		this.groupDisplayName = groupDisplayName;
	}

	/**
	 * @return the specificationEntries
	 */
	public List<SpecificationEntry> getSpecificationEntries() {
		return this.specificationEntries;
	}

	/**
	 * @param specificationEntries the specificationEntries to set
	 */
	public void setSpecificationEntries(final List<SpecificationEntry> specificationEntries) {
		this.specificationEntries = specificationEntries;
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
