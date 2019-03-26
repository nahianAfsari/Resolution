/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Clause {

    List<String> clause;
    List<String> sortedClause;
    String clauseString;
    int clauseStringLength;
    int clauseNumber;
    String sortedClauseString;

    public Clause(String input){
        
        
        clauseString = input;
        if(input == null)
        {
            clause = null;
            clauseStringLength = 0;
        }
        else
        {
            clause = new ArrayList<>();
            String[] temp = input.split(" ");
 
            clause = Arrays.asList(temp);
            sortedClause = new ArrayList<>(clause);
            Collections.sort(sortedClause);
            sortedClauseString = "";
            for(int i = 0; i < sortedClause.size(); i++)
            {
                if(i == sortedClause.size() - 1)
                {
                    sortedClauseString += sortedClause.get(i);
                }
                else
                {
                    sortedClauseString += sortedClause.get(i) + " ";
                }
            }
            clauseStringLength = clauseString.length();
            
        }
        //just adding comments so git thinks i added sth
        
        
    }
    public void setSortedString()
    {
          sortedClause = new ArrayList<>(clause);
            Collections.sort(sortedClause);
            sortedClauseString = "";
            for(int i = 0; i < sortedClause.size(); i++)
            {
                if(i == sortedClause.size() - 1)
                {
                    sortedClauseString += sortedClause.get(i);
                }
                else
                {
                    sortedClauseString += sortedClause.get(i) + " ";
                }
            }
    }
    
    
    
    
    public void setString()
    {
        clauseString = "";
        for(int i = 0; i < clause.size(); i++)
        {
            if(i == clause.size() - 1)
            {
                clauseString += clause.get(i);
            }
            else
            {
                
                clauseString += clause.get(i) + " ";
            }
            
        }
        
        clauseStringLength = clauseString.length();

        
        
    }

    public void print(){
        System.out.print(clauseNumber + ". ");
        for(int i = 0; i < clause.size(); i++){
            System.out.print(clause.get(i)+ " ");
        }
    }
}
