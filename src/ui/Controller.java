package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import model.Record;
import model.Separator;


public class Controller {

	@FXML
	private LineChart<Double, Double> chart;

	@FXML
	private TableColumn<Record, Number> activityColumn;

	@FXML
	private TableColumn<Record, Number> costColumn;

	@FXML
	private TextField activityLevelIn;

	@FXML
	private TextField costIn;

	@FXML
	private TextField activityLevelOut;

	@FXML
	private TextField costOut;

	@FXML
	private TableView<Record> table;

	@FXML
	private ToggleGroup option;

	@FXML
	private Label fixed;

	@FXML
	private Label variablePerUnit;
	
	@FXML
	private Label equation;

	@FXML
	private RadioButton halp;

	@FXML
	private RadioButton lr;

	private Separator separator;

	//TODO quitar las parametrizaciones si causan problemas
	@FXML
	public void initialize() {
		activityColumn.setCellValueFactory(new PropertyValueFactory<Record, Number>("activityLevel"));
		costColumn.setCellValueFactory(new PropertyValueFactory<Record, Number>("totalCost"));
		separator = new Separator(table.getItems());
	}

	@FXML
	public void exclude(ActionEvent event) {
		if(!activityLevelOut.getText().isEmpty() && !costOut.getText().isEmpty()) {
			separator.removeRecord(Double.parseDouble(activityLevelOut.getText().replace(',', '.')), Double.parseDouble(costOut.getText().replace(',', '.')));
		}
		refresh();
	}

	@FXML
	public void include(ActionEvent event) {
		if(!activityLevelIn.getText().isEmpty() && !costIn.getText().isEmpty()) {
			separator.addRecord(Double.parseDouble(activityLevelIn.getText().replace(',', '.')), Double.parseDouble(costIn.getText().replace(',', '.')));
		}
		refresh();
	}

	@FXML
	public void clear(ActionEvent event) {
		separator.clear();
		refresh();
	}

	@FXML
	public void onlyIntegers(KeyEvent event) {
		char input =event.getCharacter().charAt(0);
		TextField src = (TextField)event.getSource();
		if(!((input >= '0' && input <= '9') || input == '\b') && !((input == ',' || input == '.') && !(src.getText().contains(",")  || src.getText().contains(".")))) {
			event.consume();
		}
	}

	@FXML
	public void recompute(ActionEvent event) {
		refresh();
	}

	private void refresh() {
		Series<Double, Double> series = new XYChart.Series<>();
		series.setName("Records");
		ObservableList<Data<Double, Double>> list = series.getData();
		for(Record record : table.getItems()) {
			list.add(new XYChart.Data<Double, Double>(record.getActivityLevel(), record.getTotalCost()));
		}
		ObservableList<Series<Double, Double>> ol = FXCollections.observableArrayList();
		ol.add(series);
		chart.setData(ol);

		if(separator.getAccountingRecords().size() > 1) {
			double[] fav;
			if(option.getSelectedToggle() == halp) {
				fav = separator.highAndLowPoint();
			} else {
				fav = separator.linearRegression();
			}
			double[] low = separator.getBudgetedLowPoint(fav[0], fav[1]);
			double[] high = separator.getBudgetedHighPoint(fav[0], fav[1]);

			series = new XYChart.Series<>();
			series.setName("Trend line");
			list = series.getData();
			list.add(new XYChart.Data<Double, Double>(low[0], low[1]));
			list.add(new XYChart.Data<Double, Double>(high[0], high[1]));
			ol.add(series);
			chart.setData(ol);
			
			fixed.setText(fav[0]+"");
			variablePerUnit.setText(fav[1]+"");
			equation.setText(fav[0] + " + " + fav[1] + "X");
		} else {
			fixed.setText("a");
			variablePerUnit.setText("b");
			equation.setText("a + bX");
		}
	}
}
