/**
 *
 */
package com.fisherpaykel.common.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.itextpdf.kernel.color.DeviceRgb;

/**
 * @author wilsonas & damonx
 *
 */
public class FPAConstants {	

	
	/********************************************************************
	 ********************* General Constants : START ********************
	 ********************************************************************/
	public static final String EMPTY_STRING = "";
	public static final String GREATER_THAN = ">";
	public static final String SPACE = " ";
	public static final String PIPE_SEPARATOR = "   |   ";
	public static final String COLON = ":";
	public static final String PIPE = "|";
	public static final String HYPHEN = "-";
	public static final String SERVER_PORT = "server.port";
	public static final String JRXML_PATH = "jrxml.path";
	public static final String FPA_QRG_MICROSERVICE = "Welcome to FPA QRG Report Microservice";
	public static final List<String> ACCEPTED_COUNTRIES = Collections.unmodifiableList(Arrays.asList("AU", "CA", "NZ", "UK", "US"));
	public static final String IMAGE_PATH_BULLET = "image.path.bullet";
	/********************************************************************
	 ********************** General Constants : END *********************
	 ********************************************************************/

	
	/********************************************************************
	 ********************* Colour Constants : START *********************
	 ********************************************************************/
	public static final DeviceRgb GRAY = new DeviceRgb(128, 130, 133);
	public static final DeviceRgb LIGHT_GRAY = new DeviceRgb(237, 237, 237);
	/********************************************************************
	 ********************** Colour Constants : END **********************
	 ********************************************************************/
	

	/********************************************************************
	 *********************** Cache Constants : END **********************
	 ********************************************************************/
	public static int ONE_DAY_IN_SECONDS = 24 * 3600;
	public static int FIVE_DAYS_IN_SECONDS = 5 * 24 * 3600;
	/********************************************************************
	 *********************** Cache Constants : END **********************
	 ********************************************************************/
	
	
	/********************************************************************
	 ********************** Header Constants : START ********************
	 ********************************************************************/
	public static final String HEADER_QUICK_REFERENCE_GUIDE = "QUICK REFERENCE GUIDE";	
	public static final String HEADER_DATE = "Date";
	public static final String HEADER_DATE_FORMAT = "dd.MM.yyyy";
	/********************************************************************
	 *********************** Header Constants : END *********************
	 ********************************************************************/
	
	
	/********************************************************************
	 ************************ Dimensions : START ************************
	 ********************************************************************/
	public static final String DIMENSIONS = "DIMENSIONS";
	/********************************************************************
	 ************************* Dimensions : END *************************
	 ********************************************************************/	
	
	
	/********************************************************************
	 *************** Features & Benefits Constants : START **************
	 ********************************************************************/
	public static final String FEATURES_AND_BENEFITS = "FEATURES & BENEFITS";
	/********************************************************************
	 **************** Features & Benefits Constants : END ***************
	 ********************************************************************/
	
	
	/********************************************************************
	 ****************** Specification Constants : START *****************
	 ********************************************************************/
	public static final String SPECIFICATIONS = "SPECIFICATIONS";
	public static final String BULL = "•";
	public static final String DEGREE = "°";
	public static final String SKU = "SKU";
	/********************************************************************
	 ******************* Specification Constants : END ******************
	 ********************************************************************/
	

	/********************************************************************
	 ******************** Disclaimer Constants : START ******************
	 ********************************************************************/
	public static final String QRG_DISCLAIMER = "qrg.disclaimer";
	/********************************************************************
	 ********************* Disclaimer Constants : END *******************
	 ********************************************************************/
	
	
	/********************************************************************
	 ****************** Other Downloads Constants : START ***************
	 ********************************************************************/
	public static String OTHER_DOWNLOADS = "Other product downloads available at fisherpaykel.com";
	public static final String IMAGE_PATH_DOWNLOADS = "image.path.downloads";
	/********************************************************************
	 ******************* Other Downloads Constants : END ****************
	 ********************************************************************/
	
	
	/********************************************************************
	 ******************* Static Text Constants : START ******************
	 ********************************************************************/
	public static final String IMAGE_PATH_24x7 = "image.path.24x7";	
	public static final String STATIC_24x7_TEXT_1 = "A PEACE OF MIND SALE";
	public static final String STATIC_24x7_TEXT_2 = "24 Hours 7 Days a Week Customer Support";
	public static final String T = "T";
	public static final String STATIC_24x7_TEXT_PHONE_NUMBER = " 1300 650 590 ";
	public static final String W = "W";
	public static final String STATIC_24x7_TEXT_WEBSITE = " www.fisherpaykel.com";
	public static final String QRG_DOC_PATH = "qrg.doc.path";
	/********************************************************************
	 ******************** Static Text Constants : END *******************
	 ********************************************************************/
	
	
	/********************************************************************
	 ********************** Footer Constants : START ********************
	 ********************************************************************/
	public static final String IMAGE_PATH_FOOTER = "image.path.footer";
	/********************************************************************
	 *********************** Footer Constants : END *********************
	 ********************************************************************/

}
