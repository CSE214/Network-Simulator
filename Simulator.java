package networksimulator;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.LinkedList;

/**
 * The <code>Simulator</code> class allows the user to run the simulation.
 * 
 * @author Sayan Sivakumaran e-mail: sayan.sivakumaran@stonybrook.edu Stony
 *         Brook ID: 110261379
 **/
public class Simulator {
	private static Router dispatcher; // Level 1 router
	private static Router[] routers; // Level 2 routers
	private static int totalServiceTime; // Total time each packet is in the network
	private static int totalPacketsArrived; // Count of packets that have reached destination
	private static int packetsDropped; // The number of dropped packets
	private static double arrivalProb; // Probability of new packet arrival
	private static int numIntRouters; // Number of intermediate routers
	private static int maxBufferSize; // Buffer size of router
	private static int minPacketSize; // Minimum possible packet size
	private static int maxPacketSize; // Maximum possible packet size
	private static int bandwidth; // Number of packets destination router can accept at one time
	private static int duration; // Number of units of time to simulate
	private static int currentTime; // Current time of simulation
	private static LinkedList<Router> routerQueue;
	private static InputHandler in = new InputHandler();

	/**
	 * Creates a random number in the given interval
	 * 
	 * @param minVal The lowest possible value.
	 * @param maxVal The highest possible value
	 * 
	 * @return A random number in the defined interval.
	 */
	private int randInt(int minVal, int maxVal) {
		return (int) (Math.random() * (maxVal - minVal) + minVal);
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
		System.out.print("\nEnter the number of Intermediate routers: ");
		numIntRouters = in.nextPositiveInt();
		System.out.print("\nEnter the arrival probability of a packet: ");
		arrivalProb = in.nextPositiveDouble();
		System.out.print("\nEnter the maximum buffer size of a router: ");
		maxBufferSize = in.nextPositiveInt();
		System.out.print("\nEnter the minimum size of a packet: ");
		minPacketSize = in.nextPositiveInt();
		System.out.print("\nEnter the maximum size of a packet: ");
		maxPacketSize = in.nextPositiveInt();
		System.out.print("\nEnter the bandwidth size: ");
		bandwidth = in.nextPositiveInt();
		System.out.print("\nEnter the simulation duration: ");
		duration = in.nextPositiveInt();
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
		System.out.println(String.format("Packet %d arrives at dispatcher with size %d.", newPacket.getId(),
				newPacket.getPacketSize()));
	}

	/**
	 * Decrements the time to arrival for the first packets in any intermediate
	 * non-empty routers.
	 * 
	 * <dl>
	 * <dt>Postconditions:</dt>
	 * <dd>The first packet inside an intermediate non-empty router has its time to
	 * arrival decreased by 1.</dd>
	 * </dl>
	 * 
	 */
	public static void decrementArrivalTimes() {
		for (int i = 1; i < routers.length; i++) {
			if (routers[i].size() > 0) {
				routers[i].decrementArrivalTime();
			}
		}
	}

