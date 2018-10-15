package networksimulator;

import java.util.LinkedList;

/**
 * The <code>Router</code> class represents a router in the network.
 * 
 * @author Sayan Sivakumaran e-mail: sayan.sivakumaran@stonybrook.edu Stony
 *         Brook ID: 110261379
 **/
public class Router extends LinkedList<Packet> {
	private static final long serialVersionUID = -1728475378998247541L;
	private int bufferSize = 10;
	private String packetStringList = "";
	
	public void setBufferSize(int newBufferSize) {
		this.bufferSize = newBufferSize;
	}

	/**
	 * Adds a new packet to the end of the router buffer. Updates
	 * the packetStringList accordingly.
	 * 
	 * @param packet
	 * 	Packet to add
	 */
	public void enqueue(Packet packet) {
		if (packetStringList == "") {
			packetStringList += packet.toString();
		} else {
			packetStringList += ", " + packet.toString();
		}
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
		Packet packet = pollFirst();
		if (size() == 0) {
			packetStringList = "";
		} else {
			packetStringList = packetStringList.replace(packet.toString() + ", ", "");
		}
		return packet;
	}
	
	/**
	 * Returns a string representation of the router buffer.
	 */
	public String toString() {
		return "{" + packetStringList + "}";
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
	 */
	public int sendPacketTo(Router[] routers) {
		int bestRouterIndex = -1;
		for (int i = 1; i < routers.length; i++) {
			if (routers[i].size() < bufferSize && bestRouterIndex != -1) {
				bestRouterIndex = i;
			} else if (routers[i].size() < routers[bestRouterIndex].size()) {
				bestRouterIndex = i;
			}
		}
		return bestRouterIndex;
	}
}
