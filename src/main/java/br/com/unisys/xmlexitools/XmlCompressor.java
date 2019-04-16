package br.com.unisys.xmlexitools;

import com.siemens.ct.exi.core.CodingMode;
import com.siemens.ct.exi.core.EXIFactory;
import com.siemens.ct.exi.core.FidelityOptions;
import com.siemens.ct.exi.core.exceptions.EXIException;
import com.siemens.ct.exi.core.exceptions.UnsupportedOption;
import com.siemens.ct.exi.core.helpers.DefaultEXIFactory;
import com.siemens.ct.exi.main.api.sax.EXIResult;
import com.siemens.ct.exi.main.api.sax.EXISource;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import java.io.*;

public class XmlCompressor {

	private final CodingMode codingMode;
	private final FidelityOptions fidelityOptions;
	private final EXIFactory exiFactory = DefaultEXIFactory.newInstance();

	/**
	 * @param codingMode                     <p>The alignment option is used to control the alignment of event codes and
	 *                                       content items. The value is one of <tt>BIT-PACKED</tt>,
	 *                                       <tt>BYTE-ALIGNMENT</tt>, <tt>PRE-COMPRESSION</tt>, or <tt>COMPRESSION</tt>.
	 *                                       </p>
	 *                                       <p>- The alignment option value <tt>BIT-PACKED</tt> indicates that the event
	 *                                       codes and associated content are packed in bits without any padding
	 *                                       in-between.</p>
	 *                                       <p>- The alignment option value <tt>BYTE-ALIGNMENT</tt> indicates that the
	 *                                       event codes and associated content are aligned on byte boundaries. While
	 *                                       byte-alignment generally results in EXI streams of larger sizes compared
	 *                                       with their bit-packed equivalents, byte-alignment may provide a help in
	 *                                       some use cases that involve frequent copying of large arrays of scalar data
	 *                                       directly out of the stream. It can also make it possible to work with data
	 *                                       in-place and can make it easier to debug encoded data by allowing items on
	 *                                       aligned boundaries to be easily located in the stream.</p>
	 *                                       <p>- The alignment option value <tt>PRE-COMPRESSION</tt> indicates that all
	 *                                       steps involved in compression (see
	 *                                       ) are
	 *                                       to be done with the exception of the final step of applying the DEFLATE
	 *                                       algorithm. The primary use case of pre-compression is to avoid a duplicate
	 *                                       compression step when compression capability is built into the transport
	 *                                       protocol. In this case, pre-compression just prepares the stream for later
	 *                                       compression.</p>
	 *                                       <p>- The <tt>COMPRESSION</tt> option is a Boolean used to increase
	 *                                       compactness using additional computational resources. The default value
	 *                                       "false" is assumed when the "compression" element is absent in the EXI
	 *                                       Options document whereas its presence denotes the value "true". When set
	 *                                       to true, the event codes and associated content are compressed according
	 *                                       to <a href="https://www.w3.org/TR/exi/#compression">EXI Compression</a>
	 *                                       regardless of the alignment option value. As mentioned above, the
	 *                                       "compression" element MUST NOT appear in an EXI options document when the
	 *                                       "alignment" element is present.</p>
	 * @param preserveComments               <p>CM(Comment) events will be preserved if <b><tt>true</tt></b>.</p>
	 * @param preserveProcessingInstructions <p>PI events will be preserved if <b><tt>true</tt></b>. A Processing
	 *                                       Instruction (PI) is an SGML and XML node type, which may occur anywhere in
	 *                                       the document, intended to carry instructions to the application.</p>
	 * @param preserveDTDAndEntityRef        <p>DT and ER events can be preserved <b><tt>true</tt></b>.</p>
	 *                                       <p>A document type (DT) declaration information item maps to a DOCTYPE. A
	 *                                       document type (DOCTYPE) declaration consists of an internal, or references
	 *                                       an external Document Type Definition (DTD). It can also have a combination
	 *                                       of both internal and external DTDs. The DTD defines the constraints on the
	 *                                       structure of an XML document.</p>
	 *                                       <p>An entity reference (ER) is an alternative name for a series of characters.
	 *                                       You can use an entity in the &name; format, where name is the name of the
	 *                                       entity. There are some predefined entities in XML, furthermore you can
	 *                                       declare entities in a DTD (Document Type Definition). The entity value data
	 *                                       substituted in is normally treated as XML and parsed, however this behavior
	 *                                       can be modified. A character entity reference refers to the content of a
	 *                                       named entity. An entity declaration is created by using the &lt;!ENTITY
	 *                                       name "value"&gt; syntax in a Document Type Definition (DTD).</p>
	 * @param preservePrefixes               <p>NS (Namespace) events will be preserved if <b><tt>true</tt></b>. An
	 *                                       namespace (NS) event item maps to a Namespace Declaration and its usages
	 *                                       i.e., XML tag prefixes.</p>
	 * @param preserveLexicalValues          EXI defines a minimal set of datatype representations called Built-in EXI
	 *                                       datatype representations that define how content items as well as the parts
	 *                                       of an event code are represented in EXI streams. When the
	 *                                       Preserve.lexicalValues option is false, values are represented using
	 *                                       built-in EXI datatype representations (see
	 *                                       <a href="https://www.w3.org/TR/exi/#encodingDatatypes">Built-in EXI
	 *                                       Datatype Representations</a>) or user-defined datatype representations (see
	 *                                       <a href="https://www.w3.org/TR/exi/#datatypeRepresentationMap">Datatype
	 *                                       Representation Map</a>) associated with the
	 *                                       <a href="http://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#datatype">
	 *                                       schema datatypes</a>. Otherwise, values are represented as Strings with
	 *                                       restricted character sets (see
	 *                                       <a href="https://www.w3.org/TR/exi/#builtInRestrictedStrings">Table 7-2</a>
	 *                                       of the EXI specification).
	 * @param enableSelfContainedElements    The selfContained option is a Boolean used to enable the use of
	 *                                       self-contained elements in the EXI stream. Self-contained elements may be
	 *                                       read independently from the rest of the EXI body, allowing them to be
	 *                                       indexed for random access. The "selfContained" element MUST NOT appear in
	 *                                       an EXI options document when one of "compression", "pre-compression" or
	 *                                       "strict" elements are present in the same options document. The default
	 *                                       value "false" is assumed when the "selfContained" element is absent from
	 *                                       the EXI Options document whereas its presence denotes the value "true".
	 * @param stricSchemaInterpretation      The strict option is a Boolean used to increase compactness by using a
	 *                                       strict interpretation of the schemas and omitting preservation of
	 *                                       certain items, such as comments, processing instructions and namespace
	 *                                       prefixes.
	 * @return A XMLCompressor instance
	 */
	public static XmlCompressor create(CodingMode codingMode,
									   boolean preserveComments,
									   boolean preserveProcessingInstructions,
									   boolean preserveDTDAndEntityRef,
									   boolean preservePrefixes,
									   boolean preserveLexicalValues,
									   boolean enableSelfContainedElements,
									   boolean stricSchemaInterpretation) throws UnsupportedOption {

		FidelityOptions fidelityOptions;

		if (stricSchemaInterpretation) {
			fidelityOptions = FidelityOptions.createStrict();
			fidelityOptions.setFidelity(FidelityOptions.FEATURE_LEXICAL_VALUE, preserveLexicalValues);
			fidelityOptions.setFidelity(FidelityOptions.FEATURE_COMMENT, false);
			fidelityOptions.setFidelity(FidelityOptions.FEATURE_DTD, false);
			fidelityOptions.setFidelity(FidelityOptions.FEATURE_PI, false);
			fidelityOptions.setFidelity(FidelityOptions.FEATURE_PREFIX, false);
			fidelityOptions.setFidelity(FidelityOptions.FEATURE_SC, false);
			fidelityOptions.setFidelity(FidelityOptions.FEATURE_STRICT, true);
		} else {
			fidelityOptions = FidelityOptions.createStrict();
			fidelityOptions.setFidelity(FidelityOptions.FEATURE_COMMENT, preserveComments);
			fidelityOptions.setFidelity(FidelityOptions.FEATURE_PI, preserveProcessingInstructions);
			fidelityOptions.setFidelity(FidelityOptions.FEATURE_DTD, preserveDTDAndEntityRef);
			fidelityOptions.setFidelity(FidelityOptions.FEATURE_PREFIX, preservePrefixes);
			fidelityOptions.setFidelity(FidelityOptions.FEATURE_LEXICAL_VALUE, preserveLexicalValues);
			fidelityOptions.setFidelity(FidelityOptions.FEATURE_SC, enableSelfContainedElements);
			fidelityOptions.setFidelity(FidelityOptions.FEATURE_STRICT, false);
		}


		return new XmlCompressor(codingMode, fidelityOptions);
	}

