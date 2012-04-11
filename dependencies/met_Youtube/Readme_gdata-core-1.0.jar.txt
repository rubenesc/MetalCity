

The jar "gdata-core-1.0" is custom made.

I downloaded the sources and recompiled it, because it was giving me problems.

===========http://www.curiousattemptbunny.com/2010/07/comgooglegdatautilparseexception-name.html========================


com.google.gdata.util.ParseException: name must have a value
For some reason the Google Maps API gdata core recently started failing to read my feature feed. This is the error I see in my App Engine log:


Caused by: com.google.gdata.util.ParseException: [Line 1, Column 3457, element atom:name] name must have a value
 at com.google.gdata.util.XmlParser.throwParseException(XmlParser.java:730)
 at com.google.gdata.util.XmlParser.parse(XmlParser.java:693)
 at com.google.gdata.util.XmlParser.parse(XmlParser.java:576)
 at com.google.gdata.data.BaseFeed.parseAtom(BaseFeed.java:867)
 at com.google.gdata.wireformats.input.AtomDataParser.parse(AtomDataParser.java:68)
 at com.google.gdata.wireformats.input.AtomDataParser.parse(AtomDataParser.java:39)
 at com.google.gdata.wireformats.input.CharacterParser.parse(CharacterParser.java:100)
 at com.google.gdata.wireformats.input.XmlInputParser.parse(XmlInputParser.java:52)
 at com.google.gdata.wireformats.input.AtomDualParser.parse(AtomDualParser.java:66)
 at com.google.gdata.wireformats.input.AtomDualParser.parse(AtomDualParser.java:34)
 at com.google.gdata.client.Service.parseResponseData(Service.java:2165)
 at com.google.gdata.client.Service.parseResponseData(Service.java:2098)
 at com.google.gdata.client.Service.getFeed(Service.java:1136)
 at com.google.gdata.client.Service.getFeed(Service.java:998)
 at com.google.gdata.client.GoogleService.getFeed(GoogleService.java:631)
 at com.google.gdata.client.Service.getFeed(Service.java:1017)
 at com.curiousattemptbunny.server.CompanyServiceImpl.refreshCompanies(CompanyServiceImpl.java:80)
 ... 52 more
Caused by: com.google.gdata.util.ParseException: name must have a value
 at com.google.gdata.data.Person$AtomHandler$NameHandler.processEndElement(Person.java:251)
 at com.google.gdata.util.XmlParser.endElement(XmlParser.java:1004)
 at org.xml.sax.helpers.ParserAdapter.endElement(Unknown Source)
 at com.sun.org.apache.xerces.internal.parsers.AbstractSAXParser.endElement(Unknown Source)
 at com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser.emptyElement(Unknown Source)
 at com.sun.org.apache.xerces.internal.impl.XMLNSDocumentScannerImpl.scanStartElement(Unknown Source)
 at com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl$FragmentContentDriver.next(Unknown Source)
 at com.sun.org.apache.xerces.internal.impl.XMLDocumentScannerImpl.next(Unknown Source)
 at com.sun.org.apache.xerces.internal.impl.XMLNSDocumentScannerImpl.next(Unknown Source)
 at com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl.scanDocument(Unknown Source)
 at com.sun.org.apache.xerces.internal.parsers.XML11Configuration.parse(Unknown Source)
 at com.sun.org.apache.xerces.internal.parsers.XML11Configuration.parse(Unknown Source)
 at com.sun.org.apache.xerces.internal.parsers.XMLParser.parse(Unknown Source)
 at com.sun.org.apache.xerces.internal.parsers.AbstractSAXParser.parse(Unknown Source)
 at com.sun.org.apache.xerces.internal.jaxp.SAXParserImpl$JAXPSAXParser.parse(Unknown Source)
 at org.xml.sax.helpers.ParserAdapter.parse(Unknown Source)
 at com.google.gdata.util.XmlParser.parse(XmlParser.java:685)
 ... 67 more
The culprit appears to be the lack of an <atom:name> tag in the feature feed. Until the reason for this reveals itself or an update to the API is released, I'm using a custom build of the gdata-core-1.0.jar. I simply substituted a non-null value for the name in the Person.NameHandler class:
if (value == null) {
  //throw new ParseException(CoreErrorDomain.ERR.nameValueRequired);
  value = "N/A";
}