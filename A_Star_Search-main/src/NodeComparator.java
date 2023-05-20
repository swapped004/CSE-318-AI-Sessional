import java.util.Comparator;

public class NodeComparator implements Comparator<Search_Node> {
    @Override
    public int compare(Search_Node o1, Search_Node o2) {
        if(o1.getF_n() > o2.getF_n())
        {
            return 1;
        }

        else if(o1.getF_n() < o2.getF_n())
        {
            return -1;
        }

        else
            return 0;
    }
}