	/**
	 * Increments the time in network for every packet in every router.
	 * 
	 * <dl>
	 * <dt>Postconditions:</dt>
	 * <dd>Every router has their packets timeInNetwork incremented by 1.</dd>
	 * </dl>
	 * 
	 */
	public static void incrementTimeInNetwork() {
		for (int i = 1; i < routers.length; i++) {
			if (routers[i].size() > 0) {
				routers[i].incrementTimeInNetwork();
			}
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
	 * <dd>Packets have been sent to an appropriate router, if available. If no
	 * routers are available, the packets will be dropped.</dd>
	 * </dl>
	 */
	public static void sendPacketsToRouters() {
		while (dispatcher.size() != 0) {
			Packet nextPacket = dispatcher.dequeue();
			try {
				int bestRouterIndex = Router.sendPacketTo(routers);
				routers[bestRouterIndex].enqueue(nextPacket);
			} catch (FullRouterException e) {
				packetsDropped += 1;
				System.out.println("Network is congested. Packet " + nextPacket.getId() + " is dropped.");
			}
		}
	}

	/**
	 * Keeps track of intermediate routers that are ready to send their packets.
	 * 
	 * <dl>
	 * <dt>Postconditions:</dt>
	 * <dd>Any routers with their first packet having timeToDest=0 are pushed to the
	 * queue.</dd>
	 * </dl>
	 */
	public static void pushToRouterQueue() {
		for (int i = 1; i < routers.length; i++) {
			if (routers[i].size() != 0 && routers[i].getFirst().getTimeToDest() == 0
					&& routerQueue.indexOf(routers[i]) == -1) {
				routerQueue.addLast(routers[i]);
			}
		}
	}

	/**
	 * Pops any packets that are ready to leave their intermediate router.
	 * Implements fairness using a queue.
	 * 
	 * <dl>
	 * <dt>Postconditions:</dt>
	 * <dd>Any packet with timeToDest = 0 are popped from their intermediate router.
	 * </dd>
	 * </dl>
	 */
	public static void popArrivedPackets() {
		for (int i = 0; i < bandwidth; i++) {
			if (!routerQueue.isEmpty()) {
				Router nextRouter = routerQueue.pollFirst();
				Packet nextPacket = nextRouter.dequeue();
				totalServiceTime += nextPacket.getTimeInNetwork();
				totalPacketsArrived += 1;
				System.out.println("Packet " + nextPacket.getId() + " has successfully reached its destination: +"
						+ nextPacket.getTimeInNetwork());
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
	 * <dd>New packets attempt to generate. If any are generated, they are sent to
	 * the appropriate router, if possible. Any packets that can move to their
	 * destination have attempted to do so.</dd>
	 * </dl>
	 */
	public static void simulateTimeUnit() {
		currentTime += 1;
		System.out.println("\nTime: " + currentTime);
		incrementTimeInNetwork();
		tryNewPackets();
		sendPacketsToRouters();
		decrementArrivalTimes();
		pushToRouterQueue();
		popArrivedPackets();
		printRouterStatus();
	}

	/**
	 * Initializes the simulation's routers and running counts.
	 * 
	 * <dl>
	 * <dt>Postconditions:</dt>
	 * <dd>The appropriate variables have been initialized to their defaults.</dd>
	 * </dl>
	 */
	public static void init() {
		dispatcher = new Router();
		routers = null;
		totalServiceTime = 0;
		totalPacketsArrived = 0;
		packetsDropped = 0;
		currentTime = 0;
		routerQueue = new LinkedList<Router>();
		System.out.println("\nStarting simulation...");
	}

	/**
	 * Runs the simulation.
	 * 
	 * <dl>
	 * <dt>Postconditions:</dt>
	 * <dd>The simulation has started.</dd>
	 * </dl>
	 */
	public static double simulate() {
		init();
		getUserInput();
		setupRouters();
		while (currentTime != duration) {
			simulateTimeUnit();
		}
		return (double) totalServiceTime / totalPacketsArrived;
	}

	/**
	 * Prints the results of the simulation to the user.
	 * 
	 * <dl>
	 * <dt>Postconditions:</dt>
	 * <dd>The results have been printed to the user.</dd>
	 * </dl>
	 */
	public static void printResults() {
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);

		System.out.println("\nSimulation ending...");
		System.out.println("Total Service Time: " + totalServiceTime);
		System.out.println("Total Packets Served: " + totalPacketsArrived);
		System.out.println(
				"Average service time per packet: " + df.format((double) totalServiceTime / totalPacketsArrived));
		System.out.println("Total packets dropped: " + packetsDropped);
	}

	/**
	 * Runs the simulation using user-defined parameters. After the simulation is
	 * done, the user will be asked if they want to run another simulation.
	 */
	public static void main(String[] args) {
		simulate();
		printResults();

		System.out.print("\nDo you want to try another simulation? (y/n) : ");
		String input = in.nextLine();
		while (!input.equals("y") && !input.equals("n")) {
			System.out.print("Please enter \"y\" or \"n\": ");
			input = in.nextLine();
		}
		if (input.equals("y")) {
			main(args);
		} else if (input.equals("n")) {
			System.out.println("\nProgram terminating successfully...");
			System.exit(0);
		}
	}
}
