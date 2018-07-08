/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shelfassigning;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedTransferQueue;

/**
 *
 * @author soheilchangizi
 */
public class Tree {
    private Node root;
    private Table tbl;
    
    public Tree(Table tbl) {
        String[] r = tbl.Init(0);
        this.root = new Node(r, 1, null);
        this.tbl = tbl;
    }

    public Node getRoot() {
        return root;
    }
    
    
}
