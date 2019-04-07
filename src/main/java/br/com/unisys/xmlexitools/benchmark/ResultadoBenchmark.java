package br.com.unisys.xmlexitools.benchmark;

public class ResultadoBenchmark {

    private long uncompressedSize;
    private long compressedSize;
    private long decompressedSize;
    private double compressionTime;
    private double decompressionTime;
    private double editDistance;

    public ResultadoBenchmark(long uncompressedSize, long compressedSize, long decompressedSize,
                              double compressionTime, double decompressionTime, double editDistance) {
        this.uncompressedSize = uncompressedSize;
        this.compressedSize = compressedSize;
        this.decompressedSize = decompressedSize;
        this.compressionTime = compressionTime;
        this.decompressionTime = decompressionTime;
        this.editDistance = editDistance;
    }

    public double getCompressionRatio() {
        return (double) uncompressedSize / (double) compressedSize;
    }

    public double getSpaceSavings() {
        return (1.0d - ((double) compressedSize / (double) uncompressedSize)) * 100.0d;
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
}
