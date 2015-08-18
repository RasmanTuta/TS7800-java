package no.rasmantuta.ts7800.jni;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class provide access to the hardware watchdog of the TS-7800 Single Board Computer.
 * <p/> This class should be used with caution. If the dog is fed, but not re-fed within 8 seconds, the card will reboot.
 * <p/> It depends upon the lib_rasmantuta_ts7800.so library to be present on the TS-7800 card.
 * <br/> For now, no rasmantuta_ts7800.dll is provided for testing on PC platform.
 * <p/> For information on the TS-7800 Single Board Computer itself, see the <a href="http://www.embeddedarm.com/about/resource.php?item=303">TS-7800 Preliminary Manual</a>.
 * @author kristian
 *
 */
public class Watchdog {
	private static Log log = LogFactory.getLog(Watchdog.class);
	protected static native void tsFeed();
	protected static native void tsDisable();

	static {
		try{
			System.loadLibrary("_rasmantuta_ts7800");
		}catch(UnsatisfiedLinkError e){
			log.error("Failed to load peekpoke library", e);
		}
     }

	/**
	 * Constructs a new <code>Watchdog</code> object.
	 * <p/>Making the <code>Watchdog default constructor</code> protected makes this a static class, applications cannot call this constructor directly.
	 */
	protected Watchdog(){}

	/**
	 * Feeds the watchdog for 8 seconds.
	 * <p/>If this method or {@link #disable()} is not called within 8 seconds, the card will reboot.
	 */
	public static void feed(){
		tsFeed();
	}

	/**
	 * Disables the watchdog. A new call to {@link #feed()} is no longer necessary.
	 */
	public static void disable(){
		tsDisable();
	}

}
