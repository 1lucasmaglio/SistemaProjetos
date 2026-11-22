
package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import model.Projeto;

public class ViewChart {

    // ========================================
    // CORES E FONTES
    // ========================================

    private final Color FUNDO;
    private final Color TEXTO;
    private final Color CINZA;
    private final Color BORDA;

    private final Color VERDE = new Color(67, 124, 30);
    private final Color AZUL = new Color(0, 153, 153);
    private final Color VERMELHO = new Color(239, 98, 108);
    private final Color CLARO = new Color(239, 246, 238);

    private static final Font FONTE_TITULO =
            new Font("Segoe UI", Font.BOLD, 17);

    private static final Font FONTE_LEGENDA =
            new Font("Segoe UI", Font.PLAIN, 12);

    private static final Font FONTE_EIXO =
            new Font("Segoe UI", Font.PLAIN, 12);


    // ========================================
    // CONSTRUTOR
    // ========================================

    public ViewChart(boolean temaClaro) {

        if (temaClaro) {

            FUNDO = new Color(255, 255, 255);
            TEXTO = new Color(45, 45, 48);
            CINZA = new Color(110, 110, 115);
            BORDA = new Color(220, 220, 224);

        } else {

            FUNDO = new Color(36, 36, 38);
            TEXTO = new Color(229, 229, 231);
            CINZA = new Color(161, 161, 166);
            BORDA = new Color(58, 58, 60);
        }
    }


    // ========================================
    // DADOS DOS GRÁFICOS
    // ========================================

    public DefaultPieDataset<String> criarDadosStatus(
            List<Projeto> projetos
    ) {

        DefaultPieDataset<String> dados = new DefaultPieDataset<>();

        int concluidos = 0;
        int emDesenvolvimento = 0;
        int planejados = 0;

        for (Projeto projeto : projetos) {

            String status = projeto.getStatus();

            if (status == null) {
                continue;
            }

            if (status.equalsIgnoreCase("Concluído")) {
                concluidos++;

            } else if (
                    status.equalsIgnoreCase("Em desenvolvimento")
            ) {
                emDesenvolvimento++;

            } else if (status.equalsIgnoreCase("Planejado")) {
                planejados++;
            }
        }

        dados.setValue("Concluídos", concluidos);
        dados.setValue("Em desenvolvimento", emDesenvolvimento);
        dados.setValue("Planejados", planejados);

        return dados;
    }


    public DefaultCategoryDataset criarDadosCategoria(
            List<Projeto> projetos
    ) {

        DefaultCategoryDataset dados = new DefaultCategoryDataset();

        Map<String, Integer> contagemPorCategoria = new TreeMap<>();

        for (Projeto projeto : projetos) {

            String categoria = projeto.getCategoria();

            if (categoria == null || categoria.isBlank()) {
                continue;
            }

            contagemPorCategoria.put(
                    categoria,
                    contagemPorCategoria.getOrDefault(categoria, 0) + 1
            );
        }

        for (Map.Entry<String, Integer> entrada :
                contagemPorCategoria.entrySet()) {

            dados.addValue(
                    entrada.getValue(),
                    "Quantidade de projetos",
                    entrada.getKey()
            );
        }

        return dados;
    }


    public DefaultCategoryDataset criarDadosEvolucao(
            List<Projeto> projetos
    ) {

        DefaultCategoryDataset dados = new DefaultCategoryDataset();

        Map<YearMonth, Integer> contagemPorMes = new TreeMap<>();

        for (Projeto projeto : projetos) {

            if (projeto.getDataCriacao() == null) {
                continue;
            }

            YearMonth mes = YearMonth.from(
                    projeto.getDataCriacao()
            );

            contagemPorMes.put(
                    mes,
                    contagemPorMes.getOrDefault(mes, 0) + 1
            );
        }

        Locale idioma = new Locale("pt", "BR");

        for (Map.Entry<YearMonth, Integer> entrada :
                contagemPorMes.entrySet()) {

            YearMonth mes = entrada.getKey();

            String nomeMes = mes.getMonth()
                    .getDisplayName(TextStyle.FULL, idioma);

            nomeMes = nomeMes.substring(0, 1).toUpperCase(idioma)
                    + nomeMes.substring(1);

            String label = nomeMes + " " + mes.getYear();

            dados.addValue(
                    entrada.getValue(),
                    "Projetos criados",
                    label
            );
        }

        return dados;
    }


    // ========================================
    // GRÁFICO DE STATUS
    // ========================================

    public JFreeChart criarGraficoStatus(
            DefaultPieDataset<String> dados
    ) {

        JFreeChart grafico = ChartFactory.createRingChart(
                "Projetos por status",
                dados,
                true,
                true,
                false
        );

        estilizarTitulo(grafico);
        estilizarLegenda(grafico);

        RingPlot plot = (RingPlot) grafico.getPlot();

        plot.setBackgroundPaint(FUNDO);
        plot.setOutlineVisible(false);
        plot.setShadowPaint(null);
        plot.setSeparatorsVisible(false);
        plot.setSectionDepth(0.50);

        plot.setLegendItemShape(
                new Rectangle2D.Double(-6, -3, 12, 6)
        );

        plot.setSectionPaint("Concluídos", VERDE);
        plot.setSectionPaint("Em desenvolvimento", AZUL);
        plot.setSectionPaint("Planejados", VERMELHO);

        plot.setLabelGenerator(null);

        grafico.setBackgroundPaint(FUNDO);

        grafico.setPadding(
                new RectangleInsets(12, 20, 12, 20)
        );

        return grafico;
    }


