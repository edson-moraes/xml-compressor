package br.com.unisys.xmlexitools;

import com.siemens.ct.exi.core.exceptions.EXIException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Edson Moraes - Edson.MoraesJunior@unisys.com
 */
public class XmlCompressorTest {

    private final int UNENCODED_SIZE = 55219;
    private final int MAX_COMPRESSION_ENCODED_SIZE = 6639;
    private final int MAX_COMPRESSION_DECODED_SIZE = 1;
    private final int MAX_FIDELITY_ENCODED_SIZE = 14074;
    private final int MAX_FIDELITY_DECODED_SIZE = 1;

    private static File testXmlFile;
    private static InputStream testXmlInputStream;
    private static String testXml;
    private static XmlCompressor maxCompressionInstance;
    private static XmlCompressor maxFidelityInstance;


    @BeforeAll
    static void setUp() throws IOException {

        String fileName = "src/main/resources/input.xml";
        maxCompressionInstance = XmlCompressor.getMaxCompressionInstance();
        maxFidelityInstance = XmlCompressor.getMaxFidelityInstance();

        testXmlFile = new File(fileName);

        byte[] fileData = Files.readAllBytes(Paths.get(fileName));

        testXml = new String(fileData);
        testXmlInputStream = new ByteArrayInputStream(fileData);
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