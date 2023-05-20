import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class Search_Node {
    private HashMap<Pair<Integer,Integer>,Integer> value_map;
    private HashMap<Pair<Integer,Integer>,Integer> dest;
    private HashMap<Integer,Pair<Integer,Integer>> reverse_map;

    private Search_Node prev_node;
    private long g_n;
    private long h_n;
    private long f_n;
    private int k;
    private int heuristic;

    public Search_Node(long number_of_moves, HashMap<Pair<Integer, Integer>, Integer> value_map, Search_Node prev_node, int heuristic,int k)
    {
        this.g_n = number_of_moves;
        this.value_map = value_map;
        this.prev_node = prev_node;
        this.heuristic = heuristic;
        this.k = k;

        //get the destination node
        dest = new HashMap<>();
        reverse_map = new HashMap<>();
        int cell_val = 1;
        for(int i=0;i<k;i++)
        {
            for(int j=0;j<k;j++)
            {
                Pair<Integer,Integer> p = new Pair<>(i,j);

                if(i == k-1 && j == k-1)
                {
                    dest.put(p,-1);
                    reverse_map.put(-1,p);
                }
                else
                {
                    dest.put(p,cell_val);
                    reverse_map.put(cell_val,p);
                }

                cell_val++;
            }
        }

        //get the evaluation value
        eval_func();
    }

    public Pair<Integer,Integer> get_blank_rowcol()
    {
        Pair<Integer,Integer> p = null;
        for (Map.Entry<Pair<Integer, Integer>, Integer> m : value_map.entrySet()) {
            if((Integer) m.getValue() == -1)
            {
                p = (Pair<Integer, Integer>) m.getKey();
                break;
            }
        }
        return p;
    }

    public void eval_func()
    {
        if(heuristic == 1)
        {
            h_n = Hamming_Distance();
        }

        else if(heuristic == 2)
        {
            h_n = Manhattan_Distance();
        }

        else if(heuristic == 3)
        {
            h_n = Linear_Conflict();
        }

        this.f_n =  this.g_n + this.h_n;
    }


    public long getF_n()
    {
        return f_n;
    }

    public long getH_n() {
        return h_n;
    }

    public HashMap<Pair<Integer, Integer>, Integer> getValue_map() {
        return value_map;
    }

    public Search_Node getPrev_node() {
        return prev_node;
    }

    public long getNumber_of_moves() {
        return g_n;
    }

    public long Hamming_Distance()
    {
        long cost = 0;

        for (Map.Entry<Pair<Integer, Integer>, Integer> m : value_map.entrySet()) {

            if((int) dest.get(m.getKey()) !=  m.getValue())
            {
                cost++;
            }
        }

        return cost;
    }

    public long Manhattan_Distance()
    {
        long cost = 0;

        for (Map.Entry<Pair<Integer, Integer>, Integer> m : value_map.entrySet()) {
            Pair<Integer,Integer> p = m.getKey();
            Pair<Integer,Integer> q = reverse_map.get(m.getValue());

            cost+=Math.abs(p.getKey()-q.getKey())+Math.abs(p.getValue()-q.getValue());
        }

        return cost;

    }

    public long Linear_Conflict()
    {
        return Manhattan_Distance()+row_conflict()+column_conflict();

    }

    public long row_conflict()
    {
        long conflict = 0;
        for(int i=0;i<k;i++)
        {
            int max = -1;
            for(int j=0;j<k;j++)
            {
                int val = value_map.get(new Pair<>(i,j));

                if(val != -1 && val >= (i*k+1) && val <= (i+1)*k)
                {
                    if(val > max)
                    {
                        max = val;
                    }
                    else
                    {
                        conflict+=2;
                    }
                }
            }
        }

        return conflict;

    }

    public long column_conflict()
    {
        long conflict = 0;
        for(int i=0;i<k;i++)
        {
            int max = -1;
            for(int j=0;j<k;j++)
            {
                int val = value_map.get(new Pair<>(j,i));

                if(val != -1 && val%k == i+1)
                {
                    if(val > max)
                    {
                        max = val;
                    }

                    else
                        conflict+=2;
                }
            }
        }

        return conflict;
    }

    public void print_node()
    {
        System.out.println("Number of moves:"+g_n+"\n");
        for(int i=0;i<k;i++)
        {
            for(int j=0;j<k;j++)
            {
                if(value_map.get(new Pair<>(i,j)) == -1)
                    System.out.print("*");
                else
                    System.out.print(value_map.get(new Pair<>(i,j)));
                System.out.print("\t");
            }
            System.out.println("\n");
        }

        System.out.println("---------------------\n\n");

//        System.out.println("g(n): "+g_n);
//        System.out.println("h(n): "+h_n);
//        System.out.println("f(n): "+f_n);
    }
}
