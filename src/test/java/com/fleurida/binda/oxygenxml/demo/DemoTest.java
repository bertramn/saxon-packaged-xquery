package com.fleurida.binda.oxygenxml.demo;

import java.net.URL;

import net.sf.saxon.Query;

import org.junit.Test;

public class DemoTest {

	@Test
	public void testDemo() throws Exception {

		URL xqURL = getClass().getResource("/test-query.xq");

		String[] args = new String[] {
				"-init:" + DemoPlatformDesignInitializer.class.getName(),
				"-q:" + xqURL.toExternalForm() };

		Query.main(args);

	}

}
