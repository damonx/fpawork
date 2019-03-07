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

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fisherpaykel.model.experian.AddressSelectionRequest;
import com.fisherpaykel.model.experian.SelectedAddress;
import com.fisherpaykel.service.AddressSelectionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "FPA Address Selection", description = "FPA Address Selection endpoints")
@RestController
public class AddressSelectionController {

	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	AddressSelectionService addressSelectionService;

	@ApiOperation(value = "Select an address specified by the address identity string", response = List.class)
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	public SelectedAddress selectedAddress(@RequestParam(name = "data", required = true) final String data) throws IOException {
		final AddressSelectionRequest request = this.mapper.readValue(data, AddressSelectionRequest.class);
		final SelectedAddress selectedAddress = this.addressSelectionService.selectAddress(request);
		return selectedAddress;
	}
}
