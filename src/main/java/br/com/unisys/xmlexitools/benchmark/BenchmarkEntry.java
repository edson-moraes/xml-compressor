package br.com.unisys.xmlexitools.benchmark;

import java.util.Objects;

public class BenchmarkEntry {

    String logDescription;
    Long logCode;
    String content;

    public BenchmarkEntry() {
    }

    public BenchmarkEntry(String descricaoLog, Long codigoLog, String conteudo) {
        this.logDescription = descricaoLog;
        this.logCode = codigoLog;
        this.content = conteudo;
    }

    public String getLogDescription() {
        return logDescription;
    }

    public void setLogDescription(String logDescription) {
        this.logDescription = logDescription;
    }

    public Long getLogCode() {
        return logCode;
    }

    public void setLogCode(Long logCode) {
        this.logCode = logCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BenchmarkEntry that = (BenchmarkEntry) o;
        return Objects.equals(logDescription, that.logDescription) &&
                Objects.equals(logCode, that.logCode) &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logDescription, logCode, content);
    }

    @Override
    public String toString() {
        return "BenchmarkEntry{" +
                "logDescription='" + logDescription + '\'' +
                ", logCode=" + logCode +
                ", content='" + content + '\'' +
                '}';
    }
}
