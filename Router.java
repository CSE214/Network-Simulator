package networksimulator;

import java.util.Collection;
import java.util.LinkedList;

/**
 * The <code>Router</code> class represents a router in the network.
 * 
 * @author Sayan Sivakumaran e-mail: sayan.sivakumaran@stonybrook.edu Stony
 *         Brook ID: 110261379
 **/
public class Router extends LinkedList<Packet> {
	private static final long serialVersionUID = -1728475378998247541L;

	/**
	 * Adds a new packet to the end of the router buffer.
	 * @param packet
	 */
	public void enqueue(Packet packet) {
		addLast(packet);
	}
	
	/**
	 * Removes and returns the first Packet in the router buffer.
	 * @return
	 */
	public Packet dequeue() {
		return pollFirst();
	}
	
	/**
	 * Returns a string representation of the router buffer.
	 */
	public String toString() {
		return "";
	}
	
	public static int sendPacketTo(Router[] routers) {
		int bestRouterIndex = 0;
		for (int i = 1; i < routers.length; i++) {
			if (routers[i].size() < routers[bestRouterIndex].size()) {
				bestRouterIndex = i;
			}
		}
		return bestRouterIndex;
	}
}
