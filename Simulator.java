package networksimulator;


/**
 * The <code>Simulator</code> class allows the user to run the simulation.
 * 
 * @author Sayan Sivakumaran e-mail: sayan.sivakumaran@stonybrook.edu Stony
 *         Brook ID: 110261379
 **/
public class Simulator {
	public static void main (String[] args) {
		Packet packet = new Packet(20, 2000, 5000);
		System.out.println(packet.toString());
	}
}
