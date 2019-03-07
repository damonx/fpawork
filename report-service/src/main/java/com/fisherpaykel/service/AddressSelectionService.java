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
package com.fisherpaykel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fisherpaykel.cache.AddressFinderCache;
import com.fisherpaykel.generated.experian.ws.Address;
import com.fisherpaykel.model.experian.AddressSelectionRequest;
import com.fisherpaykel.model.experian.AddressSelectionResponse;
import com.fisherpaykel.model.experian.AddressSource;
import com.fisherpaykel.model.experian.RESTClient;
import com.fisherpaykel.model.experian.SOAPClient;
import com.fisherpaykel.model.experian.SelectedAddress;

@Component
public class AddressSelectionService {

	@Autowired
	private RESTClient restClient;
	@Autowired
	private SOAPClient soapClient;

	@Autowired
	private AddressFinderCache cacheService;

	/**
	 * Selects an address returned from EXPERIAN
	 *
	 * @param request - AddressSelectionRequest
	 * @return SelectedAddress
	 */
	public SelectedAddress selectAddress(final AddressSelectionRequest request) {

		final String addressIdentifier = request.getIdentifier();
		SelectedAddress selectedAddress = this.cacheService.getCache().get(addressIdentifier);
		if (selectedAddress == null) {
			if (request.getSource() == AddressSource.SOAP) {
				final Address address = this.soapClient.doGetAddress(addressIdentifier);
				if (address != null) {
					selectedAddress = new SelectedAddress(address);
					this.cacheService.getCache().put(addressIdentifier, selectedAddress);
					return selectedAddress;
				}
			} else if (request.getSource() == AddressSource.REST) {
				final AddressSelectionResponse addressSelectionResponse = this.restClient.selectAddress(addressIdentifier);
				if (addressSelectionResponse != null) {
					selectedAddress = new SelectedAddress(addressSelectionResponse, request);
					this.cacheService.getCache().put(addressIdentifier, selectedAddress);
					return selectedAddress;
				}

			}
		}
		return selectedAddress;
	}

	public void setSoapClient(final SOAPClient soapClient) {
		this.soapClient = soapClient;
	}

	public void setRestClient(final RESTClient restClient) {
		this.restClient = restClient;
	}
}
