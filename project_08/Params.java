import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class for loading GA parameters from a java properties file
 */
class Params {
	private int initialChromes, numChromes, numGenes, numGenerations, tournamentSize;
 	private double mutationFactor;

 	//main constructor using properties filename
	public Params(String filename) {
		loadProperties(filename);
	}

	/**
	 * Get GA properties from given properties file
	 */
	public void loadProperties(String filename) {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream(filename);

			// load a properties file
			prop.load(input);

			// get property values
			initialChromes = Integer.parseInt(prop.getProperty("initialPopulation"));
			numChromes = Integer.parseInt(prop.getProperty("actualPopulation"));
			numGenes = Integer.parseInt(prop.getProperty("numGenes"));
			numGenerations = Integer.parseInt(prop.getProperty("numGenerations"));
			tournamentSize = Integer.parseInt(prop.getProperty("tournamentSize"));
			mutationFactor = Double.parseDouble(prop.getProperty("mutationFactor"));

			//close
			if(input != null)
				input.close();

		//error out if necessary
		} catch (IOException ex) {
			System.out.println("Error reading config file!");
			System.exit(1);
		} 
	}

	//Getters for all the parameters
	
	public int getInitialChromes() {
		return initialChromes;
	}
	public int getNumChromes() {
		return numChromes;
	}
	public int getNumGenes() {
		return numGenes;
	}
	public int getNumGenerations() {
		return numGenerations;
	}
	public int getTournamentSize() {
		return tournamentSize;
	}
	public double getMutationFactor() {
		return mutationFactor;
	}
}