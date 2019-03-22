/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resolution;

import java.io.*;
import java.util.ArrayList;



/**
 *
 * @author nahianafsari
 */

public class Resolution {


    public static void main(String[] args) throws IOException {

        ArrayList<Clause> clauses = new ArrayList<>();
        ArrayList<Clause> knowledgeBase = new ArrayList<>();
        Clause testingClause = null;
        try {
            BufferedReader in = new BufferedReader(new FileReader("demo.in"));

            String input = in.readLine();
            int x = 0;
            while (input != null) {
                clauses.add(new Clause(input));
                x++;
                input = in.readLine();
            }

            testingClause = clauses.get(x - 1);
            clauses.remove(x - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

       
        //System.out.println("testing clause: ");
        //testingClause.print();
        for(int i = 0; i < testingClause.clause.size();i++)
        {
           //negating the negated literal
           if(testingClause.clause.get(i).charAt(0) == '~')
           {
               StringBuilder removedTildeLiteral = new StringBuilder(testingClause.clause.get(i));
               removedTildeLiteral.deleteCharAt(0);
               clauses.add(new Clause(removedTildeLiteral.toString()));
               
           }
           //negating the regular literal
           else
           {
               String addedTildeLiteral = "~" + testingClause.clause.get(i);
               clauses.add(new Clause(addedTildeLiteral));
           }
           
           
        }
        printClauses(clauses);
        
        for(int j = 0; j < clauses.size(); j++)
        {
           
            Scan scanner = new Scan(knowledgeBase, clauses.get(j));
            
            //first clean up the clause to make sure there are no redundant literals
            Clause modifiedClause = scanner.checkForRedundantLiterals();
            scanner.candidate = modifiedClause;
            
            //if removing redundant literals ends up being null then move on to the next clause
            if(modifiedClause == null)
            {
                continue;
            }
            //then check for the following conditions :
            
            //returns true if there are redundant clauses
            if(scanner.checkForRedundantClauses())
            {
                //if addition of this jth clauses leads to redundant clauses, don't add it to knowledgebase
                //continue on to evaluating the next clause in the clauses list
                
                continue;
            }
            
            //returns true if a literal and it's negation exists
            if(scanner.literalAndItsNegation())
            {
              continue;
            }
            
            knowledgeBase.add(modifiedClause);  
            
        }
        
        System.out.println("\nKnowledge Base clauses : ");
        printClauses(knowledgeBase);
        

    }

    public static void printClauses(ArrayList<Clause> clauses){
        for(int i = 0; i < clauses.size(); i++){
            clauses.get(i).print();
        }
    }
  
    
    

}