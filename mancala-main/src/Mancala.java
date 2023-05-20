import javafx.util.Pair;

import java.util.Collections;
import java.util.Scanner;
import java.util.Vector;

public class Mancala {

    private int[] init_board;
    //private Board next_move;

    public Mancala()
    {
        init_board = new int[14];

        for(int i=0;i<14;i++)
        {
            if(i == 6 || i == 13)
            {
                init_board[i] = 0;
            }

            else
            {
                init_board[i] = static_parameters.max_pebbles;
            }
        }

//        Pair<Long, Board> pair2 = alphaBeta(board, -(static_parameters.INF), static_parameters.INF, static_parameters.max_depth);
//        board = pair2.getValue();
//        System.out.println("here");
//        board.print_board();

        System.out.println("Choose a game mode:");
        System.out.println("1. User vs AI");
        System.out.println("2. AI vs AI");

        Scanner sc1 = new Scanner(System.in);
        String choice = sc1.nextLine();

        if(choice.equals("1")) {
            int next_turn = 0;

            //default first move -> player
            int player_turn = 0;

            System.out.println("choose heuristic of the AI");
            int h1 = sc1.nextInt();

            System.out.println("Do you want to give the first move?");
            System.out.println("1.No");
            System.out.println("2.Yes");

            sc1 = new Scanner(System.in);
            choice = sc1.nextLine();

            System.out.println("initial board:\n");
            Board board = new Board(init_board,0,h1);
            board.setAdditional_moves(0);
            board.setCaptured_stones(0);
            board.print_board();


            if (choice.equals("1")) {
                player_turn = 1;
            }


            while (true) {
                if (next_turn == player_turn) {
                    System.out.println("Your move. Choose a hole:");
                    Scanner sc = new Scanner(System.in);
                    int hole;
                    if (player_turn == 0)
                        hole = sc.nextInt() - 1;
                    else
                        hole = sc.nextInt();
                    if (check_valid(board, player_turn, hole)){
                        Pair<Pair<Boolean,Integer>, int[]> pair1 = get_board_after_move(board.getHole(), board.getTurn(), hole);
                        next_turn = (pair1.getKey().getKey()) ? player_turn : (1 - player_turn);
                        board = new Board(pair1.getValue(), next_turn,h1);
                        board.print_board();
                    }
                } else {
                    System.out.println("AI move:");
                    Pair<Long, Board> pair = alphaBeta(board, -(static_parameters.INF), static_parameters.INF, static_parameters.max_depth, true,h1);
                    board = pair.getValue();
                    next_turn = board.getTurn();
                    board.print_board();
                }

                if (board.is_terminal()) {
                    System.out.println("Game over!");
                    board.print_board();
                    win_or_lose(board, player_turn);
                    break;
                }
            }
        }

        else if(choice.equals("2"))
        {
            int h1,h2;

            System.out.println("Choose AI_1 heuristic:");
            h1 = sc1.nextInt();
            System.out.println("Choose AI_2 heuristic:");
            h2 = sc1.nextInt();


            int next_turn = 0;

            //AI vs AI


            int games = (static_parameters.stat_mode)?static_parameters.no_of_games:1;



            int player1_win = 0;
            int player2_win = 0;
            int tie = 0;

            Board board = new Board(init_board,0,h1);
            board.setAdditional_moves(0);
            board.setCaptured_stones(0);
            board.print_board();

            for(int i=0;i<games;i++) {
                while (true) {
                    if (next_turn == 0) {
                        if(!static_parameters.stat_mode)
                        {
                            System.out.println("AI_1 move:");
                        }
                        Pair<Long, Board> pair = alphaBeta(board, -(static_parameters.INF), static_parameters.INF, static_parameters.max_depth, true, h1);
                        board = pair.getValue();
                        next_turn = board.getTurn();
                        if(!static_parameters.stat_mode)
                            board.print_board();
                    } else {
                        if(!static_parameters.stat_mode)
                        {
                            System.out.println("AI_2 move:");
                        }
                        Pair<Long, Board> pair = alphaBeta(board, -(static_parameters.INF), static_parameters.INF, static_parameters.max_depth, true, h2);
                        board = pair.getValue();
                        next_turn = board.getTurn();
                        if(!static_parameters.stat_mode)
                            board.print_board();
                    }

                    if (board.is_terminal()) {
                        if(!static_parameters.stat_mode) {
                            System.out.println("Game over!");
                            board.print_board();
                        }

                        int win = win_or_lose(board, 2);
                        if(win == 0)
                            player1_win++;
                        else if(win == 1)
                            player2_win++;
                        else
                            tie++;
                        break;
                    }
                }

                board = new Board(init_board,0,h1);
                next_turn = 0;
            }

                    if(static_parameters.stat_mode) {
                        System.out.println("Player_1: H"+h1);
                        System.out.println("Player_2: H"+h2+"\n");
                        System.out.println("\n\nDepth: " + static_parameters.max_depth);
                        System.out.println("AI_1 win %: " + (player1_win*1.0/games)*100.0 + "%");
                        System.out.println("AI_2 win %: " + (player2_win*1.0/games)*100.0 + "%");
                        System.out.println("Tie %: " + (tie*1.0/games)*100.0 + "%");
                    }







        }


    }

