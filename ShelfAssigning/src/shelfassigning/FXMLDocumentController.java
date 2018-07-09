/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package shelfassigning;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.util.Pair;

/**
 *
 * @author soheilchangizi
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private TextArea input;
    
    @FXML
    private Button runButton;
    
    @FXML
    private Button refreshGraphButton;
    
    @FXML
    private TextArea output;
    
    @FXML
    private ChoiceBox method;
    
    @FXML
    private LineChart<Number,Number> chart;
    
    @FXML
    private NumberAxis xAxis;
    
    @FXML
    private NumberAxis yAxis;
    
    @FXML
    private TextField numberOfRuns;
    
    @FXML
    private TextField temperatureSA;
    
    @FXML
    private TextField rateSA;
    
    @FXML
    private TextField epochNumGenetic;
    
    @FXML
    private TextField populationSizeGenetic;
    
    @FXML
    private TextField mutationRateGenetic;
    
    
    @FXML
    private void handleRunButtonAction(ActionEvent event) {
        
        String lines[] = input.getText().split("\\r?\\n");
        //try{
        int n = Integer.valueOf(lines[0]);
        
        List<Integer>[] subs = new List[n];
        for (int i = 0; i < n; i++) {
            subs[i] = new ArrayList<>();
        }
        int elem = 0;
        for (int i = 1; i < n; i++) {
            String[] parts = lines[i].split("\\s+");
            elem = Integer.valueOf(parts[0].trim());
            String[] items = parts[1].split(",");
            for (String item : items) {
                subs[elem-1].add(Integer.valueOf(item.trim()));
            }
        }
        
        Table tbl = new Table(subs);
        NumberFormat formatter = new DecimalFormat("#0.00000");
        
        if(method.getValue().equals("Hill Climbing")){
            
            String res = "";
            long avgTime = 0;
            int maxH = Integer.MIN_VALUE;
            ArrayList<Pair<Integer, Integer>> bestData = new ArrayList<>();
            
            for (int i = 0; i < Integer.valueOf(numberOfRuns.getText()); i++) {
                HillClimbing hc = new HillClimbing(tbl);
                long startTime = System.currentTimeMillis();
                String output = hc.solve();
                if(maxH < Integer.valueOf(output.split(":")[1].trim())){
                    maxH = Integer.valueOf(output.split(":")[1].trim());
                    bestData = hc.getChart();
                }
                res += "Run#" + (i+1) + " -> " + output + "\n";
                long endTime = System.currentTimeMillis();
                long totalTime = endTime - startTime;
                avgTime += totalTime;
            }
            avgTime /= 10;
            output.setText(res + "\nAverage Time: " + formatter.format((avgTime) / 1000d) + "s");
            
            //Chart Plot
            XYChart.Series series = new XYChart.Series();
            series.setName("HC");
            for (Pair<Integer, Integer> pair : bestData) {
                series.getData().add(new XYChart.Data(pair.getKey(), pair.getValue()));
            }
            chart.getData().addAll(series);
            chart.getData().sorted();
            
        }else if(method.getValue().equals("Simulated Annealing")){
            
            String res = "";
            long avgTime = 0;
            int maxH = Integer.MIN_VALUE;
            ArrayList<Pair<Integer, Integer>> bestData = new ArrayList<>();
            
            for (int i = 0; i < Integer.valueOf(numberOfRuns.getText()); i++) {
                SimulatedAnnealing sa
                        = new SimulatedAnnealing(tbl,
                                Integer.valueOf(temperatureSA.getText()),
                                Double.valueOf(rateSA.getText()));
                long startTime = System.currentTimeMillis();
                String output = sa.solve();
                if(maxH < Integer.valueOf(output.split(":")[1].trim())){
                    maxH = Integer.valueOf(output.split(":")[1].trim());
                    bestData = sa.getChart();
                }
                res += "Run#" + (i+1) + " -> " + output + "\n";
                long endTime = System.currentTimeMillis();
                long totalTime = endTime - startTime;
                avgTime += totalTime;
            }
            avgTime /= 10;
            output.setText(res + "\nAverage Time: " + formatter.format((avgTime) / 1000d) + "s");
            
            //Chart Plot
            XYChart.Series series = new XYChart.Series();
            series.setName("SA");
            for (Pair<Integer, Integer> pair : bestData) {
                series.getData().add(new XYChart.Data(pair.getKey(), pair.getValue()));
            }
            chart.getData().addAll(series);
            chart.getData().sorted();
            
        }else{
            
            String res = "";
            long avgTime = 0;
            int maxH = Integer.MIN_VALUE;
            ArrayList<Pair<Integer, Integer>> bestData = new ArrayList<>();
            
            for (int j = 0; j < Integer.valueOf(numberOfRuns.getText()); j++) {
                ArrayList<Node> init = new ArrayList<>();
                for (int i = 0; i < Integer.valueOf(populationSizeGenetic.getText()); i++) {
                    init.add(new Node(tbl.Init(i+1), 0, null));
                }
                Genetic g
                        = new Genetic(tbl.subsSize, init, tbl,
                                Double.valueOf(mutationRateGenetic.getText()),
                                Integer.valueOf(populationSizeGenetic.getText()));
                String output = g.solve(Integer.valueOf(epochNumGenetic.getText()));
                if(maxH < Integer.valueOf(output.split(":")[1].trim())){
                    maxH = Integer.valueOf(output.split(":")[1].trim());
                    bestData = g.getChart();
                }
                long startTime = System.currentTimeMillis();
                res += "Run#" + (j+1) + " -> "
                        + output + "\n";
                long endTime = System.currentTimeMillis();
                long totalTime = endTime - startTime;
                avgTime += totalTime;
            }
            avgTime /= 10;
            output.setText(res + "\nAverage Time: " + formatter.format((avgTime) / 1000d) + "s");
            
            //Chart Plot
            XYChart.Series series = new XYChart.Series();
            series.setName("Gen");
            for (Pair<Integer, Integer> pair : bestData) {
                series.getData().add(new XYChart.Data(pair.getKey(), pair.getValue()));
            }
            chart.getData().addAll(series);
            chart.getData().sorted();
        }
        
//        }catch(Exception e){
//            output.setText("Error: Bad Input " + e);
//        }
    }
    
    @FXML
    private void handleRefreshGraphButtonAction(ActionEvent event) {
        chart.getData().clear();
    }
    
    
    @FXML
    private void choiceBoxAction(ActionEvent event){
        
        if(method.getValue().equals("Hill Climbing")){
            temperatureSA.setDisable(true);
            rateSA.setDisable(true);
            epochNumGenetic.setDisable(true);
            populationSizeGenetic.setDisable(true);
            mutationRateGenetic.setDisable(true);
        }else if(method.getValue().equals("Simulated Annealing")){
            temperatureSA.setDisable(false);
            rateSA.setDisable(false);
            epochNumGenetic.setDisable(true);
            populationSizeGenetic.setDisable(true);
            mutationRateGenetic.setDisable(true);
        }else{
            temperatureSA.setDisable(true);
            rateSA.setDisable(true);
            epochNumGenetic.setDisable(false);
            populationSizeGenetic.setDisable(false);
            mutationRateGenetic.setDisable(false);
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        output.setEditable(false);
        input.setText("6\n1 2,3,5\n2 1,4,6\n3 1,4,5\n4 2,3,5\n5 1,4,3\n6 2");
        
        method.getItems().removeAll(method.getItems());
        method.getItems().addAll("Hill Climbing", "Simulated Annealing", "Genetic");
        method.getSelectionModel().select("Hill Climbing");
        
        numberOfRuns.setText("10");
        temperatureSA.setText("10000");
        rateSA.setText("0.01");
        epochNumGenetic.setText("20");
        populationSizeGenetic.setText("50");
        mutationRateGenetic.setText("0.1");
        
    }
    
}
