
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

    private static final Color FUNDO = new Color(36, 36, 38);
    private static final Color TEXTO = new Color(229, 229, 231);
    private static final Color CINZA = new Color(161, 161, 166);
    private static final Color BORDA = new Color(58, 58, 60);

    private static final Color VERDE = new Color(67, 124, 30);
    private static final Color AZUL = new Color(0, 153, 153);
    private static final Color VERMELHO = new Color(239, 98, 108);
    private static final Color CLARO = new Color(239, 246, 238);

    private static final Font FONTE_TITULO =
            new Font("Segoe UI", Font.BOLD, 17);

    private static final Font FONTE_LEGENDA =
            new Font("Segoe UI", Font.PLAIN, 12);

    private static final Font FONTE_EIXO =
            new Font("Segoe UI", Font.PLAIN, 12);


    // ========================================
    // GRÁFICO DE STATUS
    // ========================================

    public DefaultPieDataset<String> criarDadosStatus() {

        DefaultPieDataset<String> dados = new DefaultPieDataset<>();

        dados.setValue("Concluídos", 1);
        dados.setValue("Em desenvolvimento", 4);
        dados.setValue("Planejados", 7);

        return dados;
    }


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

        // Título
        estilizarTitulo(grafico);

        // Legenda
        estilizarLegenda(grafico);

        RingPlot plot = (RingPlot) grafico.getPlot();

        plot.setBackgroundPaint(FUNDO);
        plot.setOutlineVisible(false);
        plot.setShadowPaint(null);

        // Aparência do anel
        plot.setSeparatorsVisible(false);
        plot.setSectionDepth(0.50);

        plot.setLegendItemShape(
                new Rectangle2D.Double(-6, -3, 12, 6)
        );

        // Cores das categorias
        plot.setSectionPaint("Concluídos", VERDE);
        plot.setSectionPaint("Em desenvolvimento", AZUL);
        plot.setSectionPaint("Planejados", VERMELHO);

        // Não mostrar valores escritos sobre as fatias.
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

    public DefaultCategoryDataset criarDadosCategoria() {

        DefaultCategoryDataset dados = new DefaultCategoryDataset();

        dados.addValue(5, "Quantidade de projetos", "Web");
        dados.addValue(3, "Quantidade de projetos", "Software");
        dados.addValue(4, "Quantidade de projetos", "Mobile");
        dados.addValue(2, "Quantidade de projetos", "Desktop");

        return dados;
    }


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

        // Título e legenda
        estilizarTitulo(grafico);
        estilizarLegenda(grafico);

        CategoryPlot plot = grafico.getCategoryPlot();

        plot.setBackgroundPaint(FUNDO);
        plot.setOutlineVisible(false);

        // Linhas horizontais do gráfico
        plot.setRangeGridlinePaint(BORDA);

        // Renderer controla a aparência das barras.
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

        // Eixo horizontal: nomes das categorias
        plot.getDomainAxis().setTickLabelPaint(TEXTO);
        plot.getDomainAxis().setTickLabelFont(FONTE_EIXO);

        // Eixo vertical: quantidades
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

    public DefaultCategoryDataset criarDadosEvolucao(
            List<Projeto> projetos
    ) {

        DefaultCategoryDataset dados = new DefaultCategoryDataset();

        // TreeMap mantém os meses em ordem cronológica.
        Map<YearMonth, Integer> contagemPorMes = new TreeMap<>();

        // Conta quantos projetos foram criados em cada mês.
        for (Projeto projeto : projetos) {

            if (projeto.getDataCriacao() != null) {

                YearMonth mes = YearMonth.from(
                        projeto.getDataCriacao()
                );

                contagemPorMes.put(
                        mes,
                        contagemPorMes.getOrDefault(mes, 0) + 1
                );
            }
        }

        // Nome completo dos meses em português.
        Locale idioma = new Locale("pt", "BR");

        for (Map.Entry<YearMonth, Integer> entrada :
                contagemPorMes.entrySet()) {

            YearMonth mes = entrada.getKey();

            String nomeMes = mes.getMonth()
                    .getDisplayName(TextStyle.FULL, idioma);

            // Primeira letra maiúscula.
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

        // Título e legenda
        estilizarTitulo(grafico);
        estilizarLegenda(grafico);

        CategoryPlot plot = grafico.getCategoryPlot();

        plot.setBackgroundPaint(FUNDO);
        plot.setOutlineVisible(false);

        plot.setRangeGridlinePaint(BORDA);

        // Eixo vertical: somente números inteiros.
        NumberAxis eixoVertical = (NumberAxis) plot.getRangeAxis();

        eixoVertical.setNumberFormatOverride(
                new DecimalFormat("0")
        );

        eixoVertical.setStandardTickUnits(
                NumberAxis.createIntegerTickUnits()
        );

        eixoVertical.setAutoRangeIncludesZero(true);

        // Renderer controla a linha e os pontos.
        LineAndShapeRenderer renderer =
                (LineAndShapeRenderer) plot.getRenderer();

        // Linha principal.
        renderer.setSeriesPaint(0, AZUL);

        renderer.setSeriesStroke(
                0,
                new BasicStroke(2.5f)
        );

        // Mostrar os pontos.
        renderer.setSeriesShapesVisible(0, true);

        renderer.setSeriesShape(
                0,
                new Ellipse2D.Double(-4, -4, 8, 8)
        );

        renderer.setSeriesShapesFilled(0, true);

        // Cor interna dos pontos.
        renderer.setUseFillPaint(true);
        renderer.setSeriesFillPaint(0, VERMELHO);

        // Contorno dos pontos.
        renderer.setDrawOutlines(true);
        renderer.setSeriesOutlinePaint(0, CLARO);

        // Textos dos eixos.
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

        grafico.getTitle().setPaint(
                new Color(245, 245, 247)
        );

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