    public int win_or_lose(Board board,int player_turn)
    {

        int[] new_hole = board.getHole();

        if(player_turn == 0)
        {
            if(new_hole[6] > new_hole[13])
            {
                if(!static_parameters.stat_mode)
                    System.out.println("Congrats! You beat the mighty AI !!!");
                return 0;
            }

            else if(new_hole[6] < new_hole[13])
            {
                if(!static_parameters.stat_mode)
                    System.out.println("You lost against the mighty AI !!!");
                return 1;
            }

            else {
                if(!static_parameters.stat_mode)
                    System.out.println("Match drawn !!! Good game !!!");
                return -1;
            }
        }

        else if(player_turn == 1)
        {
            if(new_hole[13] > new_hole[6])
            {
                if(!static_parameters.stat_mode)
                    System.out.println("Congrats! You beat the mighty AI !!!");
                return 1;
            }

            else if(new_hole[13] < new_hole[6])
            {
                if(!static_parameters.stat_mode)
                    System.out.println("You lost against the mighty AI !!!");
                return 0;
            }

            else
            {
                if(!static_parameters.stat_mode)
                    System.out.println("Match drawn !!! Good game !!!");
                return -1;
            }
        }

        else if(player_turn == 2)
        {
            //AI vs AI
            if(new_hole[6] > new_hole[13])
            {
                if(!static_parameters.stat_mode)
                    System.out.println("AI_1 won!!!");
                return 0;
            }

            else if(new_hole[6] < new_hole[13])
            {
                if(!static_parameters.stat_mode)
                    System.out.println("AI_2 won!!!");
                return 1;
            }

            else
            {
                if(!static_parameters.stat_mode)
                    System.out.println("Match drawn between AIs");
                return -1;
            }
        }

        else
            return -2;


    }

