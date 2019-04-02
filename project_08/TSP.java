import java.util.*;
import java.lang.*;
import java.io.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Traveling Salesman implementation of the Genetic Algorithm
 */
public class TSP extends GA {
    //Cost for all paths
    private List<List<Integer>> costMatrix;

    //main constructor used by TspTest
    public TSP(String paramsFileName, String target, String inputDataFilename, int matingType) {
        //create GA
        super(paramsFileName,target, matingType);
        
        //parse costs from file
        costMatrix = new ArrayList<List<Integer>>();
        parseInputData(inputDataFilename);

        //Confirm input costs match the number of genes
        if (costMatrix.size() != GA_numGenes) {
            System.out.println("Error: Input data size differs from number of genes");
            DisplayParams();
            System.exit(1);
        }

        DisplayParams();
        DisplayMatingType();

        //initialize the population
        InitPop();
    }

    /**
     * Display the pair and mating algorithm types
     */
    public void DisplayMatingType() {
        System.out.println("\nPair & Mating Algorithms:");
        switch(GA_matingType) {
        case 0:
            System.out.println("Top Down    &   Cycle Crossover");
            break;
        case 1:
             System.out.println("Top Down   &   Partially Matched Crossover");
            break;
        case 2:
             System.out.println("Tournament &   Cycle Crossover");
            break;
        default:
             System.out.println("Tournament &   Partially Matched Crossover");
            break;
        }
    }

    /**
     * Parse the cost data from the given file
     */
    private void parseInputData(String filename) {
        //read csv into matrix of strings
        List<String[]> rowList = new ArrayList<String[]>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineItems = line.split(",");
                rowList.add(lineItems);
            }
        } catch(Exception e){
            System.out.println("Error parsing input file!");
            System.exit(1);
        }

        //convert matrix of strings to integers
        for (int i = 0; i < rowList.size(); i++) {
            String[] row = rowList.get(i);
            List<Integer> newRow = new ArrayList<Integer>();

            for (int j = 0; j < row.length; j++) {
                try {
                    newRow.add(Integer.parseInt(row[j]));
                } catch(Exception e) {
                    System.out.println("Error parsing input data as integers!");
                    System.exit(1);
                }
            }
            
            costMatrix.add(newRow);
        }
    }

    /**
     * Generate the population by shuffling all possible nodes
     */
    private void generatePop() {
        //create list of all possible genes
        List<Character> allGenes = new ArrayList<Character>();
        for (int i = 0; i < GA_numGenes; i++) {
            allGenes.add(i, (char) (97+i)); //starting from 'a'
        }

        //create population by shuffling genes
        for(int i = 0; i < GA_numChromesInit; i++) {
            //shuffle possible genes
            Collections.shuffle(allGenes);

            //create new chromose
            Chromosome Chrom = new Chromosome(GA_numGenes);

            for (int j = 0; j < GA_numGenes; j++) { 
                Chrom.SetGene(j,allGenes.get(j));
            }
            Chrom.SetCost(0);
            GA_pop.add(Chrom);
        }
    }

    /**
     * Initialize the population
     */
    public void InitPop(){
        generatePop();
        ComputeCost();
        SortPop();
        TidyUp();
    }


    public void DisplayParams(){
        super.DisplayParams();
    }

    /**
     * Compute the cost of all chromosomes in the population
     * by adding all costs of edges in the TSP 
     */
    protected void ComputeCost() {
        //Loop through all chromosomes
        for (int i = 0; i < GA_pop.size(); i++) {
            int cost = 0;
            Chromosome chrom = GA_pop.remove(i);

            //Compute cost from each node to the next
            int start = 0, end = 1;
            for (int j = 0; j < GA_numGenes; j++) {
                cost += costMatrix.get(chrom.GetGene(start) - 97).get(chrom.GetGene(end) - 97);
                //increment current location
                start++;
                end++;
                end = end % GA_numGenes;
            }

            //set costs
            chrom.SetCost(cost);
            GA_pop.add(i,chrom);
        }
    }

    /**
     * Mutate TSP genes by switching places of randomly chosen genes
     */
    @Override
    protected void Mutate()  {
        int totalGenes  = (GA_numGenes * GA_numChromes);
        int numMutate   = (int) (totalGenes * GA_mutFact);
        Random rnum     = new Random();

        for (int i = 0; i < numMutate; i++)  {
            //position of chromosome to mutate--but not the first one
            //the number generated is in the range: [1..GA_numChromes)
            int chromMut = 1 + (rnum.nextInt(GA_numChromes - 1));
            Chromosome newChromosome = GA_pop.remove(chromMut); //get chromosome

            //get index of genes to switch
            int firstIndex = rnum.nextInt(GA_numGenes);
            int secondIndex = rnum.nextInt(GA_numGenes);

            //switch genes (requiring a temp)
            char tempGene = newChromosome.GetGene(firstIndex);
            newChromosome.SetGene(firstIndex, newChromosome.GetGene(secondIndex));
            newChromosome.SetGene(secondIndex, tempGene);

            GA_pop.add(newChromosome); //add mutated chromosome at the end
        }
    }
 }