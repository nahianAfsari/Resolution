/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nahianAfsari
 */
public class GenerateClause {
    
     ArrayList<Clause> knowledgeBase;
     KnowledgeBase kb;
     int startPosition = 1;
     
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
         
         //while i is not equal to kbSize means checking until all the clauses in the knowledge base have been checked
         //againts every other clause in the kb
         
         //if it is equal to the numOfClauses then : no more clauses in the kb to check against the rest of kb
         while(startPosition < kb.numOfClauses)
         {
             //System.out.println(startPosition);
             int kbSize = knowledgeBase.size();
             
             for(int j = 0; j < startPosition; j++)
             {
                 //System.out.println(knowledgeBase.get(j).clause.get(0));
                 //System.out.println(knowledgeBase.get(i).clause.get(0));
                 GenClause genClause = newClause(knowledgeBase.get(j),knowledgeBase.get(startPosition));
                 
                 //if genClause is not null, there was a new clause that was generated
                 if(genClause.clause != null )
                 {
                    int parent1 = genClause.parentClauses.get(0);
                    int parent2 = genClause.parentClauses.get(1);
                    Scan scanner = new Scan(knowledgeBase, genClause, kb);
            
                    //first clean up the clause to make sure there are no redundant literals
                    Clause modifiedClause = scanner.checkForRedundantLiterals();
                    scanner.candidate = modifiedClause;

                    //if removing redundant literals ends up being null then move on to the next clause
                    if(modifiedClause.clause.isEmpty())
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
                    modifiedClause.print();
                    System.out.print("{" + parent1 + ", " + parent2 + "}");
                    System.out.println();
                 }
                 
                 //if genCase was null, it could mean two things -
                 //1. There was a contradiction
                 //2. No new clause was generated
                 if(genClause.clause == null && genClause.contradiction == true)
                 {
                     
                     int clauseNum = kb.numOfClauses + 1;
                     System.out.println(clauseNum + ". Constradiction "+ "{" + genClause.parentClauses.get(0) + ", "
                             + genClause.parentClauses.get(1) + "}");
                     System.out.println("Valid");
                     done = true;
                     return done;
                     
                 }
                 
             }
             
             startPosition++;
         }
         
         
             
         
         return done;
         
     }
     
     //generates new clause givent two parent clauses
     public GenClause newClause(Clause parent1, Clause parent2)
     {
       
         
        int eliminated = 0;
        Map<String, String> parent1Clauses = new HashMap<String, String>();
        Map<String, String> parent2Hashmap = new HashMap<String, String>();
        GenClause generated = new GenClause(null);
        generated.clause = new ArrayList<>();
        
        
        for(int k = 0; k < parent1.clause.size(); k++)
        {
            if(parent1.clause.get(k).charAt(0) == '~')
            {
                StringBuilder removedTildeLiteral = new StringBuilder(parent1.clause.get(k));
                removedTildeLiteral.deleteCharAt(0);
                
               parent1Clauses.put(removedTildeLiteral.toString(), parent1.clause.get(k));
                
            }
            else
            {
                parent1Clauses.put(parent1.clause.get(k), null);
            }
        }
        
        for(int m = 0; m < parent2.clause.size(); m++)
        {
            if(parent2.clause.get(m).charAt(0) == '~')
            {
                StringBuilder removedTildeLiteral = new StringBuilder(parent2.clause.get(m));
                removedTildeLiteral.deleteCharAt(0);
                
               parent2Hashmap.put(removedTildeLiteral.toString(),null);
                
            }
            else
            {
                parent2Hashmap.put(parent2.clause.get(m), null);
            }
        }
        boolean neg = false;
        List<String> parent2Clauses = parent2.clause;
        for(int l = 0; l <parent2Clauses.size(); l++)
        {
            neg = false;
            if(parent2Clauses.get(l).charAt(0) == '~')
            {
                neg = true;
            }
            
            if(parent1Clauses.containsKey(parent2Clauses.get(l)))
            {
                if(neg == true && parent1Clauses.get(parent2Clauses.get(l)) != null)
                {
                    parent1Clauses.remove(parent2Clauses.get(l));
                    parent2Hashmap.remove(parent2Clauses.get(l));
                    eliminated++;
                      if(eliminated > 1)
                        {
                            generated.clause = null;
                            generated.contradiction = false;
                            return generated;

                        }
                }
                else if(neg == false && parent1Clauses.get(parent2Clauses.get(l)) == null)
                {
                    parent1Clauses.remove(parent2Clauses.get(l));
                    parent2Hashmap.remove(parent2Clauses.get(l));
                    eliminated++;
                     if(eliminated > 1)
                        {
                            generated.clause = null;
                            generated.contradiction = false;
                            return generated;

                        }
                }
                else
                {
                    generated.clause.add(parent2Clauses.get(l));
                }
            }
            else
            {
                generated.clause.add(parent2Clauses.get(l));
            }
            
        }
        
        if(!parent2Hashmap.isEmpty())
        {
            for(String key : parent2Hashmap.keySet())
            {
                generated.clause.add(parent2Hashmap.get(key));
            }
            
        }
        
        if(!parent1Clauses.isEmpty())
        {
            for(String key : parent1Clauses.keySet())
            {
                generated.clause.add(parent1Clauses.get(key));
            }
            
        }


///////////////////////////////////////////
//Previous stuff (works)

      /*

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
                
                parent1negatedLiterals.put(removedTildeLiteral.toString(), parent1.clause.get(i));
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

                parent2negatedLiterals.put(removedTildeLiteral.toString(), parent2.clause.get(i));
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
        genClause.clause = new ArrayList<>();
        
        //we cannot eliminate more than one literal at a time
        int numOfLiteralsEliminated = 0;
        
        
        
        
     
        
        
        for(String key : parent1literals.keySet())
        {
            //if corresponding negated literal doesn't exist, add that to the generated clause
            if(!parent2negatedLiterals.containsKey(key))
            {
                genClause.clause.add(parent1literals.get(key));
                
            }
            else
            {
                parent2negatedLiterals.remove(key);
                numOfLiteralsEliminated++;
                if(numOfLiteralsEliminated > 1)
                {
                    genClause.clause = null;
                    genClause.contradiction = false;
                    return genClause;
                    
                }
            }
        }
       
        for(String key : parent1negatedLiterals.keySet())
        {
            //if corresponding negated literal doesn't exist, add that to the generated clause
            if(!parent2literals.containsKey(key))
            {
                genClause.clause.add(parent1negatedLiterals.get(key));
            }
            else
            {
                parent2literals.remove(key);
                numOfLiteralsEliminated++;
                if(numOfLiteralsEliminated > 1)
                {
                    genClause.clause = null;
                    genClause.contradiction = false;
                    return genClause;
                    
                }
            }
            
        }
        
        if(!parent2literals.isEmpty())
        {
            for(String key : parent2literals.keySet())
            {
                genClause.clause.add(parent2literals.get(key));
            }
            
        }
        if(!parent2negatedLiterals.isEmpty())
        {
            for(String key : parent2negatedLiterals.keySet())
            {
                genClause.clause.add(parent2negatedLiterals.get(key));
            }
            
        }
        
        */
  ////////////////////////////////////////////////////////////////      
        

         //if the two clauses were completely different then the genClause.clause would have ALL the literals
        //from both parent clauses. So the followings checks whether the size of clause is equal to the size
        //of parent1 and parent2's literals. If so, we do not add to knowledgebase
        
        if(generated.clause.size() == (parent1.clause.size() + parent2.clause.size()))
        {
           generated.clause = null;
            generated.contradiction = false;
            return generated;
        }
        if(eliminated > 1)
        {
            generated.clause = null;
            generated.contradiction = false;
            return generated;
            
        }
        //add the parent clauses for the new general clause
         generated.parentClauses.add(parent2.clauseNumber);
         generated.parentClauses.add(parent1.clauseNumber);
        
        
        //if genClause is null that means all the literals were successfully removed due to a negated version
        //of that literal in the other clause
        if(generated.clause.size() == 0 && parent1.clause.size() == 1 && parent2.clause.size() == 1)
        {
          
            generated.clause = null;
            generated.contradiction = true;
            return generated;
        }
        
       
        return generated;
        
        
     }
     
     
    
}
