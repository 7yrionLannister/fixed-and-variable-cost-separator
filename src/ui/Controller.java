package ui;

import java.io.File;

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
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Record;
import model.Separator;


public class Controller {

	/**Line chart that is going to represent the records and the budget function
	 * */
	@FXML
	private LineChart<Double, Double> chart;

	/**Column of the table containing information about the activity level of the records
	 * */
	@FXML
	private TableColumn<Record, Number> activityColumn;

	/**Column of the table containing information about the total cost of the records
	 * */
	@FXML
	private TableColumn<Record, Number> costColumn;

	/**The text field that receives input for the activity level of the record to be added
	 * */
	@FXML
	private TextField activityLevelIn;

	/**The text field that receives input for the cost of the record to be added
	 * */
	@FXML
	private TextField costIn;

	/**The text field that receives input for the activity level of the record to be removed
	 * */
	@FXML
	private TextField activityLevelOut;

	/**Table containing the attributes of the records in two separate columns
	 * */
	@FXML
	private TableView<Record> table;

	/**Toggle group containing the radio buttons representing the ways the budget function can be computed
	 * */
	@FXML
	private ToggleGroup option;

	/**Label containing the budgeted fixed cost
	 * */
	@FXML
	private Label fixed;

	/**Label containing the budgeted variable cost per unit
	 */
	@FXML
	private Label variablePerUnit;

	/**Label containing the budgeted fixed cost
	 * */
	@FXML
	private Label fixedE;

	/**Label containing the budgeted variable cost per unit
	 */
	@FXML
	private Label variablePerUnitE;

	/**Radio button representing the high and low point method
	 * */
	@FXML
	private RadioButton halp;

	/**Radio button representing the linear regression method
	 * */
	@FXML
	private RadioButton lr;

	/**Button that triggers the high and low point or linear regression method to separate the components of the cost
	 * */
	@FXML
	private TextField budgetActivityLevel;

	/**This separator is in charge of all the logic of the problem(finding budget functions)
	 * */
	private Separator separator;

	/**Set the initial state of the GUI components and initialize the Separator
	 * */
	@FXML
	public void initialize() {
		activityColumn.setCellValueFactory(new PropertyValueFactory<Record, Number>("activityLevel"));
		costColumn.setCellValueFactory(new PropertyValueFactory<Record, Number>("totalCost"));
		separator = new Separator();
	}

	/**Remove a record by specifying its activity level
	 * */
	@FXML
	public void exclude(ActionEvent event) {
		if(!activityLevelOut.getText().isEmpty()) {
			separator.removeRecord(Double.parseDouble(activityLevelOut.getText().replace(',', '.')));
			activityLevelOut.setText("");
		}
		refresh();
	}

	/**Add a new record by specifying its activity level and its associated cost
	 * */
	@FXML
	public void include(ActionEvent event) {
		if(!activityLevelIn.getText().isEmpty() && !costIn.getText().isEmpty()) {
			separator.addRecord(Double.parseDouble(activityLevelIn.getText().replace(',', '.')), Double.parseDouble(costIn.getText().replace(',', '.')));
			activityLevelIn.setText("");
			costIn.setText("");
		}
		refresh();
	}

	/**Clear the register
	 * */
	@FXML
	public void clear(ActionEvent event) {
		separator.clear();
		refresh();
	}

	/**Verify if an illegal numerical character is being added to the text fields and ignore them if it's the case
	 * */
	@FXML
	public void onlyIntegers(KeyEvent event) {
		char input =event.getCharacter().charAt(0);
		TextField src = (TextField)event.getSource();
		if(!((input >= '0' && input <= '9') || input == '\b') && !((input == ',' || input == '.') && !(src.getText().contains(",")  || src.getText().contains(".")))) {
			event.consume();
		}
	}

	/**This method recomputes the budget function every time the user selects a radio button
	 * */
	@FXML
	public void recompute(ActionEvent event) {
		refresh();
	}

	/**Refresh the GUI and the budget function
	 * */
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
				separator.budgetFunctionByHighAndLowPoint();;
			} else {
				separator.budgetFunctionByLinearRegression();;
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
				variablePerUnit.setText("($"+v+"/unidad)");
			} else {
				variablePerUnit.setText("$"+v+"/unidad");
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

	/**This method triggers the budget function in the Separator class and places the result in a pop-up window 
	 * */
	@FXML
	public void budget(ActionEvent event) {
		if(!budgetActivityLevel.getText().isEmpty()) {
			Stage popUp = new Stage();
			Label l = new Label("El costo estimado para un nivel de actividad de "+budgetActivityLevel.getText()+" unidades es: \n ~~~> $"+String.format("%.2f", separator.getCostEstimate(Double.parseDouble(budgetActivityLevel.getText().replace(',', '.')))));
			l.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
			Scene scene = new Scene(l);
			popUp.setWidth(620);
			popUp.setHeight(150);
			popUp.setScene(scene);
			popUp.setTitle("Presupuesto ("+budgetActivityLevel.getText()+" unidades)");
			popUp.getIcons().add(new Image(new File("icon.svg.png").toURI().toString()));
			popUp.showAndWait();
		}
	}
}
