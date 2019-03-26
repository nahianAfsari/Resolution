/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
                    modifiedClause.setSortedString();
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
                    
                    kb.kbMap.put(modifiedClause.sortedClauseString, modifiedClause.clauseString);
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
        Map<String, String> parent1Clauses = new LinkedHashMap<String, String>();
        Map<String, String> parent2Hashmap = new LinkedHashMap<String, String>();
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
                
               parent2Hashmap.put(removedTildeLiteral.toString(),parent2.clause.get(m));
                
            }
            else
            {
                parent2Hashmap.put(parent2.clause.get(m), null);
            }
        }
        boolean neg = false;
        List<String> parent2Clauses = parent2.clause;
        Iterator<Map.Entry<String, String> > iterator =parent2Hashmap.entrySet().iterator();
        while(iterator.hasNext())
        {
             Map.Entry<String, String> entry = iterator.next();
             
            if(parent1Clauses.containsKey(entry.getKey()))
            {
                 if(entry.getValue() != null && parent1Clauses.get(entry.getKey()) == null)
                 {
                    iterator.remove();
                    parent1Clauses.remove(entry.getKey());
                    eliminated++;
                    if(eliminated > 1)
                        {
                            generated.clause = null;
                            generated.contradiction = false;
                            return generated;

                        }
                    
                 }
                 else if(entry.getValue() == null && parent1Clauses.get(entry.getKey()) != null)
                 {
                    iterator.remove();
                    parent1Clauses.remove(entry.getKey());
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
                    if(entry.getValue() == null)
                    {
                        generated.clause.add(entry.getKey());
                    }
                    else
                    {
                        generated.clause.add(entry.getValue());
                    }
                    iterator.remove();
                 }
                
            }
            
            else
            {
                    if(entry.getValue() == null)
                    {
                        generated.clause.add(entry.getKey());
                    }
                    else
                    {
                        generated.clause.add(entry.getValue());
                    }
                    iterator.remove();
            }
             
        }
        
        
        if(!parent1Clauses.isEmpty())
        {
            for(String key : parent1Clauses.keySet())
            {
                if(parent1Clauses.get(key) == null)
                {
                    generated.clause.add(key);
                }
                else
                {
                     generated.clause.add(parent1Clauses.get(key));
                }
               
            }
            
        }
        

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
