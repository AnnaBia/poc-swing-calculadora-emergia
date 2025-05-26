package main.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;

import main.model.ResultadoEmergia;

public class LeitorUEV {

    public static BigDecimal parseUEV(String valor) {
        try {
            return new BigDecimal(valor.trim());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    public static void exportarCSV(
            String nomeArquivo,
            ResultadoEmergia resultado,
            int inferencias,
            int horas,
            BigDecimal emergiaComputacional,
            BigDecimal emergiaHumana,
            BigDecimal emergiaEquipamento
    ) {
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            writer.append("Categoria,Valor (sej)\n");
            writer.append("Emergia Computacional,").append(emergiaComputacional.toEngineeringString()).append("\n");
            writer.append("Emergia Humana,").append(emergiaHumana.toEngineeringString()).append("\n");
            writer.append("Emergia Equipamento,").append(emergiaEquipamento.toEngineeringString()).append("\n");
            writer.append("Emergia Total,").append(resultado.total().toEngineeringString()).append("\n");
            writer.append("Emergia por Hora,").append(resultado.porHora().toEngineeringString()).append("\n");
            writer.append("Emergia por Inferência,").append(resultado.porInferencia().toEngineeringString()).append("\n");
            writer.append("Inferências Realizadas,").append(String.valueOf(inferencias)).append("\n");
            writer.append("Horas de Operação,").append(String.valueOf(horas)).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
