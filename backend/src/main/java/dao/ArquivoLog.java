
package dao;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import model.Log;

public class ArquivoLog {

        private Path pastaLogs;

        private DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        private DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");

        public ArquivoLog() {
                pastaLogs = Paths.get("logs");
        }

        // Registrar mensagens simples
        public void registrar(String arquivo, String mensagem)
                        throws Exception {

                Files.createDirectories(pastaLogs);

                Path caminho = pastaLogs.resolve(arquivo);

                String registro = LocalDateTime.now().format(
                                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + " - " + mensagem
                                + System.lineSeparator();

                Files.writeString(
                                caminho,
                                registro,
                                StandardOpenOption.CREATE,
                                StandardOpenOption.APPEND);
        }

        // Registrar eventos estruturados, como auditoria
        public void registrar(String arquivo, Log log)
                        throws Exception {

                Files.createDirectories(pastaLogs);

                Path caminho = pastaLogs.resolve(arquivo);

                String registro = "Data: " +
                                log.getDataHora().format(formatoData) +
                                System.lineSeparator() +

                                "Hora: " +
                                log.getDataHora().format(formatoHora) +
                                System.lineSeparator() +

                                "Usuário: " +
                                log.getUsuarioId() +
                                System.lineSeparator() +

                                "Tipo: " +
                                log.getTipo() +
                                System.lineSeparator() +

                                "Descrição: " +
                                log.getDescricao() +
                                System.lineSeparator() +
                                System.lineSeparator();

                Files.writeString(
                                caminho,
                                registro,
                                StandardOpenOption.CREATE,
                                StandardOpenOption.APPEND);
        }
}
