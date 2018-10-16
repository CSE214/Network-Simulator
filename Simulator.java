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
	private static int maxBufferSize = 10; // Buffer size of router
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
	 * Receives necessary input from the user.
	 * 
	 * <dl>
	 * <dt>Postconditions:</dt>
	 * <dd>Necessary parameters have been initialized.</dd>
	 * </dl>
	 */
	private static void getUserInput() {
		arrivalProb = 0.5;
		numIntRouters = 4;
		maxBufferSize = 10;
		minPacketSize = 500;
		maxPacketSize = 1500;
		bandwidth = 2;
		duration = 25;
	}
	
	/**
	 * Sets up the routers according to user specification.
	 * 
	 * <dl>
	 * <dt>Postconditions:</dt>
	 * <dd>A router array with the specified parameters has been initialized.</dd>
	 * </dl>	 
	 */
	private static void setupRouters() {
		Router.setBufferSize(maxBufferSize);
		routers = new Router[numIntRouters + 1];
		for (int i = 1; i < routers.length; i++) {
			routers[i] = new Router();
		}
	}
	
	/**
	 * Create a new packet in the simulation.
	 * 
	 * <dl>
	 * <dt>Postconditions:</dt>
	 * <dd>A new packet has been created and pushed into the simulation.</dd>
	 * </dl>
	 */
	public static void newPacket() {
		Packet newPacket = new Packet(currentTime, minPacketSize, maxPacketSize);
		dispatcher.enqueue(newPacket);
		System.out.println(String.format(
				"Packet %d arrives at dispatcher with size %d.", 
				newPacket.getId(), newPacket.getPacketSize()));
	}
	
	/**
	 * Decrements the time to arrival for all packets in the
	 * intermediate routers.
	 * 
	 * <dl>
	 * <dt>Postconditions:</dt>
	 * <dd>
	 * Each packet inside an intermediate router has its time to arrival
	 * decreased by 1.
	 * </dd>
	 * </dl>
	 * 
	 */
	public static void decrementArrivalTimes() {
		for (int i = 1; i < routers.length; i++) {
			routers[i].decrementArrivalTime();
		}
	}
	
	/**
	 * Responsible for generating new packets at the start of the time unit.
	 * 
	 * <dl>
	 * <dt>Postconditions:</dt>
	 * <dd>A new packet has been created and pushed into the simulation.</dd>
	 * </dl>
	 */
	public static void tryNewPackets() {
		double diceRoll;
		int countArrived = 0;
		for (int i = 0; i < 3; i++) {
			diceRoll = Math.random();
			if (diceRoll < arrivalProb) {
				countArrived += 1;
				newPacket();
			}
		}
		if (countArrived == 0) {
			System.out.println("No packets have arrived.");
		}
	}
	
	/**
	 * Responsible for sending new packets to the appropriate intermediate router.
	 * 
	 * <dl>
	 * <dt>Postconditions:</dt>
	 * <dd>
	 * Packets have been sent to an appropriate router, if available. If no routers
	 * are available, the packets will be dropped.
	 * </dd>
	 * </dl>
	 */
	public static void sendPacketsToRouters() {
		while (dispatcher.size() != 0) {
			Packet nextPacket = dispatcher.dequeue();
			try {
				int bestRouterIndex = Router.sendPacketTo(routers);
				routers[bestRouterIndex].enqueue(nextPacket);
			} catch (FullRouterException e) {
				System.out.println("Network is congested. Packet " + nextPacket.getId() + " is dropped.");
			}	
		}
	}
	
	/**
	 * Prints the status of the routers to the user.
	 * 
	 * <dl>
	 * <dt>Postconditions:</dt>
	 * <dd>The status of the routers have been printed to the user.</dd>
	 * </dl>
	 */
	public static void printRouterStatus() {
		for (int i = 1; i < routers.length; i++) {
			System.out.println("R" + i + ": " + routers[i].toString());
		}
	}
	
	/**
	 * Simulates one unit of time in the simulation.
	 * 
	 * <dl>
	 * <dt>Postconditions:</dt>
	 * <dd>
	 * New packets attempt to generate. If any are generated, they are
	 * sent to the appropriate router, if possible. Any packets that can 
	 * move to their destination
	 * have attempted to do so.
	 * </dd>
	 * </dl>
	 */
	public static void simulateTimeUnit() {
		currentTime += 1;
		System.out.println("\nTime: " + currentTime);
		decrementArrivalTimes();
		tryNewPackets();
		sendPacketsToRouters();
		printRouterStatus();
	}
	
	/**
	 * Runs the simulation.
	 * 
	 * <dl>
	 * <dt>Postconditions:</dt>
	 * <dd>
	 * The simulation has started.
	 * </dd>
	 * </dl>
	 */
	public static double simulate () {
		getUserInput();
		setupRouters();
		while (currentTime != duration) {
			simulateTimeUnit();
		}
		return 0.3;
	}
	
	/**
	 * Runs the simulation using user-defined parameters. After the
	 * simulation is done, the user will be asked if they want
	 * to run another simulation.
	 */
	public static void main (String[] args) {
		getUserInput();
		simulate();
	}
}
