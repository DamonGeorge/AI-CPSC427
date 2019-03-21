import java.util.*;
import java.lang.*;
import java.io.*;
import java.util.concurrent.ThreadLocalRandom;


public class TSP extends GA
{
 private List<List<Integer>> costMatrix;

 public TSP(String paramsFileName, String target, String inputDataFilename, int matingType)
    {
        super(paramsFileName,target);
        
        costMatrix = new ArrayList<List<Integer>>();

        parseInputData(inputDataFilename);

        if (costMatrix.size() != GA_numGenes)
        {
            System.out.println("Error: Input data size differs from number of genes");
            DisplayParams();
            System.exit(1);
        }

        InitPop();
    }

 private void parseInputData(String filename) 
    {
        //read csv into matrix of strings
        List<String[]> rowList = new ArrayList<String[]>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineItems = line.split(",");
                rowList.add(lineItems);
            }
        }
        catch(Exception e){
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

        for(List<Integer> row : costMatrix) {
            for(Integer val: row) {
                System.out.print(val + ", ");
            }
            System.out.println("");
        }

    }

 private void generatePop() 
    {
        List<Character> allGenes = new ArrayList<Character>();
        for (int i = 0; i < GA_numGenes; i++) {
            allGenes.add(i, (char) (97+i)); //starting from 'a'
        }

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

 public void InitPop()
    {
        System.out.println("Test!");
        //super.InitPop();
        generatePop();
        ComputeCost();
        SortPop();
        TidyUp();
    }


 public void DisplayParams()
    {
        //System.out.print("Input Data Filename: ");
        //System.out.println(WG_target);
        super.DisplayParams();
    }

 protected void ComputeCost()
    {
        for (int i = 0; i < GA_pop.size(); i++)
        {
            int cost = 0;
            Chromosome chrom = GA_pop.remove(i);
            //start and end of current path
            int start = 0, end = 1;
            for (int j = 0; j < GA_numGenes; j++) {
                cost += costMatrix.get(chrom.GetGene(start) - 97).get(chrom.GetGene(end) - 97);


                start++;
                end++;
                end = end % GA_numGenes;
            }
            chrom.SetCost(cost);
            GA_pop.add(i,chrom);
        }
    }

 @Override
 protected void Mutate() 
    {
        int totalGenes  = (GA_numGenes * GA_numChromes);
        int numMutate   = (int) (totalGenes * GA_mutFact);
        Random rnum     = new Random();

        for (int i = 0; i < numMutate; i++) 
        {
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

