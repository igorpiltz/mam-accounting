package corporation.test.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import corporation.model.invoice.Invoice;

public class XmlTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		List<Invoice> invoices = new ArrayList<Invoice>();
		invoices.add(new Invoice());
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		docFactory.setNamespaceAware(true);
		docFactory.setValidating(true);
		
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
		// root elements
		Document doc = docBuilder.newDocument();
		
		
		Element rootElement = doc.createElement("HtAnsokan");
		doc.appendChild(rootElement);
		rootElement.setAttribute("xmlns", "se/skatteverket/hunten/ansokan/2.0");
		rootElement.setAttribute("xmlns:htko", "se/skatteverket/hunten/komponent/2.0");
		rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");	// staff elements
		
		
		Element requestNumber = doc.createElement("htko:AnsokningsNr");
		rootElement.appendChild(requestNumber);
		requestNumber.appendChild(doc.createTextNode("5"));
		
		Element request = doc.createElement("htko:HushallAnsokan");
		rootElement.appendChild(request);
		
		for (int index = 0; index < invoices.size(); index++){
			Invoice invoice = invoices.get(index);
			
			
		}
 
		/*
		// set attribute to staff element
		Attr attr = doc.createAttribute("id");
		attr.setValue("1");
		staff.setAttributeNode(attr);
 
		// shorten way
		// staff.setAttribute("id", "1");
 
		// firstname elements
		Element firstname = doc.createElement("firstname");
		firstname.appendChild(doc.createTextNode("yong"));
		staff.appendChild(firstname);
 
		// lastname elements
		Element lastname = doc.createElement("lastname");
		lastname.appendChild(doc.createTextNode("mook kim"));
		staff.appendChild(lastname);
 
		// nickname elements
		Element nickname = doc.createElement("nickname");
		nickname.appendChild(doc.createTextNode("mkyong"));
		staff.appendChild(nickname);
 
		// salary elements
		Element salary = doc.createElement("salary");
		salary.appendChild(doc.createTextNode("100000"));
		staff.appendChild(salary);
 		*/
		
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		// StreamResult result = new StreamResult(new File("file.xml"));
 
		// Output to console for testing
		StreamResult result = new StreamResult(System.out);
 
		transformer.transform(source, result);
 
		// System.out.println("File saved!");


	}

}


/*
 
DocumentBuilderFactory factory = 
DocumentBuilderFactory.newInstance();
factory.setValidating(true);
DocumentBuilder builder = factory.newDocumentBuilder();
builder.setErrorHandler(new org.xml.sax.ErrorHandler() {
//Ignore the fatal errors
public void fatalError(SAXParseException exception)
 throws SAXException { }
//Validation errors 
public void error(SAXParseException e)
throws SAXParseException {
System.out.println("Error at " +e.getLineNumber() + " line.");
System.out.println(e.getMessage());
System.exit(0);
}
//Show warnings
public void warning(SAXParseException err)
throws SAXParseException{
System.out.println(err.getMessage());
System.exit(0);
}
});
Document xmlDocument = builder.parse(
 new FileInputStream("Employeexy.xml"));
DOMSource source = new DOMSource(xmlDocument);
StreamResult result = new StreamResult(System.out);
TransformerFactory tf = TransformerFactory.newInstance();
Transformer transformer = tf.newTransformer();
transformer.setOutputProperty(
OutputKeys.DOCTYPE_SYSTEM, "Employee.dtd");
transformer.transform(source, result);
*/