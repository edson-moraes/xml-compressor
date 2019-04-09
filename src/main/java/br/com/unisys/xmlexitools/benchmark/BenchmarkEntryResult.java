package br.com.unisys.xmlexitools.benchmark;

import java.util.Objects;
import java.util.StringJoiner;

public class BenchmarkEntryResult {

    private BenchmarkEntry benchmarkEntry;
    private long uncompressedSize;
    private long compressedSize;
    private long decompressedSize;
    private double compressionTime;
    private double decompressionTime;
    private double editDistance;

    public BenchmarkEntryResult(BenchmarkEntry benchmarkEntry, long uncompressedSize, long compressedSize,
                                long decompressedSize, double compressionTime, double decompressionTime,
                                double editDistance) {
        this.benchmarkEntry = benchmarkEntry;
        this.uncompressedSize = uncompressedSize;
        this.compressedSize = compressedSize;
        this.decompressedSize = decompressedSize;
        this.compressionTime = compressionTime;
        this.decompressionTime = decompressionTime;
        this.editDistance = editDistance;
    }

    public BenchmarkEntry getBenchmarkEntry() {
        return benchmarkEntry;
    }

    public void setBenchmarkEntry(BenchmarkEntry benchmarkEntry) {
        this.benchmarkEntry = benchmarkEntry;
    }

    public long getUncompressedSize() {
        return uncompressedSize;
    }

    public void setUncompressedSize(long uncompressedSize) {
        this.uncompressedSize = uncompressedSize;
    }

    public long getCompressedSize() {
        return compressedSize;
    }

    public void setCompressedSize(long compressedSize) {
        this.compressedSize = compressedSize;
    }

    public long getDecompressedSize() {
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

    public double getCompressionRatio() {
        return (double) uncompressedSize / (double) compressedSize;
    }

    public double getSpaceSavings() {
        return (1.0d - ((double) compressedSize / (double) uncompressedSize)) * 100.0d;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BenchmarkEntryResult that = (BenchmarkEntryResult) o;
        return uncompressedSize == that.uncompressedSize &&
                compressedSize == that.compressedSize &&
                decompressedSize == that.decompressedSize &&
                Double.compare(that.compressionTime, compressionTime) == 0 &&
                Double.compare(that.decompressionTime, decompressionTime) == 0 &&
                Double.compare(that.editDistance, editDistance) == 0 &&
                Objects.equals(benchmarkEntry, that.benchmarkEntry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(benchmarkEntry, uncompressedSize, compressedSize, decompressedSize, compressionTime, decompressionTime, editDistance);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BenchmarkEntryResult.class.getSimpleName() + "[", "]")
                .add("benchmarkEntry=" + benchmarkEntry)
                .add("uncompressedSize=" + uncompressedSize)
                .add("compressedSize=" + compressedSize)
                .add("decompressedSize=" + decompressedSize)
                .add("compressionTime=" + compressionTime)
                .add("decompressionTime=" + decompressionTime)
                .add("editDistance=" + editDistance)
                .toString();
    }
}
