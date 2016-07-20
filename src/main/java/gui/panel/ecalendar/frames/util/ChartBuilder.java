package gui.panel.ecalendar.frames.util;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.ui.RectangleInsets;

import gui.panel.ecalendar.data.Converter;
import gui.panel.ecalendar.data.ExtendCalendarRow;
import gui.panel.ecalendar.styles.ChartStyles;

public class ChartBuilder {

	public ChartPanel createChart() {
		series = new TimeSeries("Дата события");
		IntervalXYDataset dataset = new TimeSeriesCollection(series);
		jFreeChart = ChartFactory.createXYBarChart("", "", true, "", dataset, PlotOrientation.VERTICAL, false, false, false);
		//===========================================================================	
		jFreeChart.setBackgroundPaint(ChartStyles.BACKGROUND_COLOR);										//цвет фона
		jFreeChart.getXYPlot().setBackgroundAlpha(0);														//прозрачность фона
		jFreeChart.getXYPlot().getDomainAxis().setAxisLineVisible(false);									//линия OX
		jFreeChart.getXYPlot().setAxisOffset(new RectangleInsets(0, 0, 0, 10));								//отступы
		jFreeChart.getXYPlot().setOutlinePaint(ChartStyles.OUTLINE_COLOR);									//цвет рамки вокруг диаграммы
		jFreeChart.getXYPlot().setDomainGridlinesVisible(false);											//вертикальные линии
		jFreeChart.getXYPlot().setRangeGridlinesVisible(true);												//горизонтальные линии
		jFreeChart.getXYPlot().setShadowGenerator(null);													//тени	
		jFreeChart.getXYPlot().getDomainAxis().setTickLabelPaint(ChartStyles.LABEL_COLOR);					//цвет подписей снизу
		jFreeChart.getXYPlot().getRangeAxis().setTickLabelPaint(ChartStyles.LABEL_COLOR);					//цвет подписей слева

		XYBarRenderer renderer = (XYBarRenderer) jFreeChart.getXYPlot().getRenderer();

		renderer.setDrawBarOutline(true);																	//нужно для установки минимальной ширины баров
		renderer.setSeriesOutlineStroke(0, new BasicStroke(6.5f));											//установка ширины баров
		renderer.setBaseOutlinePaint(ChartStyles.SERIES_COLOR);												//цвет окантовки баров (если outline = true)
		renderer.setSeriesPaint(0, ChartStyles.SERIES_COLOR);												//цвет баров
		renderer.setBaseToolTipGenerator(tooltipGenerator);													//tooltip

		//============================================================================
		ChartPanel chart = new ChartPanel(jFreeChart);
		chart.setPreferredSize(new Dimension(CHART_WIDTH, CHART_HEIGHT));
		chart.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		chart.repaint();
		return chart;
	}

	public boolean updateChart(List<ExtendCalendarRow> rows) {
		series.clear();
		minutesHashSet.clear();
		tooltipList.clear();
		tooltipList.add(null);

		noData = true;
		List<Date> chartDataItems = new ArrayList<Date>();

		for (ExtendCalendarRow row : rows) {
			String value = Converter.valueConverter(row.getValue());
			value = Converter.excludePercent(value);

			if (!value.isEmpty()) {
				Minute minute = new Minute(row.getPrimaryDate());
				if (!minutesHashSet.contains(minute)) {
					series.add(minute, Float.valueOf(value));
					minutesHashSet.add(minute);
				}

				tooltipList.add(new TooltipEntity(row.getDateTime(), row.getValue(), row.getForecast()));
				chartDataItems.add(row.getPrimaryDate());
				noData = false;
			}
		}

		setBorderTime(chartDataItems);
		return noData;
	}

	private void setBorderTime(List<Date> items) {
		if (items.size() > 0) {
			max.setTime(items.get(0));
			min.setTime(items.get(items.size() - 1));
			
			min.add(Calendar.MINUTE, -1);
			min.add(Calendar.DAY_OF_YEAR, -1);
			max.add(Calendar.MINUTE, +1);
			max.add(Calendar.DAY_OF_YEAR, +1);
			
			series.add(new Minute(min.getTime()), 0.0);
			series.add(new Minute(max.getTime()), 0.0);
			tooltipList.add(null);
		}
	}

	public TimeSeries getSeries() {
		return series;
	}

	public JFreeChart getjFreeChart() {
		return jFreeChart;
	}

	public boolean isNoData() {
		return noData;
	}

	private TimeSeries series;
	private boolean noData = true;
	private Set<Minute> minutesHashSet = new HashSet<Minute>();
	private Calendar min = Calendar.getInstance();
	private Calendar max = Calendar.getInstance();
	private JFreeChart jFreeChart;
	private ExtendCustomXYToolTipGenerator tooltipGenerator = new ExtendCustomXYToolTipGenerator();
	private List<TooltipEntity> tooltipList = tooltipGenerator.getTooltipList();

	public static final int CHART_WIDTH = 500;
	public static final int CHART_HEIGHT = 245;
}
