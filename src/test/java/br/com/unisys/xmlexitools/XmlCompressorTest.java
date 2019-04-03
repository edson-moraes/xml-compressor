package br.com.unisys.xmlexitools;

import com.siemens.ct.exi.core.exceptions.EXIException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Edson Moraes - Edson.MoraesJunior@unisys.com
 */
public class XmlCompressorTest {

	private static int UNENCODED_SIZE;
	private static int MAX_COMPRESSION_ENCODED_SIZE;
	private static int MAX_COMPRESSION_DECODED_SIZE;
	private static int MAX_FIDELITY_ENCODED_SIZE;
	private static int MAX_FIDELITY_DECODED_SIZE;

	private static String testUnencodedXmlFilePath;
	private static String testEncodedMaxFidelityXmlFilePath;
	private static String testDecodedMaxFidelityXmlFilePath;
	private static String testEncodedMaxCompressionXmlFilePath;
	private static String testDecodedMaxCompressionXmlFilePath;

	private static File testUnencodeXmlFile;
	private static File testEncodedMaxFidelittyXmlFile;
	private static File testDecodedMaxFidelityXmlFile;
	private static File testEncodedMaxCompressionXmlFile;
	private static File testDecodedMaxCompressionXmlFile;

	private static ByteArrayInputStream testUnencodedXmlInputStream;
	private static ByteArrayInputStream testEncodedMaxFidelityInputStream;
	private static ByteArrayInputStream testDecodedMaxFidelityXmlInputStream;
	private static ByteArrayInputStream testEncodedMaxCompressionInputStream;
	private static ByteArrayInputStream testDecodedXmlMaxCompressionInputStream;

	private static String testUnencodedXml;
	private static String testEncodedMaxFidelityXml;
	private static String testDecodedMaxFidelityXml;
	private static String testEncodedMaxCompressionXml;
	private static String testDecodedMaxCompressionXml;

	private static XmlCompressor maxCompressionInstance;
	private static XmlCompressor maxFidelityInstance;
	private static XmlCompressor decodeInstance;


	@BeforeAll
	static void setUp() throws IOException {

		maxCompressionInstance = XmlCompressor.getMaxCompressionInstance();
		maxFidelityInstance = XmlCompressor.getMaxFidelityInstance();
		decodeInstance = XmlCompressor.getMaxFidelityInstance();

		testUnencodedXmlFilePath = "src/test/resources/input.xml";
		testEncodedMaxFidelityXmlFilePath = "src/test/resources/output_max_fidelity.exi";
		testDecodedMaxFidelityXmlFilePath = "src/test/resources/output_max_fidelity_decoded.xml";
		testEncodedMaxCompressionXmlFilePath = "src/test/resources/output_max_compression.exi";
		testDecodedMaxCompressionXmlFilePath = "src/test/resources/output_max_compression_decoded.xml";

		testUnencodeXmlFile = new File(testUnencodedXmlFilePath);
		testEncodedMaxFidelittyXmlFile = new File(testEncodedMaxFidelityXmlFilePath);
		testDecodedMaxFidelityXmlFile = new File(testDecodedMaxFidelityXmlFilePath);
		testEncodedMaxCompressionXmlFile = new File(testEncodedMaxCompressionXmlFilePath);
		testDecodedMaxCompressionXmlFile = new File(testDecodedMaxCompressionXmlFilePath);

		byte[] testUnencodedXmlData = Files.readAllBytes(Paths.get(testUnencodedXmlFilePath));
		byte[] testEncodedMaxFidelityXmlData = Files.readAllBytes(Paths.get(testEncodedMaxFidelityXmlFilePath));
		byte[] testDecodedMaxFidelityXmlData = Files.readAllBytes(Paths.get(testDecodedMaxFidelityXmlFilePath));
		byte[] testEncodedMaxCompressionXmlData = Files.readAllBytes(Paths.get(testEncodedMaxCompressionXmlFilePath));
		byte[] testDecodedMaxCompressionXmlData = Files.readAllBytes(Paths.get(testDecodedMaxCompressionXmlFilePath));

		testUnencodedXml = new String(testUnencodedXmlData);
		testEncodedMaxFidelityXml = new String(testEncodedMaxFidelityXmlData);
		testDecodedMaxFidelityXml = new String(testDecodedMaxFidelityXmlData);
		testEncodedMaxCompressionXml = new String(testEncodedMaxCompressionXmlData);
		testDecodedMaxCompressionXml = new String(testDecodedMaxCompressionXmlData);

		testUnencodedXmlInputStream = new ByteArrayInputStream(testUnencodedXmlData);
		testEncodedMaxFidelityInputStream = new ByteArrayInputStream(testEncodedMaxFidelityXmlData);
		testDecodedMaxFidelityXmlInputStream = new ByteArrayInputStream(testDecodedMaxFidelityXmlData);
		testEncodedMaxCompressionInputStream = new ByteArrayInputStream(testEncodedMaxCompressionXmlData);
		testDecodedXmlMaxCompressionInputStream = new ByteArrayInputStream(testDecodedMaxCompressionXmlData);

		UNENCODED_SIZE = testUnencodedXmlData.length;
		MAX_COMPRESSION_ENCODED_SIZE = testEncodedMaxCompressionXmlData.length;
		MAX_COMPRESSION_DECODED_SIZE = testDecodedMaxCompressionXmlData.length;
		MAX_FIDELITY_ENCODED_SIZE = testEncodedMaxFidelityXmlData.length;
		MAX_FIDELITY_DECODED_SIZE = testDecodedMaxFidelityXmlData.length;
	}