	public static XmlCompressor getMaxCompressionInstance() throws UnsupportedOption {
		return create(CodingMode.COMPRESSION,
				false,
				false,
				false,
				false,
				false,
				false,
				false);
	}

	public static XmlCompressor getMaxFidelityInstance() throws UnsupportedOption {
		return create(CodingMode.BIT_PACKED,
				true,
				true,
				true,
				true,
				true,
				true,
				false);
	}

	public static XmlCompressor getDefaultInstance() throws UnsupportedOption {
		return create(CodingMode.COMPRESSION,
				true,
				true,
				true,
				true,
				true,
				false,
				false);
	}

	private XmlCompressor(CodingMode codingMode, FidelityOptions fidelityOptions) {
		this.codingMode = codingMode;
		this.fidelityOptions = fidelityOptions;
		exiFactory.setCodingMode(codingMode);
		exiFactory.setFidelityOptions(fidelityOptions);
	}

	private byte[] encodeFromStream2(InputStream inputStream) throws EXIException, IOException, SAXException {
		ByteArrayOutputStream exiOutputStream = new ByteArrayOutputStream();
		InputSource inputSource = new InputSource(inputStream);
		EXIResult exiResult = new EXIResult(exiFactory);
		exiResult.setOutputStream(exiOutputStream);
		XMLReader xmlReader = XMLReaderFactory.createXMLReader();
		xmlReader.setContentHandler(exiResult.getHandler());
		xmlReader.parse(inputSource); // parse XML input
		return exiOutputStream.toByteArray();
	}

