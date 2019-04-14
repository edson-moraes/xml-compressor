package br.com.unisys.xmlexitools.benchmark;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.util.*;
import java.util.stream.Collectors;

public class BenchmarkResult {

    private final String FRAME_START = "\\begin{frame}\n" +
            "\t\\frametitle{\\small{%s}}\n" +
            "\t\\begin{figure}[ht]\n" +
            "\t\\centering\n" +
            "\t\\resizebox {.75\\textwidth} {!} {\n" +
            "\t\t\\begin{tikzpicture}\n" +
            "\t\t\t\\begin{axis} [\n" +
            "\t\t\t\t\t\t\tybar stacked, bar width=1, xmin=0, xmax=%d, ymin= 0,\n" +
            "\t\t\t\t\t\t\tylabel=Tamanho(bytes), scaled y ticks = false, \n" +
            "\t\t\t\t\t\t\tlegend style={area legend, at={(0.5,-0.15)}, anchor=north, legend columns=-1 } \n" +
            "\t\t\t\t\t\t ]\n\t\t\t\t";

    private final String FRAME_END = "\n\t\t\t\t\\legend{EXI, GZIP, Uncompressed}\n\t\t\t\\end{axis}\n" +
            "\t\t\\end{tikzpicture}\n" +
            "\t}\n" +
            "\t\\end{figure}\n" +
            "\\end{frame}\n";

    private final String PLOT_CORDINATES_START = "\\addplot coordinates {";
    private final String PLOT_CORDINATES_END = "};";

    private final String TABLE_START = "\\begin{frame}\n" +
            "\\frametitle{\\small{%s}}\n" +
            "\\begin{table}[]\n" +
            "\\small\n" +
            "\\centering\n" +
            "\\begin{tabular}{l|l|l|l|l|}\n" +
            "                                & Min.    & Max.    & Mean    & SD     \\\\ \\hline\n";
    private final String TABLE_FINAL = "\n\\end{tabular}\n" +
            "\\end{table}\n" +
            "\\end{frame}";

    private HashMap<Long, List<BenchmarkEntryResult>> results;

    public BenchmarkResult() {
        results = new HashMap<>();
    }

    public void addBenchmarkResult(BenchmarkEntryResult benchmarkEntryResult) {
        if (benchmarkEntryResult != null) {
            List<BenchmarkEntryResult> benchmarkEntryResults = results.get(benchmarkEntryResult.getBenchmarkEntry().getLogCode());
            if (benchmarkEntryResults == null) {
                benchmarkEntryResults = new ArrayList<>();
                results.put(benchmarkEntryResult.getBenchmarkEntry().getLogCode(), benchmarkEntryResults);
            }
            benchmarkEntryResults.add(benchmarkEntryResult);
        }
    }

    public void printResults() {
        Set<Long> keys = results.keySet();
        for (Long key : keys) {
            List<BenchmarkEntryResult> resultList = results.get(key);
            BenchmarkEntry be = resultList.get(0).getBenchmarkEntry();

            printLatexPlots(resultList, be.getLogDescription(), be.getLogCode());
            printLatexTables(resultList, be.getLogDescription(), be.getLogCode());

        }
    }

    public void printLatexPlots(List<BenchmarkEntryResult> resultList, String logDescription, Long logCode) {

            StringJoiner latexPlot = new StringJoiner("\n\t\t\t\t", String.format(FRAME_START, (logDescription + "(Codigo Log: " + logCode + ")"), (resultList.size() + 1)), FRAME_END);
            StringJoiner plotCordinates = new StringJoiner(" ", PLOT_CORDINATES_START, PLOT_CORDINATES_END);


            List<Double> compressedSize = resultList.stream().map(BenchmarkEntryResult::getCompressedSize).collect(Collectors.toList());
            List<Double> gzipCompressedSize = resultList.stream().map(BenchmarkEntryResult::getGzipCompressedSize).collect(Collectors.toList());
            List<Double> uncompressedSize = resultList.stream().map(BenchmarkEntryResult::getUncompressedSize).collect(Collectors.toList());

            for (int i = 0; i < compressedSize.size(); i++) {
                plotCordinates.add("(" + (i + 1) + "," + compressedSize.get(i) + ")");
            }
            latexPlot.add(plotCordinates.toString());

            plotCordinates = new StringJoiner(" ", PLOT_CORDINATES_START, PLOT_CORDINATES_END);
            for (int i = 0; i < gzipCompressedSize.size(); i++) {
                plotCordinates.add("(" + (i + 1) + "," + gzipCompressedSize.get(i) + ")");
            }
            latexPlot.add(plotCordinates.toString());

            plotCordinates = new StringJoiner(" ", PLOT_CORDINATES_START, PLOT_CORDINATES_END);
            for (int i = 0; i < uncompressedSize.size(); i++) {
                plotCordinates.add("(" + (i + 1) + "," + uncompressedSize.get(i) + ")");
            }
            latexPlot.add(plotCordinates.toString());

            System.out.println();
            System.out.println(latexPlot.toString());
            System.out.println();

    }

