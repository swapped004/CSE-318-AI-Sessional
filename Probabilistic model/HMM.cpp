#include<iostream>
#include<fstream>
#include <sstream>
#include <string>
#include<bits/stdc++.h>

using namespace std;

void print_grid(double grid[20][20],int n,int m)
{
    cout<<"-----------------------------------------------printing grid-----------------------------------------------"<<endl;
    for(int i=0;i<n;i++)
    {
        for(int j=0;j<m;j++)
        {
            cout<<fixed<<setprecision(4)<<grid[i][j]*100<<"\t";
        }

        cout<<endl;
    }

    cout<<"-----------------------------------------------------end-----------------------------------------------------\n\n"<<endl;

}

bool check_edge_adj(int a, int b, int c, int d, int n, int m)
{
    if(c == a-1 && d==b)
    {
        return true;
    }

    if(c == a+1 && d==b)
    {
        return true;
    }

    if(d == b-1 && c==a)
    {
        return true;
    }

    if(d == b+1 && c==a)
    {
        return true;
    }

    return false;
}

bool check_corner_adj(int a, int b, int c, int d, int n, int m)
{
    if(c == a && d==b)
    {
        return true;
    }

    if(c == a+1 && d==b+1)
    {
        return true;
    }

    if(c == a+1 && d==b-1)
    {
        return true;
    }

    if(c == a-1 && d==b+1)
    {
        return true;
    }

    if(c == a-1 && d==b-1)
    {
        return true;
    }

    return false;
}

void print_trans_prob(map<pair<pair<int,int>,pair<int,int>>,double> ma,int n,int m)
{
    for(int i=0; i<n;i++)
    {
        for(int j=0;j<m;j++)
        {
            for(int k=0;k<n;k++)
            {
                for(int l=0;l<m;l++)
                {
                    cout<<fixed<<setprecision(4)<<ma[make_pair(make_pair(i,j),make_pair(k,l))]<<"\t";
                }
            }

            cout<<endl;
        }
    }

}

