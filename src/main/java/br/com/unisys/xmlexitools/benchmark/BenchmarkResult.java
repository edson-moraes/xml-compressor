package br.com.unisys.xmlexitools.benchmark;

import org.apache.commons.lang3.StringUtils;
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

	private final String TABLE_START = "\\begin{frame}{%s}\n" +
			"\\begin{table}[]\n" +
			"\\small\n" +
			"\\centering\n" +
			"\\begin{tabular}{l|l|l|l|l|}\n" +
			"                                & Min.    & Max.    & Mean    & SD     \\\\ \\hline";
	private final String TABLE_FINAL = "\\end{tabular}\n" +
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
			double resultCount = resultList.size();

			double meanUncompressedSize = resultList.stream().mapToDouble(x -> x.getUncompressedSize()).sum() / resultCount;
			double meanCompressedSize = resultList.stream().mapToDouble(x -> x.getCompressedSize()).sum() / resultCount;
			double meanGzipCompressedSize = resultList.stream().mapToDouble(x -> x.getGzipCompressedSize()).sum() / resultCount;
			double meanDecompressedSize = resultList.stream().mapToDouble(x -> x.getDecompressedSize()).sum() / resultCount;
			double meanCompressionTime = resultList.stream().mapToDouble(x -> x.getCompressionTime()).sum() / resultCount;
			double meanDecompressionTime = resultList.stream().mapToDouble(x -> x.getDecompressionTime()).sum() / resultCount;
			double meanEditDistance = resultList.stream().mapToDouble(x -> x.getEditDistance()).sum() / resultCount;
			double meanSpaceSaving = resultList.stream().mapToDouble(x -> x.getSpaceSaving()).sum() / resultCount;

			double maxUncompressedSize = resultList.stream().mapToDouble(x -> x.getUncompressedSize()).max().getAsDouble();
			double maxCompressedSize = resultList.stream().mapToDouble(x -> x.getCompressedSize()).max().getAsDouble();
			double maxGzipCompressedSize = resultList.stream().mapToDouble(x -> x.getGzipCompressedSize()).max().getAsDouble();
			double maxDecompressedSize = resultList.stream().mapToDouble(x -> x.getDecompressedSize()).max().getAsDouble();
			double maxCompressionTime = resultList.stream().mapToDouble(x -> x.getCompressionTime()).max().getAsDouble();
			double maxDecompressionTime = resultList.stream().mapToDouble(x -> x.getDecompressionTime()).max().getAsDouble();
			double maxEditDistance = resultList.stream().mapToDouble(x -> x.getEditDistance()).max().getAsDouble();
			double maxSpaceSaving = resultList.stream().mapToDouble(x -> x.getSpaceSaving()).max().getAsDouble();

			double minUncompressedSize = resultList.stream().mapToDouble(x -> x.getUncompressedSize()).min().getAsDouble();
			double minCompressedSize = resultList.stream().mapToDouble(x -> x.getCompressedSize()).min().getAsDouble();
			double minGzipCompressedSize = resultList.stream().mapToDouble(x -> x.getGzipCompressedSize()).min().getAsDouble();
			double minDecompressedSize = resultList.stream().mapToDouble(x -> x.getDecompressedSize()).min().getAsDouble();
			double minCompressionTime = resultList.stream().mapToDouble(x -> x.getCompressionTime()).min().getAsDouble();
			double minDecompressionTime = resultList.stream().mapToDouble(x -> x.getDecompressionTime()).min().getAsDouble();
			double minEditDistance = resultList.stream().mapToDouble(x -> x.getEditDistance()).min().getAsDouble();
			double minSpaceSaving = resultList.stream().mapToDouble(x -> x.getSpaceSaving()).min().getAsDouble();

			StandardDeviation standardDeviation = new StandardDeviation();

			double sdUncompressedSize = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getUncompressedSize()).toArray(), meanUncompressedSize);
			double sdCompressedSize = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getCompressedSize()).toArray(), meanCompressedSize);
			double sdGzipCompressedSize = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getGzipCompressedSize()).toArray(), meanCompressedSize);
			double sdDecompressedSize = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getDecompressedSize()).toArray(), meanDecompressedSize);
			double sdCompressionTime = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getCompressionTime()).toArray(), meanCompressionTime);
			double sdDecompressionTime = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getDecompressionTime()).toArray(), meanDecompressionTime);
			double sdEditDistance = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getEditDistance()).toArray(), meanEditDistance);
			double sdSpaceSaving = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getSpaceSaving()).toArray(), meanEditDistance);

			BenchmarkEntry be = resultList.get(0).getBenchmarkEntry();

			String resultString = new StringJoiner("\n\t", be.getLogDescription() + " (" + be.getLogCode() + ") {\n\t", "\n}")

					.add(StringUtils.rightPad(String.format("Mean Uncompressed Size   = %10.3f bytes", meanUncompressedSize), 50, ' ') + String.format("(Min. %10.3f / Max. %10.3f / S.D. %10.3f)", minUncompressedSize, maxUncompressedSize, sdUncompressedSize))
					.add(StringUtils.rightPad(String.format("Mean Compressed Size     = %10.3f bytes", meanCompressedSize), 50, ' ') + String.format("(Min. %10.3f / Max. %10.3f / S.D. %10.3f)", minCompressedSize, maxCompressedSize, sdCompressedSize))
					.add(StringUtils.rightPad(String.format("Mean GzipCompressed Size = %10.3f bytes", meanGzipCompressedSize), 50, ' ') + String.format("(Min. %10.3f / Max. %10.3f / S.D. %10.3f)", minGzipCompressedSize, maxGzipCompressedSize, sdGzipCompressedSize))
					.add(StringUtils.rightPad(String.format("Mean Decompressed Size   = %10.3f bytes", meanDecompressedSize), 50, ' ') + String.format("(Min. %10.3f / Max. %10.3f / S.D. %10.3f)", minDecompressedSize, maxDecompressedSize, sdDecompressedSize))
					.add(StringUtils.rightPad(String.format("Mean Compression Time    = %10.3f ms", meanCompressionTime), 50, ' ') + String.format("(Min. %10.3f / Max. %10.3f / S.D. %10.3f)", minCompressionTime, maxCompressionTime, sdCompressionTime))
					.add(StringUtils.rightPad(String.format("Mean Decompression Time  = %10.3f ms", meanDecompressionTime), 50, ' ') + String.format("(Min. %10.3f / Max. %10.3f / S.D. %10.3f)", minDecompressionTime, maxDecompressionTime, sdDecompressionTime))
					.add(StringUtils.rightPad(String.format("Mean Edit Distance       = %10.3f", meanEditDistance), 50, ' ') + String.format("(Min. %10.3f / Max. %10.3f / S.D. %10.3f)", minEditDistance, maxEditDistance, sdEditDistance))
					.add(StringUtils.rightPad(String.format("Mean Space Savings       = %10.3f", meanSpaceSaving), 50, ' ') + String.format("(Min. %10.3f / Max. %10.3f / S.D. %10.3f)", minSpaceSaving, maxSpaceSaving, sdSpaceSaving))
					.toString();

			System.out.println(resultString);
			System.out.println();
		}
	}

	public void genLatexPlots() {

		for (Long key : results.keySet()) {

			List<BenchmarkEntryResult> resultList = results.get(key);
			BenchmarkEntry be = resultList.get(0).getBenchmarkEntry();
			StringJoiner latexPlot = new StringJoiner("\n\t\t\t\t", String.format(FRAME_START, (be.getLogDescription() + "(Codigo Log: " + be.getLogCode() + ")"), (resultList.size() + 1)), FRAME_END);
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
	}

	public void genLatexTables() {
		final String TABLE_ENTRY = "%s & %f & %f & %f & %f \\\\hline";

		double meanUncompressedSize = 0.0d;
		double meanCompressedSize = 0.0d;
		double meanGzipCompressedSize = 0.0d;
		double meanDecompressedSize = 0.0d;
		double meanCompressionTime = 0.0d;
		double meanDecompressionTime = 0.0d;
		double meanEditDistance = 0.0d;
		double meanSpaceSaving = 0.0d;

		double maxUncompressedSize = 0.0d;
		double maxCompressedSize = 0.0d;
		double maxGzipCompressedSize = 0.0d;
		double maxDecompressedSize = 0.0d;
		double maxCompressionTime = 0.0d;
		double maxDecompressionTime = 0.0d;
		double maxEditDistance = 0.0d;
		double maxSpaceSaving = 0.0d;

		double minUncompressedSize = 0.0d;
		double minCompressedSize = 0.0d;
		double minGzipCompressedSize = 0.0d;
		double minDecompressedSize = 0.0d;
		double minCompressionTime = 0.0d;
		double minDecompressionTime = 0.0d;
		double minEditDistance = 0.0d;
		double minSpaceSaving = 0.0d;

		double sdUncompressedSize = 0.0d;
		double sdCompressedSize = 0.0d;
		double sdGzipCompressedSize = 0.0d;
		double sdDecompressedSize = 0.0d;
		double sdCompressionTime = 0.0d;
		double sdDecompressionTime = 0.0d;
		double sdEditDistance = 0.0d;
		double sdSpaceSaving = 0.0d;

		BenchmarkEntry be = results.get(1).get(0).getBenchmarkEntry();
		StringJoiner tablePlot = new StringJoiner("\n", String.format(TABLE_START, (be.getLogDescription() + "(Codigo Log: " + be.getLogCode() + ")")), TABLE_FINAL);

		for (Long key : results.keySet()) {
			List<BenchmarkEntryResult> resultList = results.get(key);
			be = resultList.get(0).getBenchmarkEntry();
			double resultCount = resultList.size();

			meanUncompressedSize = resultList.stream().mapToDouble(x -> x.getUncompressedSize()).sum() / resultCount;
			meanCompressedSize = resultList.stream().mapToDouble(x -> x.getCompressedSize()).sum() / resultCount;
			meanGzipCompressedSize = resultList.stream().mapToDouble(x -> x.getGzipCompressedSize()).sum() / resultCount;
			meanDecompressedSize = resultList.stream().mapToDouble(x -> x.getDecompressedSize()).sum() / resultCount;
			meanCompressionTime = resultList.stream().mapToDouble(x -> x.getCompressionTime()).sum() / resultCount;
			meanDecompressionTime = resultList.stream().mapToDouble(x -> x.getDecompressionTime()).sum() / resultCount;
			meanEditDistance = resultList.stream().mapToDouble(x -> x.getEditDistance()).sum() / resultCount;
			meanSpaceSaving = resultList.stream().mapToDouble(x -> x.getSpaceSaving()).sum() / resultCount;

			maxUncompressedSize = resultList.stream().mapToDouble(x -> x.getUncompressedSize()).max().getAsDouble();
			maxCompressedSize = resultList.stream().mapToDouble(x -> x.getCompressedSize()).max().getAsDouble();
			maxGzipCompressedSize = resultList.stream().mapToDouble(x -> x.getGzipCompressedSize()).max().getAsDouble();
			maxDecompressedSize = resultList.stream().mapToDouble(x -> x.getDecompressedSize()).max().getAsDouble();
			maxCompressionTime = resultList.stream().mapToDouble(x -> x.getCompressionTime()).max().getAsDouble();
			maxDecompressionTime = resultList.stream().mapToDouble(x -> x.getDecompressionTime()).max().getAsDouble();
			maxEditDistance = resultList.stream().mapToDouble(x -> x.getEditDistance()).max().getAsDouble();
			maxSpaceSaving = resultList.stream().mapToDouble(x -> x.getSpaceSaving()).max().getAsDouble();

			minUncompressedSize = resultList.stream().mapToDouble(x -> x.getUncompressedSize()).min().getAsDouble();
			minCompressedSize = resultList.stream().mapToDouble(x -> x.getCompressedSize()).min().getAsDouble();
			minGzipCompressedSize = resultList.stream().mapToDouble(x -> x.getGzipCompressedSize()).min().getAsDouble();
			minDecompressedSize = resultList.stream().mapToDouble(x -> x.getDecompressedSize()).min().getAsDouble();
			minCompressionTime = resultList.stream().mapToDouble(x -> x.getCompressionTime()).min().getAsDouble();
			minDecompressionTime = resultList.stream().mapToDouble(x -> x.getDecompressionTime()).min().getAsDouble();
			minEditDistance = resultList.stream().mapToDouble(x -> x.getEditDistance()).min().getAsDouble();
			minSpaceSaving = resultList.stream().mapToDouble(x -> x.getSpaceSaving()).min().getAsDouble();

			StandardDeviation standardDeviation = new StandardDeviation();

			sdUncompressedSize = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getUncompressedSize()).toArray(), meanUncompressedSize);
			sdCompressedSize = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getCompressedSize()).toArray(), meanCompressedSize);
			sdGzipCompressedSize = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getGzipCompressedSize()).toArray(), meanCompressedSize);
			sdDecompressedSize = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getDecompressedSize()).toArray(), meanDecompressedSize);
			sdCompressionTime = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getCompressionTime()).toArray(), meanCompressionTime);
			sdDecompressionTime = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getDecompressionTime()).toArray(), meanDecompressionTime);
			sdEditDistance = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getEditDistance()).toArray(), meanEditDistance);
			sdSpaceSaving = standardDeviation.evaluate(resultList.stream().mapToDouble(x -> x.getSpaceSaving()).toArray(), meanEditDistance);
		}

		tablePlot
				.add(String.format(TABLE_ENTRY, "Uncompressed Size(bytes)", minUncompressedSize, maxUncompressedSize, meanUncompressedSize, sdUncompressedSize))
				.add(String.format(TABLE_ENTRY, "Compressed Size(bytes)", minCompressedSize, maxCompressedSize, meanCompressedSize, sdCompressedSize))
				.add(String.format(TABLE_ENTRY, "GzipCompressed Size(bytes)", minGzipCompressedSize, maxGzipCompressedSize, meanGzipCompressedSize, sdGzipCompressedSize))
				.add(String.format(TABLE_ENTRY, "Decompressed Size(bytes)", minDecompressedSize, maxDecompressedSize, meanDecompressedSize, sdDecompressedSize))
				.add(String.format(TABLE_ENTRY, "Compression Time(ms)", minCompressionTime, maxCompressionTime, meanCompressionTime, sdCompressionTime))
				.add(String.format(TABLE_ENTRY, "Decompression Time(ms) ", minDecompressionTime, maxDecompressionTime, meanDecompressionTime, sdDecompressionTime))
				.add(String.format(TABLE_ENTRY, "Space Saving(\\%)", minSpaceSaving, maxSpaceSaving, meanSpaceSaving, sdSpaceSaving));

		System.out.println(tablePlot.toString());
	}
}