/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Clause {

    List<String> clause;
    String clauseString;
    int clauseStringLength;
    int clauseNumber;

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
            
            clauseStringLength = clauseString.length();
            
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
