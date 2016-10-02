import java.util.Comparator;

/**
 * Created by tonykim on 10/1/16.
 */
public class StringAscComparator implements Comparator<Node> {
    @Override
    public int compare(Node node1, Node node2){
        return node1.getKey().compareTo(node2.getKey());
    }
}

