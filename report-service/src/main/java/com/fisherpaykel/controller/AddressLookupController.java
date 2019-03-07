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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fisherpaykel.model.experian.AddressResult;
import com.fisherpaykel.service.AddressLookupService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "FPA Address Lookup", description = "FPA Address Lookup endpoints")
public class AddressLookupController {

	@Autowired
	AddressLookupService addressLookupService;

	@ApiOperation(value = "Look up an address specified by country and query string", response = List.class)
	@RequestMapping(value = { "/lookup" }, method = RequestMethod.GET)
	public List<AddressResult> lookupAddress(@RequestParam(name = "country", required = true) final String country,
			@RequestParam(name = "q", required = true) final String q,
			@RequestParam(name = "postcode", required = false) final String postcode) {
		return this.addressLookupService.lookupAddress(country.toUpperCase(), q, postcode);
	}

}
