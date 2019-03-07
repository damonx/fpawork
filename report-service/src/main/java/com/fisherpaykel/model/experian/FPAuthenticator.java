package com.fisherpaykel.model.experian;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**Authentication configurations of FPA Proxy server.
 * @author damonx
 * 
 */
public class FPAuthenticator extends Authenticator {

	public FPAuthenticator() {
	}

	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		return (new PasswordAuthentication(System.getProperty("client.proxy.username"),
				System.getProperty("client.proxy.password").toCharArray()));
	}

}

