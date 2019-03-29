package br.com.unisys.xmlexitools;

import com.siemens.ct.exi.core.EXIFactory;
import com.siemens.ct.exi.core.exceptions.EXIException;
import com.siemens.ct.exi.core.helpers.DefaultEXIFactory;
import com.siemens.ct.exi.main.api.sax.EXIResult;
import com.siemens.ct.exi.main.api.sax.EXISource;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.XMLConstants;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * @author Edson Moraes - Edson.MoraesJunior@unisys.com
 */
public class XmlExiUtil {

	public static void main(String[] args) {
		/*
		 *  Setup EXIFactory as required
		 */
		EXIFactory exiFactory = DefaultEXIFactory.newInstance();
		// e.g., add additional settings beyond the default values
		// exiFactory.setGrammars(GrammarFactory.newInstance().createGrammars("foo.xsd")); // use XML schema
		// exiFactory.setCodingMode(CodingMode.COMPRESSION); // use deflate compression for larger XML files

		/*
		 *  encode XML to EXI
		 */
		String fileEXI = "C:/IDEA-Projects/xml-compressor/xml-compressor/src/main/resources/output.exi";

		try (OutputStream osEXI = new FileOutputStream(fileEXI)) {
			// EXI output
			EXIResult exiResult = new EXIResult(exiFactory);
			exiResult.setOutputStream(osEXI);
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			xmlReader.setContentHandler(exiResult.getHandler());
			xmlReader.parse("file:///C:/IDEA-Projects/xml-compressor/xml-compressor/src/main/resources/input.xml"); // parse XML input

			/*
			 *  decode EXI to XML
			 */
			String fileXML = "file:///C:/IDEA-Projects/xml-compressor/xml-compressor/src/main/resources/input-decompressed.xml"; // XML output again
			Result result = new StreamResult(fileXML);
			InputSource is = new InputSource(fileEXI);
			SAXSource exiSource = new EXISource(exiFactory);
			exiSource.setInputSource(is);
			TransformerFactory tf = TransformerFactory.newInstance();
			tf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			Transformer transformer = tf.newTransformer();
			transformer.transform(exiSource, result);
		} catch (TransformerException | EXIException | SAXException | IOException e) {
			e.printStackTrace();
		}

		File folder = new File("C:/IDEA-Projects/xml-compressor/xml-compressor/src/main/resources");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println("File: " + listOfFiles[i].getName() + "\t\t\t\tSize: " + listOfFiles[i].length());
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Directory " + listOfFiles[i].getName());
			}
		}

	}
}