	private String decodeFromStream2(InputStream inputStream, StreamResult result) throws EXIException, TransformerException {
		InputSource is = new InputSource(inputStream);
		SAXSource exiSource = new EXISource(exiFactory);
		exiSource.setInputSource(is);
		TransformerFactory tf = TransformerFactory.newInstance();
		tf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		Transformer transformer = tf.newTransformer();
		transformer.transform(exiSource, result);
		return result.getOutputStream().toString();
	}

	public byte[] encodeFromString(String inputString) throws IOException, EXIException, SAXException {
		return encodeFromStream2(new ByteArrayInputStream(inputString.getBytes()));
	}

	public String decodeFromByteArray(byte[] inputByteArray) throws EXIException, TransformerException {
		StreamResult result = new StreamResult(new ByteArrayOutputStream());
		decodeFromStream2(new ByteArrayInputStream(inputByteArray), result);
		return result.getOutputStream().toString();
	}

	public byte[] encodeFromStream(InputStream inputStream) throws EXIException, IOException, SAXException {
		return encodeFromStream2(inputStream);
	}

	public String decodeFromStream(InputStream inputStream) throws EXIException, TransformerException {
		StreamResult result = new StreamResult(new ByteArrayOutputStream());
		decodeFromStream2(inputStream, result);
		return result.getOutputStream().toString();
	}

	public byte[] encodeFromFile(String filePath) throws IOException, EXIException, SAXException {
		return encodeFromStream2(new FileInputStream(filePath));
	}

	public byte[] encodeFromFile(File file) throws IOException, EXIException, SAXException {
		return encodeFromStream2(new FileInputStream(file));
	}

	public String decodeFromFile(String filePath) throws FileNotFoundException, TransformerException, EXIException {
		FileInputStream fileInputStream = new FileInputStream(filePath);
		StreamResult result = new StreamResult(new ByteArrayOutputStream());
		return decodeFromStream2(fileInputStream, result);
	}

	public String decodeFromFile(File file) throws FileNotFoundException, TransformerException, EXIException {
		FileInputStream fileInputStream = new FileInputStream(file);
		StreamResult result = new StreamResult(new ByteArrayOutputStream());
		return decodeFromStream2(fileInputStream, result);
	}
}
