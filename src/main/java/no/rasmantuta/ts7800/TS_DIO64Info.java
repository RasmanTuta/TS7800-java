package no.rasmantuta.ts7800;

/**
 * This card represents the information available for TS-DIO64 cards. For this initial version the SRAM control register info is omitted.
 *
 * @author Kristian Berg
 * @see TS_DIO64
 */
public class TS_DIO64Info {
	private boolean cardPresent;
	private boolean sramPresent = false;
	private char pldRevision = '@';

	/**
	 * Creates a TS_DIO64.
	 * <p/>If <code>cardPresent</code> is <code>false</code>, the other parameters will not be used.
	 *
	 * @param cardPresent is this card present on the TS-7800.
	 * @param sramPresent does this card have the SRAM option.
	 * @param pldRevision the revision of the PLD.
	 */
	public TS_DIO64Info(boolean cardPresent, boolean sramPresent, byte pldRevision) {
		this.cardPresent = cardPresent;
		if(cardPresent){
			this.sramPresent = sramPresent;
			this.pldRevision = (char)('@' + pldRevision);
		}
	}

	/**
	 * Tells if this TS-DIO64 card is present on the TS-7800.
	 *
	 * @return <code>true</code> if card is present.
	 */
	public boolean isCardPresent() {
		return cardPresent;
	}

	/**
	 * Tells if this TS-DIO64 card has the optional SRAM function.
	 *
	 * @return <code>true</code> if card has SRAM option.
	 */
	public boolean isSramPresent() {
		return sramPresent;
	}

	/**
	 * The PLD revision if this TS-DIO64 card.
	 * <p/> If card is not present <code>'@'</code> is returned
	 *
	 * @return the character based revision of the card.
	 */
	public char getPldRevision() {
		return pldRevision;
	}

}
