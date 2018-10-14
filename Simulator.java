package networksimulator;


/**
 * The <code>Simulator</code> class allows the user to run the simulation.
 * 
 * @author Sayan Sivakumaran e-mail: sayan.sivakumaran@stonybrook.edu Stony
 *         Brook ID: 110261379
 **/
public class Simulator {
	private Router dispatcher; // Level 1 router
	private Router[] routers; // Level 2 routers
	private int totalServiceTime; // Total time each packet is in the network
	private int totalPacketsArrived; // Count of packets that have reached destination
	private int packetsDropped; // The number of dropped packets
	private double arrivalProb; // Probability of new packet arrival
	private int numIntRouters; // Number of intermediate routers
	private int maxBufferSize; // Buffer size of router
	private int minPacketSize; // Minimum possible packet size
	private int maxPacketSize; // Maximum possible packet size
	private int bandwidth; // Number of packets destination router can accept at one time
	private int duration; // Number of units of time to simulate
	
	/**
	 * Creates a random number in the given interval
	 * 
	 * @param minVal
	 * 	The lowest possible value.
	 * @param maxVal
	 * 	The highest possible value
	 * 
	 * @return
	 * 	A random number in the defined interval.
	 */
	private int randInt(int minVal, int maxVal) {
		return (int) (Math.random()*(maxVal-minVal) + minVal);
	}
	
	/**
	 * Runs the simulation using user-defined parameters. After the
	 * simulation is done, the user will be asked if they want
	 * to run another simulation.
	 */
	public static void main (String[] args) {

	}
}
