package no.rasmantuta.ts7800.jni;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is designed to give access to TS-7800 memory.
 * <p/> It depends upon the lib_rasmantuta_ts7800.so library to be present on the TS-7800 card.
 * <br/> For now, no rasmantuta_ts7800.dll is provided for testing on PC platform.
 * <p/> This class should be used with care since altering the wrong memory location will give unpredictable results!
 *
 * @author Kristian Berg
 *
 */
public class PeekPoke {
	private static Log log = LogFactory.getLog(PeekPoke.class);

	/**
	 * Native method for setting a 32 bit value at the specified address.
	 *
	 * @param address the memory location of the value to change.
	 * @param value the new value of the memory location.
	 */
	protected static native void tsPoke(int address, int value);

	/**
	 * Native method for setting a 16 bit value at the specified address.
	 *
	 * @param address the memory location of the value to change.
	 * @param value the new value of the memory location.
	 */
	protected static native void tsPoke(int address, short value);

	/**
	 * Native method for setting a 8 bit value at the specified address.
	 *
	 * @param address the memory location of the value to change.
	 * @param value the new value of the memory location.
	 */
	protected static native void tsPoke(int address, byte value);

	/**
	 * Native method for reading a 32 bit value from the specified address.
	 *
	 * @param address the memory location of the value to read.
	 * @return the 32 bit value at the memory location specified.
	 */
	protected static native int tsPeek32(int address);

	/**
	 * Native method for reading a 16 bit value from the specified address.
	 *
	 * @param address the memory location of the value to read.
	 * @return the 16 bit value at the memory location specified.
	 */
	protected static native short tsPeek16(int address);

	/**
	 * Native method for reading a 8 bit value from the specified address.
	 *
	 * @param address the memory location of the value to read.
	 * @return the 8 bit value at the memory location specified.
	 */
	protected static native byte tsPeek8(int address);

	static {
		try{
			System.loadLibrary("_rasmantuta_ts7800");
		}catch(UnsatisfiedLinkError e){
			log.error("Failed to load peekpoke library", e);
		}
    }

	/**
	 * Constructs a new <code>PeekPoke</code> object.
	 * <p/>Making the <code>PeekPoke default constructor</code> protected makes this a static class, applications cannot call this constructor directly.
	 */
	protected PeekPoke(){}

	/**
	 * Method for setting a 32 bit value at the specified address.
	 *
	 * @param address the memory location of the value to change.
	 * @param value the new value of the memory location.
	 */
    public static void poke(int address, byte value){
    	tsPoke(address, value);
    }

	/**
	 * Method for setting a 16 bit value at the specified address.
	 *
	 * @param address the memory location of the value to change.
	 * @param value the new value of the memory location.
	 */
    public static void poke(int address, short value){
    	tsPoke(address, value);
    }

    /**
	 * Method for setting a 8 bit value at the specified address.
	 *
	 * @param address the memory location of the value to change.
	 * @param value the new value of the memory location.
	 */
    public static void poke(int address, int value){
    	tsPoke(address, value);
    }

	/**
	 * Method for reading a 32 bit value from the specified address.
	 *
	 * @param address the memory location of the value to read.
	 * @return the 32 bit value at the memory location specified.
	 */
    public static int peek32(int address){
    	return tsPeek32(address);
    }

	/**
	 * Method for reading a 16 bit value from the specified address.
	 *
	 * @param address the memory location of the value to read.
	 * @return the 16 bit value at the memory location specified.
	 */
    public static short peek16(int address){
    	return tsPeek16(address);
    }

	/**
	 * Method for reading a 8 bit value from the specified address.
	 *
	 * @param address the memory location of the value to read.
	 * @return the 8 bit value at the memory location specified.
	 */
    public static byte peek8(int address){
    	return tsPeek8(address);
    }
}
