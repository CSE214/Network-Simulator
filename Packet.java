package networksimulator;

/**
 * The <code>Packet</code> class represents a packet that will be sent 
 * through the network.
 * 
 * @author Sayan Sivakumaran e-mail: sayan.sivakumaran@stonybrook.edu Stony
 *         Brook ID: 110261379
 **/
public class Packet {
	private static int packetCount = 0; // Number of packets
	private int id; // Packet identifier
	private int packetSize; // Size of packet
	private int timeArrive; // Time of creation
	private int timeToDest; // Time to destination
	/**
	 * @return 
	 *	The packetCount
	 */
	public static int getPacketCount() {
		return packetCount;
	}
	/**
	 * @return 
	 *	The packetCount
	 */
	public static void setPacketCount(int newPacketCount) {
		packetCount = newPacketCount;
	}
	/**
	 * @return 
	 *	The id of this instance
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id 
	 * 	The new id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return 
	 *	The packetSize of this instance
	 */
	public int getPacketSize() {
		return packetSize;
	}
	/**
	 * @param packetSize 
	 * 	The new packetSize to set
	 */
	public void setPacketSize(int packetSize) {
		this.packetSize = packetSize;
	}
	/**
	 * @return 
	 *	The timeArrive of this instance
	 */
	public int getTimeArrive() {
		return timeArrive;
	}
	/**
	 * @param timeArrive 
	 * 	The new timeArrive to set
	 */
	public void setTimeArrive(int timeArrive) {
		this.timeArrive = timeArrive;
	}
	/**
	 * @return 
	 *	The timeToDest of this instance
	 */
	public int getTimeToDest() {
		return timeToDest;
	}
	/**
	 * @param timeToDest 
	 * 	The new timeToDest to set
	 */
	public void setTimeToDest(int timeToDest) {
		this.timeToDest = timeToDest;
	}
	
	/**
	 * Returns an instance of Packet
	 * 
	 * @param timeArrive
	 * 	The unit of time this packet was created.
	 * @param minSize
	 * 	The minimum size of the packet.
	 * @param maxSize
	 * 	The maximum size of the packet.
	 */
	public Packet(int timeArrive, int minSize, int maxSize) {
		this.id = packetCount;
		packetCount += 1;
		this.packetSize = (int) (Math.random()*(maxSize - minSize) + minSize);
		this.timeArrive = timeArrive;
		this.timeToDest = packetSize/100;
	}
	
	@Override
	public String toString() {
		return String.format("[%s, %d, %d]", id, timeArrive, timeToDest);
	}
}
