package corporation.model.invoice.rut;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import corporation.model.invoice.Invoice;

public class RUTRequest {
	private int requestIDYear; 	// id-nummer som löper över ett år och börjar på 1.
	private List<Invoice> invoices;
	
	void xmlRequest(StreamResult result) throws ParserConfigurationException, TransformerException, XMLStreamException {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
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
		requestNumber.appendChild(doc.createTextNode("" + requestIDYear));
		
		Element request = doc.createElement("htko:HushallAnsokan");
		rootElement.appendChild(request);
		
		for (int index = 0; index < invoices.size(); index++) {
			Invoice invoice = invoices.get(index);
			Element requestAtom = doc.createElement("htko:Arenden");
			request.appendChild(requestAtom);
			
			Element buyer = doc.createElement("htko:Kopare");
			buyer.appendChild(doc.createTextNode(invoice.getCustomer().getStateIdNumber().getLongVersion()));
			requestAtom.appendChild(buyer);
			
			Date datePaid = invoice.getInvoiceStatus().getDatePaid();
			if (datePaid == null)
				throw new XMLStreamException("Invoice Not Paid");
			Element datePaidElement = doc.createElement("htko:BetalningsDatum");
			datePaidElement.appendChild(doc.createTextNode(format.format(datePaid)));
			requestAtom.appendChild(datePaidElement);
			
			Element sum = doc.createElement("htko:FaktureratBelopp");
			sum.appendChild(doc.createTextNode(invoice.getSum().toString()));
			requestAtom.appendChild(sum);
			
			
		}
			
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		 
		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);
 
		transformer.transform(source, result);
 					
	}
}
		
	
	

