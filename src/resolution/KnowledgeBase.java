/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resolution;

import java.util.ArrayList;

/**
 *
 * @author nahianAfsari
 */
public class KnowledgeBase {
    
    public int numOfClauses;
    public ArrayList<Clause> clauses;
    
    KnowledgeBase()
    {
        clauses = new ArrayList<>();
        numOfClauses = 0;
    }
    
    
    
    
}
