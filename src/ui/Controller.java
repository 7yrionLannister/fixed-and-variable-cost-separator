package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import model.Record;


public class Controller {

    @FXML
    private LineChart<String, Integer> chart;

    @FXML
    private TableColumn<Record, Number> activityColumn;

    @FXML
    private TableColumn<Record, Number> costColumn;
    
    //TODO quitar las parametrizaciones si causan problemas
    @FXML
    public void initialize() {
    	Series<String, Integer> series = new XYChart.Series<>();
         Callback<TableColumn<Record, Number>, TableCell<Record, Number>> cellFactory =
              new Callback<TableColumn<Record, Number>, TableCell<Record, Number>>() {
                  public TableCell<Record, Number> call(TableColumn<Record, Number> p) {
                     return new EditingCell();
                  }
              };
    	ObservableList<Data<String, Integer>> list = series.getData();
    	for(int i = 1; i < 30; i++) {
    		list.add(new XYChart.Data<String, Integer>(i+"", (i+(int)(Math.random()*10))));
    	}
    	ObservableList<Series<String, Integer>> ol = FXCollections.observableArrayList();
    	ol.add(series);
    	chart.setData(ol);
    }
}