    public Pair<Long,Board> alphaBeta(Board board, long alpha, long beta ,int depth, boolean maximizing, int heuristic)
    {

        long best_value;
        Board next_move = null;
//        System.out.println("depth: "+depth);
//        System.out.println("turn: "+board.getTurn());
//
//        System.out.println("board:");
//
//
//        board.print_board();

        if(depth == 0 || board.is_terminal())
        {
//            System.out.println("static evaluation: "+board.evaluate());
            Pair<Long,Board> pair = new Pair<>(board.evaluate(),board);
//            System.out.println("debug");
//            board.print_board();
//            System.out.println("add_moves: "+board.getAdditional_moves());
//            System.out.println(board.evaluate());
            return pair;
        }

        int[] ara = board.getHole();
        int turn = board.getTurn();
        Vector<Board> nodes;
        //maximize
        if(turn == 0)
        {
            nodes  = new Vector<>();
            if(maximizing)
                best_value = -(static_parameters.INF);
            else
                best_value = static_parameters.INF;

            for(int i=0;i<6;i++)
            {
                if(ara[i] > 0)
                {
//                    System.out.println("depth: "+depth);
//                    System.out.println("turn: "+board.getTurn());
//                    System.out.println("i:"+i);

                    //create a child
                    Pair<Pair<Boolean,Integer>, int[]> pair = get_board_after_move(ara,0,i);
                    int[] child = pair.getValue();
                    int new_turn = (pair.getKey().getKey())?turn:(1-turn);



                    //int new_turn = 1;
                    Board child_board = new Board(child,new_turn,heuristic);
                    nodes.add(child_board);
                    //add if a move is earned
                    child_board.setAdditional_moves(board.getAdditional_moves());
                    //add captured stones to child node
                    child_board.setCaptured_stones(board.getAdditional_moves()+pair.getKey().getValue());
                    //add one additional move if a turn is won
                    if(pair.getKey().getKey())
                        child_board.setAdditional_moves(child_board.getAdditional_moves()+1);
                    //child_board.print_board();
                }



            }

            if(static_parameters.random_ordering)
                Collections.shuffle(nodes);

            for(Board child_board:nodes)
            {
                Pair<Long,Board> pair1 = alphaBeta(child_board,alpha,beta,depth-1,maximizing,heuristic);
                long val = pair1.getKey();

                if(maximizing) {

                    if (val > best_value) {

                        best_value = val;
                        next_move = child_board;
//                            System.out.println("update-> depth: "+depth+" , turn: "+turn+", updated_value: "+best_value);
                    }
                    //best_value = Math.max(best_value,val);
                    alpha = Math.max(alpha, best_value);

//                    System.out.println("best_value: "+best_value);
//                    System.out.println("alpha: "+alpha);
//                    System.out.println("beta:" +beta);
                }

                else
                {
                    if(val < best_value)
                    {
                        best_value = val;
                        next_move = child_board;
//                            System.out.println("update-> depth: "+depth+" , turn: "+turn+", updated_value: "+best_value);
                    }
                    //best_value = Math.min(best_value,val);

                    beta = Math.min(beta,best_value);

                }

                if(beta <= alpha)
                {
                    //System.out.println("pruned maxi");
                    break;
                }
            }

        }

        else
        {
            nodes = new Vector<>();
            if(!maximizing)
                best_value = -(static_parameters.INF);
            else
                best_value = static_parameters.INF;

            for(int i=7;i<13;i++)
            {
                if(ara[i] > 0)
                {
//                    System.out.println("depth: "+depth);
//                    System.out.println("turn: "+board.getTurn());
//                    System.out.println("i:"+i);

                    //create a child
                    Pair<Pair<Boolean,Integer>, int[]> pair = get_board_after_move(ara,1,i);
                    int[] child = pair.getValue();
                    int new_turn = (pair.getKey().getKey())?turn:(1-turn);

                    //int new_turn = 0;
                    Board child_board = new Board(child,new_turn,heuristic);
                    //set additional moves
                    nodes.add(child_board);
                    //pass additional moves
                    child_board.setAdditional_moves(board.getAdditional_moves());
//                    if(pair.getKey().getKey())
//                        child_board.setAdditional_moves(child_board.getAdditional_moves()-1);
                    //pass captured stones to child node
                    child_board.setCaptured_stones(board.getCaptured_stones());
//                    child_board.setCaptured_stones(board.getCaptured_stones()-pair.getKey().getValue());

                }

            }

            if(static_parameters.random_ordering)
                Collections.shuffle(nodes);

            for(Board child_board:nodes)
            {
                Pair<Long,Board> pair1 = alphaBeta(child_board,alpha,beta,depth-1,maximizing,heuristic);
                long val = pair1.getKey();

                if(!maximizing) {

                    if (val > best_value) {
                        best_value = val;
                        next_move = child_board;
//                            System.out.println("update-> depth: "+depth+" , turn: "+turn+", updated_value: "+best_value);
                    }
                    //best_value = Math.max(best_value,val);
                    alpha = Math.max(alpha, best_value);

//                    System.out.println("best_value: "+best_value);
//                    System.out.println("alpha: "+alpha);
//                    System.out.println("beta:" +beta);
                }

                else
                {
                    if(val < best_value)
                    {
                        best_value = val;
                        next_move = child_board;
//                            System.out.println("update-> depth: "+depth+" , turn: "+turn+", updated_value: "+best_value);
                    }
                    //best_value = Math.min(best_value,val);

                    beta = Math.min(beta,best_value);

                }

                if(beta <= alpha)
                {
//                        System.out.println("pruned mini");
                    break;
                }
            }
        }

        Pair<Long,Board> pair2 = new Pair<>(best_value,next_move);
        return pair2;
    }


