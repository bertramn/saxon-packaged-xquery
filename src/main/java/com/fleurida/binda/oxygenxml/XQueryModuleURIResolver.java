package com.fleurida.binda.oxygenxml;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Locale;
import java.util.Stack;
import java.util.logging.Logger;

import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.lib.ModuleURIResolver;
import net.sf.saxon.trans.XPathException;

public class XQueryModuleURIResolver implements ModuleURIResolver {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger
			.getLogger(XQueryModuleURIResolver.class.getName());

	private static boolean windowsOs = initWindowsOs();

	private static boolean initWindowsOs() {
		String osName = System.getProperty("os.name").toLowerCase(Locale.US);
		return osName.contains("windows");
	}

	@Override
	public StreamSource[] resolve(String moduleURI, String baseURI,
			String[] locations) throws XPathException {

		StreamSource[] ss = new StreamSource[locations.length];
		try {
			for (int i = 0; i < locations.length; i++) {

				URL url = resolveMandatoryResourceAsUrl(locations[i]);

				ss[i] = new StreamSource(url.openStream());
			}
		} catch (Exception e) {
			throw new XPathException("Error resolving resource: " + locations,
					e);
		}
		return ss;

	}

	public static URL resolveMandatoryResourceAsUrl(String uri)
			throws FileNotFoundException, MalformedURLException {
		URL url = resolveResourceAsUrl(uri);
		if (url == null) {
			String resolvedName = resolveUriPath(uri);
			throw new FileNotFoundException("Cannot find resource: "
					+ resolvedName + " in classpath for URI: " + uri);
		} else {
			return url;
		}
	}

	public static URL resolveResourceAsUrl(String uri)
			throws MalformedURLException {
		if (uri.startsWith("file:")) {
			String name = after(uri, "file:");
			uri = tryDecodeUri(uri);
			LOG.finest("Loading resource: " + uri + " from file system");
			File file = new File(name);
			if (!file.exists()) {
				return null;
			}
			return new URL(uri);
		} else if (uri.startsWith("http:")) {
			LOG.finest("Loading resource: " + uri + " from HTTP");
			return new URL(uri);
		} else if (uri.startsWith("classpath:")) {
			uri = after(uri, "classpath:");
			uri = tryDecodeUri(uri);
		}

		String resolvedName = resolveUriPath(uri);
		LOG.finest("Loading resource: " + resolvedName + " from classpath");
		return loadResourceAsURL(resolvedName);
	}

	public static URL loadResourceAsURL(String name) {

		notEmpty(name, "uri");

		URL url = null;

		String resolvedName = resolveUriPath(name);
		ClassLoader contextClassLoader = Thread.currentThread()
				.getContextClassLoader();
		if (contextClassLoader != null) {
			url = contextClassLoader.getResource(resolvedName);
		}
		if (url == null) {
			url = XQueryModuleURIResolver.class.getClassLoader().getResource(
					resolvedName);
		}
		if (url == null) {
			url = XQueryModuleURIResolver.class.getResource(resolvedName);
		}

		return url;
	}

	private static String resolveUriPath(String name) {
		return compactPath(name, '/');
	}

	public static String compactPath(String path, char separator) {
		if (path == null) {
			return null;
		}

		if (path.indexOf('/') == -1 && path.indexOf('\\') == -1) {
			return path;
		}

		path = normalizePath(path);

		boolean endsWithSlash = path.endsWith("/") || path.endsWith("\\");

		boolean startsWithSlash = path.startsWith("/") || path.startsWith("\\");

		Stack<String> stack = new Stack<String>();

		String separatorRegex = "\\\\|/";
		String[] parts = path.split(separatorRegex);
		for (String part : parts) {
			if (part.equals("..") && !stack.isEmpty()
					&& !"..".equals(stack.peek())) {
				stack.pop();
			} else if (part.equals(".") || part.isEmpty()) {
			} else {
				stack.push(part);
			}
		}

		StringBuilder sb = new StringBuilder();

		if (startsWithSlash) {
			sb.append(separator);
		}

		for (Iterator<String> it = stack.iterator(); it.hasNext();) {
			sb.append(it.next());
			if (it.hasNext()) {
				sb.append(separator);
			}
		}

		if (endsWithSlash && stack.size() > 0) {
			sb.append(separator);
		}

		return sb.toString();
	}

	public static String normalizePath(String path) {
		if (path == null) {
			return null;
		}

		if (windowsOs) {
			return path.replace('/', '\\');
		} else {
			return path.replace('\\', '/');
		}
	}

	private static String tryDecodeUri(String uri) {
		try {
			uri = URLDecoder.decode(uri, "UTF-8");
		} catch (Exception e) {
			LOG.finest("Error URL decoding uri using UTF-8 encoding: " + uri
					+ ". This exception is ignored.");
		}
		return uri;
	}

	public static String notEmpty(String value, String name) {
		if (isEmpty(value)) {
			throw new IllegalArgumentException(name
					+ " must be specified and not empty");
		}

		return value;
	}

	public static boolean isEmpty(Object value) {
		return !isNotEmpty(value);
	}

	public static boolean isNotEmpty(Object value) {
		if (value == null) {
			return false;
		} else if (value instanceof String) {
			String text = (String) value;
			return text.trim().length() > 0;
		} else {
			return true;
		}
	}

	public static String after(String text, String after) {
		if (!text.contains(after)) {
			return null;
		}
		return text.substring(text.indexOf(after) + after.length());
	}

}