    // ========================================
    // GRÁFICO DE CATEGORIAS
    // ========================================

    public JFreeChart criarGraficoCategoria(
            DefaultCategoryDataset dados
    ) {

        JFreeChart grafico = ChartFactory.createBarChart(
                "Projetos por categoria",
                null,
                null,
                dados,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        grafico.setBackgroundPaint(FUNDO);

        grafico.setPadding(
                new RectangleInsets(12, 20, 12, 20)
        );

        estilizarTitulo(grafico);
        estilizarLegenda(grafico);

        CategoryPlot plot = grafico.getCategoryPlot();

        plot.setBackgroundPaint(FUNDO);
        plot.setOutlineVisible(false);
        plot.setRangeGridlinePaint(BORDA);

        BarRenderer renderer = new BarRenderer() {

            @Override
            public Paint getItemPaint(int linha, int coluna) {

                String categoria = (String) getPlot()
                        .getDataset()
                        .getColumnKey(coluna);

                switch (categoria) {

                    case "Web":
                        return AZUL;

                    case "Software":
                        return VERDE;

                    case "Mobile":
                        return VERMELHO;

                    case "Desktop":
                        return CLARO;

                    default:
                        return Color.GRAY;
                }
            }
        };

        renderer.setBarPainter(
                new org.jfree.chart.renderer.category.StandardBarPainter()
        );

        renderer.setShadowVisible(false);

        plot.setRenderer(renderer);

        plot.getDomainAxis().setTickLabelPaint(TEXTO);
        plot.getDomainAxis().setTickLabelFont(FONTE_EIXO);

        NumberAxis eixoVertical = (NumberAxis) plot.getRangeAxis();

        eixoVertical.setTickLabelPaint(TEXTO);
        eixoVertical.setTickLabelFont(FONTE_EIXO);

        eixoVertical.setStandardTickUnits(
                NumberAxis.createIntegerTickUnits()
        );

        eixoVertical.setAutoRangeIncludesZero(true);

        return grafico;
    }


    // ========================================
    // GRÁFICO DE EVOLUÇÃO MENSAL
    // ========================================

    public JFreeChart criarGraficoEvolucao(
            DefaultCategoryDataset dados
    ) {

        JFreeChart grafico = ChartFactory.createLineChart(
                "Evolução mensal de projetos",
                null,
                null,
                dados,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        grafico.setBackgroundPaint(FUNDO);

        grafico.setPadding(
                new RectangleInsets(12, 20, 12, 20)
        );

        estilizarTitulo(grafico);
        estilizarLegenda(grafico);

        CategoryPlot plot = grafico.getCategoryPlot();

        plot.setBackgroundPaint(FUNDO);
        plot.setOutlineVisible(false);
        plot.setRangeGridlinePaint(BORDA);

        NumberAxis eixoVertical = (NumberAxis) plot.getRangeAxis();

        eixoVertical.setNumberFormatOverride(
                new DecimalFormat("0")
        );

        eixoVertical.setStandardTickUnits(
                NumberAxis.createIntegerTickUnits()
        );

        eixoVertical.setAutoRangeIncludesZero(true);

        LineAndShapeRenderer renderer =
                (LineAndShapeRenderer) plot.getRenderer();

        renderer.setSeriesPaint(0, AZUL);

        renderer.setSeriesStroke(
                0,
                new BasicStroke(2.5f)
        );

        renderer.setSeriesShapesVisible(0, true);

        renderer.setSeriesShape(
                0,
                new Ellipse2D.Double(-4, -4, 8, 8)
        );

        renderer.setSeriesShapesFilled(0, true);
        renderer.setUseFillPaint(true);
        renderer.setSeriesFillPaint(0, VERMELHO);

        renderer.setDrawOutlines(true);
        renderer.setSeriesOutlinePaint(0, CLARO);

        plot.getDomainAxis().setTickLabelPaint(TEXTO);
        plot.getDomainAxis().setTickLabelFont(FONTE_EIXO);

        plot.getRangeAxis().setTickLabelPaint(TEXTO);
        plot.getRangeAxis().setTickLabelFont(FONTE_EIXO);

        return grafico;
    }


    // ========================================
    // PAINEL DO GRÁFICO
    // ========================================

    public ChartPanel criarPainelGrafico(JFreeChart grafico) {

        ChartPanel painel = new ChartPanel(grafico);

        painel.setBackground(FUNDO);
        painel.setMouseWheelEnabled(true);

        return painel;
    }


    // ========================================
    // ESTILIZAÇÃO COMPARTILHADA
    // ========================================

    private void estilizarTitulo(JFreeChart grafico) {

        grafico.getTitle().setPaint(TEXTO);
        grafico.getTitle().setFont(FONTE_TITULO);

        grafico.getTitle().setHorizontalAlignment(
                org.jfree.chart.ui.HorizontalAlignment.LEFT
        );
    }


    private void estilizarLegenda(JFreeChart grafico) {

        grafico.getLegend().setPosition(
                org.jfree.chart.ui.RectangleEdge.TOP
        );

        grafico.getLegend().setBackgroundPaint(FUNDO);
        grafico.getLegend().setItemPaint(CINZA);
        grafico.getLegend().setItemFont(FONTE_LEGENDA);

        grafico.getLegend().setFrame(
                org.jfree.chart.block.BlockBorder.NONE
        );
    }
}
