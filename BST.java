// (Nearly) Optimal Binary Search Tree
// Bongki Moon (bkmoon@snu.ac.kr), Sep/23/2014

import java.util.ArrayList;
import java.util.HashMap;

public class BST { // Binary Search Tree implementation

  protected boolean NOBSTified = false;
  protected boolean OBSTified = false;
  protected int nodeCount = 0;
  protected Node rootNode = null;

  public BST() {

  }

  public int size() {return this.nodeCount;}

  public void insert(String key) {
    if (this.rootNode == null){
      this.rootNode = new Node(key);
      nodeCount++;
    }
    else insertHelper(key, this.rootNode);
  }

  private void insertHelper(String key, Node node){

    if(key.compareTo(node.getKey()) < 0){
      if(node.getLeftNode() == null){
        this.nodeCount++;
        node.setLeftNode(new Node(key));
      }
      else insertHelper(key, node.getLeftNode());
    }

    else if(key.compareTo(node.getKey()) == 0){
      node.incFrequency();
    }

    else {
      if(node.getRightNode() == null){
        node.setRightNode(new Node(key));
        this.nodeCount++;
      }
      else insertHelper(key, node.getRightNode());
    }
  }

  public boolean find(String key) {

    return rootNode != null && findHelper(key, this.rootNode);

  }

  private boolean findHelper(String key, Node node){

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

  private int sumFreqHelper(Node node){

    if(node == null) return 0;
    return node.getFrequency() + sumFreqHelper(node.getLeftNode()) + sumFreqHelper(node.getRightNode());

  }

  public int sumProbes() {
    return sumProbesHelper(this.rootNode);
  }

  private int sumProbesHelper(Node node){
    if(node == null) return 0;
    return node.getAccessCount() + sumProbesHelper(node.getLeftNode()) + sumProbesHelper(node.getRightNode());
  }

  public int sumWeightedPath() {
    return sumWeightedPathHelper(this.rootNode, 0);
  }

  private int sumWeightedPathHelper(Node node, int level){
    if(node == null) return 0;
    return node.getFrequency() * (level + 1) +
            sumWeightedPathHelper(node.getLeftNode(), level + 1) +
            sumWeightedPathHelper(node.getRightNode(), level + 1);
  }

  public void resetCounters() {
    resetCountersHelper(this.rootNode);
  }

  private void resetCountersHelper(Node node){
    if(node == null) return;
    node.reset();
    resetCountersHelper(node.getLeftNode());
    resetCountersHelper(node.getRightNode());
  }


  public void nobst() { }	// Set NOBSTified to true.




  public void obst() {
    HashMap<String, Float> valueMemoizeTable = new HashMap<String, Float> ();
    HashMap<String, ArrayList<Integer>> keyListMemoizeTable = new HashMap<String, ArrayList<Integer>> ();
    int freqSum = this.sumFreq();
    float maxFloat = 999999999;

    ArrayList<Node> nodeListSortedByKey = new ArrayList<> ();
    this.initializeNodeList(this.rootNode, nodeListSortedByKey);

//    Bottom up DP

    for(int size=1; size<=nodeListSortedByKey.size(); size++){ // for all list size
      int j = size - 1;
      for(int i=0; i<size; i++){ // select i, j
        if (i == j) {
          valueMemoizeTable.put(i + "_" + j, (float) nodeListSortedByKey.get(i).getFrequency() / freqSum);
          continue;
        }
        valueMemoizeTable.put(i + "_" + j, maxFloat);
        for(int r=i; r <= j; r++){
          float prevValue = getValueFromValueMemoizeTable(i, j, valueMemoizeTable);
          float temp = Math.min(
                  getValueFromValueMemoizeTable(i, j, valueMemoizeTable),
                  getValueFromValueMemoizeTable(i, r-1, valueMemoizeTable)
                          + getValueFromValueMemoizeTable(r+1, j, valueMemoizeTable)
                          + getFreqSumByRange(i, j, nodeListSortedByKey) / freqSum
          );
          if (temp != prevValue){
            valueMemoizeTable.put(i + "_" + j, temp);
            keyListMemoizeTable.put(i + "_" + j, )
          }

        }

      }
    }

  }	// Set OBSTified to true.

  private float getValueFromValueMemoizeTable(int fromIndex, int toIndex, HashMap<String, Float> valueMemoizeTable){
    if(fromIndex > toIndex) return 0;
    return valueMemoizeTable.get(fromIndex + "_" + toIndex);
  }

  private float getFreqSumByRange(int fromIndex, int toIndex, ArrayList<Node> nodeListSortedByKey){
    float freqSum = 0;
    for(int i=fromIndex; i <= toIndex; i++){
      freqSum += nodeListSortedByKey.get(i).getFrequency();
    }
    return freqSum;
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
    System.out.println(node.getKey() + ":" + node.getFrequency() + ":" + node.getAccessCount());
    printHelper(node.getRightNode());
  }

  public static void main(String args[]){
    BST bst = new BST();
    bst.insert("1");
    bst.insert("1234");
    bst.insert("1234");
    bst.insert("4444");
    bst.insert("3333");
    bst.print();
    bst.obst();

    HashMap<String, Integer> valueMemoizeTable = new HashMap<String, Integer> ();

  }
}

