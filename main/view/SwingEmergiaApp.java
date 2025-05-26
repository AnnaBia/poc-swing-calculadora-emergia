package main.view;

import main.model.*;
import main.services.CalculadoraEmergia;
import main.utils.LeitorUEV;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;


import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

public class SwingEmergiaApp {

    public void mostrar() {
        JFrame frame = new JFrame("Simulador de Cálculo de Emergia");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 700);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nomeField = new JTextField(20);
        JComboBox<FaseIA> faseCombo = new JComboBox<>(FaseIA.values());
        JTextField energiaField = new JTextField(10);
        JTextField uevEnergiaField = new JTextField(10);
        JTextField horasField = new JTextField(10);
        JTextField uevHoraField = new JTextField(10);
        JTextField custoEquipField = new JTextField(10);
        JTextField uevEquipField = new JTextField(10);
        JTextField inferenciasField = new JTextField(10);
        JTextArea resultadoArea = new JTextArea(6, 30);
        resultadoArea.setEditable(false);
        JScrollPane scrollResultado = new JScrollPane(resultadoArea);

        JPanel grupoIA = criarGrupo("Informações do Sistema de IA", new JComponent[][]{
                {new JLabel("Nome IA:"), nomeField},
                {new JLabel("Fase:"), faseCombo}
        });

        JPanel grupoComputacional = criarGrupo("Recursos Computacionais", new JComponent[][]{
                {new JLabel("Energia Consumida (kWh):"), energiaField},
                {new JLabel("UEV Energia (sej/kWh):"), uevEnergiaField}
        });

        JPanel grupoHumano = criarGrupo("Recursos Humanos e Serviços", new JComponent[][]{
                {new JLabel("Horas de Trabalho:"), horasField},
                {new JLabel("UEV Hora (sej/h):"), uevHoraField}
        });

        JPanel grupoEquipamento = criarGrupo("Materiais e Equipamentos", new JComponent[][]{
                {new JLabel("Custo Equipamento ($):"), custoEquipField},
                {new JLabel("UEV Equipamento (sej/$):"), uevEquipField}
        });

        JPanel grupoOperacao = criarGrupo("Parâmetros de Operação", new JComponent[][]{
                {new JLabel("Inferências:"), inferenciasField}
        });

        JButton calcularBtn = new JButton("Calcular");

        JPanel grupoResultado = new JPanel(new BorderLayout());
        grupoResultado.setBorder(BorderFactory.createTitledBorder("Resultado do Cálculo"));
        grupoResultado.add(scrollResultado, BorderLayout.CENTER);
        grupoResultado.add(calcularBtn, BorderLayout.SOUTH);

        mainPanel.add(grupoIA);
        mainPanel.add(grupoComputacional);
        mainPanel.add(grupoHumano);
        mainPanel.add(grupoEquipamento);
        mainPanel.add(grupoOperacao);
        mainPanel.add(grupoResultado);

        calcularBtn.addActionListener(a -> {
            try {
                ProcessoIA processo = new ProcessoIA(
                        nomeField.getText(),
                        (FaseIA) faseCombo.getSelectedItem(),
                        Integer.parseInt(inferenciasField.getText()),
                        Integer.parseInt(horasField.getText())
                );

                RecursosComputacionais rc = new RecursosComputacionais(
                        new BigDecimal(energiaField.getText()),
                        new BigDecimal(uevEnergiaField.getText())
                );

                RecursosHumanos rh = new RecursosHumanos(
                        new BigDecimal(horasField.getText()),
                        new BigDecimal(uevHoraField.getText())
                );

                MateriaisEquipamentos me = new MateriaisEquipamentos(
                        new BigDecimal(custoEquipField.getText()),
                        new BigDecimal(uevEquipField.getText())
                );

                ResultadoEmergia resultado = CalculadoraEmergia.calcular(processo, rc, rh, me);

                BigDecimal emergiaRC = rc.calcularEmergia();
                BigDecimal emergiaRH = rh.calcularEmergia();
                BigDecimal emergiaME = me.calcularEmergia();

                String saida = String.format(
                        """
                ▸ Detalhamento do Cálculo de Emergia

                ➤ Recursos Computacionais: %.2e sej
                ➤ Recursos Humanos e Serviços: %.2e sej
                ➤ Materiais e Equipamentos: %.2e sej

                ➤ Emergia Total: %.2e sej
                ➤ Emergia por Hora: %.2e sej/h
                ➤ Emergia por Inferência: %.2e sej/inf
                """,
                        emergiaRC,
                        emergiaRH,
                        emergiaME,
                        resultado.total(),
                        resultado.porHora(),
                        resultado.porInferencia());

                resultadoArea.setText(saida);

                LeitorUEV.exportarCSV("resultado_emergia.csv", resultado,
                        processo.getInferencias(), processo.getHoras(),
                        emergiaRC, emergiaRH, emergiaME);

                gerarPDF(processo, rc, rh, me, resultado, emergiaRC, emergiaRH, emergiaME);

                JOptionPane.showMessageDialog(null, "Cálculo realizado e PDF gerado com sucesso!");

            } catch (Exception ex) {
                resultadoArea.setText("Erro ao calcular: verifique se todos os campos foram preenchidos corretamente. " + ex.getMessage());
            }
        });

