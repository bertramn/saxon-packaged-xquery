package com.fleurida.binda.oxygenxml;

import javax.xml.transform.TransformerException;

import net.sf.saxon.Configuration;
import net.sf.saxon.lib.Initializer;

/**
 * A base implementation for a OxygenXML configuration initializer. This Saxon
 * Initializer has the capability to load xqm modules from the classpath and can
 * also be used to register custom extension functions.
 * 
 * If using the classpath function, please be aware that the jar containing the
 * resource must also be configured in the OxygenXML setup section.
 */
public abstract class PlatformDesignInitializer implements Initializer {

	@Override
	public void initialize(Configuration config) throws TransformerException {
		// enable to use packaged xquery module files to be loaded
		config.setModuleURIResolver(new XQueryModuleURIResolver());

		// also need to register extension functions
		registerExtensions(config);

	}

	/**
	 * Needs to be overridden by the implementation if any custom extension
	 * functions need to be registered. Inside the call just use
	 * <code>config.registerExtensionFunction(function)</code> to register the
	 * custom extension function.
	 * 
	 * @param config
	 *            the saxon configuration
	 */
	protected void registerExtensions(Configuration config) {
	}

}
