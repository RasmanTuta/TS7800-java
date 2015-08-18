package no.rasmantuta.ts7800;

import no.rasmantuta.ts7800.jni.PeekPoke;

/**
 * <code>AbstractPC104Card</code> is the abstract base class for all PC/104 based extension
 * cards for the TS-7800 single board computer.
 * <p/>
 * The static initializer will set up the T-7800 for PC/104 mode rather than GPIO, which is the default.
 *
 * @author Kristian Berg
 *
 */
public abstract class AbstractPC104Card {
	static {
		PeekPoke.poke(0xE8000030, 0x55555555);
		PeekPoke.poke(0xE8000034, 0x55555555);
		PeekPoke.poke(0xE8000038, 0x55555);
		PeekPoke.poke(0xE800003C, 0x55555);
	}

	/**
	 * Constructs a new <code>AbstractPC104Card</code> object. This constructor is the default constructor for a PC/104 card context.
	 * Since <code>AbstractPC104Card</code> is an abstract class, applications cannot call this constructor directly.
	 */
	protected AbstractPC104Card(){}

}
