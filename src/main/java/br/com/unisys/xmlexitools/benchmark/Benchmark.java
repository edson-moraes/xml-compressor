package br.com.unisys.xmlexitools.benchmark;

import br.com.unisys.xmlexitools.XmlCompressor;
import com.siemens.ct.exi.core.exceptions.EXIException;
import com.siemens.ct.exi.core.exceptions.UnsupportedOption;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

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

        long uncompressedSize;
        long compressedSize;
        long decompressedSize;
        double compressionTime;
        double decompressionTime;
        double editDistance;

//        ResultadoBenchmark resultadoBenchmark = new ResultadoBenchmark();

        String conteudo = benchmarkEntry.getConteudo();
        byte[] uncompressedData = conteudo.getBytes();
        try {
            xmlCompressor.encodeFromString(conteudo);
        } catch (IOException | EXIException | SAXException e) {

        }

        //Medir:
        // Tamanho Inicial (Sem Compressao)
        // Tamanho Comprimido (Com Compressao)
        // Tamanho Descomprimido
        // Distancia de Edicao
        // Tempo de Execucao
        return null;

    }


    public static void runBenchmark() {
        for (BenchmarkEntry be : benchmarkEntries) {

        }

    }

}
