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
public class KnowledgeBase {
    
    public int numOfClauses;
    public ArrayList<Clause> clauses;
    Map<String,String> sortedClauseString = new HashMap<String,String>();
    
    KnowledgeBase()
    {
        clauses = new ArrayList<>();
        numOfClauses = 0;
    }
    
    
    
    
}
