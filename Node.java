/**
 * Created by tonykim on 9/27/16.
 */
public class Node {
    private Node leftNode = null;
    private Node rightNode = null;
    private String key;
    private int frequency = 1;
    private int accessCount = 0;

    public Node(String key){
        this.key = key;
    }

    public Node(String key, int freq){
        this.key = key;
        this.frequency = freq;
    }

    public Node(){

    }

    public void setNode(Node node){
        this.setLeftNode(getNodeCopy(node.getLeftNode()));
        this.setRightNode(getNodeCopy(node.getRightNode()));
        this.setKey(node.getKey());
        this.frequency = node.frequency;
        this.accessCount = node.accessCount;
    }

    public static Node getNodeCopy(Node node){
        if(node == null) return null;
        Node temp = new Node();
        temp.setKey(node.getKey());
        temp.setLeftNode(getNodeCopy(node.getLeftNode()));
        temp.setRightNode(getNodeCopy(node.getRightNode()));
        temp.setFrequency(node.getFrequency());
        temp.accessCount = node.getAccessCount();
        return temp;
    }

    public void setRightNode(Node node){
        this.rightNode = node;
    }

    public void setLeftNode(Node node){
        this.leftNode = node;
    }

    public void setKey(String key){
        this.key = key;
    }

    public void incFrequency() {frequency++;}

    public void incFrequency(int freq) {frequency += freq;}

    public void incAccessCount() {accessCount++;}

    public int getFrequency() {return frequency;}

    public void setFrequency(int freq){this.frequency = freq;}

    public int getAccessCount() {return accessCount;}

    public void reset(){
        this.frequency = 0;
        this.accessCount = 0;
    }

    public String getKey(){
        return this.key;
    }

    public Node getLeftNode(){
        return this.leftNode;
    }

    public Node getRightNode(){
        return this.rightNode;
    }
}
