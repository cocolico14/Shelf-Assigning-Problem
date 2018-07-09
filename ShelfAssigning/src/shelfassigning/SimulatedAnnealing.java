/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package shelfassigning;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;
import javafx.util.Pair;

/**
 *
 * @author soheilchangizi
 */
public class SimulatedAnnealing {
    
    private Table tbl;
    private Tree tree;
    private double temp;
    private final double rate;
    private ArrayList<Pair<Integer, Integer>> chart;
    
    public SimulatedAnnealing(Table tbl, double temp, double rate) {
        this.tbl = tbl;
        this.temp = temp;
        this.rate = rate;
        this.chart = new ArrayList<>();
    }
    
    public String solve(){
        this.tree = new Tree(tbl);
        return sa();
    }
    
    public double accProb(int energy, int newEnergy, double temperature) {
        if (newEnergy > energy) {
            return 1.0;
        }
        return Math.exp((newEnergy - energy) / temperature);
    }
    
    public String sa(){
        Node bestNode = this.tree.getRoot();
        int counter = 0;
        while(temp > 1){
            bestNode.addChilden(tbl.subsSize);
            chart.add(new Pair<Integer, Integer>(counter, bestNode.getHeru(tbl)));
            Random rnd = new Random(System.currentTimeMillis());
            Node neighbor = bestNode;
            neighbor = bestNode.getChildren().get(rnd.nextInt(bestNode.getChildren().size()));
            if(accProb(bestNode.getHeru(tbl), neighbor.getHeru(tbl), temp)
                    > Math.random()){
                bestNode = neighbor;
            }
            temp *= 1-rate;
            counter++;
        }
        return bestNode + " : " + bestNode.getHeru(tbl);
    }
    
    public ArrayList<Pair<Integer, Integer>> getChart() {
        return chart;
    }
    
    
}
