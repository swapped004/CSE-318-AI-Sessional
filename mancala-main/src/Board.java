public class Board {
    private int[] hole;
    private int heuristic;
    private int turn; //0 -> own turn , 1-> opponent turn
    private int own_total_stones;
    private int opp_total_stones;
    private int additional_moves;
    private int captured_stones;

    public Board(int[] hole, int turn, int heuristic)
    {
        this.hole = hole;
        this.turn = turn;
        this.heuristic = heuristic;

        own_total_stones = 0;
        opp_total_stones = 0;

        if(true)
        {
            //own_total_stones+=hole[own_mancala];
            //opp_total_stones+=hole[opp_mancala];

            for(int i=0;i<6;i++)
            {
                own_total_stones+=hole[i];
                opp_total_stones+=hole[i+7];
            }
        }
    }

    public boolean is_terminal()
    {
        return  (own_total_stones == 0 && opp_total_stones == 0);
    }

    public void print_board()
    {
        System.out.println("\n!--------------------------------------------------!");
        for(int i=12;i>=7;i--)
        {
            System.out.print("\t");
            System.out.print(i);
            System.out.print(" : "+hole[i]+" ");
        }
        System.out.println();

        System.out.println("L: "+hole[13]+"\t\t\t\t\t\t\t\t\t\t\tR: "+hole[6]);

        for(int i=0;i<6;i++)
        {
            System.out.print("\t");
            System.out.print(i+1);
            System.out.print(" : "+hole[i]+" ");
        }

        System.out.println("\n!--------------------------------------------------!\n");
    }

    public long evaluate()
    {
        if(heuristic == 1)
            return heuristic_1();
        else if(heuristic == 2)
            return heuristic_2();
        else if(heuristic == 3)
            return heuristic_3();
        else if(heuristic == 4)
            return heuristic_4();
        else if(heuristic == 5)
            return heuristic_5();
        else if(heuristic == 6)
            return heuristic_6();
        else
            return -1;

    }

    //difference between two mancalas
    public long heuristic_1()
    {
        return (hole[6]-hole[13]);
    }

    //difference between stones of two sides
    public long heuristic_2()
    {
        //return 3*(hole[6]-hole[13]) + 2*(own_total_stones-opp_total_stones);
        return 3*(hole[6]-hole[13]) + (long)(2*(own_total_stones-opp_total_stones));
    }

    //Additional move earned by the main player
    public long heuristic_3()
    {
        return 3*(hole[6]-hole[13]) + (long)(0.1*(own_total_stones-opp_total_stones))+ 6*additional_moves;
    }

    //maximize how far the opponent is from winning
    public long heuristic_4()
    {
        //return static_parameters.W1*(hole[6]-hole[13]) + static_parameters.W2*(own_total_stones-opp_total_stones)+ static_parameters.W3*additional_moves + (long)(static_parameters.W4*(24-hole[13]));
//        long val = 0;
//        if(hole[6] >= 5)
//            val = (long)(-(static_parameters.W5*(hole[6]*1.5-hole[13])));
//        return 2*(hole[6]-hole[13]) + 3*(own_total_stones-opp_total_stones)+ 5*additional_moves+ 1*val;
        return 3*(hole[6]-hole[13]) + (long)(0.1*(own_total_stones-opp_total_stones)) + 6*additional_moves+ (long)(0.5*(24-hole[13]));
    }

    //maximize how far the player is from winning
    public long heuristic_5()
    {

        //return static_parameters.W1*(hole[6]-hole[13]) + static_parameters.W2*(own_total_stones-opp_total_stones)+ static_parameters.W3*additional_moves + (long)(static_parameters.W4*(24-hole[13]));
//        long val = 0;
//        if(hole[13] >= 5)
//            val = (long)(-(static_parameters.W5*(hole[13]*1.5-hole[6])));
//        return 2*(hole[6]-hole[13]) + 3*(own_total_stones-opp_total_stones)+ 5*additional_moves+ 1*val;

        return 3*(hole[6]-hole[13]) + (long)(0.1*(own_total_stones-opp_total_stones)) + 6*additional_moves+ (long)(-(0.5*(24-hole[6])));

    }


    //how many stones have been captured
    public long heuristic_6()
    {
        return 3*(hole[6]-hole[13]) +(long)(0.1*(own_total_stones-opp_total_stones))+ 6*additional_moves + (long)(captured_stones);
    }

    public int[] getHole() {
        return hole;
    }


    public int getTurn() {
        return turn;
    }

    public int getAdditional_moves() {
        return additional_moves;
    }

    public void setAdditional_moves(int additional_moves) {
        this.additional_moves = additional_moves;
    }

    public int getCaptured_stones() {
        return captured_stones;
    }

    public void setCaptured_stones(int captured_stones) {
        this.captured_stones = captured_stones;
    }
}
