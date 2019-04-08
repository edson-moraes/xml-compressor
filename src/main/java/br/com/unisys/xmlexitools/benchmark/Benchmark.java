package br.com.unisys.xmlexitools.benchmark;

import br.com.unisys.xmlexitools.XmlCompressor;
import com.siemens.ct.exi.core.exceptions.EXIException;
import com.siemens.ct.exi.core.exceptions.UnsupportedOption;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Benchmark {

	private static List<BenchmarkEntry> benchmarkEntries;
	private static XmlCompressor maxFidelityInstance;
	private static XmlCompressor maxCompressionInstance;

	static {
		JSONParser jsonParser = new JSONParser();

		try (FileReader reader = new FileReader("src/main/resources/data_dump.json")) {

			Object obj = jsonParser.parse(reader);
			JSONArray employeeList = (JSONArray) obj;
			employeeList.forEach(emp -> benchmarkEntries.add(parseEmployeeObject((JSONObject) emp)));

			maxFidelityInstance = XmlCompressor.getMaxFidelityInstance();
			maxCompressionInstance = XmlCompressor.getMaxCompressionInstance();

		} catch (IOException | ParseException | UnsupportedOption e) {
			e.printStackTrace();
		}
	}

	private static BenchmarkEntry parseEmployeeObject(JSONObject entry) {

		String descricaoLog = (String) entry.get("TPLG_DSC_LOG");
		Long codigoLog = (Long) entry.get("TPLG_COD_LOG");
		String conteudo = (String) entry.get("AUMI_DSC_CONTEUDO_ENTRADA");

		return new BenchmarkEntry(descricaoLog, codigoLog, conteudo);
	}

	private static ResultadoBenchmark runBenchmarkForEntry(XmlCompressor xmlCompressor, BenchmarkEntry benchmarkEntry) {
		//Medir:
		// Tamanho Inicial (Sem Compressao)
		// Tamanho Comprimido (Com Compressao)
		// Tamanho Descomprimido
		// Distancia de Edicao
		// Tempo de Execucao

		long uncompressedSize;
		long compressedSize;
		long decompressedSize;
		long compressionTime;
		long decompressionTime;
		long editDistance;

//        ResultadoBenchmark resultadoBenchmark = new ResultadoBenchmark();

		StopWatch watch = new StopWatch();

		String conteudo = benchmarkEntry.getConteudo();
		uncompressedSize = conteudo.getBytes().length;
		try {
			watch.start();
			byte[] encodedData = xmlCompressor.encodeFromString(conteudo);
			watch.stop();
			compressionTime = watch.getTime(TimeUnit.MILLISECONDS);
			compressedSize = encodedData.length;
			watch.reset();

			watch.start();
			String decompressedString = xmlCompressor.decodeFromByteArray(encodedData);
			watch.stop();
			decompressionTime = watch.getTime(TimeUnit.MILLISECONDS);
			decompressedSize = decompressedString.getBytes(Charset.forName("ASCII")).length;
			watch.reset();

			LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();
			editDistance = levenshteinDistance.apply(conteudo, decompressedString);

			return new ResultadoBenchmark(uncompressedSize, compressedSize, decompressedSize, compressionTime, decompressionTime, editDistance);

		} catch (IOException | EXIException | SAXException | TransformerException e) {
			return null;
		}
	}


	public static void runBenchmark() {
		for (BenchmarkEntry be : benchmarkEntries) {

		}

	}

}
