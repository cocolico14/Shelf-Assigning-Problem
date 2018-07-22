# Shelf-Assigning-Problem
Solving Shelf Assigning Problem with Hill Climbing, Simulated Annealing and Genetic Algorithms

![overview image](./assets/1-Overview.png)

<hr />

## Documentation

  Tree and Node Class:
  
  - Making Tree Data Structure for Hill Climbing and Simulated Annealing
  - String[] items is the initial state given in input
  - ArrayList<Node> is arraylist of children made in addChildren method
  - addChildren(int n) method in Node Class : making n nodes and each node is splited item into different shelfs randomly and randomize order of each item in shelfs
    - for example : ["1, 2, 3, 4, 5"] (1 distinct shelf contain 5 item - initial state) -> ["2, 3", "1, 4, 5"] (2 distinct shelf each contain 2 and 3 items respectively - each node is array of string and each string represent each shelf)
        