	@Test
	void encodeFromString() {
		byte[] maxFidelityEncodeOutput = new byte[0];
		byte[] maxCompressionEncodeOutput = new byte[0];
		try {
			maxFidelityEncodeOutput = maxFidelityInstance.encodeFromString(testUnencodedXml);
			maxCompressionEncodeOutput = maxCompressionInstance.encodeFromString(testUnencodedXml);
		} catch (EXIException | IOException | SAXException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals(MAX_FIDELITY_ENCODED_SIZE, maxFidelityEncodeOutput.length);
		assertEquals(MAX_COMPRESSION_ENCODED_SIZE, maxCompressionEncodeOutput.length);
	}

	@Test
	void decodeFromString() {
		String maxFidelityDecodeOutput = "";
		String maxCompressionDecodeOutput = "";
		try {
			maxFidelityDecodeOutput = decodeInstance.decodeFromString(testEncodedMaxFidelityXml);
			maxCompressionDecodeOutput = decodeInstance.decodeFromString(testEncodedMaxCompressionXml);
		} catch (EXIException | TransformerException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals(MAX_FIDELITY_DECODED_SIZE, maxFidelityDecodeOutput.getBytes().length);
		assertEquals(MAX_COMPRESSION_DECODED_SIZE, maxCompressionDecodeOutput.getBytes().length);
	}

	@Test
	void encodeFromStream() {
		byte[] maxFidelityEncodeOutput = new byte[0];
		byte[] maxCompressionEncodeOutput = new byte[0];

		try {
			maxFidelityEncodeOutput = maxFidelityInstance.encodeFromStream(testUnencodedXmlInputStream);
			testUnencodedXmlInputStream.reset();
			maxCompressionEncodeOutput = maxCompressionInstance.encodeFromStream(testUnencodedXmlInputStream);
		} catch (EXIException | IOException | SAXException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals(MAX_FIDELITY_ENCODED_SIZE, maxFidelityEncodeOutput.length);
		assertEquals(MAX_COMPRESSION_ENCODED_SIZE, maxCompressionEncodeOutput.length);
	}

	@Test
	void decodeFromStream() {
		String maxFidelityDecodeOutput = "";
		String maxCompressionDecodeOutput = "";
		try {
			maxFidelityDecodeOutput = decodeInstance.decodeFromStream(testEncodedMaxFidelityInputStream);
			maxCompressionDecodeOutput = decodeInstance.decodeFromStream(testEncodedMaxCompressionInputStream);
		} catch (EXIException | TransformerException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals(MAX_FIDELITY_DECODED_SIZE, maxFidelityDecodeOutput.getBytes().length);
		assertEquals(MAX_COMPRESSION_DECODED_SIZE, maxCompressionDecodeOutput.getBytes().length);
	}

	@Test
	void encodeFromFile() {
		byte[] maxFidelityEncodeOutput = new byte[0];
		byte[] maxCompressionEncodeOutput = new byte[0];
		try {
			maxFidelityEncodeOutput = maxFidelityInstance.encodeFromFile(testUnencodeXmlFile);
			maxCompressionEncodeOutput = maxCompressionInstance.encodeFromFile(testUnencodeXmlFile);
		} catch (EXIException | IOException | SAXException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals(MAX_FIDELITY_ENCODED_SIZE, maxFidelityEncodeOutput.length);
		assertEquals(MAX_COMPRESSION_ENCODED_SIZE, maxCompressionEncodeOutput.length);
	}

	@Test
	void encodeFromFile1() {
		byte[] maxFidelityEncodeOutput = new byte[0];
		byte[] maxCompressionEncodeOutput = new byte[0];
		try {
			maxFidelityEncodeOutput = maxFidelityInstance.encodeFromFile(testUnencodedXmlFilePath);
			maxCompressionEncodeOutput = maxCompressionInstance.encodeFromFile(testUnencodedXmlFilePath);
		} catch (EXIException | IOException | SAXException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals(MAX_FIDELITY_ENCODED_SIZE, maxFidelityEncodeOutput.length);
		assertEquals(MAX_COMPRESSION_ENCODED_SIZE, maxCompressionEncodeOutput.length);
	}

	@Test
	void decodeFromFile() {
		String maxFidelityDecodeOutput = "";
		String maxCompressionDecodeOutput = "";
		try {
			maxFidelityDecodeOutput = decodeInstance.decodeFromFile(testEncodedMaxFidelityXmlFilePath);
			maxCompressionDecodeOutput = decodeInstance.decodeFromFile(testEncodedMaxCompressionXmlFilePath);
		} catch (EXIException | IOException | TransformerException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals(MAX_FIDELITY_DECODED_SIZE, maxFidelityDecodeOutput.getBytes().length);
		assertEquals(MAX_COMPRESSION_DECODED_SIZE, maxCompressionDecodeOutput.getBytes().length);
	}

	@Test
	void decodeFromFile1() {
		String maxFidelityDecodeOutput = "";
		String maxCompressionDecodeOutput = "";
		try {
			maxFidelityDecodeOutput = decodeInstance.decodeFromFile(testEncodedMaxFidelittyXmlFile);
			maxCompressionDecodeOutput = decodeInstance.decodeFromFile(testEncodedMaxCompressionXmlFile);
		} catch (EXIException | IOException | TransformerException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals(MAX_FIDELITY_DECODED_SIZE, maxFidelityDecodeOutput.getBytes().length);
		assertEquals(MAX_COMPRESSION_DECODED_SIZE, maxCompressionDecodeOutput.getBytes().length);
	}
}