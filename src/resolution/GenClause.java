/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author nahianAfsari
 */
//this class has everthing of a clause but it has an additional list of integers that 
//correspond to the parent clauses that helped generate this new clause
public class GenClause extends Clause {
    
    List<Integer> parentClauses;
    boolean contradiction = false;
    String sortedClauseString;
    
    
    public GenClause(String input) {
        super(input);
      //  System.out.println(sortedClause);
        parentClauses = new ArrayList<>();
    }
    
   

    
}
