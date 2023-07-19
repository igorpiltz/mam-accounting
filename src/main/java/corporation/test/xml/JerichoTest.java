package corporation.test.xml;

import net.htmlparser.jericho.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.*;

import corporation.model.bookkeeping.Account;
import corporation.model.bookkeeping.AccountClass;
import corporation.model.bookkeeping.Part;
import corporation.model.bookkeeping.Transaction;

public class JerichoTest {
	public static void main(String[] args) throws Exception {
		String sourceUrlString="res/swedbank.htm";
		if (args.length==0)
		  System.err.println("Using default argument of \""+sourceUrlString+'"');
		else
			sourceUrlString=args[0];
		if (sourceUrlString.indexOf(':')==-1) sourceUrlString="file:"+sourceUrlString;
		MicrosoftConditionalCommentTagTypes.register();
		
		Source source=new Source(new URL(sourceUrlString));
		System.out.println("\n*******************************************************************************\n");
		

		System.out.println("Table:");
		List<Element> segments = source.getAllElements(HTMLElementName.TABLE);
		displaySegments(segments);
		List<Element> rows = segments.get(5).getAllElements(HTMLElementName.TR);
		
		for (int index = 0; index < rows.size(); index++) {
			List<Element> cells = rows.get(index).getAllElements(HTMLElementName.TD);
			displaySegmentsWithText(cells);
			if (cells.size() != 0)
				processAndDisplay(cells);
			System.out.println("\n*******************************************************************************\n");
		}
		
		
		
/*
		System.out.println("Document Type Declarations:");
		displaySegments(source.getAllTags(StartTagType.DOCTYPE_DECLARATION));

		System.out.println("Unregistered start tags:");
		displaySegments(source.getAllTags(StartTagType.UNREGISTERED));

		System.out.println("Unregistered end tags:");
		displaySegments(source.getAllTags(EndTagType.UNREGISTERED));
		
		System.out.println(source.getCacheDebugInfo());
		*/
  }

	private static void displaySegments(List<? extends Element> segments) {
		for (Element segment : segments) {
			System.out.println("-------------------------------------------------------------------------------");
			System.out.println(segment.getDebugInfo());
			// System.out.println(segment);
		}
		System.out.println("\n*******************************************************************************\n");
	}
	
	private static void displaySegmentsWithText(List<? extends Element> segments) {
		for (Element segment : segments) {
			System.out.println("-------------------------------------------------------------------------------");
			System.out.println(segment.getDebugInfo());
			System.out.println(segment.getContent());
			Element subElement = segment;
			for (int index = 0; index < 10; index++) {
				if (subElement.getChildElements().size() == 0) {
					System.out.println("Text: \"" + CharacterReference.decodeCollapseWhiteSpace(subElement.getContent()).trim() + "\"");
					break;
				} else subElement = subElement.getChildElements().get(0);
			}
		}
	}
	
	
	private static void processAndDisplay(List<? extends Element> cells) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        // dateNoticed
		Date dateNoticed = formatter.parse(getText(cells.get(0)));	
		
		// dateOccurred
		Date dateOccurred = formatter.parse(getText(cells.get(1)));
		
		// description
		String description = getText(cells.get(3));
		
		// amount
		String amount = getDecimal(cells.get(5));
		System.out.println(amount);
		
		Transaction trans = new Transaction(new Date(), dateOccurred,
				dateNoticed, description, null, null);
		
		Account acc = new Account(AccountClass.ASSET, "Konto", 2);
		Part part = new Part(acc, new BigDecimal(amount));
		trans.add(part);
		
		System.out.println(trans);
		
	}

	private static String getText(Element element) throws ParseException {
		Element subElement = element;
		for (int index = 0; index < 10; index++) {
			if (subElement.getChildElements().size() == 0)
				return CharacterReference.decodeCollapseWhiteSpace(subElement.getContent()).trim();
			else subElement = subElement.getChildElements().get(0);
		}
		throw new java.text.ParseException(element.toString(), element.getBegin());
	}
	
	private static String getDecimal(Element element) throws ParseException {
		Element subElement = element;
		for (int index = 0; index < 10; index++) {
			if (subElement.getChildElements().size() == 0)
				return CharacterReference.decodeCollapseWhiteSpace(subElement.getContent()).replaceAll("\\s", "").replaceAll(",", ".");
			else subElement = subElement.getChildElements().get(0);
		}
		throw new java.text.ParseException(element.toString(), element.getBegin());
	}
	
}

