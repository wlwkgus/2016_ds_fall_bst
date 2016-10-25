// (Nearly) Optimal Binary Search Tree
// Bongki Moon (bkmoon@snu.ac.kr), Sep/23/2014

import java.util.ArrayList;

public class BST { // Binary Search Tree implementation

  protected boolean NOBSTified = false;
  protected boolean OBSTified = false;
  protected int nodeCount = 0;
  protected Node rootNode = null;
  protected float freqSum = this.sumFreq();

  public BST() {

  }

  public int size() {return this.nodeCount;}

  public void insert(String key) {
    if (this.rootNode == null){
      this.rootNode = new Node(key);
      nodeCount++;
    }
    else{
      Node node = this.rootNode;

      while(true){
        if(key.compareTo(node.getKey()) < 0){
          if(node.getLeftNode() == null){
            this.nodeCount++;
            node.setLeftNode(new Node(key));
            break;
          }
          else{
            node = node.getLeftNode();
          }
        }

        else if(key.compareTo(node.getKey()) == 0){
          node.incFrequency();
          break;
        }

        else {
          if(node.getRightNode() == null){
            node.setRightNode(new Node(key));
            this.nodeCount++;
            break;
          }
          else{
            node = node.getRightNode();
          }
        }
      }

    }
    freqSum = this.freqSum + 1;
  }

  public void insertFreq(String key, int freq) {
    if (this.rootNode == null){
      this.rootNode = new Node(key, freq);
      nodeCount++;
    }
    else{
      Node node = this.rootNode;
      while(true){
        if(key.compareTo(node.getKey()) < 0){
          if(node.getLeftNode() == null){
            this.nodeCount++;
            node.setLeftNode(new Node(key, freq));
            break;
          }
          else{
            node = node.getLeftNode();
          }
        }

        else if(key.compareTo(node.getKey()) == 0){
          node.incFrequency(freq);
          break;
        }

        else {
          if(node.getRightNode() == null){
            node.setRightNode(new Node(key, freq));
            this.nodeCount++;
            break;
          }
          else{
            node = node.getRightNode();
          }
        }
      }
    }
    freqSum = this.freqSum + freq;
  }

  public boolean find(String key) {

    return rootNode != null && findHelper(key, this.rootNode);

  }

  protected boolean findHelper(String key, Node node){
    node.incAccessCount();

    if(key.compareTo(node.getKey()) < 0){
      if(node.getLeftNode() == null) return false;
      else return findHelper(key, node.getLeftNode());
    }

    else if(key.compareTo(node.getKey()) == 0){
      return true;
    }

    else {
      if(node.getRightNode() == null) return false;
      else return findHelper(key, node.getRightNode());
    }

  }

  public int sumFreq() {
    return sumFreqHelper(this.rootNode);
  }

  protected int sumFreqHelper(Node node){

    if(node == null) return 0;
    return node.getFrequency() + sumFreqHelper(node.getLeftNode()) + sumFreqHelper(node.getRightNode());

  }

  public int sumProbes() {
    return sumProbesHelper(this.rootNode);
  }

  protected int sumProbesHelper(Node node){
    if(node == null) return 0;
    return node.getAccessCount() + sumProbesHelper(node.getLeftNode()) + sumProbesHelper(node.getRightNode());
  }

  public int sumWeightedPath() {
    return sumWeightedPathHelper(this.rootNode, 0);
  }

  protected int sumWeightedPathHelper(Node node, int level){
    if(node == null) return 0;
    return node.getFrequency() * (level + 1) +
            sumWeightedPathHelper(node.getLeftNode(), level + 1) +
            sumWeightedPathHelper(node.getRightNode(), level + 1);
  }

  public void resetCounters() {
    resetCountersHelper(this.rootNode);
  }

  protected void resetCountersHelper(Node node){
    if(node == null) return;
    node.reset();
    resetCountersHelper(node.getLeftNode());
    resetCountersHelper(node.getRightNode());
  }


