/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package shelfassigning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 *
 * @author soheilchangizi
 */
public class Table {
    private List<Integer>[] subs;
    public final int subsSize;
    
    public Table(List<Integer>[] subs) {
        this.subs = subs;
        this.subsSize = subs.length;
    }
    
    public String[] Init(){
        ArrayList<String> res = new ArrayList<>();
        Stack<Integer> init = new Stack<>();
        Random gen = new Random(System.currentTimeMillis());
        
        for (int i = 0; i < subsSize; i++) {
            init.push(i+1);
        }
        Collections.shuffle(init, gen);
        int n = gen.nextInt(init.size());
        String tmp = "";
        while(!init.isEmpty()){
            if(init.size() == 1) tmp += init.pop() + ",";
            else{
                for (int j = 0; j < n; j++) {
                    tmp += init.pop() + ",";
                }
                n = gen.nextInt(init.size()); // ["1,2,3", "4"]
            }
            if(tmp.length()>0){
                tmp = tmp.substring(0, tmp.length() - 1);
                res.add(tmp);
            }
            tmp = "";
        }
        return res.toArray(new String[res.size()]);
    }
    
    
    
    public String[] Init(long seed){
        ArrayList<String> res = new ArrayList<>();
        Stack<Integer> init = new Stack<>();
        Random gen = new Random(System.currentTimeMillis() + seed);
        
        for (int i = 0; i < subsSize; i++) {
            init.push(i+1);
        }
        Collections.shuffle(init, gen);
        int n = gen.nextInt(init.size());
        String tmp = "";
        int random = gen.nextInt(init.size()/2);
        if(random == 0){
            if(Math.random() > 0.4) random+=1;
            else random+=2;
        }
        while(random-->0){
            for (int j = 0; j < n; j++) {
                tmp += init.pop() + ",";
            }
            n = gen.nextInt(init.size());
            if(tmp.length()>0){
                tmp = tmp.substring(0, tmp.length() - 1);
                res.add(tmp);
            }
            tmp = "";
        }
        tmp = "";
        while(!init.isEmpty()){
            tmp += init.pop() + ",";
        }
        if(tmp.length()>0){
            tmp = tmp.substring(0, tmp.length() - 1);
            res.add(tmp);
        }
        return res.toArray(new String[res.size()]);
    }
    
    public List<Integer> getDanger(int i){
        return subs[i];
    }
    
    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < this.subs.length; i++) {
            result += "Subs #" + (i+1) + " Danger Subs: " + this.subs[i] + "\n";
        }
        return result;
    }
    
    
}
