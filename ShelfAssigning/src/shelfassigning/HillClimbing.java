/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package shelfassigning;

import java.util.ArrayList;
import javafx.util.Pair;

/**
 *
 * @author soheilchangizi
 */
public class HillClimbing {
    
    private Table tbl;
    private Tree tree;
    private ArrayList<Pair<String, Integer>> chart;
    
    public HillClimbing(Table tbl) {
        this.tbl = tbl;
        this.chart = new ArrayList<>();
    }
    
    public String solve(){
        this.tree = new Tree(tbl);
        return hc();
    }
    
    public String hc(){
        String[] dummy = new String[1];
        Node bestNode = tree.getRoot();
        int counter = 0;
        while(true){
            bestNode.addChilden(tbl.subsSize);
            chart.add(new Pair<String, Integer>(Integer.toString(counter), bestNode.getHeru(tbl)));
            Node neighbor = bestNode.getChildren().get(0);
            for (Node pz : bestNode.getChildren()) {
                if(pz.getHeru(tbl) > neighbor.getHeru(tbl)){
                    neighbor = pz;
                }
            }
            if(bestNode.getHeru(tbl) < neighbor.getHeru(tbl)){
                bestNode = neighbor;
            }else{
                return bestNode + " : " + bestNode.getHeru(tbl);
            }
            counter++;
        }
    }

    public ArrayList<Pair<String, Integer>> getChart() {
        return chart;
    }
    
    
}