        frame.add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public void gerarPDF(ProcessoIA processo,
                         RecursosComputacionais rc,
                         RecursosHumanos rh,
                         MateriaisEquipamentos me,
                         ResultadoEmergia resultado,
                         BigDecimal emergiaRC,
                         BigDecimal emergiaRH,
                         BigDecimal emergiaME) throws FileNotFoundException {

        String caminhoArquivo = "resultado_emergia.pdf";

        PdfWriter writer = new PdfWriter(caminhoArquivo);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Relatório de Cálculo de Emergia")
                .setBold()
                .setFontSize(16)
                .setMarginBottom(20));

        document.add(new Paragraph("Informações do Sistema de IA"));
        Table tabelaProcesso = new Table(2);
        tabelaProcesso.addCell("Nome");
        tabelaProcesso.addCell(processo.getNome());
        tabelaProcesso.addCell("Fase");
        tabelaProcesso.addCell(processo.getFase().toString());
        tabelaProcesso.addCell("Horas");
        tabelaProcesso.addCell(String.valueOf(processo.getHoras()));
        tabelaProcesso.addCell("Inferências");
        tabelaProcesso.addCell(String.valueOf(processo.getInferencias()));
        document.add(tabelaProcesso);

        document.add(new Paragraph("\nRecursos Computacionais"));
        Table tabelaRC = new Table(2);
        tabelaRC.addCell("Energia Consumida (kWh)");
        tabelaRC.addCell(rc.getEnergiaConsumida().toPlainString());
        tabelaRC.addCell("UEV Energia (sej/kWh)");
        tabelaRC.addCell(rc.getUevEnergia().toPlainString());
        tabelaRC.addCell("Emergia (sej)");
        tabelaRC.addCell(formatarCientifico(emergiaRC));
        document.add(tabelaRC);

        document.add(new Paragraph("\nRecursos Humanos e Serviços"));
        Table tabelaRH = new Table(2);
        tabelaRH.addCell("Horas de Trabalho");
        tabelaRH.addCell(rh.getHorasTrabalho().toPlainString());
        tabelaRH.addCell("UEV Hora (sej/h)");
        tabelaRH.addCell(rh.getUevHora().toPlainString());
        tabelaRH.addCell("Emergia (sej)");
        tabelaRH.addCell(formatarCientifico(emergiaRH));
        document.add(tabelaRH);

        document.add(new Paragraph("\nMateriais e Equipamentos"));
        Table tabelaME = new Table(2);
        tabelaME.addCell("Custo Equipamento ($)");
        tabelaME.addCell(me.getCustoEquipamento().toPlainString());
        tabelaME.addCell("UEV Equipamento (sej/$)");
        tabelaME.addCell(me.getUevEquipamento().toPlainString());
        tabelaME.addCell("Emergia (sej)");
        tabelaME.addCell(formatarCientifico(emergiaME));
        document.add(tabelaME);

        document.add(new Paragraph("\nResultado Final"));
        Table tabelaResultado = new Table(2);
        tabelaResultado.addCell("Emergia Total (sej)");
        tabelaResultado.addCell(formatarCientifico(resultado.total()));
        tabelaResultado.addCell("Emergia por Hora (sej/h)");
        tabelaResultado.addCell(formatarCientifico(resultado.porHora()));
        tabelaResultado.addCell("Emergia por Inferência (sej/inf)");
        tabelaResultado.addCell(formatarCientifico(resultado.porInferencia()));
        document.add(tabelaResultado);

        document.close();
    }


    private JPanel criarGrupo(String titulo, JComponent[][] componentes) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), titulo,
                TitledBorder.LEFT, TitledBorder.TOP));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < componentes.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            panel.add(componentes[i][0], gbc);

            gbc.gridx = 1;
            panel.add(componentes[i][1], gbc);
        }

        return panel;
    }

    private String formatarCientifico(BigDecimal valor) {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols(new Locale("pt", "BR"));
        simbolos.setDecimalSeparator(',');
        simbolos.setExponentSeparator("e");

        DecimalFormat formato = new DecimalFormat("0.00E0", simbolos);
        return formato.format(valor);
    }

}

