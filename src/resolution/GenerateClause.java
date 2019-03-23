/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nahianAfsari
 */
public class GenerateClause {
    
     ArrayList<Clause> knowledgeBase;
     KnowledgeBase kb;
     
     GenerateClause(KnowledgeBase kb)
     {
         this.kb = kb;
         
         this.knowledgeBase = kb.clauses;
     }
     
     //this method returns true if a new clause was generated
     public boolean generateNewClause()
     {
         //returns true if there was constradiction
         boolean done = false;
         int i = 1;
         
         //while i is not equal to kbSize means checking until all the clauses in the knowledge base have been checked
         //againts every other clause in the kb
         
         //if it is equal to the numOfClauses then : no more clauses in the kb to check against the rest of kb
         while(i < kb.numOfClauses - 1)
         {
             int kbSize = knowledgeBase.size();
             for(int j = 0; j < i; j++)
             {
                 GenClause genClause = newClause(knowledgeBase.get(j),knowledgeBase.get(i));
                 
                 //if genClause is not null, there was a new clause that was generated
                 if(genClause.clause != null )
                 {
                    Scan scanner = new Scan(knowledgeBase, genClause);
            
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

                    kb.numOfClauses++;
                    modifiedClause.clauseNumber = kb.numOfClauses;
                    kb.clauses.add(modifiedClause);
                    System.out.println();
                    modifiedClause.print();
                 }
                 
                 //if genCase was null, it could mean two things -
                 //1. There was a contradiction
                 //2. No new clause was generated
                 if(genClause.clause == null && genClause.contradiction == true)
                 {
                     System.out.println("Constradiction "+ "{" + genClause.parentClauses.get(0) + ", "
                             + genClause.parentClauses.get(0) + "}");
                     System.out.println("Valid");
                     done = true;
                     
                 }
                 
             }
             
             i++;
         }
         
         
             
         
         return done;
         
     }
     
     //generates new clause givent two parent clauses
     public GenClause newClause(Clause parent1, Clause parent2)
     {
        //first separate the regular literals and negated literals for each parent
        Map<String, String> parent1literals = new HashMap<String, String>();
        Map<String, String> parent1negatedLiterals = new HashMap<String, String>();
        
        Map<String, String> parent2literals = new HashMap<String, String>();
        Map<String, String> parent2negatedLiterals = new HashMap<String, String>();
        
        //look at comments from Scan.java inside literalAndItsNegation method for comments to understand
        //this section
        for(int i = 0; i < parent1.clause.size(); i++)
        {
        
            if(parent1.clause.get(i).charAt(0) == '~')
            {
                StringBuilder removedTildeLiteral = new StringBuilder(parent1.clause.get(i));
                removedTildeLiteral.deleteCharAt(0);
                
                parent1negatedLiterals.put(removedTildeLiteral.toString(), removedTildeLiteral.toString());
            }
            else
            {
                parent1literals.put(parent1.clause.get(i), parent1.clause.get(i));
            }
            
        }
        
        for(int i = 0; i < parent2.clause.size(); i++)
        {
            if(parent2.clause.get(i).charAt(0) == '~')
            {
               
                StringBuilder removedTildeLiteral = new StringBuilder(parent2.clause.get(i));
                removedTildeLiteral.deleteCharAt(0);

                parent2negatedLiterals.put(removedTildeLiteral.toString(), removedTildeLiteral.toString());
            }
            else
            {
                parent2literals.put(parent2.clause.get(i), parent2.clause.get(i));
            }
            
        }
        
        //after adding literals and negated literals to the respective hashmaps for parent1 and parent2
        //check the parent1literals against parent2negatedliterals 
        //check the parent2literals against parent1negatedliterals
        GenClause genClause = new GenClause(null);
        
        for(String key : parent1literals.keySet())
        {
            //if corresponding negated literal doesn't exist, add that to the generated clause
            if(!parent2negatedLiterals.containsKey(key))
            {
                genClause.clause.add(key);
            }
        }
        
        for(String key : parent2literals.keySet())
        {
            if(!parent1negatedLiterals.containsKey(key))
            {
                genClause.clause.add(key);
            }
        }
        
        //if the two clauses were completely different then the genClause.clause would have ALL the literals
        //from both parent clauses. So the followings checks whether the size of clause is equal to the size
        //of parent1 and parent2's literals. If so, we do not add to knowledgebase
        
        if(genClause.clause.size() == (parent1.clause.size() + parent2.clause.size()))
        {
            genClause.clause = null;
            genClause.contradiction = false;
            return genClause;
        }
        
        //add the parent clauses for the new general clause
         genClause.parentClauses.add(parent2.clauseNumber);
         genClause.parentClauses.add(parent1.clauseNumber);
        
        
        //if genClause is null that means all the literals were successfully removed due to a negated version
        //of that literal in the other clause
        if(genClause.clause.size() == 0)
        {
          
            genClause.clause = null;
            genClause.contradiction = true;
            return genClause;
        }
        
       
        return genClause;
        
        
     }
     
     
    
}
