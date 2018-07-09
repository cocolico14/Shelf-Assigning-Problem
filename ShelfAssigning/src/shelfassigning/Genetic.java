/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package shelfassigning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import javafx.util.Pair;

/**
 *
 * @author soheilchangizi
 */
public class Genetic {
    
    private int subSize;
    private Map<String, Integer> population;
    private Table tlb;
    private int populationSize;
    private double mutationProb;
    private int max;
    private ArrayList<Pair<Integer, Integer>> chart;
    private int counter = 0;
    
    public Genetic(int subSize, ArrayList<Node> init, Table tlb,
            double mutationProb, int populationSize) {
        this.subSize = subSize;
        this.population = new ConcurrentHashMap<>();
        this.tlb = tlb;
        this.mutationProb = mutationProb;
        this.populationSize = populationSize;
        this.chart = new ArrayList<>();
        this.max = Integer.MIN_VALUE;
        populate(init);
        fitness();
    }
    
    public String solve(int epochs){
        double allE = epochs;
        ArrayList<String> chromosome;
        while(epochs-->0){
            counter++;
            chromosome = selection();
            chromosome = crossOver(chromosome);
            chromosome = mutation(chromosome, mutationProb);
            for (int i = 0; i < chromosome.size(); i++) {
                this.population.put(chromosome.get(i), 0);
            }
            ArrayList<Node> init = new ArrayList<>();
            for (int i = 0; i < this.populationSize; i++) {
                init.add(new Node(this.tlb.Init(i+1), 0, null));
            }
            populate(init);
            fitness();
        }
        int max = Integer.MIN_VALUE;
        String Chromosome = "";
        for (Map.Entry<String, Integer> entry : population.entrySet()){
            if(entry.getValue() > max){
                Chromosome = entry.getKey();
                max = entry.getValue();
            }
        }
        return toNode(Chromosome)+" : " + max;
    }
    
    public void populate(ArrayList<Node> init){
        String chromosome = "";
        for (Node nodes : init) {
            String[] items = nodes.getItems();
            int[] spot = new int[subSize];
            for (int i = 0; i < items.length; i++) {
                String[] elems = items[i].split(",");
                for (int j = 0; j < elems.length; j++) {
                    spot[Integer.valueOf(elems[j])-1] = i+1; // 112233
                }
            }
            for (int i = 0; i < spot.length; i++) {
                chromosome += spot[i];
            }
            population.put(chromosome, 0);
            chromosome = "";
        }
    }
    
    public void fitness(){
        int fitness = 0;
        ArrayList<String> reject = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : population.entrySet()){
            if(!isValid(entry.getKey())){
                fitness = Integer.MIN_VALUE;
            }else{
                fitness = toNode(entry.getKey()).getHeru(this.tlb);
            }
            entry.setValue(fitness);
            if(fitness > this.max){
                this.max = fitness;
            }
            fitness = 0;
        }
        
        List<Entry<String, Integer>> list = new ArrayList<>(population.entrySet());
        list.sort(Entry.comparingByValue());
        
        int c = 0;
        for (Entry<String, Integer> entry : list) {
            if(this.population.size() - c > this.populationSize){
                reject.add(entry.getKey());
                c++;
            }else{
                break;
            }
        }
        for (Map.Entry<String, Integer> ent : population.entrySet()){
            if(reject.contains(ent.getKey())){
                population.remove(ent.getKey());
            }
        }
        for (Map.Entry<String, Integer> ent : population.entrySet()){
            chart.add(new Pair<Integer, Integer>(counter, ent.getValue()));
            break;
        }
        
    }
    
    public ArrayList<String> selection(){
        ArrayList<String> selected = new ArrayList<>();
        List<Entry<String, Integer>> list = new ArrayList<>(population.entrySet());
        list.sort(Entry.comparingByValue());
        for (Entry<String, Integer> entry : list) {
            selected.add(entry.getKey());
        }
        ArrayList<String> lucky = new ArrayList<>();
        for (int i = 0; i < selected.size()/3; i++) {
            lucky.add(selected.get(i));
        }
        Collections.shuffle(lucky);
        for (int i = 0; i < selected.size()/3; i++) {
            selected.set(i, lucky.get(i));
        }
        if(selected.size() % 2 !=0){
            selected.remove(0);
        }
        return selected;
    }
    
    public ArrayList<String> crossOver(ArrayList<String> selected){
        Collections.shuffle(selected);
        Random gen = new Random(System.currentTimeMillis());
        for (int i = 0; i < selected.size()-1; i++) {
            int slice = gen.nextInt(this.subSize);
            String chrom11 = selected.get(i).substring(0, slice);
            String chrom12 = selected.get(i).substring(slice);
            String chrom21 = selected.get(i+1).substring(0, slice);
            String chrom22 = selected.get(i+1).substring(slice);
            selected.set(i, chrom11+chrom22);
            selected.set(i+1, chrom21+chrom12);
        }
        return selected;
    }
    
    public ArrayList<String> mutation(ArrayList<String> selected, double mutationProb){
        Random gen = new Random(System.currentTimeMillis());
        for (int i = 0; i < selected.size()-1; i++) {
            if(Math.random() < mutationProb){
                int max = Integer.valueOf(selected.get(i).charAt(0)+"");
                for (int j = 0; j < selected.get(i).length(); j++) {
                    if(Integer.valueOf(selected.get(i).charAt(j)+"") > max){
                        max = Integer.valueOf(selected.get(i).charAt(j)+"");
                    }
                }
                if(max + 1 < this.subSize) max++;
                int elem = gen.nextInt(this.subSize);
                int mutate = gen.nextInt(max);
                StringBuilder sb = new StringBuilder(selected.get(i));
                sb.setCharAt(elem, Character.forDigit(mutate+1, 10));
                selected.set(i, sb.toString());
            }
        }
        return selected;
    }
    
    public Map<String, Integer> getPopulation() {
        return population;
    }
    
    public Node toNode(String chromosome){
        int max = Integer.valueOf(chromosome.charAt(0)+"");
        for (int j = 0; j < chromosome.length(); j++) {
            if(Integer.valueOf(chromosome.charAt(j)+"") > max){
                max = Integer.valueOf(chromosome.charAt(j)+"");
            }
        }
        String[] items = new String[max];
        for (int i = 0; i < max; i++) {
            items[i] = "";
        }
        for (int j = 0; j < chromosome.length(); j++) {
            items[Integer.valueOf(chromosome.charAt(j)+"")-1] += (j+1) + ",";
        }
        return new Node(items, 0, null);
    }
    
    public boolean isValid(String chromosome){
        int max = Integer.valueOf(chromosome.charAt(0)+"");
        for (int j = 0; j < chromosome.length(); j++) {
            if(Integer.valueOf(chromosome.charAt(j)+"") > max){
                max = Integer.valueOf(chromosome.charAt(j)+"");
            }
        }
        for (int i = 0; i < max; i++) {
            if(chromosome.indexOf('0' + (i+1)) == -1) return false;
        }
        return true;
    }

    public ArrayList<Pair<Integer, Integer>> getChart() {
        return chart;
    }
    
    
    
}
