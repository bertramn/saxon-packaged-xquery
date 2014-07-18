package com.fleurida.binda.oxygenxml.demo;

import java.util.Date;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;

/**
 * A simple example of a saxon extension function
 */
public class DemoExtention extends ExtensionFunctionDefinition {

	private static final long serialVersionUID = 1L;

	@Override
	public SequenceType[] getArgumentTypes() {
		return new SequenceType[] { SequenceType.SINGLE_STRING };
	}

	@Override
	public SequenceType getResultType(SequenceType[] suppliedArgumentTypes) {
		return SequenceType.SINGLE_STRING;
	}

	@Override
	public StructuredQName getFunctionQName() {
		return new StructuredQName("fext", "http://ndc.fleurida.com/test",
				"test-extension-function");
	}

	@Override
	public ExtensionFunctionCall makeCallExpression() {
		return new ExtensionFunctionCall() {

			private static final long serialVersionUID = 1L;

			@Override
			public Sequence call(XPathContext context, Sequence[] arguments)
					throws XPathException {
				if (arguments == null || arguments.length != 1) {
					throw new XPathException(
							this.getDefinition().getFunctionQName()
									+ " does not contain the expected number of arguments.");
				}

				// 1st argument is the schema attribute value xsd, xml,
				// wsdl etc.
				StringValue sv = null;
				for (Sequence seq : arguments) {
					SequenceIterator<? extends Item> se = seq.iterate();
					Item i = se.next();
					String firstArgument = i.getStringValue();

					sv = new StringValue(
							"extension ["
									+ firstArgument
									+ "]: "
									+ new java.text.SimpleDateFormat(
											"yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
											.format(new Date()));
				}
				return sv;
			}
		};
	}
}
