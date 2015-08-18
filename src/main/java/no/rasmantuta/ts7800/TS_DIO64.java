package no.rasmantuta.ts7800;

import no.rasmantuta.ts7800.jni.PeekPoke;

/**
 * TS_DIO64 is a static class used for accessing the digital inputs and outputs on a TS-DIO64 card on the TS-7800 Single Board Computer.
 * <p/> For information on the TS-DIO64 board itself, see the <a href="http://www.embeddedarm.com/documentation/ts-dio64-manual.pdf">TS-DIO64 Manual</a>.
 *
 * @author Kristian Berg
 *
 */
public class TS_DIO64 extends AbstractPC104Card {
	private static final String CARD_IS_NOT_PRESENT = "Card is not present!";
	private static final String INDEX_MUST_BE_ONE_OF_0_3 = "Index must be one of [0-3]!";
	private static final int DIOBASE = 0xee000000;
	private static final int IOBASE_START = 0x100;
	private static final int CARD_OFFSET = 0x10;
	private static final int OUTPUT_BASE = DIOBASE + IOBASE_START + 0x4;
	private static final int INPUT_BASE = DIOBASE + IOBASE_START + 0x8;
	private static final byte DIO_64_ID = (byte)0xa4;

	/** The bit used to address the input or output pin 1. */
	public static final int DIO_01 = 0x00000001;
	/** The bit used to address the input or output pin 2. */
	public static final int DIO_02 = 0x00000002;
	/** The bit used to address the input or output pin 3. */
	public static final int DIO_03 = 0x00000004;
	/** The bit used to address the input or output pin 4. */
	public static final int DIO_04 = 0x00000008;
	/** The bit used to address the input or output pin 5. */
	public static final int DIO_05 = 0x00000010;
	/** The bit used to address the input or output pin 6. */
	public static final int DIO_06 = 0x00000020;
	/** The bit used to address the input or output pin 7. */
	public static final int DIO_07 = 0x00000040;
	/** The bit used to address the input or output pin 8. */
	public static final int DIO_08 = 0x00000080;
	/** The bit used to address the input or output pin 9. */
	public static final int DIO_09 = 0x00000100;
	/** The bit used to address the input or output pin 10. */
	public static final int DIO_10 = 0x00000200;
	/** The bit used to address the input or output pin 11. */
	public static final int DIO_11 = 0x00000400;
	/** The bit used to address the input or output pin 12. */
	public static final int DIO_12 = 0x00000800;
	/** The bit used to address the input or output pin 13. */
	public static final int DIO_13 = 0x00001000;
	/** The bit used to address the input or output pin 14. */
	public static final int DIO_14 = 0x00002000;
	/** The bit used to address the input or output pin 15. */
	public static final int DIO_15 = 0x00004000;
	/** The bit used to address the input or output pin 16. */
	public static final int DIO_16 = 0x00008000;
	/** The bit used to address the input or output pin 17. */
	public static final int DIO_17 = 0x00010000;
	/** The bit used to address the input or output pin 18. */
	public static final int DIO_18 = 0x00020000;
	/** The bit used to address the input or output pin 19. */
	public static final int DIO_19 = 0x00040000;
	/** The bit used to address the input or output pin 20. */
	public static final int DIO_20 = 0x00080000;
	/** The bit used to address the input or output pin 21. */
	public static final int DIO_21 = 0x00100000;
	/** The bit used to address the input or output pin 22. */
	public static final int DIO_22 = 0x00200000;
	/** The bit used to address the input or output pin 23. */
	public static final int DIO_23 = 0x00400000;
	/** The bit used to address the input or output pin 24. */
	public static final int DIO_24 = 0x00800000;
	/** The bit used to address the input or output pin 25. */
	public static final int DIO_25 = 0x01000000;
	/** The bit used to address the input or output pin 26. */
	public static final int DIO_26 = 0x02000000;
	/** The bit used to address the input or output pin 27. */
	public static final int DIO_27 = 0x04000000;
	/** The bit used to address the input or output pin 28. */
	public static final int DIO_28 = 0x08000000;
	/** The bit used to address the input or output pin 29. */
	public static final int DIO_29 = 0x10000000;
	/** The bit used to address the input or output pin 30. */
	public static final int DIO_30 = 0x20000000;
	/** The bit used to address the input or output pin 31. */
	public static final int DIO_31 = 0x40000000;
	/** The bit used to address the input or output pin 32. */
	public static final int DIO_32 = 0x80000000;

	private static TS_DIO64Info[] info;

	static{
		info = new TS_DIO64Info[4];
		createInfo();
	}

	/**
	 * Constructs a new <code>TS_DIO64</code> object. This constructor is the default constructor for a TS-DIO64 on board DIO context.
	 * Making the <code>TS_DIO64 default constructor</code> protected makes this a static class, applications cannot call this constructor directly.
	 */
	protected TS_DIO64(){}

	/**
	 * Tests to see if cards are present and creates a <code>TS_DIO64Info</code> instance for four possible cards.
	 */
	private static void createInfo() {
		for (int i=0; i<4; i++){
			int address = DIOBASE + IOBASE_START + i*CARD_OFFSET;
			boolean present = PeekPoke.peek8(address) == DIO_64_ID;
			boolean sram = false;
			byte rev = 0;
			if(present){
				byte opt = PeekPoke.peek8(address + 1);
				sram = (opt & 0x10) != 0;
				rev = (byte)(opt & 0x0f);
			}
			info[i] = new TS_DIO64Info(present, sram, rev);
		}
	}

