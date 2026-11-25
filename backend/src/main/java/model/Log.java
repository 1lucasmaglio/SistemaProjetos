
package model;

import java.time.LocalDateTime;

public class Log {

    private LocalDateTime dataHora;
    private int usuarioId;
    private String tipo;
    private String descricao;

    public Log() {
    }

    public Log(LocalDateTime dataHora, int usuarioId,
            String tipo, String descricao) {

        this.dataHora = dataHora;
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
