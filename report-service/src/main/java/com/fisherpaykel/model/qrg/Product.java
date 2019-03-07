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

import java.util.ArrayList;
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
public class Product {

	@JsonProperty("modelNo")
	private String modelNumber;

	@JsonProperty("sku")
	private String sku;

	@JsonProperty("name")
	private String name;

	@JsonProperty("shortDescription")
	private String shortDescription;

	@JsonProperty("longDescription")
	private String longDescription;

	@JsonProperty("imageURL")
	private String imageUrl;

	@JsonProperty("productURL")
	private String productUrl;

	@JsonProperty("dimensionImageURL")
	private String dimensionImageUrl;

	@JsonProperty("variationAttributes")
	private List<VariationAttribute> variationAttributes;

	@JsonProperty("dimensions")
	private List<ProductDimension> dimensions;

	@JsonProperty("featuresBenefits")
	private List<ProductFeature> featuresBenefits;

	@JsonProperty("specifications")
	private List<Specification> specifications;

	@JsonProperty("assets")
	private List<Asset> assets = new ArrayList<>();

	@JsonProperty("style")
	private String style;

	@JsonProperty("series")
	private String series;

	@JsonProperty("unit")
	private String unit;

	/**
	 * Default Constructor.
	 */
	public Product() {
	}

	/**
	 * Parameterized Constructor.
	 *
	 * @param modelNumber
	 * @param sku
	 * @param name
	 * @param shortDescription
	 * @param imageUrl
	 * @param productUrl
	 * @param variationAttributes
	 * @param dimensions
	 * @param featuresBenefits
	 * @param specifications
	 * @param style
	 * @param series
	 */
	public Product(final String modelNumber, final String sku, final String name, final String shortDescription, final String imageUrl,
			final String productUrl, final List<VariationAttribute> variationAttributes, final List<ProductDimension> dimensions,
			final List<ProductFeature> featuresBenefits, final List<Specification> specifications, final String style,
			final String series) {
		this.modelNumber = modelNumber;
		this.sku = sku;
		this.name = name;
		this.shortDescription = shortDescription;
		this.imageUrl = imageUrl;
		this.productUrl = productUrl;
		this.variationAttributes = variationAttributes;
		this.dimensions = dimensions;
		this.featuresBenefits = featuresBenefits;
		this.specifications = specifications;
		this.style = style;
		this.series = series;
	}

	/**
	 * @return the modelNumber
	 */
	public String getModelNumber() {
		return this.modelNumber;
	}

	/**
	 * @param modelNumber the modelNumber to set
	 */
	public void setModelNumber(final String modelNumber) {
		this.modelNumber = modelNumber;
	}

	/**
	 * @return the sku
	 */
	public String getSku() {
		return this.sku;
	}

	/**
	 * @param sku the sku to set
	 */
	public void setSku(final String sku) {
		this.sku = sku;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the shortDescription
	 */
	public String getShortDescription() {
		return this.shortDescription;
	}

	/**
	 * @param shortDescription the shortDescription to set
	 */
	public void setShortDescription(final String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/**
	 * @return the longDescription
	 */
	public String getLongDescription() {
		return this.longDescription;
	}

	/**
	 * @param longDescription the longDescription to set
	 */
	public void setLongDescription(final String longDescription) {
		this.longDescription = longDescription;
	}

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return this.imageUrl;
	}

	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(final String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * @return the productUrl
	 */
	public String getProductUrl() {
		return this.productUrl;
	}

	/**
	 * @param productUrl the productUrl to set
	 */
	public void setProductUrl(final String productUrl) {
		this.productUrl = productUrl;
	}

	/**
	 * @return the dimensionImageUrl
	 */
	public String getDimensionImageUrl() {
		return this.dimensionImageUrl;
	}

	/**
	 * @param dimensionImageUrl the dimensionImageUrl to set
	 */
	public void setDimensionImageUrl(final String dimensionImageUrl) {
		this.dimensionImageUrl = dimensionImageUrl;
	}

	/**
	 * @return the variationAttributes
	 */
	public List<VariationAttribute> getVariationAttributes() {
		return this.variationAttributes;
	}

	/**
	 * @param variationAttributes the variationAttributes to set
	 */
	public void setVariationAttributes(final List<VariationAttribute> variationAttributes) {
		this.variationAttributes = variationAttributes;
	}

	/**
	 * @return the dimensions
	 */
	public List<ProductDimension> getDimensions() {
		return this.dimensions;
	}

	/**
	 * @param dimensions the dimensions to set
	 */
	public void setDimensions(final List<ProductDimension> dimensions) {
		this.dimensions = dimensions;
	}

	/**
	 * @return the featuresBenefits
	 */
	public List<ProductFeature> getFeaturesBenefits() {
		return this.featuresBenefits;
	}

	/**
	 * @param featuresBenefits the featuresBenefits to set
	 */
	public void setFeaturesBenefits(final List<ProductFeature> featuresBenefits) {
		this.featuresBenefits = featuresBenefits;
	}

	/**
	 * @return the specifications
	 */
	public List<Specification> getSpecifications() {
		return this.specifications;
	}

	/**
	 * @param specifications the specifications to set
	 */
	public void setSpecifications(final List<Specification> specifications) {
		this.specifications = specifications;
	}

	/**
	 * @return the assets
	 */
	public List<Asset> getAssets() {
		return this.assets;
	}

	/**
	 * @param assets the specifications to set
	 */
	public void setAssets(final List<Asset> assets) {
		this.assets = assets;
	}

	/**
	 * @return the style
	 */
	public String getStyle() {
		return this.style;
	}

	/**
	 * @param style the style to set
	 */
	public void setStyle(final String style) {
		this.style = style;
	}

	/**
	 * @return the series
	 */
	public String getSeries() {
		return this.series;
	}

	/**
	 * @param series the series to set
	 */
	public void setSeries(final String series) {
		this.series = series;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return this.unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(final String unit) {
		this.unit = unit;
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