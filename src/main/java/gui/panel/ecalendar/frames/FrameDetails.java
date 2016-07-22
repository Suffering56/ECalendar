package gui.panel.ecalendar.frames;

import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.JTableHeader;

import org.jfree.chart.ChartPanel;

import gui.panel.ecalendar.data.Converter;
import gui.panel.ecalendar.data.DetailsTableModel;
import gui.panel.ecalendar.data.ExtendCalendarRow;
import gui.panel.ecalendar.data.remote.ExtendFilterRemoteService;
import gui.panel.ecalendar.frames.parents.DataFrame;
import gui.panel.ecalendar.frames.renderers.DetailsRenderer;
import gui.panel.ecalendar.frames.renderers.HeaderRenderer;
import gui.panel.ecalendar.frames.util.ChartBuilder;
import gui.panel.ecalendar.frames.util.JSystemFileChooser;
import gui.panel.ecalendar.frames.util.PDFBuilder;
import p.calendar.data.CalendarRow.COUNTRY;

public class FrameDetails extends DataFrame {
	public FrameDetails(FrameCalendar primaryFrame, ExtendCalendarRow extRow) {
		this.primaryFrame = primaryFrame;
		this.extRow = extRow;
		
		render("ecalendar/FrameDetails");
	}
	
	@Override
	protected void beforeRenderInit() {
		startDate.add(Calendar.YEAR, -2);
		endDate.add(Calendar.WEEK_OF_YEAR, 1);
	}

	@Override
	protected void afterRenderInit() {		
		initComponents();
		initTable();
		initControls();

		chartBuilder = new ChartBuilder();
		chartPanel = chartBuilder.createChart();
		chartViewPanel.add(chartPanel);
	}

	public Action COPY_TO_BUFFER = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {

			StringBuilder copyString = new StringBuilder();
			char enter = (char) 13;

			copyString.append("Дата: " + extRow.getDateTime() + enter);
			copyString.append("Событие: " + extRow.getEvent() + enter);
			copyString.append("Важность: " + extRow.getImportance() + enter);
			copyString.append("Страна: " + countryLabel.getText() + enter);
			copyString.append("Фактическое значение: " + extRow.getValue() + enter);
			copyString.append("Прогноз: " + extRow.getForecast() + enter);
			copyString.append("Предыдущее значение: " + extRow.getPrevValue() + enter);
			copyString.append("Источник: " + extRow.getSource() + enter);

			StringSelection selection = new StringSelection(copyString.toString());
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(selection, selection);
		}
	};

	public Action PRINT = new AbstractAction() {
		public void actionPerformed(ActionEvent evt) {
			exportToPDF();
		}
	};

	private void exportToPDF() {
		/**
		 * Переключатель между javax.swing.JFileChooser и java.awt.FileDialog
		 * true - используем JFileChooser
		 * false - используем FileDialog
		 */
		boolean isFileChooser = true;

		if (isFileChooser) {
			JSystemFileChooser chooser = new JSystemFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setFileFilter(new FileNameExtensionFilter("*.pdf", "pdf"));

			if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
				String path = chooser.getSelectedFile().getAbsolutePath();
				path = path.replace(".pdf", "");
				path = path + ".pdf";

				BufferedImage chartImage = null;
				if (!chartBuilder.isNoData()) {
					chartImage = chartBuilder.getjFreeChart().createBufferedImage(ChartBuilder.CHART_WIDTH, ChartBuilder.CHART_HEIGHT);
				}

				PDFBuilder.getInstance().createDocument(extRow, chartImage, path);
			}
		} else {
			FileDialog chooser = new FileDialog(frame, "Сохранить как pdf...", FileDialog.SAVE);
			chooser.setVisible(true);
			String filename = chooser.getFile();
			String path = chooser.getDirectory() + filename;

			if (filename != null) {
				path = path.replace(".pdf", "");
				path = path + ".pdf";

				BufferedImage chartImage = null;
				if (!chartBuilder.isNoData()) {
					chartImage = chartBuilder.getjFreeChart().createBufferedImage(ChartBuilder.CHART_WIDTH, ChartBuilder.CHART_HEIGHT);
				}

				PDFBuilder.getInstance().createDocument(extRow, chartImage, path);
			}
		}
	}

	private void initComponents() {
		frame.setTitle(extRow.getEvent());
		eventDateLabel.setText(extRow.getDateTime());

		valueLabel.setText(extRow.getValue());
		forecastLabel.setText(extRow.getForecast());
		prevValueLabel.setText(extRow.getPrevValue());

		eventDescriptionArea.setText(extRow.getEvent());

		importanceLabel.setText(extRow.getImportance());
		countryLabel.setText(Converter.translateCountry(COUNTRY.valueOf(extRow.getCountry())));
		sourceArea.setText(extRow.getSource());
	}

	private void initTable() {
		model = new DetailsTableModel();

		table.setModel(model);
		table.setDefaultRenderer(String.class, new DetailsRenderer());

		header = table.getTableHeader();
		header.setDefaultRenderer(new HeaderRenderer(header));
		header.setReorderingAllowed(false);

		for (int i = 0; i < model.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setResizable(false);
		}

		remote = new ExtendFilterRemoteService(this, false);
		remote.sendDetailsRequest(startDate, endDate, extRow.getEvent());
	}

	public void updateModel(List<ExtendCalendarRow> rows) {
		if (rows.size() > 1) {
			prevEventDateLabel.setText(rows.get(1).getDate());
		}
		model.update(rows);
		updateChart(rows);
		pack();
	}

	private void updateChart(List<ExtendCalendarRow> rows) {
		boolean noData = chartBuilder.updateChart(rows);
		chartLoadLabel.setVisible(false);

		if (noData) {
			chartViewPanel.setVisible(false);
			noChartLabel.setVisible(true);
		} else {
			noChartLabel.setVisible(false);
			chartViewPanel.setVisible(true);
		}
	}

	private void initControls() {
		initCommonListeners();
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				primaryFrame.enable();
			}
		});
		
		showMoreBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chartBuilder != null) {
					chartBuilder.getSeries().clear();
					chartViewPanel.setVisible(false);
					noChartLabel.setVisible(false);
					chartLoadLabel.setVisible(true);

					endDate = (Calendar) startDate.clone();
					startDate.add(Calendar.YEAR, -2);

					remote.sendDetailsRequest(startDate, endDate, extRow.getEvent());
				}
			}
		});
	}

	public JTable getTable() {
		return table;
	}

	public JFrame getFrame() {
		return frame;
	}

	// main
	private FrameCalendar primaryFrame;
	private ChartBuilder chartBuilder;
	private ChartPanel chartPanel;

	private ExtendCalendarRow extRow;
	private DetailsTableModel model;

	// view
	private JTable table;
	public JPanel chartViewPanel;
	public JLabel noChartLabel;
	private JLabel chartLoadLabel;
	private JTableHeader header;
	public JButton showMoreBtn;

	private JLabel eventDateLabel;
	private JLabel prevEventDateLabel;
	private JLabel valueLabel;
	private JLabel forecastLabel;
	private JLabel prevValueLabel;
	private JTextArea eventDescriptionArea;
	private JLabel importanceLabel;
	private JLabel countryLabel;
	private JTextArea sourceArea;

	// temp 
	private Calendar startDate = Calendar.getInstance();
	private Calendar endDate = Calendar.getInstance();
}
