
import java.util.*;
import java.lang.*;

public class Pair
{

	private Chromosome parent1, parent2;

	public Pair(Chromosome parent1, Chromosome parent2) {
		this.parent1 = parent1;
		this.parent2 = parent2;
	}

	public static ArrayList<Pair> getTopDownPairs(ArrayList<Chromosome> population) {
		ArrayList<Pair> pairs = new ArrayList<>();

		int numLoops = population.size()/2;
		//increment every two chromosomes
        for (int i = 0; i < numLoops; i+=2) {
        	Pair pair = new Pair(population.get(i), population.get(i+1));
        	pairs.add(pair);
        }

        return pairs;
	}

	public static ArrayList<Pair> getTournamentPairs(ArrayList<Chromosome> population, int tournamentSize) {
		ArrayList<Pair> pairs = new ArrayList<>();
		
		int numLoops = population.size()/4;

        for (int i = 0; i < numLoops; i++) {
        	Pair pair = new Pair(tournamentSelect(population, tournamentSize), tournamentSelect(population, tournamentSize));
        	pairs.add(pair);
        }

        return pairs;
	}

	public static Chromosome tournamentSelect(ArrayList<Chromosome> population, int tournamentSize) {
		Random random = new Random();
		int popSize = population.size();

		int bestIndex = -1;

		//tournament
    	for (int j = 0; j < tournamentSize; j++) {
    		int currentIndex = random.nextInt(popSize);
    		if(bestIndex == -1 || population.get(currentIndex).GetCost() < population.get(bestIndex).GetCost())
    			bestIndex = currentIndex; 
    	}

    	//remove at then end -> this is why we tracked the index
    	return population.remove(bestIndex);
	}

	public void setParent1(Chromosome parent1) {
		this.parent1 = parent1;
	}
	public void setParent2(Chromosome parent2) {
		this.parent2 = parent2;
	}

	public Chromosome getParent1() {
		return parent1;
	}
	public Chromosome getParent2() {
		return parent2;
	}

/*

 private ArrayList<Chromosome> PR_pop;

 public Pair(ArrayList<Chromosome> population)
    {
        PR_pop = population;
    }

 public int SimplePair() 
    {
        return (PR_pop.size() / 4);//the number of mating pairs
    }
   */
 }

