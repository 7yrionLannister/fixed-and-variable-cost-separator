package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;


public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("vs.fxml"));
		Scene s = new Scene(root);
		primaryStage.setScene(s);
		primaryStage.setResizable(false); //FIXME :o
		primaryStage.show();
	}

	/*@Override
	public void start(Stage stage) {
	    final LineChart<Number,Number> sc = new LineChart<>(new NumberAxis(),new NumberAxis());

	    XYChart.Series series1 = new XYChart.Series();
	    series1.setName("Equities");
	    series1.getData().add(new XYChart.Data(4.2, 193.2));
	    series1.getData().add(new XYChart.Data(2.8, 33.6));
	    series1.getData().add(new XYChart.Data(6.8, 23.6));

	    XYChart.Series series2 = new XYChart.Series();
	    series2.setName("Mutual funds");
	    series2.getData().add(new XYChart.Data(5.2, 229.2));
	    series2.getData().add(new XYChart.Data(2.4, 37.6));
	    series2.getData().add(new XYChart.Data(6.4, 15.6));

	    sc.setAnimated(false);
	    sc.setCreateSymbols(true);

	    sc.getData().addAll(series1, series2);

	    Scene scene  = new Scene(sc, 500, 400);
	    scene.getStylesheets().add(getClass().getResource("chart.css").toExternalForm());
	    stage.setScene(scene);
	    stage.show();
	}*/
}
