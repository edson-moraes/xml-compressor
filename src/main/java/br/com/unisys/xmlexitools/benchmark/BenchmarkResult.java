package br.com.unisys.xmlexitools.benchmark;

import java.util.*;
import java.util.function.BinaryOperator;

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
            int resultCount = resultList.size();
            Optional<BenchmarkEntryResult> reduce = resultList.stream()
                    .reduce((x, y) -> new BenchmarkEntryResult(new BenchmarkEntry(),
                            x.getUncompressedSize() / resultCount + y.getUncompressedSize() / resultCount,
                            x.getCompressedSize() / resultCount + y.getCompressedSize() / resultCount,
                            x.getDecompressedSize() / resultCount + y.getDecompressedSize() / resultCount,
                            x.getCompressionTime() / resultCount + y.getCompressionTime() / resultCount,
                            x.getDecompressionTime() / resultCount + y.getDecompressionTime() / resultCount,
                            x.getEditDistance() / resultCount + y.getEditDistance() / resultCount));

            BenchmarkEntryResult mean = reduce.get();
            BenchmarkEntry be = resultList.get(0).getBenchmarkEntry();

            String resultString = new StringJoiner("\n\t", be.getLogDescription() + " (" + be.getLogCode() + ") {\n\t", "\n}")
                    .add("Mean Uncompressed Size=" + mean.getUncompressedSize())
                    .add("Mean Compressed Size=" + mean.getCompressedSize())
                    .add("Mean Decompressed Size=" + mean.getDecompressedSize())
                    .add("Mean Compression Time=" + mean.getCompressionTime())
                    .add("Mean Decompression Time=" + mean.getDecompressionTime())
                    .add("Mean Edit Distance=" + mean.getEditDistance())
                    .toString();

            System.out.println(resultString);
        }
    }

}
