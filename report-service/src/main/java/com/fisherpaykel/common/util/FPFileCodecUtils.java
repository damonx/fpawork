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
package com.fisherpaykel.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.io.codec.Base64;

/**
 * @author damonx
 *
 */
public class FPFileCodecUtils {

	private FPFileCodecUtils() {
		throw new Error("Don't instantiate me!");
	}

	/**
	 * This method converts the content of a source file into Base64 encoded data and saves that to a target file. If isChunked parameter is
	 * set to true, there is a hard wrap of the output encoded text.
	 * 
	 * @throws Exception
	 */
	public static File encodeFile(final String sourceFile) throws IOException {

		final byte[] base64EncodedData = Base64.encodeBytes(loadFileAsBytesArray(sourceFile)).getBytes();
		final String targetFile = sourceFile.concat("-base64encoded");

		return writeByteArraysToFile(targetFile, base64EncodedData);
	}

	public static void decode(final String sourceFile, final String targetFile) throws Exception {

		final byte[] decodedBytes = Base64.decode(new String(loadFileAsBytesArray(sourceFile)));

		writeByteArraysToFile(targetFile, decodedBytes);
	}

	/**
	 * Loads a file from file system and returns the byte array of the content.
	 *
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	private static byte[] loadFileAsBytesArray(final String fileName) throws IOException {

		final File file = new File(fileName);
		final int length = (int) file.length();
		final BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
		final byte[] bytes = new byte[length];
		reader.read(bytes, 0, length);
		reader.close();
		return bytes;
	}

	/**
	 * This method writes byte array content into a file and returns the file.
	 *
	 * @param fileName
	 * @param content
	 * @throws IOException
	 */
	public static File writeByteArraysToFile(final String fileName, final byte[] content) throws IOException {

		final File file = new File(fileName);
		final BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(file));
		writer.write(content);
		writer.flush();
		writer.close();
		return file;
	}

}
