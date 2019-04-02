package br.com.unisys.xmlexitools;

import com.siemens.ct.exi.core.exceptions.EXIException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Edson Moraes - Edson.MoraesJunior@unisys.com
 */
public class XmlCompressorTest {

	private final int UNENCODED_SIZE = 1;
	private final int MAX_COMPRESSION_ENCODED_SIZE = 1;
	private final int MAX_COMPRESSION_DECODED_SIZE = 1;
	private final int MAX_FIDELITY_ENCODED_SIZE = 1;
	private final int MAX_FIDELITY_DECODED_SIZE = 1;

	private File testXmlFile;
	private InputStream testXmlInputStream;
	private String testXml;
	private XmlCompressor maxCompressionInstance;
	private XmlCompressor maxFidelityInstance;


	@BeforeAll
	void setUp() {

		maxCompressionInstance = XmlCompressor.getMaxCompressionInstance();
		maxFidelityInstance = XmlCompressor.getMaxFidelityInstance();

		testXmlFile = new File("input.xml");
		try (FileInputStream testInputStream = new FileInputStream(testXmlFile)) {
			this.testXmlInputStream = testInputStream;
			this.testXml = testXmlInputStream.toString();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void encodeFromString() {
		String maxFidelityOutput = "";
		String maxCompressionOutput = "";
		try {
			maxFidelityOutput = maxFidelityInstance.encodeFromString(testXml);
			maxCompressionOutput = maxCompressionInstance.encodeFromString(testXml);
		} catch (EXIException | IOException | SAXException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals(MAX_FIDELITY_ENCODED_SIZE, maxFidelityOutput.getBytes().length);
		assertEquals(MAX_COMPRESSION_ENCODED_SIZE, maxCompressionOutput.getBytes().length);
	}

	@Test
	public void decodeFromString() {
	}

	@Test
	public void encodeFromStream() {
	}

	@Test
	public void decodeFromStream() {
	}

	@Test
	public void encodeFromFile() {
	}

	@Test
	public void encodeFromFile1() {
	}

	@Test
	public void decodeFromFile() {
	}

	@Test
	public void decodeFromFile1() {
	}
}