package gui.panel.ecalendar.frames.util;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ToolTipManager;

import org.jfree.chart.labels.CustomXYToolTipGenerator;

public class ExtendCustomXYToolTipGenerator extends CustomXYToolTipGenerator {

	public ExtendCustomXYToolTipGenerator() {
		super();
		ToolTipManager.sharedInstance().setInitialDelay(0);
	}

	@Override
	public String getToolTipText(int series, int item) {
		if (tooltipList.get(item) == null) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		builder.append("Дата: ");
		builder.append(tooltipList.get(item).getDateTime());
		builder.append(" Факт. значение: ");
		builder.append(tooltipList.get(item).getValue());
		builder.append(" Прогноз: ");
		builder.append(tooltipList.get(item).getForecast());
		return builder.toString();
	}

	public List<TooltipEntity> getTooltipList() {
		return tooltipList;
	}

	private List<TooltipEntity> tooltipList = new ArrayList<TooltipEntity>();
}