    private void printLatexTables(List<BenchmarkEntryResult> resultList, String logDescription, Long logCode) {
        StringJoiner tablePlot = new StringJoiner("\n", String.format(TABLE_START, (logDescription + "(Codigo Log: " + logCode + ")")), TABLE_FINAL);
        double resultCount = resultList.size();
        final String TABLE_ENTRY = "%32s & %10.3f & %10.3f & %10.3f & %10.3f \\\\ \\hline";

        double meanUncompressedSize, meanCompressedSize, meanGzipCompressedSize, meanDecompressedSize,
                meanCompressionTime, meanDecompressionTime, meanSpaceSaving;

        double maxUncompressedSize, maxCompressedSize, maxGzipCompressedSize, maxDecompressedSize,
                maxCompressionTime, maxDecompressionTime, maxSpaceSaving;

        double minUncompressedSize, minCompressedSize, minGzipCompressedSize, minDecompressedSize,
                minCompressionTime, minDecompressionTime, minSpaceSaving;

        double sdUncompressedSize, sdCompressedSize, sdGzipCompressedSize, sdDecompressedSize,
                sdCompressionTime, sdDecompressionTime, sdSpaceSaving;

        meanUncompressedSize = resultList.stream().mapToDouble(x -> x.getUncompressedSize()).sum() / resultCount;
        meanCompressedSize = resultList.stream().mapToDouble(x -> x.getCompressedSize()).sum() / resultCount;
        meanGzipCompressedSize = resultList.stream().mapToDouble(x -> x.getGzipCompressedSize()).sum() / resultCount;
        meanDecompressedSize = resultList.stream().mapToDouble(x -> x.getDecompressedSize()).sum() / resultCount;
        meanCompressionTime = resultList.stream().mapToDouble(x -> x.getCompressionTime()).sum() / resultCount;
        meanDecompressionTime = resultList.stream().mapToDouble(x -> x.getDecompressionTime()).sum() / resultCount;
        meanSpaceSaving = resultList.stream().mapToDouble(x -> x.getSpaceSaving()).sum() / resultCount;

        maxUncompressedSize = resultList.stream().mapToDouble(x -> x.getUncompressedSize()).max().getAsDouble();
        maxCompressedSize = resultList.stream().mapToDouble(x -> x.getCompressedSize()).max().getAsDouble();
        maxGzipCompressedSize = resultList.stream().mapToDouble(x -> x.getGzipCompressedSize()).max().getAsDouble();
        maxDecompressedSize = resultList.stream().mapToDouble(x -> x.getDecompressedSize()).max().getAsDouble();
        maxCompressionTime = resultList.stream().mapToDouble(x -> x.getCompressionTime()).max().getAsDouble();
        maxDecompressionTime = resultList.stream().mapToDouble(x -> x.getDecompressionTime()).max().getAsDouble();
        maxSpaceSaving = resultList.stream().mapToDouble(x -> x.getSpaceSaving()).max().getAsDouble();

        minUncompressedSize = resultList.stream().mapToDouble(x -> x.getUncompressedSize()).min().getAsDouble();
        minCompressedSize = resultList.stream().mapToDouble(x -> x.getCompressedSize()).min().getAsDouble();
        minGzipCompressedSize = resultList.stream().mapToDouble(x -> x.getGzipCompressedSize()).min().getAsDouble();
        minDecompressedSize = resultList.stream().mapToDouble(x -> x.getDecompressedSize()).min().getAsDouble();
        minCompressionTime = resultList.stream().mapToDouble(x -> x.getCompressionTime()).min().getAsDouble();
        minDecompressionTime = resultList.stream().mapToDouble(x -> x.getDecompressionTime()).min().getAsDouble();
        minSpaceSaving = resultList.stream().mapToDouble(x -> x.getSpaceSaving()).min().getAsDouble();

        StandardDeviation standardDeviation = new StandardDeviation();

        sdUncompressedSize = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getUncompressedSize()).toArray(), meanUncompressedSize);
        sdCompressedSize = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getCompressedSize()).toArray(), meanCompressedSize);
        sdGzipCompressedSize = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getGzipCompressedSize()).toArray(), meanCompressedSize);
        sdDecompressedSize = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getDecompressedSize()).toArray(), meanDecompressedSize);
        sdCompressionTime = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getCompressionTime()).toArray(), meanCompressionTime);
        sdDecompressionTime = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getDecompressionTime()).toArray(), meanDecompressionTime);
        sdSpaceSaving = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getSpaceSaving()).toArray(), meanSpaceSaving);

        tablePlot
                .add(String.format(TABLE_ENTRY, "Uncompressed Size(bytes)", minUncompressedSize, maxUncompressedSize, meanUncompressedSize, sdUncompressedSize))
                .add(String.format(TABLE_ENTRY, "Compressed Size(bytes)", minCompressedSize, maxCompressedSize, meanCompressedSize, sdCompressedSize))
                .add(String.format(TABLE_ENTRY, "GzipCompressed Size(bytes)", minGzipCompressedSize, maxGzipCompressedSize, meanGzipCompressedSize, sdGzipCompressedSize))
                .add(String.format(TABLE_ENTRY, "Decompressed Size(bytes)", minDecompressedSize, maxDecompressedSize, meanDecompressedSize, sdDecompressedSize))
                .add(String.format(TABLE_ENTRY, "Compression Time(ms)", minCompressionTime, maxCompressionTime, meanCompressionTime, sdCompressionTime))
                .add(String.format(TABLE_ENTRY, "Decompression Time(ms) ", minDecompressionTime, maxDecompressionTime, meanDecompressionTime, sdDecompressionTime))
                .add(String.format(TABLE_ENTRY, "Space Saving(\\%)", minSpaceSaving, maxSpaceSaving, meanSpaceSaving, sdSpaceSaving));

        System.out.println();
        System.out.println(tablePlot.toString());
        System.out.println();
    }
}