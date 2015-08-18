package no.rasmantuta.ts7800.jni;

import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class provides different utility methods for the TS-7800 Single Board Computer.
 * <p/> It depends upon the lib_rasmantuta_ts7800.so library to be present on the TS-7800 card.
 * <br/> For now, no rasmantuta_ts7800.dll is provided for testing on PC platform.
 * <p/> For information on the TS-7800 Single Board Computer itself, see the <a href="http://www.embeddedarm.com/about/resource.php?item=303">TS-7800 Preliminary Manual</a>.
 *
 * @author Kristian Berg
 *
 */
public class Util {
	private static Log log = LogFactory.getLog(Util.class);
	/**
	 * Native method for controlling the red LED placed on the TS-7800 board itself.
	 *
	 * @param on <code>true</code> to light the LED <code>false</code> to shut it off.
	 */
	protected static native void tsSetRedLEDOn(boolean on);

	/**
	 * Native method for reading the running hours of the TS-7800.
	 * <p/>This value does not make any sense. It seems like this is not documented correctly in the example code from TS.
	 *
	 * @return should return the running hours. In this implementation, you don't really know.
	 */
	protected static native int tsReadOdometer();

	/**
	 * Native method for reading the production date for this TS-7800.
	 *
	 * @return an int on the format 0xMMDDYYYY
	 */
	protected static native int tsReadBirthDate();

	/**
	 * Native method for reading the hardware address of the TS-7800.
	 * @return a short array with length 6 containing the six parts of the address.
	 */
	protected static native short[] tsReadMACAddress();

	static {
		try{
			System.loadLibrary("_rasmantuta_ts7800");
		}catch(UnsatisfiedLinkError e){
			log.error("Failed to load peekpoke library", e);
		}
     }

	/**
	 * Constructs a new <code>Util</code> object.
	 * <p/>Making the <code>Util default constructor</code> protected makes this a static class, applications cannot call this constructor directly.
	 */
	protected Util(){}

	/**
	 * Method for controlling the red LED placed on the TS-7800 board itself.
	 *
	 * @param on <code>true</code> to light the LED <code>false</code> to shut it off.
	 */
	public static void setRedLEDOn(boolean on){
		tsSetRedLEDOn(on);
	}

	/**
	 * Method for reading the running hours of the TS-7800.
	 * <p/>This value does not make any sense. It seems like this is not documented correctly in the example code from TS.
	 *
	 * @return should return the running hours. In this implementation, you don't really know.
	 */
	public static int getOdometer(){
		return tsReadOdometer();
	}

	/**
	 * Native method for reading the production date for this TS-7800.
	 *
	 * @return an Date for the day of production.
	 */
	public static Date getBirthDate(){
		int bday = tsReadBirthDate();
		GregorianCalendar cal = new GregorianCalendar(bday&0xFFFF, (bday>>24), (bday>>16)&0xFF );

		return cal.getTime();
	}

	/**
	 * Method for reading the hardware address of the TS-7800.
	 * @return a short array with length 6 containing the six parts of the address.
	 */
	public static short[] getMACAddress(){
		return tsReadMACAddress();
	}

	/**
	 * Method for reading the hardware address of the TS-7800.
	 * @return a String containing the six parts of the address separated by ':'.
	 */
	public static String getMACAddressAsString(){
		short[] mac = tsReadMACAddress();
 		return "" + toHexString(mac[5]) + ":" + toHexString(mac[4]) + ":" + toHexString(mac[3]) + ":" + toHexString(mac[2]) + ":" +
 					toHexString(mac[1]) + ":" + toHexString(mac[0]);
	}

	private static String toHexString(int val){
		String ret = val < 0x10 ? "0" + Integer.toHexString(val) : Integer.toHexString(val);
		return ret;
	}

}
