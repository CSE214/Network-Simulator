package networksimulator;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * The <code>Router</code> class represents a router in the network.
 * 
 * @author Sayan Sivakumaran e-mail: sayan.sivakumaran@stonybrook.edu Stony
 *         Brook ID: 110261379
 **/
public class Router extends LinkedList<Packet> {
	private static final long serialVersionUID = -1728475378998247541L;
	private static int bufferSize = 10;
	private String packetStringList = "";
	
	public static void setBufferSize(int newBufferSize) {
		bufferSize = newBufferSize;
	}

	/**
	 * Adds a new packet to the end of the router buffer. Updates
	 * the packetStringList accordingly.
	 * 
	 * @param packet
	 * 	Packet to add
	 */
	public void enqueue(Packet packet) {
		addLast(packet);
	}
	
	/**
	 * Removes and returns the first Packet in the router buffer. Updates the packet
	 * string list accordingly.
	 * 
	 * @return
	 * 	The removed packet.
	 */
	public Packet dequeue() {
		return pollFirst();
	}
	
	/**
	 * Decrements the arrival time of each packet in this router
	 * 
	 * <dl>
	 * <dt>Postconditions:</dt>
	 * <dd>
	 * Each packet inside this router has its time to arrival
	 * decreased by 1.
	 * </dd>
	 * </dl>
	 */
	public void decrementArrivalTime() {
		ListIterator<Packet> list = listIterator(0);
		while (list.hasNext()) {
			list.next().countDown();
		}
	}
	
	/**
	 * Returns a string representation of the router buffer.
	 */
	public String toString() {
		String[] packetStringList = new String[size()];
		ListIterator<Packet> list = listIterator(0);
		for (int i = 0; i < size(); i++) {
			packetStringList[i] = list.next().toString();
		}
		return "{" + String.join(", ", packetStringList) + "}";
	}
	
	/**
	 * Loops through an array of routers, and returns the index
	 * of the router with the most available space.
	 * 
	 * @param routers
	 * 	The array of routers to loop through
	 * 
	 * @return
	 * 	The router with the most available space.
	 * 
	 * @throws FullRouterException 
	 * 	If all the routers are full
	 */
	public static int sendPacketTo(Router[] routers) throws FullRouterException {
		int bestRouterIndex = -1;
		for (int i = 1; i < routers.length; i++) {
			if (routers[i].size() < bufferSize && bestRouterIndex == -1) {
				bestRouterIndex = i;
			} else if (bestRouterIndex != -1 && //Using short-circuit evaluation
					   routers[i].size() < routers[bestRouterIndex].size()) {
				bestRouterIndex = i;
			}
		}
		if (bestRouterIndex == -1) {
			throw new FullRouterException("All routers are currently full");
		}
		return bestRouterIndex;
	}
}
