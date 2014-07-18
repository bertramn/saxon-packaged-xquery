package com.fleurida.binda.oxygenxml.demo;

import com.fleurida.binda.oxygenxml.PlatformDesignInitializer;

import net.sf.saxon.Configuration;

public class DemoPlatformDesignInitializer extends PlatformDesignInitializer {

	@Override
	protected void registerExtensions(Configuration config) {
		config.registerExtensionFunction(new DemoExtention());
	}

}
