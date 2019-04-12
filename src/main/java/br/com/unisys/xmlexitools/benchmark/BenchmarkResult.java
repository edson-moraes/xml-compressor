package br.com.unisys.xmlexitools.benchmark;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.util.*;
import java.util.stream.Collectors;

public class BenchmarkResult {

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
            double resultCount = resultList.size();

            double meanUncompressedSize = resultList.stream().mapToDouble(x -> x.getUncompressedSize()).sum() / resultCount;
            double meanCompressedSize = resultList.stream().mapToDouble(x -> x.getCompressedSize()).sum() / resultCount;
            double meanGzipCompressedSize = resultList.stream().mapToDouble(x -> x.getGzipCompressedSize()).sum() / resultCount;
            double meanDecompressedSize = resultList.stream().mapToDouble(x -> x.getDecompressedSize()).sum() / resultCount;
            double meanCompressionTime = resultList.stream().mapToDouble(x -> x.getCompressionTime()).sum() / resultCount;
            double meanDecompressionTime = resultList.stream().mapToDouble(x -> x.getDecompressionTime()).sum() / resultCount;
            double meanEditDistance = resultList.stream().mapToDouble(x -> x.getEditDistance()).sum() / resultCount;

            double maxUncompressedSize = resultList.stream().mapToDouble(x -> x.getUncompressedSize()).max().getAsDouble();
            double maxCompressedSize = resultList.stream().mapToDouble(x -> x.getCompressedSize()).max().getAsDouble();
            double maxGzipCompressedSize = resultList.stream().mapToDouble(x -> x.getGzipCompressedSize()).max().getAsDouble();
            double maxDecompressedSize = resultList.stream().mapToDouble(x -> x.getDecompressedSize()).max().getAsDouble();
            double maxCompressionTime = resultList.stream().mapToDouble(x -> x.getCompressionTime()).max().getAsDouble();
            double maxDecompressionTime = resultList.stream().mapToDouble(x -> x.getDecompressionTime()).max().getAsDouble();
            double maxEditDistance = resultList.stream().mapToDouble(x -> x.getEditDistance()).max().getAsDouble();

            double minUncompressedSize = resultList.stream().mapToDouble(x -> x.getUncompressedSize()).min().getAsDouble();
            double minCompressedSize = resultList.stream().mapToDouble(x -> x.getCompressedSize()).min().getAsDouble();
            double minGzipCompressedSize = resultList.stream().mapToDouble(x -> x.getGzipCompressedSize()).min().getAsDouble();
            double minDecompressedSize = resultList.stream().mapToDouble(x -> x.getDecompressedSize()).min().getAsDouble();
            double minCompressionTime = resultList.stream().mapToDouble(x -> x.getCompressionTime()).min().getAsDouble();
            double minDecompressionTime = resultList.stream().mapToDouble(x -> x.getDecompressionTime()).min().getAsDouble();
            double minEditDistance = resultList.stream().mapToDouble(x -> x.getEditDistance()).min().getAsDouble();

            StandardDeviation standardDeviation = new StandardDeviation();

