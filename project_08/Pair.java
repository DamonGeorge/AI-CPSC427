
import java.util.*;
import java.lang.*;

/**
 * Holds pair of chromosomes with static functions for creating pairs from population
 */
public class Pair
{
	//The two chromosome members
	private Chromosome parent1, parent2;

	//main constructor
	public Pair(Chromosome parent1, Chromosome parent2) {
		this.parent1 = parent1;
		this.parent2 = parent2;
	}

	/**
	 * Top Down pairing algorithm
	 */
	public static ArrayList<Pair> getTopDownPairs(ArrayList<Chromosome> population) {
		ArrayList<Pair> pairs = new ArrayList<>();
		int numLoops = population.size()/2;

		//increment every two chromosomes
		//just choose the next best 2 parentss
        for (int i = 0; i < numLoops; i+=2) {
        	Pair pair = new Pair(population.get(i), population.get(i+1));
        	pairs.add(pair);
        }

        return pairs;
	}

	/**
	 * Tournament pairing algorithm using the given tournament size
	 */
	public static ArrayList<Pair> getTournamentPairs(ArrayList<Chromosome> population, int tournamentSize) {
		ArrayList<Pair> pairs = new ArrayList<>();
		
		int numLoops = population.size()/4;

        for (int i = 0; i < numLoops; i++) {
        	Pair pair = new Pair(tournamentSelect(population, tournamentSize), tournamentSelect(population, tournamentSize));
        	pairs.add(pair);
        }

        return pairs;
	}

	/**
	 * Select a population member using tournament selection with the given size
	 */
	public static Chromosome tournamentSelect(ArrayList<Chromosome> population, int tournamentSize) {
		Random random = new Random();
		int popSize = population.size();

		int bestIndex = -1;

		//tournament -> select the best chromosome out of the given size of randomly selected chromsomes
    	for (int j = 0; j < tournamentSize; j++) {
    		int currentIndex = random.nextInt(popSize);
    		if(bestIndex == -1 || population.get(currentIndex).GetCost() < population.get(bestIndex).GetCost())
    			bestIndex = currentIndex; 
    	}

    	//remove at then end -> this is why we tracked the index
    	return population.remove(bestIndex);
	}

	//Getters and Setters
	
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
 }

