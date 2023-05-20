import javafx.util.Pair;

import java.sql.SQLOutput;
import java.util.*;

public class Solve {
    private int k;
    private HashMap<Pair<Integer,Integer>,Integer> source;
    private int heuristics;
    private ArrayList<HashMap<Pair<Integer,Integer>,Integer>> closed_set;
    public long no_of_expanded_nodes;
    public long no_of_explored_nodes;

    int cnt = 0;


    public void run()
    {
        //take value of k

        while(true)
        {
            System.out.println("Enter value of k :");
            Scanner sc = new Scanner(System.in);
            k = sc.nextInt();

            //take initial state
            HashMap<Pair<Integer,Integer>,Integer> state = new HashMap<>();


            System.out.println("Input board:\n");

            for(int i=0;i<k;i++)
            {
                sc = new Scanner(System.in);
                String val = sc.nextLine();

                StringTokenizer st = new StringTokenizer(val," ");

                int j = 0;

                while(st.hasMoreTokens())
                {
                    val = st.nextToken();

                    int int_val;
                    if(val.equals("*"))
                        int_val = -1;
                    else
                        int_val = Integer.parseInt(val);


                    Pair<Integer,Integer> p = new Pair<>(i,j);
                    state.put(p,int_val);

                    j++;
                }
            }

            this.source = state;

            //detect solvable or not

            boolean solvable = detect_solvability();

            if(!solvable)
            {
                System.out.println("This puzzle is not solvable! Try another one...");
                continue;
            }


            for(int h=1;h<=3;h++) {

                if(h == 1)
                    System.out.println("Hamming Distance:\n");
                else if(h == 2)
                    System.out.println("Manhattan Distance:\n");
                else
                    System.out.println("Linear Conflict:\n");

                this.heuristics = h;
                //Start A* search

                //make the initial node
                Search_Node init_node = new Search_Node(0, state, null, heuristics, k);

                //make priority queue
                PriorityQueue<Search_Node> priorityQueue = new PriorityQueue<Search_Node>(10000, new NodeComparator());

                //add start node to priority queue
                priorityQueue.add(init_node);

                closed_set = new ArrayList<>();
                no_of_expanded_nodes = 0;
                no_of_explored_nodes = 1;


                while (true) {
                    Search_Node popped_node = priorityQueue.peek();
                    priorityQueue.remove(popped_node);

                    no_of_expanded_nodes++;
                    closed_set.add(popped_node.getValue_map());
                    //popped_node.print_node();

                    if (popped_node.getH_n() == 0) {
                        //System.out.println("Destination popped");
                        print_path(popped_node);
                        System.out.println("No of Explored nodes: " + no_of_explored_nodes);
                        System.out.println("No of Expanded nodes: " + no_of_expanded_nodes + "\n");
                        break;
                    }

                    add_neighbours(popped_node, priorityQueue);
                }
            }
        }
    }



    public void add_neighbours(Search_Node search_node, PriorityQueue<Search_Node> pq)
    {
        Pair<Integer,Integer> get_blank_pos = search_node.get_blank_rowcol();

        int row = get_blank_pos.getKey();
        int col = get_blank_pos.getValue();

        HashMap<Pair<Integer,Integer>,Integer> board = search_node.getValue_map();
        HashMap<Pair<Integer,Integer>,Integer> dup = new HashMap<>(board);

        Pair<Integer,Integer> p = new Pair<>(row,col);
        Pair<Integer,Integer> q = null;

        if(row > 0)
        {
            q = new Pair<>(row-1,col);
            dup.put(p,board.get(q));
            dup.put(q,-1);

            Search_Node temp = new Search_Node(search_node.getNumber_of_moves()+1,dup,search_node,heuristics,k);


            if(!closed_set.contains(dup))
            {
                pq.add(temp);
                no_of_explored_nodes++;
            }
        }

        if(row < k-1)
        {
            dup = new HashMap<>(board);
            q = new Pair<>(row+1,col);
            dup.put(p,board.get(q));
            dup.put(q,-1);

            Search_Node temp = new Search_Node(search_node.getNumber_of_moves()+1,dup,search_node,heuristics,k);
            if(!closed_set.contains(dup))
            {
                pq.add(temp);
                no_of_explored_nodes++;
            }
        }

        if(col > 0)
        {
            dup = new HashMap<>(board);
            q = new Pair<>(row,col-1);
            dup.put(p,board.get(q));
            dup.put(q,-1);

            Search_Node temp = new Search_Node(search_node.getNumber_of_moves()+1,dup,search_node,heuristics,k);
            if(!closed_set.contains(dup))
            {
                pq.add(temp);
                no_of_explored_nodes++;
            }
        }

        if(col < k-1)
        {
            dup = new HashMap<>(board);
            q = new Pair<>(row,col+1);
            dup.put(p,board.get(q));
            dup.put(q,-1);

            Search_Node temp = new Search_Node(search_node.getNumber_of_moves()+1,dup,search_node,heuristics,k);
            if(!closed_set.contains(dup))
            {
                pq.add(temp);
                no_of_explored_nodes++;
            }
        }

        cnt++;


    }


    public void print_path(Search_Node node)
    {
        if(node == null)
        {
            System.out.println("Path");
            return;
        }

        print_path(node.getPrev_node());
        node.print_node();
    }

    public boolean detect_solvability()
    {
        int no_of_inversions = get_number_of_inversions();
        if(k%2 != 0)
        {
            if(no_of_inversions%2 !=  0)
            {
                return false;
            }

            else
                return true;
        }

        else
        {
            Search_Node init_node = new Search_Node(0,source,null,1,k);
            if(no_of_inversions%2 != 0)
            {
                if((init_node.get_blank_rowcol().getKey())%2 == 0)
                {
                    return true;
                }

            }

            else
            {
                if((init_node.get_blank_rowcol().getKey())%2 != 0)
                {
                    return true;
                }
            }

            return false;
        }
    }

    public int get_number_of_inversions()
    {
        int[] ara = new int[k*k];
        int m = 0;
        for(int i=0;i<k;i++)
        {
            for(int j=0;j<k;j++)
            {
                ara[m] = source.get(new Pair<>(i,j));
                m++;
            }
        }

        int no_of_inversion = 0;
        for(int i=0;i<k*k;i++)
        {
            if(ara[i] != -1)
            {
                for (int j = i + 1; j < k * k; j++)
                {
                    if (ara[i] > ara[j] && ara[j] != -1) {
                        no_of_inversion++;
                    }
                }
            }
        }

        return no_of_inversion;

    }
}



