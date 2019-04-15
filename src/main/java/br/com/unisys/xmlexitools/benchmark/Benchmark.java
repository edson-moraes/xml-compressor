package br.com.unisys.xmlexitools.benchmark;

import br.com.unisys.xmlexitools.XmlCompressor;
import com.siemens.ct.exi.core.exceptions.EXIException;
import com.siemens.ct.exi.core.exceptions.UnsupportedOption;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Benchmark {

	private static List<BenchmarkEntry> benchmarkEntries;
	private static XmlCompressor maxFidelityInstance;
	private static XmlCompressor maxCompressionInstance;
	private static BenchmarkResult benchmarkResult;

	static {
		JSONParser jsonParser = new JSONParser();

		try (FileReader reader = new FileReader("src/main/resources/data_dump.json")) {

			benchmarkEntries = new ArrayList<>();

			Object obj = jsonParser.parse(reader);
			JSONArray employeeList = (JSONArray) obj;
			for (Object jsonObject : employeeList) {
				BenchmarkEntry benchmarkEntry = parseEmployeeObject((JSONObject) jsonObject);
				if (benchmarkEntry != null) {
					benchmarkEntries.add(benchmarkEntry);
				}
			}

			maxFidelityInstance = XmlCompressor.getMaxFidelityInstance();
			maxCompressionInstance = XmlCompressor.getMaxCompressionInstance();

			benchmarkResult = new BenchmarkResult();

		} catch (IOException | ParseException | UnsupportedOption e) {
			e.printStackTrace();
		}
	}

	private static BenchmarkEntry parseEmployeeObject(JSONObject entry) {

		String logDescription = (String) entry.get("TPLG_DSC_LOG");
		Long logCode = (Long) entry.get("TPLG_COD_LOG");
		String content = (String) entry.get("AUMI_DSC_CONTEUDO_ENTRADA");

		if (logCode <= 1L) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				dBuilder.setErrorHandler(null);
				dBuilder.parse(new InputSource(new ByteArrayInputStream(content.getBytes(StandardCharsets.US_ASCII))));
			} catch (SAXException | ParserConfigurationException | IOException e) {
				return null;
			}

			return new BenchmarkEntry(logDescription, logCode, content);
		} else {
			return null;
		}
	}

	private static BenchmarkEntryResult runBenchmarkForEntry(XmlCompressor xmlCompressor, BenchmarkEntry benchmarkEntry) {

		long uncompressedSize;
		long compressedSize;
		long decompressedSize;
		double compressionTime;
		double decompressionTime;
		long editDistance;
		double gzipCompressedSize;

		StopWatch watch = new StopWatch();

		String conteudo = benchmarkEntry.getContent();
		uncompressedSize = conteudo.getBytes().length;
		try {
			watch.start();
			byte[] encodedData = xmlCompressor.encodeFromString(conteudo);
			watch.stop();

			compressionTime = watch.getTime(TimeUnit.MICROSECONDS) / 1000.0d;
			watch.reset();

			compressedSize = encodedData.length;

			watch.start();
			String decompressedString = xmlCompressor.decodeFromByteArray(encodedData);
			watch.stop();

			decompressionTime = watch.getTime(TimeUnit.MICROSECONDS) / 1000.0d;
			watch.reset();

			ByteArrayOutputStream gzipCompressed = new ByteArrayOutputStream();
			compressGZIP(new ByteArrayInputStream(conteudo.getBytes()), gzipCompressed);
			gzipCompressedSize = gzipCompressed.toByteArray().length;

			decompressedSize = decompressedString.getBytes(Charset.forName("ASCII")).length;

			LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();
			editDistance = levenshteinDistance.apply(conteudo, decompressedString);

			return new BenchmarkEntryResult(benchmarkEntry, uncompressedSize, compressedSize, gzipCompressedSize, decompressedSize, compressionTime, decompressionTime, editDistance);

		} catch (IOException | EXIException | SAXException | TransformerException e) {
			return null;
		}
	}

	public static void compressGZIP(InputStream inputStream, OutputStream outputStream) throws IOException {
		try (GzipCompressorOutputStream out = new GzipCompressorOutputStream(outputStream)) {
			IOUtils.copy(inputStream, out);
		}
	}

	public static void decompressGZIP(InputStream inputStream, OutputStream outputStream) throws IOException {
		try (GzipCompressorInputStream in = new GzipCompressorInputStream(inputStream)) {
			IOUtils.copy(in, outputStream);
		}
	}


	public static void runBenchmark() {
		BenchmarkEntry current = new BenchmarkEntry();
		BenchmarkEntry previous;
		for (BenchmarkEntry be : benchmarkEntries) {
			previous = current;
			current = be;
			if (!Objects.equals(current.getLogCode(), previous.getLogCode())) {
				System.out.println("Executando benchmark para " + current.getLogDescription() + " (Codigo de log: " + current.getLogCode() + ")...");
			}
			benchmarkResult.addBenchmarkResult(runBenchmarkForEntry(maxCompressionInstance, be));
		}
		System.out.println();

	}

	public static void main(String[] args) {
		runBenchmark();
		benchmarkResult.printResults();
	}

}
