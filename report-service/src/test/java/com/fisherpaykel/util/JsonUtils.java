package com.fisherpaykel.util;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Util class to do: - MARSHALLING: convert string json or file into Object - UNMARSHALLING: convert Object into string json
 *
 * @author Damon Xu
 */
public class JsonUtils {

	private JsonUtils() {
	}

	public static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

	public static <T> T json2Object(final String str, final Class<T> clazz)
			throws IOException {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		return objectMapper.readValue(str, clazz);
	}

	public static <T> String object2Json(final T obj) throws IOException {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		return objectMapper.writeValueAsString(obj);
	}

	public static <T> T jsonFile2Object(final String fileName, final Class<T> clazz)
			throws IOException {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		// Ignoring missing fields in model objects
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper.readValue(new File(concatenate(fileName)), clazz);
	}

	private static String concatenate(final String fileName) {
		return "src/test/resources/" + fileName;
	}

}