int main()
{
    ifstream in;
    in.open("input.txt");

    int n,m,k;
    in>>n>>m>>k;

    double edge_prob = 90;
    double non_edge_prob = 10;
    double sensor_prob = 85;

    //make initial state

    double init_prob = 1.0/(n*m-k);
    //cout<<init_prob<<endl;
    double grid[20][20];


    //init with -1
    for(int i=0;i<n;i++)
    {
        for(int j=0;j<m;j++)
        {
            grid[i][j] = -1;
        }
    }


    for(int i=0;i<k;i++)
    {
        int u,v;
        in>>u>>v;

        grid[u][v] = 0;

        // cout<<u<<":"<<v<<endl;
    }

   
    for(int i=0;i<n;i++)
    {
        for(int j=0;j<m;j++)
        {
            if(grid[i][j] == -1)
            {
                grid[i][j] = init_prob;
            }
        }
    }


    cout<<"Step 0:"<<endl;
    print_grid(grid, n,m);


    //find edge and corner neighbours
    vector<pair<pair<int,int>,vector<pair<int,int>>>> edge_map,corner_map;

    for(int i=0;i<n;i++)
    {
        for(int j=0;j<m;j++)
        {
            vector<pair<int,int>> temp1,temp2;

            if(grid[i][j] != 0){
                for(int p=0;p<n;p++)
                {
                    for(int q=0;q<m;q++)
                    {
                        if(check_edge_adj(i,j,p,q,n,m) && grid[p][q] != 0)
                        {
                            temp1.push_back(make_pair(p,q));
                        }

                        else if(check_corner_adj(i,j,p,q,n,m) && grid[p][q] != 0)
                        {
                            temp2.push_back(make_pair(p,q));

                        }
                    }
                }
            }

            edge_map.push_back(make_pair(make_pair(i,j),temp1));
            corner_map.push_back(make_pair(make_pair(i,j),temp2));


        }
    }

    map<pair<pair<int,int>,pair<int,int>>,double> trans_map;

    for(int i=0;i<edge_map.size();i++)
    {
        //cout<<"source:("<<edge_map[i].first.first<<","<<edge_map[i].first.second<<")"<<endl;
        int s_u = edge_map[i].first.first;
        int s_v = edge_map[i].first.second;

        double prob = (edge_prob/100.0)/((int)edge_map[i].second.size());

        for(int j=0;j<edge_map[i].second.size();j++)
        {
             //cout<<"dest:("<<edge_map[i].second[j].first<<","<<edge_map[i].second[j].second<<")"<<endl;
             int d_u = edge_map[i].second[j].first;
             int d_v = edge_map[i].second[j].second;
             trans_map[make_pair(make_pair(s_u,s_v),make_pair(d_u,d_v))] = prob;
        }
    }

    //cout<<"-----------------------------------------------------"<<endl;

    for(int i=0;i<corner_map.size();i++)
    {
        //cout<<"source:("<<corner_map[i].first.first<<","<<corner_map[i].first.second<<")"<<endl;

        int s_u = corner_map[i].first.first;
        int s_v = corner_map[i].first.second;

        double prob = (non_edge_prob/100.0)/((int)corner_map[i].second.size());

        for(int j=0;j<corner_map[i].second.size();j++)
        {
             //cout<<"dest:("<<corner_map[i].second[j].first<<","<<corner_map[i].second[j].second<<")"<<endl;
             int d_u = corner_map[i].second[j].first;
             int d_v = corner_map[i].second[j].second;
             trans_map[make_pair(make_pair(s_u,s_v),make_pair(d_u,d_v))] = prob;
        }
    }

    //print_trans_prob(trans_map,n,m);

    //make transitional prob matrix


    string input;


    int steps = 1;
    while(getline(in,input))
    {
        
        //in>>input;
        // cout<<input<<endl;
        // cout<<input[0]<<endl;

        if(input[0] == 'Q')
        {
            cout<<"Bye Casper!"<<endl;
            break;
        }

        else if(input[0] == 'C')
        {
            double max = -1;
            int row = -1, col = -1;
            for(int i=0;i<n;i++)
            {
                for(int j=0;j<m;j++)
                {
                    if(max < grid[i][j])
                    {
                        max = grid[i][j];
                        row = i;
                        col = j;
                    }

                }
            }

            cout<<"Most probable cell: ( "<<row<<", "<<col<<" )\n\n"<<endl;

        }

        else if(input[0] == 'R')
        {
            
            int u,v,b;
            // Vector of string to save tokens
            vector <string> tokens;
            
            // stringstream class check1
            stringstream check1(input);
            
            string intermediate;
            
            // Tokenizing w.r.t. space ' '
            while(getline(check1, intermediate, ' '))
            {
                tokens.push_back(intermediate);
            }
            

            u = stoi(tokens[1]);
            v = stoi(tokens[2]);
            b = stoi(tokens[3]);


            //calculate transition update

            double sum=0;
            double new_grid[20][20];

            for(int i=0;i<n;i++)
            {
                for(int j=0;j<m;j++)
                {
                    double val = 0;
                    if(grid[i][j] != 0)
                    {
                        for(int p=0;p<n;p++)
                        {
                            for(int q=0;q<m;q++)
                            {
                                val+=trans_map[make_pair(make_pair(p,q),make_pair(i,j))]*grid[p][q];
                            }
                        }
                    }

                    new_grid[i][j] = val;
                    sum+=grid[i][j];
                }
            }

            for(int i=0;i<n;i++)
            {
                for(int j=0;j<m;j++)
                {
                    grid[i][j] = new_grid[i][j];
                }
            }

            //print_grid(grid,n,m);

            //calculate observation update
            int total_adj = (int)(edge_map[u*m+v].second.size())+(int)(corner_map[u*m+v].second.size());
            sum=0;
            for(int i=0;i<n;i++)
            {
                for(int j=0;j<m;j++)
                {
                    if(grid[i][j] != 0){
                        if((check_edge_adj(u,v,i,j,n,m) || check_corner_adj(u,v,i,j,n,m)))
                        {
                            if(b == 1)
                                grid[i][j] = (((sensor_prob)/100.0)*grid[i][j]);
                            else
                                grid[i][j] = (((100-sensor_prob)/100.0)*grid[i][j]);

                        }
                        else
                        {
                             if(b == 0)
                                grid[i][j] = (((sensor_prob)/100.0)*grid[i][j]);
                            else
                                grid[i][j] = (((100-sensor_prob)/100.0)*grid[i][j]);
                        }
                    }

                    sum+=grid[i][j];
                }
            }

            //normalize
            for(int i=0;i<n;i++)
            {
                for(int j=0;j<m;j++)
                {
                    grid[i][j] = grid[i][j]*(1.0/sum);
                }
            }

             cout<<"Step "<<steps<<":"<<endl;
             print_grid(grid,n,m);

             steps++;


            // cout<<u<<":"<<v<<":"<<b<<endl;
        }
    }


}