  public void nobst() {
    ArrayList<Node> nodeListSortedByKey = new ArrayList<> ();
    this.initializeNodeList(this.rootNode, nodeListSortedByKey);

    BST nobst = new BST();
    nobstInsertHelper(0, nodeCount-1, nobst, nodeListSortedByKey, (int) freqSum);

    this.rootNode = nobst.rootNode;
    NOBSTified = true;

  }	// Set NOBSTified to true.

  private void nobstInsertHelper(int from, int to, BST nobst, ArrayList<Node> nodeList, int frequencySum){
    if(from == to){
//      for(int i=0; i<nodeList.get(from).getFrequency(); i++) nobst.insert(nodeList.get(from).getKey());
      nobst.insertFreq(nodeList.get(from).getKey(), nodeList.get(from).getFrequency());
      return;
    } else if (from > to) return;
    int indexFrom = from, indexTo = to, middle;
    int leftSum, rightSum;

    while(true){
      leftSum = 0;

      if (indexFrom + 1 == indexTo){
        middle = indexFrom;
        for(int i=from; i<middle; i++) leftSum += nodeList.get(i).getFrequency();
        rightSum = frequencySum - leftSum - nodeList.get(middle).getFrequency();
        int newLeftSum = leftSum + nodeList.get(middle).getFrequency();
        int newRightSum = frequencySum - newLeftSum - nodeList.get(middle+1).getFrequency();
        if(Math.abs(((double)(leftSum - rightSum))) <= Math.abs((double)(newLeftSum - newRightSum))){
          break;
        } else{
          middle = indexTo;
          leftSum = newLeftSum;
          rightSum = newRightSum;
          break;
        }
      }

      middle = (indexFrom + indexTo + 1) / 2;
      for(int i=from; i<middle; i++) leftSum += nodeList.get(i).getFrequency();
      rightSum = frequencySum - leftSum - nodeList.get(middle).getFrequency();

      if(leftSum < rightSum){
        indexFrom = middle;
      } else if (leftSum > rightSum){
        indexTo = middle;
      } else{
        break;
      }
    }

//    for (int i = 0; i < nodeList.get(middle).getFrequency(); i++) nobst.insert(nodeList.get(middle).getKey());
    nobst.insertFreq(nodeList.get(middle).getKey(), nodeList.get(middle).getFrequency());
    nobstInsertHelper(from, middle - 1, nobst, nodeList, leftSum);
    nobstInsertHelper(middle + 1, to, nobst, nodeList, rightSum);

  }


  public void obst() {
    int[][] valueMemoizeTable = new int[nodeCount+1][nodeCount+1];
    int[][] rootMemoizeTable = new int[nodeCount+1][nodeCount+1];
    int[][] freqSumMemoizeTable = new int[nodeCount+1][nodeCount+1];
    int maxInt = 999999999;

    ArrayList<Node> nodeListSortedByKey = new ArrayList<> ();
    this.initializeNodeList(this.rootNode, nodeListSortedByKey);

    //    initialize Table
    for(int i=0; i< nodeCount; i++){
      valueMemoizeTable[i][i] = nodeListSortedByKey.get(i).getFrequency();
      rootMemoizeTable[i][i] = i;
    }


//    Bottom up DP

    System.out.println("System constructing");

    for(int i=nodeListSortedByKey.size() - 1; i>=0 ; i--){ // select i, j
      for(int j=i; j<= nodeListSortedByKey.size()-1; j++){
        int temp = maxInt;
        int newResult;
        for(int r= i==j? i : rootMemoizeTable[i][j-1]; r <= (i==j? i : rootMemoizeTable[i+1][j]); r++){
          newResult = getValueFromValueMemoizeTable(i, r-1, valueMemoizeTable)
                  + getValueFromValueMemoizeTable(r+1, j, valueMemoizeTable)
                  + getFreqSumByRange(i, j, nodeListSortedByKey, freqSumMemoizeTable);
//          if(i == 1){
//            System.out.println("i:" + i + " " + "r:" + r);
//            System.out.println(newResult);
//            System.out.println(temp);
//          }

          if (temp > newResult){
            temp = newResult;
            valueMemoizeTable[i][j] = temp;
            rootMemoizeTable[i][j] = r;
          }

        }
      }

    }

    System.out.println("construct finish");

//    System.out.println("this is it!");
//    System.out.println(valueMemoizeTable[1][2]);
//    System.out.println(rootMemoizeTable[1][2]);
    BST obst = new BST();
    obstInsertHelper(0, nodeCount - 1, rootMemoizeTable, nodeListSortedByKey, obst);

    this.rootNode = obst.rootNode;
    OBSTified = true;

  }	// Set OBSTified to true.

