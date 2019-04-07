package br.com.unisys.xmlexitools.benchmark;

import java.util.Objects;

public class BenchmarkEntry {

    String descricaoLog;
    Long codigoLog;
    String conteudo;

    public BenchmarkEntry() {
    }

    public BenchmarkEntry(String descricaoLog, Long codigoLog, String conteudo) {
        this.descricaoLog = descricaoLog;
        this.codigoLog = codigoLog;
        this.conteudo = conteudo;
    }

    public String getDescricaoLog() {
        return descricaoLog;
    }

    public void setDescricaoLog(String descricaoLog) {
        this.descricaoLog = descricaoLog;
    }

    public Long getCodigoLog() {
        return codigoLog;
    }

    public void setCodigoLog(Long codigoLog) {
        this.codigoLog = codigoLog;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BenchmarkEntry that = (BenchmarkEntry) o;
        return Objects.equals(descricaoLog, that.descricaoLog) &&
                Objects.equals(codigoLog, that.codigoLog) &&
                Objects.equals(conteudo, that.conteudo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(descricaoLog, codigoLog, conteudo);
    }

    @Override
    public String toString() {
        return "BenchmarkEntry{" +
                "descricaoLog='" + descricaoLog + '\'' +
                ", codigoLog=" + codigoLog +
                ", conteudo='" + conteudo + '\'' +
                '}';
    }
}
