package com.fisherpaykel.model.experian;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author damonx
 */
public class FPProxySelector extends ProxySelector {
	private static Logger log = LoggerFactory.getLogger(FPProxySelector.class);

	List<Proxy> proxies = new ArrayList<>();
	
	{
		
		this.proxies.add(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(System.getProperty("client.proxy.url"),
				Integer.parseInt(System.getProperty("client.proxy.port")))));
	}

	public FPProxySelector() {
	}

	@Override
	public List<Proxy> select(final URI uri) {
		return this.proxies;
	}

	@Override
	public void connectFailed(final URI uri, final SocketAddress sa, final IOException ioe) {
		log.info("failed to connect to uri:" + uri + " sa:" + sa, ioe);
	}
}

