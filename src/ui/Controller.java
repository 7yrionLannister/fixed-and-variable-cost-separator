package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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
	private TableView<Record> table;

	@FXML
	private ToggleGroup option;

	@FXML
	private Label fixed;

	@FXML
	private Label variablePerUnit;

	@FXML
	private Label fixedE;

	@FXML
	private Label variablePerUnitE;

	@FXML
	private RadioButton halp;

	@FXML
	private RadioButton lr;

	@FXML
	private TextField budgetActivityLevel;

	private Separator separator;

	//TODO quitar las parametrizaciones si causan problemas
	@FXML
	public void initialize() {
		activityColumn.setCellValueFactory(new PropertyValueFactory<Record, Number>("activityLevel"));
		costColumn.setCellValueFactory(new PropertyValueFactory<Record, Number>("totalCost"));
		separator = new Separator();
	}

	@FXML
	public void exclude(ActionEvent event) {
		if(!activityLevelOut.getText().isEmpty()) {
			separator.removeRecord(Double.parseDouble(activityLevelOut.getText().replace(',', '.')));
			activityLevelOut.setText("");
		}
		refresh();
	}

	@FXML
	public void include(ActionEvent event) {
		if(!activityLevelIn.getText().isEmpty() && !costIn.getText().isEmpty()) {
			separator.addRecord(Double.parseDouble(activityLevelIn.getText().replace(',', '.')), Double.parseDouble(costIn.getText().replace(',', '.')));
			activityLevelIn.setText("");
			costIn.setText("");
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

		for(Record record : separator.getAccountingRecords()) {
			list.add(new XYChart.Data<Double, Double>(record.getActivityLevel(), record.getTotalCost()));
		}
		ObservableList<Series<Double, Double>> ol = FXCollections.observableArrayList();
		ol.add(series);
		chart.setData(ol);

		fixed.setTextFill(Color.BLACK);
		variablePerUnit.setTextFill(Color.BLACK);
		if(separator.getAccountingRecords().size() > 1) {
			if(option.getSelectedToggle() == halp) {
				separator.presupuestalEquationByHighAndLowPoint();;
			} else {
				separator.presupuestalEquationByLinearRegression();;
			}
			double[] low = separator.getBudgetedLowPoint();
			double[] high = separator.getBudgetedHighPoint();

			series = new XYChart.Series<>();
			series.setName("Trend line");
			list = series.getData();
			list.add(new XYChart.Data<Double, Double>(low[0], low[1]));
			list.add(new XYChart.Data<Double, Double>(high[0], high[1]));
			ol.add(series);
			chart.setData(ol);

			String f = String.format("%.2f", separator.getFixed());
			String v = String.format("%.2f", separator.getVariablePerUnit());
			if(f.contains("-")) {
				fixed.setTextFill(Color.RED);
				fixed.setText("($"+f.replace("-", "")+")");
			} else {
				fixed.setText("$"+f);
			}
			if(v.contains("-")) {
				variablePerUnit.setTextFill(Color.RED);
				variablePerUnit.setText("($"+v+"/unit)");
			} else {
				variablePerUnit.setText("$"+v+"/unit");
			}
		} else {
			fixed.setText("a");
			variablePerUnit.setText("b");
		}

		variablePerUnitE.setTextFill(variablePerUnit.getTextFill());
		variablePerUnitE.setText(variablePerUnit.getText());
		fixedE.setTextFill(fixed.getTextFill());
		fixedE.setText(fixed.getText() + " +");

		table.getItems().clear();
		table.setItems(FXCollections.observableArrayList(separator.getAccountingRecords()));
	}

	@FXML
	public void budget(ActionEvent event) {
		if(!budgetActivityLevel.getText().isEmpty()) {
			Stage popUp = new Stage();
			Label l = new Label("Cost estimate for an activity level of "+budgetActivityLevel.getText()+": \n ~~~> $"+String.format("%.2f", separator.getCostEstimate(Double.parseDouble(budgetActivityLevel.getText().replace(',', '.')))));
			l.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
			Scene scene = new Scene(l);
			popUp.setWidth(550);
			popUp.setHeight(150);
			popUp.setScene(scene);
			popUp.setTitle("Budget "+budgetActivityLevel.getText());
			popUp.showAndWait();
		}
	}
}
