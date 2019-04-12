package br.com.unisys.xmlexitools.benchmark;

import java.util.Objects;
import java.util.StringJoiner;

public class BenchmarkEntryResult {

	private BenchmarkEntry benchmarkEntry;
	private double uncompressedSize;
	private double compressedSize;
	private double gzipCompressedSize;
	private double decompressedSize;
	private double compressionTime;
	private double decompressionTime;
	private double editDistance;

	public BenchmarkEntryResult(BenchmarkEntry benchmarkEntry, double uncompressedSize, double compressedSize,
								double gzipCompressedSize, double decompressedSize, double compressionTime,
								double decompressionTime, double editDistance) {
		this.benchmarkEntry = benchmarkEntry;
		this.uncompressedSize = uncompressedSize;
		this.compressedSize = compressedSize;
		this.gzipCompressedSize = gzipCompressedSize;
		this.decompressedSize = decompressedSize;
		this.compressionTime = compressionTime;
		this.decompressionTime = decompressionTime;
		this.editDistance = editDistance;
	}

	public BenchmarkEntry getBenchmarkEntry() {
		return benchmarkEntry;
	}

	public double getUncompressedSize() {
		return uncompressedSize;
	}

	public double getCompressedSize() {
		return compressedSize;
	}

	public double getGzipCompressedSize() {
		return gzipCompressedSize;
	}

	public double getDecompressedSize() {
		return decompressedSize;
	}

	public double getCompressionTime() {
		return compressionTime;
	}

	public double getDecompressionTime() {
		return decompressionTime;
	}

	public double getEditDistance() {
		return editDistance;
	}

	public double getSpaceSaving() {
		return (1.0d - (compressedSize / uncompressedSize)) * 100.0d;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BenchmarkEntryResult)) return false;
		BenchmarkEntryResult that = (BenchmarkEntryResult) o;
		return Double.compare(that.uncompressedSize, uncompressedSize) == 0 &&
				Double.compare(that.compressedSize, compressedSize) == 0 &&
				Double.compare(that.gzipCompressedSize, gzipCompressedSize) == 0 &&
				Double.compare(that.decompressedSize, decompressedSize) == 0 &&
				Double.compare(that.compressionTime, compressionTime) == 0 &&
				Double.compare(that.decompressionTime, decompressionTime) == 0 &&
				Double.compare(that.editDistance, editDistance) == 0 &&
				Objects.equals(benchmarkEntry, that.benchmarkEntry);
	}

	@Override
	public int hashCode() {
		return Objects.hash(benchmarkEntry, uncompressedSize, compressedSize, gzipCompressedSize, decompressedSize,
				compressionTime, decompressionTime, editDistance);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", BenchmarkEntryResult.class.getSimpleName() + "[", "]")
				.add("benchmarkEntry=" + benchmarkEntry)
				.add("uncompressedSize=" + uncompressedSize)
				.add("compressedSize=" + compressedSize)
				.add("gzipCompressedSize=" + gzipCompressedSize)
				.add("decompressedSize=" + decompressedSize)
				.add("compressionTime=" + compressionTime)
				.add("decompressionTime=" + decompressionTime)
				.add("editDistance=" + editDistance)
				.toString();
	}
}
