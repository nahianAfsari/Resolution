/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resolution;

import java.io.*;
import java.util.ArrayList;
import java.util.List;



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
            BufferedReader in = new BufferedReader(new FileReader("task6.in"));

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
        
        KnowledgeBase kb = new KnowledgeBase();
  
        for(int j = 0; j < clauses.size(); j++)
        {
            kb.numOfClauses++;
            clauses.get(j).clauseNumber = kb.numOfClauses;
            kb.clauses.add(clauses.get(j));
            
            
            
        }
        
        printInitialClauses(kb.clauses);
        
        
        
        
        GenerateClause generateClause = new GenerateClause(kb);
        generateClause.generateNewClause();
        
        
        
        
        

    }
    
    

    //initially in the knowledge base, we're not doing any resolution
    // so the curly brackets are always empty
    public static void printInitialClauses(ArrayList<Clause> clauses){
        for(int i = 0; i < clauses.size(); i++){
            clauses.get(i).print();
            System.out.print("{}\n");
            
        }
    }
  
    
    

}