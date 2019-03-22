/*
 Usage: java TspTest <configFilename> <inputDataFilename> <Mate/Pair Type>
 Example: java TspTest config.properties tsp_input_data.csv 0
*/

import java.lang.*;

public class TspTest {

 public static void main(String args[])
    {

        //no target with TSP!
        TSP tsp = new TSP(args[0],"", args[1], Integer.parseInt(args[2]));

        System.out.println();
        //tsp.DisplayParams(); //Uncomment to display the contents of the parameter file
        //tsp.DisplayPop(); //Uncomment to display the population before evolution
        tsp.Evolve();
        //tsp.DisplayPop(); //Uncomment to display the population after evolution
    }
}

