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

        printClauses(clauses);
        System.out.println("testing clause: ");
        testingClause.print();

    }

    public static void printClauses(ArrayList<Clause> clauses){
        for(int i = 0; i < clauses.size(); i++){
            clauses.get(i).print();
        }
    }

}