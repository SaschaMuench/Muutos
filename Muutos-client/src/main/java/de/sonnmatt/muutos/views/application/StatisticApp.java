package de.sonnmatt.muutos.views.application;

import java.util.ArrayList;
import java.util.List;

import org.pepstock.charba.client.DoughnutChart;
import org.pepstock.charba.client.GaugeChart;
import org.pepstock.charba.client.data.DoughnutDataset;
import org.pepstock.charba.client.data.GaugeDataset;
import org.pepstock.charba.client.enums.MeterDisplay;
import org.pepstock.charba.client.enums.Position;
import org.pepstock.charba.client.utils.Color;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class StatisticApp extends Composite /*implements HasText*/ {

	private static StatisticAppUiBinder uiBinder = GWT.create(StatisticAppUiBinder.class);

	@UiField DoughnutChart chart;
	@UiField GaugeChart chartPercent;
	@UiField GaugeChart chartValue;

	private final List<GaugeDataset> datasets = new ArrayList<GaugeDataset>();

	interface StatisticAppUiBinder extends UiBinder<Widget, StatisticApp> {
	}

	public StatisticApp() {
		initWidget(uiBinder.createAndBindUi(this));
		setText("Text 1");
	}

	public StatisticApp(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
		setText("Text 2: " + firstName);
	}

	public void setText(String text) {
//		chart.getOptions().setResponsive(true);
//		chart.getOptions().getLegend().setPosition(Position.top);
		chart.getOptions().getTitle().setDisplay(true);
		chart.getOptions().getTitle().setText("Charba Doughnut Chart: " + text);
		chart.getOptions().setCutoutPercentage(85);
		
		//chart.setHeight("100px");
//		chart.setSize("400px", "400px");

		DoughnutDataset dataset = chart.newDataset();
		dataset.setLabel("dataset 1");
		dataset.setBackgroundColor("red", "lightblue", "green");
		dataset.setData(3, 7, 5);
		dataset.setLabel("Label");
		//dataset.setBorderColor("black");

		chart.getData().setLabels("Roter Bereich", "Blauer Bereich", "Grüner Bereich");
		//chart.getData().setLabels("", "");
		chart.getOptions().getLegend().setDisplay(false);
		chart.getData().setDatasets(dataset);
		
		chartPercent.getOptions().getTitle().setDisplay(true);
		chartPercent.getOptions().getTitle().setText("GAUGE chart to represent percentage value");
		chartPercent.getOptions().setDisplay(MeterDisplay.percentage);
		chartPercent.getOptions().setFormat("###.##%");
		chartPercent.getData().setDatasets(getDataset(chartPercent, "Percent", 100D));

		chartValue.getOptions().getTitle().setDisplay(true);
		chartValue.getOptions().getTitle().setText("GAUGE chart to represent value and dataset label");
		chartValue.getOptions().setDisplay(MeterDisplay.valueAndLabel);
		chartValue.getOptions().setFormat("#### MB");
		chartValue.getData().setDatasets(getDataset(chartValue, "Memory", 2048D));
		
	}

	private GaugeDataset getDataset(GaugeChart chart, String label, double max){
		chart.getOptions().setResponsive(true);
		GaugeDataset dataset = chart.newDataset(max);
		dataset.setLabel(label);
		dataset.setValue(Math.random()*max);
		datasets.add(dataset);
		return dataset;
	}
	public String getText() {
		return "";
	}

}