    public Pair<Pair<Boolean,Integer>, int[]> get_board_after_move(int[] board, int turn, int chosen_hole)
    {
        int[] new_board = new int[14];

        for(int i=0;i<14;i++)
        {
            new_board[i] = board[i];
        }

        new_board[chosen_hole] = 0;

        boolean move_earned = false;

        int no_of_captured_stones = 0;

        if(turn == 0)
        {
            for(int i=0,j=chosen_hole+1;i<board[chosen_hole];i++,j++)
            {
                //System.out.println("i->"+i+"\tj->"+j);
                if(j == 13)
                {
                    j=-1;
                    i--;
                }

                else
                {
                    new_board[j] = new_board[j]+1;

                    if( i == board[chosen_hole]-1)
                    {
                        if(j == 6)
                            move_earned = true;
                            //capture
                        else if( new_board[j] == 1 && j<=5)
                        {
                            int k = 12-j;
                            //move to own mancala
                            new_board[6]+=new_board[k];
                            no_of_captured_stones+=new_board[k];
                            new_board[k] = 0;
                        }
                    }

                }
            }
        }

        else
        {
            for(int i=0,j=chosen_hole+1;i<board[chosen_hole];i++,j++)
            {

                if(j == 6)
                {
                    i--;
                }

                else
                {
                    if(j == 14)
                    {
                        j = 0;
                    }

                    new_board[j] = new_board[j]+1;

                    if( i == board[chosen_hole]-1)
                    {
                        if(j == 13)
                            move_earned = true;
                            //capture
                        else if( new_board[j] == 1 && j>=7)
                        {
                            int k = 12-j;
                            //move to own mancala
                            new_board[13]+=new_board[k];
                            no_of_captured_stones+=new_board[k];
                            new_board[k] = 0;
                        }
                    }


                }
            }

        }

        //check if is terminal?
        boolean isTerminal = check_terminal(new_board);

        if(isTerminal)
        {
            int[] new_hole = new int[14];

            for(int i=0;i<14;i++)
                new_hole[i] = 0;

            new_hole[6] = new_board[6];
            new_hole[13] = new_board[13];

            int total_stones_down = 0, total_stones_up = 0;
            boolean ok = true;
            for(int i=0;i<=5;i++)
            {
                if(new_board[i] != 0)
                {
                    ok = false;
                }

                total_stones_down+=new_board[i];
                total_stones_up+=new_board[i+7];
            }

            if(!ok)
            {
                new_hole[13]+=total_stones_down;
            }

            else
            {
                new_hole[6]+=total_stones_up;
            }

            new_board = new_hole;
        }

        Pair<Boolean,Integer> temp_pair = new Pair<>(move_earned,no_of_captured_stones);

        Pair<Pair<Boolean,Integer>, int[]> pair = new Pair<>(temp_pair,new_board);

        return pair;
    }

    public boolean check_terminal(int[] board)
    {
        int down = 0, up=0;
        for(int i=0;i<=5;i++)
        {
            down+=board[i];
            up+=board[i+7];
        }

        if(down==0 || up==0)
        {
            return true;
        }

        else
            return false;

    }

    boolean check_valid(Board board, int player_turn, int choice)
    {
        int val = 0;
        if(player_turn == 1)
            val=7;

        if(choice >=val && choice<=5+val)
        {
            if(board.getHole()[choice] != 0)
            {
                return true;
            }
            else {
                System.out.println("No stones in the pit you selected! Try another one");
                return false;
            }
        }

        else {
            System.out.println("Invalid pit selected");
            return false;
        }
    }
}
