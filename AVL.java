public class AVL extends BST
{
  public Node rootNode = null;
  public AVL() { }

  public void insert(String key) {
    if (this.rootNode == null){
      this.rootNode = new Node(key);
      nodeCount++;
    }
    else avlInsertHelper(key, this.rootNode);

    freqSum = freqSum + 1;
  }

  private void avlInsertHelper(String key, Node node){

    int comparedValue = key.compareTo(node.getKey());

    if (comparedValue < 0 && node.getLeftNode() == null){
      this.nodeCount++;
      node.setLeftNode(new Node(key));
      return;
    } else if (comparedValue > 0 && node.getRightNode() == null){
      this.nodeCount++;
      node.setRightNode(new Node(key));
      return;
    } else if(comparedValue == 0){
      this.nodeCount++;
      node.incFrequency();
      return;
    }

    // AVL rotation
    if(comparedValue < 0){
      avlInsertHelper(key, node.getLeftNode());
      if(getHeight(node.getLeftNode()) - getHeight(node.getRightNode()) == 2){
        if(key.compareTo(node.getLeftNode().getKey()) < 0){
          Node temp = rotateRight(node);
          node.setNode(temp);
        }
        else node.setNode(rotateLeftRight(node));
      }

    } else if(comparedValue > 0){
      avlInsertHelper(key, node.getRightNode());
      if(getHeight(node.getRightNode()) - getHeight(node.getLeftNode()) == 2){
        if(key.compareTo(node.getRightNode().getKey()) > 0){
          Node temp = rotateLeft(node);
          node.setNode(temp);
        }
        else node.setNode(rotateRightLeft(node));
      }
    }
  }

  private Node rotateRight(Node node){
    Node temp = node.getLeftNode();
    node.setLeftNode(temp.getRightNode());
    temp.setRightNode(node);
    return Node.getNodeCopy(temp);
  }

  private Node rotateLeft(Node node){
    Node temp = node.getRightNode();
    node.setRightNode(temp.getLeftNode());
    temp.setLeftNode(node);
    return Node.getNodeCopy(temp);
  }

  private Node rotateLeftRight(Node node){
    node.setLeftNode(rotateLeft(node.getLeftNode()));
    return rotateRight(node);
  }

  private Node rotateRightLeft(Node node){
    node.setRightNode(rotateRight(node.getRightNode()));
    return rotateLeft(node);
  }

  private int getHeight(Node node){
    if(node == null) return 0;
    return Math.max(getHeightHelper(node.getLeftNode(), 1), getHeightHelper(node.getRightNode(), 1));
  }

  private int getHeightHelper(Node node, int level){
    if(node == null) return level;
    return Math.max(getHeightHelper(node.getLeftNode(), level+1), getHeightHelper(node.getRightNode(), level+1));
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

  public static void main(String args[]){
    AVL a = new AVL();
    a.insert("6");
    a.insert("7");
    a.insert("9");
    a.insert("5");
    a.insert("4");
    a.print();
    System.out.println(a.rootNode);
    System.out.println(a.find("6"));
  }
 
}

