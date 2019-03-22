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
public class Scan {
    
    //Scan needs two things to check redundancy
    //1. A candidate clause that needs to be checked against the knowledge base to see if it can be added
    //2. The actual knowledge base
    
    ArrayList<Clause> knowledgeBase;
    Clause candidate;
    
    Scan(ArrayList<Clause> knowledgeBase, Clause candidate)
    {
        this.knowledgeBase = knowledgeBase;
        this.candidate = candidate;
    }
    
    public boolean literalAndItsNegation()
    {
        //boolean to hold whether a literal and it's negation exists
        boolean exists = false;
        
        //create two hashmaps, one containing regular literals and another containing the negated literals
        Map<String, String> literals = new HashMap<String, String>();
        Map<String, String> negatedLiterals = new HashMap<String, String>();
        for(int i = 0; i < candidate.clause.size(); i++)
        {
            //if the clause is negated
            if(candidate.clause.get(i).charAt(0) == '~')
            {
                //removing the ~ so I can look it up in the hashmap and match it to a regular literal
                //(regular literals are inside the literals hashmap)
                StringBuilder removedTildeLiteral = new StringBuilder(candidate.clause.get(i));
                removedTildeLiteral.deleteCharAt(0);
                
                //made key and value the same because idk what else to put for value cuz I don't really need it
                negatedLiterals.put(removedTildeLiteral.toString(), removedTildeLiteral.toString());
            }
            else
            {
                literals.put(candidate.clause.get(i), candidate.clause.get(i));
            }
            
        }
        
        //if a literal from literals hashmap is inside negatedLiterals hashmap then that means : 
        //a literal and it's negation exists so we shouldn't add it to knowledge base
        for(String key : literals.keySet())
        {
            if(negatedLiterals.containsKey(key))
            {
                return true;
            }
        }
        
        //if there are no matching literals in either of the hashmaps then that means :
        //a literal and it's negation doesn't exist in the candidate clause so return false;
        return exists;
        
    }
    
    //this method checks for redundant literals, if there are any that exists, it removes the literals
    //from the clause and returns the modified clause
    public Clause checkForRedundantLiterals()
    {
        Clause modifiedClause = new Clause(null);
        modifiedClause.clause = new ArrayList<String>();
       
        
        //contains literals that are encountered for the very first time
        Map<String, String> uniqueLiterals = new HashMap<String, String>();
        
        for(int i = 0; i < candidate.clause.size(); i++)
        {
            //before adding to uniqueLiterals, check if that literal already exists
            //if it does then it's a redundandant literal so don't add to modified clause
            if(uniqueLiterals.containsKey(candidate.clause.get(i)))
            {
                 //do nothing
            }
            else
            {
                String current = candidate.clause.get(i);
                 uniqueLiterals.put(candidate.clause.get(i), candidate.clause.get(i));
                 modifiedClause.clause.add(candidate.clause.get(i));
            }
          
            
        }
        
        
        return modifiedClause;
        
    }
    public boolean checkForRedundantClauses()
    {
        //a boolean that will be set to true if a redundant clause exits
        boolean redundantClause = false;
        
        //if the knowledgeBase is empty that means there can be no redundant clause since it's empty
        if(knowledgeBase == null)
        {
            return true;
        }
        else
        {
            //check redundancy for every clause inside the knowledge base
            for(int i = 0; i < knowledgeBase.size(); i++)
            {
                boolean redundancy = false;
                redundancy = checkForRedundancyHelper(knowledgeBase.get(i));
                //if there was redundancy, return true immediately
                if(redundancy)
                {
                    return true;
                }
                //if no redundancy was found, move on to the next clause of the knowledge base to check against
               
            }
        }
        
        return redundantClause;
        
    }
    
    //this is a helper function of checkForRedundancy, it takes in the candidate clause and takes in another
    //clause to be checked against
    public boolean checkForRedundancyHelper(Clause kb)
    {
        boolean redundantClause  = false;
        
        //Creating a hashmap to store all the individual literals of the candidate clause
        Map<String, String> hashmap = new HashMap<String, String>();
        for(int i = 0; i < candidate.clause.size(); i++)
        {
            //made key and value the same because idk what else to put for value
            //I just need key to look up whether this literal exists or not in the knowledgeBase clause
            hashmap.put(candidate.clause.get(i), candidate.clause.get(i));
        }
        
        //if every literal in the knowledgeBase clause is also in the hashmap of literals from the candidate clause
        //that means it is the same clause
        int kbSize = kb.clause.size();
        for(int i = 0; i < kb.clause.size(); i++)
        {
            if(hashmap.containsKey(kb.clause.get(i)))
            {
                hashmap.remove(kb.clause.get(i));
                kbSize--;
            }
        }
        
        //checking whether they end up being size 0 which indicates every literal matched
        if(hashmap.size() == 0 && kbSize == 0)
        {
            //candidate clause and knowledgeBase clause is the same
            return true;
        }
        
        //candidate clause and knowledgeBase clause is not the same
        return redundantClause;
    }
    
}
