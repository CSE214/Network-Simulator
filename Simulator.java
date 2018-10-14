package networksimulator;


/**
 * The <code>Simulator</code> class allows the user to run the simulation.
 * 
 * @author Sayan Sivakumaran e-mail: sayan.sivakumaran@stonybrook.edu Stony
 *         Brook ID: 110261379
 **/
public class Simulator {
	private static Router dispatcher = new Router(); // Level 1 router
	private static Router[] routers; // Level 2 routers
	private static int totalServiceTime; // Total time each packet is in the network
	private static int totalPacketsArrived; // Count of packets that have reached destination
	private static int packetsDropped; // The number of dropped packets
	private static double arrivalProb; // Probability of new packet arrival
	private static int numIntRouters; // Number of intermediate routers
	private static int maxBufferSize; // Buffer size of router
	private static int minPacketSize = 0; // Minimum possible packet size
	private static int maxPacketSize = 10; // Maximum possible packet size
	private static int bandwidth; // Number of packets destination router can accept at one time
	private static int duration; // Number of units of time to simulate
	private static int currentTime = 0; // Current time of simulation
	
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
	 * Create a new packet in the simulation.
	 */
	public static void newPacket() {
		Packet newPacket = new Packet(currentTime, minPacketSize, maxPacketSize);
		dispatcher.enqueue(newPacket);
		System.out.println(String.format(
				"Packet %d arrives at dispatcher with size %d.", 
				newPacket.getId(), newPacket.getPacketSize()));
	}
	
	/**
	 * Runs the simulation using user-defined parameters. After the
	 * simulation is done, the user will be asked if they want
	 * to run another simulation.
	 */
	public static void main (String[] args) {
	
	}
}
