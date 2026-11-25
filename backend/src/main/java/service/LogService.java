
package service;

import java.time.LocalDateTime;

import dao.ArquivoLog;
import model.Log;

public class LogService {

    private ArquivoLog dao;

    public LogService() {
        dao = new ArquivoLog();
    }

    public void adicionar(
            int usuarioId,
            String tipo,
            String descricao) throws Exception {

        Log log = new Log(
                LocalDateTime.now().withNano(0),
                usuarioId,
                tipo,
                descricao);

        dao.registrar("audit.log", log);
    }
}
