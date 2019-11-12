package com.fisherpaykel.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.io.IOException;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fisherpaykel.service.impl.ReportServiceImpl;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ReportController.class, secure = false)
public class ReportControllerUnitTest {

	@MockBean
	private ReportServiceImpl reportService;

	@Autowired
	private MockMvc mockMvc;

	@Mock
	FileInputStream inputStream;

	@Mock
	ResponseEntity<Object> responseEntity;

	@Before
	public void setup() throws IOException {
		when(this.reportService.generateQRGFromIText(any(), anyString(), anyBoolean())).thenReturn(this.inputStream);
	}

	@Test
	public void testGenerateQRG_when_request_parameter_is_empty() throws Exception {

		// WHEN
		final RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
				"/report/itext/qrg/v1").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE);

		final MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

		// THEN
		assertThat(result.getResponse()).isNotNull();
		assertThat(result.getResponse().getStatus()).isNotNull().isEqualTo(400);

	}

	@Test
	public void testGenerateQRG_when_request_parameter_country_not_supported() throws Exception {

		// WHEN
		final RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
				"/report/itext/qrg/v1").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE)
				.content("{\"test\":10,\"value\":1000}").param("country", "country-not-supported");

		final MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

		// THEN
		assertThat(result.getResponse()).isNotNull();
		assertThat(result.getResponse().getStatus()).isNotNull().isEqualTo(400);

		final JSONObject resultJson = new JSONObject(result.getResponse().getContentAsString());
		assertThat(resultJson.get("status")).isNotNull().isEqualTo("BAD_REQUEST");
		assertThat(resultJson.get("message")).isNotNull().isEqualTo("The product json payload is empty or the country is not supported.");
	}

	@Test
	public void test_fetchExistingQRG_when_country_not_supported() throws Exception {
		// WHEN

		final RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/report/itext/qrg/b2b/v1")
				.param("country", "country-not-supported")
				.param("sku", "12345");
		final MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

		// THEN
		assertThat(result.getResponse()).isNotNull();
		assertThat(result.getResponse().getStatus()).isNotNull().isEqualTo(400);
		final JSONObject resultJson = new JSONObject(result.getResponse().getContentAsString());
		assertThat(resultJson.get("status")).isNotNull().isEqualTo("BAD_REQUEST");
		assertThat(resultJson.get("message")).isNotNull().isEqualTo("The product json payload is empty or the country is not supported.");
	}

	@Test
	public void test_fetchExistingQRG_when_sku_is_empty() throws Exception {
		// WHEN
		final RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/report/itext/qrg/b2b/v1")
				.param("country", "nz");
		final MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

		// THEN
		assertThat(result.getResponse()).isNotNull();
		assertThat(result.getResponse().getStatus()).isNotNull().isEqualTo(400);
	}

}