  private void obstInsertHelper(int from, int to, int[][] rootMemoizeTable, ArrayList<Node> nodeListSortedByKey, BST obst){
    if (from > to) return;
    int rootNodeIndex = rootMemoizeTable[from][to];
//    for(int i=0; i < nodeListSortedByKey.get(rootNodeIndex).getFrequency(); i++){
//      obst.insert(nodeListSortedByKey.get(rootNodeIndex).getKey());
//    }
    obst.insertFreq(nodeListSortedByKey.get(rootNodeIndex).getKey(), nodeListSortedByKey.get(rootNodeIndex).getFrequency());
    obstInsertHelper(from, rootNodeIndex - 1, rootMemoizeTable, nodeListSortedByKey, obst);
    obstInsertHelper(rootNodeIndex + 1, to, rootMemoizeTable, nodeListSortedByKey, obst);
  }


  private int getValueFromValueMemoizeTable(int fromIndex, int toIndex, int[][] valueMemoizeTable){
    if (toIndex < fromIndex) return 0;
    return valueMemoizeTable[fromIndex][toIndex];
  }

  private int getFreqSumByRange(int fromIndex, int toIndex, ArrayList<Node> nodeListSortedByKey, int[][] freqSumMemiozeTable){
    if (freqSumMemiozeTable[fromIndex][toIndex] != 0) return freqSumMemiozeTable[fromIndex][toIndex];
    if (fromIndex > toIndex) return 0;
//    for(int i=fromIndex; i <= toIndex; i++){
//      freqSum += nodeListSortedByKey.get(i).getFrequency();
//    }
    int freqSum = getFreqSumFromFreqSumMemoizeTable(fromIndex + 1, toIndex, freqSumMemiozeTable)
            + nodeListSortedByKey.get(fromIndex).getFrequency();
    freqSumMemiozeTable[fromIndex][toIndex] = freqSum;
    return freqSum;
  }

  private int getFreqSumFromFreqSumMemoizeTable(int fromIndex, int toIndex, int[][] freqSumMemoizeTable){
    if(fromIndex > toIndex) return 0;
    return freqSumMemoizeTable[fromIndex][toIndex];
  }

  private void initializeNodeList(Node node, ArrayList<Node> nodeList){
    if (node == null) return;
    initializeNodeList(node.getLeftNode(), nodeList);
    nodeList.add(node);
    initializeNodeList(node.getRightNode(), nodeList);
  }



  public void print() {
    printHelper(this.rootNode);
  }

  private void printHelper(Node node){
    if (node == null) return;
    printHelper(node.getLeftNode());
    System.out.println("[" + node.getKey() + ":" + node.getFrequency() + ":" + node.getAccessCount() + "]");
    printHelper(node.getRightNode());
  }

  public static void main(String args[]){
    BST bst = new BST();
    bst.insert("a");
    bst.insert("b");
    bst.insert("c");
    bst.insert("d");
    bst.insert("b");
    bst.insert("c");
    bst.insert("d");
    bst.insert("c");
    bst.insert("d");
    bst.insert("c");
//    for(int i=0; i<80000; i++){
//      bst.insert(Integer.toString((int) (Math.random() * 2000)));
//    }
    bst.print();
    System.out.println();
    bst.obst();
    bst.print();
    System.out.println(bst.freqSum);
    System.out.println(bst.rootNode.getRightNode().getKey());
    System.out.println(bst.sumWeightedPath());


  }
}

