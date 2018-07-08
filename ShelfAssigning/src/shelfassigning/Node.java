/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package shelfassigning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

/**
 *
 * @author soheilchangizi
 */
public class Node {
    private String[] items;
    private Node parrent;
    private ArrayList<Node> children;
    private int depth;
    
    public Node(String[] items, int depth, Node parrent) {
        this.items = items;
        this.depth = depth;
        this.parrent = parrent;
        children = new ArrayList<>();
    }
    
    public void setParrent(Node parrent) {
        this.parrent = parrent;
    }
    
    static void shuffleArray(String[] ar){
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--){
            int index = rnd.nextInt(i + 1);
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
    
    public void addChilden(int n){
        int cursor = 0;
        int counter = 0;
        String tmp1 = "";
        String tmp2 = "";
        int exp =0;
        if(items.length != n){
            while(cursor != n){
                for (int i = 0; i < items.length; i++) {
                    String[] elems = items[i].split(",");
                    shuffleArray(elems);
                    if(cursor < counter + elems.length){
                        int j = cursor - counter;
                        for (int k = 0; k <= j; k++) {
                            tmp1 += elems[k] + ",";
                        }
                        for(int k = j+1; k<elems.length; k++){
                            tmp2 += elems[k] + ",";
                        }
                        if(tmp1.length()>0) tmp1 = tmp1.substring(0, tmp1.length() - 1);
                        if(tmp2.length()>0) tmp2 = tmp2.substring(0, tmp2.length() - 1);
                        exp = i;
                        cursor++;
                        break;
                    }
                    counter += elems.length;
                }
                String[] newItem = new String[items.length+1];
                int j = 0;
                if(tmp1.length() > 0  && tmp2.length() > 0){
                    for (int i = 0; i < items.length; i++) {
                        if(i == exp){
                            newItem[j] = tmp1;
                            newItem[j+1] = tmp2;
                            j+=2;
                        }else{
                            newItem[j] = items[i];
                            j++;
                        }
                    }
                    this.children.add(new Node(newItem, this.depth+1, this));
                }
                tmp1 = "";
                tmp2 = "";
                counter = 0;
            }
        }
        if(items.length != 1){
            int c = 0;
            for (int j = 0; j < items.length-1; j++) {
                String[] tmp = new String[items.length-1];
                for (int i = 0; i < items.length; i++) {
                    if(c == items.length-1) break;
                    if(i == j){
                        String[] elems = items[j].split(",");
                        String[] elems2 = items[j+1].split(",");
                        String[] both = Stream.concat(Arrays.stream(elems), Arrays.stream(elems2))
                                .toArray(String[]::new);
                        String res = "";
                        for (String string : both) {
                            if(!string.equals(null)) res += string.trim() + ",";
                        }
                        if(res.length()>0) res = res.substring(0, res.length() - 1);
                        tmp[c] = res;
                        c++;
                        i++;
                    }else{
                        tmp[c] = items[i];
                        c++;
                    }
                }
                c = 0;
                this.children.add(new Node(tmp, this.depth+1, this));
            }
        }
    }
    
    public ArrayList<Node> getChildren() {
        return children;
    }
    
    public int getDepth() {
        return depth;
    }
    
    public Node getParrent() {
        return parrent;
    }

    public String[] getItems() {
        return items;
    }
    
    public int getHeru(Table tlb) {
        int heru = 0;
        for (int i = 0; i < items.length; i++) {
            String[] elems = items[i].split(",");
            for (int j = 0; j < elems.length; j++) {
                for (int k = j; k < elems.length; k++) {
                    if(tlb.getDanger(Integer.valueOf(elems[j])-1).contains(Integer.valueOf(elems[k]))){
                        heru -= 150;
                    }
                }
            }
        }
        heru -= 100 * items.length;
        return heru;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Arrays.deepHashCode(this.items);
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;
        if (!Arrays.deepEquals(this.items, other.items)) {
            return false;
        }
        return true;
    }
    
    
    
    @Override
    public String toString() {
        String res = "";
        for (int i = 0; i < items.length; i++) {
            res += Arrays.toString(items[i].split(",")) + "";
        }
        return res;
    }
    
}
