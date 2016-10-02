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

    public void incAccessCount() {accessCount++;}

    public int getFrequency() {return frequency;}

    public int getAccessCount() {return accessCount;}

    public void reset(){
        this.frequency = 1;
        this.accessCount = 1;
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
