import java.util.*;
import java.lang.*;

/**
 * Static mating functions for lists of pairs
 */
public class Mate
{

    /**
     * Cycle Crossover algorithm for mating the given list of pairs
     */
    public static ArrayList<Chromosome> cycleCrossover(ArrayList<Pair> pairs, int numGenes) {
        ArrayList<Chromosome> newPop = new ArrayList<>();
        Random random = new Random();

        //loop through all pairs
        for(Pair pair : pairs) {
            //get parents and children
            Chromosome parent1 = pair.getParent1();
            Chromosome parent2 = pair.getParent2();
           
            Chromosome child1 = new Chromosome(parent1);
            Chromosome child2 = new Chromosome(parent2);

            int switchIndex = 0;
            boolean duplicateFound;
            do {
                //get genes
                char gene1 = child1.GetGene(switchIndex);
                char gene2 = child2.GetGene(switchIndex);

                //switch genes
                child1.SetGene(switchIndex, gene2);
                child2.SetGene(switchIndex, gene1);

                //find duplicate
                duplicateFound = false;
                for (int j = 0; j < numGenes; j++) {
                    if(j != switchIndex && child1.GetGene(j) == child1.GetGene(switchIndex)) {
                        duplicateFound = true;
                        switchIndex = j;
                        break;
                    }
                }
            }while(duplicateFound);

            //add all to the population
            newPop.add(parent1);
            newPop.add(parent2);
            newPop.add(child1);
            newPop.add(child2);
        }

        return newPop;
    }

    /**
     * PMX Crossover algorithm for generating children from all the given pairs
     */
    public static ArrayList<Chromosome> partiallyMatchedCrossover(ArrayList<Pair> pairs, int numGenes) {
        ArrayList<Chromosome> newPop = new ArrayList<>();
        Random random = new Random();

        //loop through all pairs
        for(Pair pair : pairs) {
            Chromosome parent1 = pair.getParent1();
            Chromosome parent2 = pair.getParent2();
           
            //start and end crosspoints -> inclusive
            int crossPoint1 = random.nextInt(numGenes); 
            int crossPoint2 = crossPoint1 + random.nextInt(numGenes - crossPoint1);

            //num chars to switch. (1 to numGenes)
            int crossSize = (crossPoint2 - crossPoint1) + 1;

            //generate children
            Chromosome child1 = generatePMXChild(parent1, parent2, crossPoint1, crossSize);
            Chromosome child2 = generatePMXChild(parent2, parent1, crossPoint1, crossSize);

            //add all to the population
            newPop.add(parent1);
            newPop.add(parent2);
            newPop.add(child1);
            newPop.add(child2);
        }

        return newPop;
    }

    /**
     * PMX Crossover algorithm for generating single child from the two parents
     * using the provided crossover start and length
     */
    private static Chromosome generatePMXChild(Chromosome parent1, Chromosome parent2, int crossStart, int crossLength) {
        Chromosome child = new Chromosome(parent1);

        //loop through crossover chars
        for (int i = crossStart; i < crossLength; i++) {
            char geneToRemove = child.GetGene(i);
            char geneToAdd = parent2.GetGene(i);

            //switch genes in the child
            int switchIndex = child.findGene(geneToAdd);
            child.SetGene(i, geneToAdd);
            child.SetGene(switchIndex, geneToRemove);
        }

        return child;
    }
 }