            double sdUncompressedSize = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getUncompressedSize()).toArray(), meanUncompressedSize);
            double sdCompressedSize = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getCompressedSize()).toArray(), meanCompressedSize);
            double sdGzipCompressedSize = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getGzipCompressedSize()).toArray(), meanCompressedSize);
            double sdDecompressedSize = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getDecompressedSize()).toArray(), meanDecompressedSize);
            double sdCompressionTime = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getCompressionTime()).toArray(), meanCompressionTime);
            double sdDecompressionTime = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getDecompressionTime()).toArray(), meanDecompressionTime);
            double sdEditDistance = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getEditDistance()).toArray(), meanEditDistance);

            BenchmarkEntry be = resultList.get(0).getBenchmarkEntry();

            String resultString = new StringJoiner("\n\t", be.getLogDescription() + " (" + be.getLogCode() + ") {\n\t", "\n}")

                    .add(StringUtils.rightPad(String.format("Mean Uncompressed Size   = %10.3f bytes", meanUncompressedSize), 50, ' ') + String.format("(Min. %10.3f / Max. %10.3f / S.D. %10.3f)", minUncompressedSize, maxUncompressedSize, sdUncompressedSize))
                    .add(StringUtils.rightPad(String.format("Mean Compressed Size     = %10.3f bytes", meanCompressedSize), 50, ' ') + String.format("(Min. %10.3f / Max. %10.3f / S.D. %10.3f)", minCompressedSize, maxCompressedSize, sdCompressedSize))
                    .add(StringUtils.rightPad(String.format("Mean GzipCompressed Size = %10.3f bytes", meanGzipCompressedSize), 50, ' ') + String.format("(Min. %10.3f / Max. %10.3f / S.D. %10.3f)", minGzipCompressedSize, maxGzipCompressedSize, sdGzipCompressedSize))
                    .add(StringUtils.rightPad(String.format("Mean Decompressed Size   = %10.3f bytes", meanDecompressedSize), 50, ' ') + String.format("(Min. %10.3f / Max. %10.3f / S.D. %10.3f)", minDecompressedSize, maxDecompressedSize, sdDecompressedSize))
                    .add(StringUtils.rightPad(String.format("Mean Compression Time    = %10.3f ms", meanCompressionTime), 50, ' ') + String.format("(Min. %10.3f / Max. %10.3f / S.D. %10.3f)", minCompressionTime, maxCompressionTime, sdCompressionTime))
                    .add(StringUtils.rightPad(String.format("Mean Decompression Time  = %10.3f ms", meanDecompressionTime), 50, ' ') + String.format("(Min. %10.3f / Max. %10.3f / S.D. %10.3f)", minDecompressionTime, maxDecompressionTime, sdDecompressionTime))
                    .add(StringUtils.rightPad(String.format("Mean Edit Distance       = %10.3f", meanEditDistance), 50, ' ') + String.format("(Min. %10.3f / Max. %10.3f / S.D. %10.3f)", minEditDistance, maxEditDistance, sdEditDistance))
                    .toString();

            System.out.println(resultString);
            System.out.println();
        }
    }

    public void genLatexPlots() {

        for (Long key : results.keySet()) {

            List<BenchmarkEntryResult> resultList = results.get(key);

            StringJoiner latexPlot = new StringJoiner("\n\t", "\\begin{tikzpicture}\n\t\\begin{axis}[ybar stacked, bar width=1, xmin=0, xmax=" + (resultList.size() + 1) + ", ymin= 0, ylabel=Tamanho(bytes), scaled y ticks = false, legend style={area legend, at={(0.5,-0.15)}, anchor=north, legend columns=-1 } ]\n\t", "\t\\legend{EXI, GZIP, Uncompressed}\n\t\\end{axis}\n\\end{tikzpicture}");
            StringJoiner plotCordinates = new StringJoiner(" ", "\\addplot coordinates\n\t{", "};");

            List<Double> compressedSize = resultList.stream().map(BenchmarkEntryResult::getCompressedSize).collect(Collectors.toList());
            List<Double> gzipCompressedSize = resultList.stream().map(BenchmarkEntryResult::getGzipCompressedSize).collect(Collectors.toList());
            List<Double> uncompressedSize = resultList.stream().map(BenchmarkEntryResult::getUncompressedSize).collect(Collectors.toList());

            for (int i = 0; i < compressedSize.size(); i++) {
                plotCordinates.add("(" + (i + 1) + "," + compressedSize.get(i) + ")");
            }
            latexPlot.add(plotCordinates.toString());

            plotCordinates = new StringJoiner(" ", "\\addplot coordinates\n\t{", "};");
            for (int i = 0; i < gzipCompressedSize.size(); i++) {
                plotCordinates.add("(" + (i + 1) + "," + gzipCompressedSize.get(i) + ")");
            }
            latexPlot.add(plotCordinates.toString());

            plotCordinates = new StringJoiner(" ", "\\addplot coordinates\n\t{", "};");
            for (int i = 0; i < uncompressedSize.size(); i++) {
                plotCordinates.add("(" + (i + 1) + "," + uncompressedSize.get(i) + ")");
            }
            latexPlot.add(plotCordinates.toString());

            System.out.println();
            System.out.println(latexPlot.toString());
            System.out.println();
        }
    }

}