	/**
	 * Tests to see if a card with the given <code>index</code> is present.
	 * @param index the index of the card to test for. This should be one of [0-3].
	 * @return <code>true</code> if the card is present.
	 * @throws IllegalArgumentException thrown if <code>index</code> is not one of [0-3].
	 */
	public static boolean isCardPresent(int index) throws IllegalArgumentException{
		if(index < 0 || index > 3)
			throw new IllegalArgumentException(INDEX_MUST_BE_ONE_OF_0_3);

		return info[index].isCardPresent();
	}

	/**
	 * Returns the <code>TS_DIO64Info</code> object for the index given. This will give information on the presence of SRAM and PLD revision.
	 * @param index the index of the card to get info on. This should be one of [0-3].
	 * @return a <code>TS_DIO64Info</code> object for the <code>index</code> given.
	 * @throws IllegalArgumentException thrown if <code>index</code> is not one of [0-3].
	 */
	public static TS_DIO64Info getInfo(int index) throws IllegalArgumentException{
		if(index < 0 || index > 3)
			throw new IllegalArgumentException(INDEX_MUST_BE_ONE_OF_0_3);

		return info[index];
	}

	/**
	 * Sets the output pins in the output header of the TS-DIO64 card. This is done according to the <code>mask</code> and <code>value</code> parameters.
	 *
	 * @param card the index of the card to set outputs on. This should be one of [0-3].
	 * @param mask a AND function will be performed on <code>value</code> with the <code>mask</code> parameter.
	 * This is to ensure that value does not change the state of a pin without intention. This should be a combination of the constants DIO_1-32.
	 * @param value the state to set on the DIO pins. 1 indicates high and 0 indicates low.
	 * @throws IllegalArgumentException thrown if <code>index</code> is not one of [0-3] or if card is not present.
	 */
	public static void setOutput(int card, int mask, int value) throws IllegalArgumentException{
		checkCard(card);
		value = (short)(value & mask);
		int address = OUTPUT_BASE + card * CARD_OFFSET;
		int oldValue = PeekPoke.peek32(address);
		value = prepareValue(mask, value, oldValue);

		PeekPoke.poke(address, value);
	}

	private static void checkCard(int card) throws IllegalArgumentException{
		if(card < 0 || card > 3)
			throw new IllegalArgumentException(INDEX_MUST_BE_ONE_OF_0_3);
		if(isCardPresent(card) == false)
			throw new IllegalArgumentException(CARD_IS_NOT_PRESENT);
	}

	static int prepareValue(int mask, int value, int oldValue) {
		oldValue = (short)(oldValue & (~mask));
		value = (short)(oldValue | (value & mask));
		return value;
	}

	/**
	 * Returns the state of the output pins of the output header of the TS-DIO64 card.
	 * The state of the pins will be filtered with the <code>mask</code> parameter, so that only the pins of interest are returned.
	 *
	 * @param card the index of the card to get outputs from. This should be one of [0-3].
	 * @param mask a AND function will be performed on <code>value</code> with the <code>mask</code> parameter.
	 * This is to ensure that only the state of the pins of interest are returned. This should be a combination of the constants DIO_1-32.
	 * @return The state of the pins of interest.
	 * @throws IllegalArgumentException thrown if <code>index</code> is not one of [0-3] or if card is not present.
	 */
	public static int getOutput(int card, int mask) throws IllegalArgumentException{
		checkCard(card);
		int address = OUTPUT_BASE + card * CARD_OFFSET;
		return (PeekPoke.peek32(address) & mask);
	}

	/**
	 * Toggles the state of the output pins of the output header of the TS-DIO64 card.
	 * The pins to toggle will be given in the <code>mask</code> parameter. This should be a combination of the constants DIO_1-32.
	 *
	 * @param card the index of the card to toggle outputs on. This should be one of [0-3].
	 * @param mask a combination of the constants DIO_1-32 to indicate which pin states are to be altered.
	 * @throws IllegalArgumentException thrown if <code>index</code> is not one of [0-3] or if card is not present.
	 */
	public static void toggleOutput(int card, int mask) throws IllegalArgumentException{
		checkCard(card);
		int address = OUTPUT_BASE + card * CARD_OFFSET;
		int value = PeekPoke.peek32(address);
		value = toggleValue(mask, value);

		PeekPoke.poke(address, value);
	}

	static int toggleValue(int mask, int value) {
		int maskedValue = value & mask;
		value = (value & (~mask));
		maskedValue = ~maskedValue;

		value = (value | (maskedValue & mask));
		return value;
	}

	/**
	 * Returns the state of the input pins of the input header of the TS-DIO64 card.
	 * The state of the pins will be filtered with the <code>mask</code> parameter, so that only the pins of interest are returned.
	 *
	 * @param card the index of the card to get inputs from. This should be one of [0-3].
	 * @param mask a AND function will be performed on <code>value</code> with the <code>mask</code> parameter.
	 * This is to ensure that only the state of the pins of interest are returned. This should be a combination of the constants DIO_1-32.
	 * @return The state of the pins of interest.
	 * @throws IllegalArgumentException thrown if <code>index</code> is not one of [0-3] or if card is not present.
	 */
	public static int getInput(int card, int mask) throws IllegalArgumentException{
		checkCard(card);
		int address = INPUT_BASE + card * CARD_OFFSET;
		return (PeekPoke.peek32(address) & mask);
	}

	/**
	 * Returns <code>true</code> if one or more of the bits in the <code>value</code> parameter, indicated by the <code>mask</code> parameter, is 1.
	 *
	 * @param mask a combination of the constants DIO_1-15 and SPI_* to indicate which bits in the <code>value</code> parameter is to be evaluated.
	 * @param value the value to be evaluated by the method.
	 * @return <code>true</code> if one or more of the bits in the <code>value</code> parameter is 1.
	 */
	public static boolean isBitSet(int mask, int value){
		return (value & mask) != 0;
	}
}
