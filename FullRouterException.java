package networksimulator;

/**
* <code>FullRoutersException</code> is when every router in the
* network is full.
*    
*
* @author Sayan Sivakumaran
*    e-mail: sayan.sivakumaran@stonybrook.edu
*    Stony Brook ID: 110261379
**/
public class FullRouterException extends Exception {
	private static final long serialVersionUID = -8568120812429473972L;

	/**
	 * Returns an instance of <code>FullPlannerException</code>.
	 */
	public FullRouterException() {
		super();
	}
	
	/**
	 * Returns an instance of <code>FullPlannerException</code> along with the specified message.
	 * 
	 * @param message 
	 * 	The message that accompanies the error.
	 */
	public FullRouterException(String message) {
		super(message);
	}
}
