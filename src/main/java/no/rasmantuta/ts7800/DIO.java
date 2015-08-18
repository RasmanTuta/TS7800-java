package no.rasmantuta.ts7800;

import no.rasmantuta.ts7800.jni.PeekPoke;

/**
 * DIO is a static class used for accessing the on board DIO frame on the TS-7800.
 * <p/> For information on the TS-7800 Single Board Computer itself, see the <a href="http://www.embeddedarm.com/about/resource.php?item=303">TS-7800 Preliminary Manual</a>.
 *
 * @author Kristian Berg
 *
 */
public class DIO {
	/** The bit used to address the DIO pin 1. */
	public static final short DIO_01 = 0x0001;
	/** The bit used to address the DIO pin 3. */
	public static final short DIO_03 = 0x0004;
	/** The bit used to address the DIO pin 4. */
	public static final short DIO_04 = 0x0008;
	/** The bit used to address the DIO pin 5. */
	public static final short DIO_05 = 0x0010;
	/** The bit used to address the DIO SPI FRAME pin. */
	public static final short SPI_FRAME = 0x0020;
	/** The bit used to address the DIO pin 7. */
	public static final short DIO_07 = 0x0040;
	/** The bit used to address the DIO pin 8. */
	public static final short DIO_08 = 0x0080;
	/** The bit used to address the DIO pin 9. */
	public static final short DIO_09 = 0x0100;
	/** The bit used to address the SPI MISO pin. */
	public static final short SPI_MISO = 0x0200;
	/** The bit used to address the DIO pin 11. */
	public static final short DIO_11 = 0x0400;
	/** The bit used to address the SPI MOSI pin. */
	public static final short SPI_MOSI = 0x0800;
	/** The bit used to address the DIO pin 13. */
	public static final short DIO_13 = 0x1000;
	/** The bit used to address the SPI CLK pin. */
	public static final short SPI_CLK = 0x2000;
	/** The bit used to address the DIO pin 15. */
	public static final short DIO_15 = 0x4000;
	/** Mask used for evaluating an address. Pin 2 and 16 used for power and can thus not be addressed. */
	public static final short DIO_VALID_MASK = 0x7FFD;

	/** The TS-7800 address used for addressing Digital Input. This should be used as a 16 bit address. */
	protected static final int INPUT_ADDRESS = 0xE8000004;
	/** The TS-7800 address used for addressing Digital Output. This should be used as a 16 bit address. */
	protected static final int OUTPUT_ADDRESS = 0xE8000008;

	/**
	 * Constructs a new <code>DIO</code> object. This constructor is the default constructor for a TS-7800 on board DIO context.
	 * Making the <code>DIO default constructor</code> protected makes this a static class, applications cannot call this constructor directly.
	 */
	protected DIO(){}

	/**
	 * Sets the output pins in the TS-7800 DIO-header. This is done according to the <code>mask</code> and <code>value</code> parameters.
	 *
	 * @param mask a AND function will be performed on <code>value</code> with the <code>mask</code> parameter.
	 * This is to ensure that value does not change the state of a pin without intention. This should be a combination of the constants DIO_1-15 and SPI_*.
	 * @param value the state to set on the DIO pins. 1 indicates high and 0 indicates low.
	 * @throws IllegalArgumentException thrown if invalid bits 1 and 15 are used in the <code>mask</code> parameter.
	 */
	public static void setOutput(short mask, short value) throws IllegalArgumentException{
		checkMask(mask);
		value = (short)(value & mask);

		short oldValue = PeekPoke.peek16(OUTPUT_ADDRESS);
		value = prepareValue(mask, value, oldValue);

		PeekPoke.poke(OUTPUT_ADDRESS, value);
	}

	static short prepareValue(short mask, short value, short oldValue) {
		oldValue = (short)((oldValue & (~mask)) & 0x0000FFFF);
		value = (short)(oldValue | (value & mask));
		return value;
	}

	/**
	 * Returns the state of the output pins of the TS-7800 DIO frame.
	 * The state of the pins will be filtered with the <code>mask</code> parameter, so that only the pins of interest are returned.
	 *
	 * @param mask a AND function will be performed on <code>value</code> with the <code>mask</code> parameter.
	 * This is to ensure that only the state of the pins of interest are returned. This should be a combination of the constants DIO_1-15 and SPI_*.
	 * @return The state of the pins of interest.
	 * @throws IllegalArgumentException thrown if invalid bits 1 and 15 are used in the <code>mask</code> parameter.
	 */
	public static short getOutput(short mask) throws IllegalArgumentException{
		checkMask(mask);
		return (short)(PeekPoke.peek16(OUTPUT_ADDRESS) & mask);
	}

	/**
	 * Toggles the state of the output pins of the TS-7800 DIO frame.
	 * The pins to toggle will be given in the <code>mask</code> parameter. This should be a combination of the constants DIO_1-15 and SPI_*.
	 *
	 * @param mask a combination of the constants DIO_1-15 and SPI_* to indicate which pin states are to be altered.
	 * @throws IllegalArgumentException thrown if invalid bits 1 and 15 are used in the <code>mask</code> parameter.
	 */
	public static void toggleOutput(short mask) throws IllegalArgumentException{
		checkMask(mask);
		short value = PeekPoke.peek16(OUTPUT_ADDRESS);
		value = toggleValue(mask, value);

		PeekPoke.poke(OUTPUT_ADDRESS, value);
	}

	static short toggleValue(short mask, short value) {
		short maskedValue = (short)(value & mask);
		value = (short)((value & (~mask)) & 0x0000FFFF);
		maskedValue = (short)((~maskedValue) & 0x0000FFFF);

		value = (short)(value | (maskedValue & mask));
		return value;
	}

	/**
	 * Returns the state of the input pins of the TS-7800 DIO frame.
	 * The state of the pins will be filtered with the <code>mask</code> parameter, so that only the pins of interest are returned.
	 *
	 * @param mask a combination of the constants DIO_1-15 and SPI_* to indicate which pin states are to be returned.
	 * @return the state of the pins requested.
	 * @throws IllegalArgumentException thrown if invalid bits 1 and 15 are used in the <code>mask</code> parameter.
	 */
	public static short getInput(short mask) throws IllegalArgumentException{
		checkMask(mask);
		return (short)(PeekPoke.peek16(INPUT_ADDRESS) & mask);
	}

	/**
	 * Returns <code>true</code> if one or more of the bits in the <code>value</code> parameter, indicated by the <code>mask</code> parameter, is 1.
	 *
	 * @param mask a combination of the constants DIO_1-15 and SPI_* to indicate which bits in the <code>value</code> parameter is to be evaluated.
	 * @param value the value to be evaluated by the method.
	 * @return <code>true</code> if one or more of the bits in the <code>value</code> parameter is 1.
	 */
	public static boolean isBitSet(short mask, short value){
		return (value & mask) != 0;
	}

	static void checkMask(short mask) throws IllegalArgumentException{
		if((mask & (short)(~DIO_VALID_MASK &0x0000ffff)) != 0)
			throw new IllegalArgumentException("Bit 1 and 15 can not be used in mask.");
	}


}
