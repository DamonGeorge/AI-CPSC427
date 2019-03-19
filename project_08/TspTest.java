/*
 Usage: java TspTest <paramFile> <dataFile> <Mate/Pair Type>
 Example: java WordGuess param.dat genetic
    Using the parameter file, param.dat, try to generate the word "genetic."
*/

import java.lang.*;

public class TspTest {

 public static void main(String args[])
    {

        //no target with TSP!
        TSP tsp = new TSP(args[0],"", args[1], Integer.parseInt(args[2]));

        System.out.println();
        tsp.DisplayParams(); //Uncomment to display the contents of the parameter file
        tsp.DisplayPop(); //Uncomment to display the population before evolution
        //tsp.Evolve();
        //WG1.DisplayPop(); Uncomment to display the population after evolution
        System.out.println();
    }
}

