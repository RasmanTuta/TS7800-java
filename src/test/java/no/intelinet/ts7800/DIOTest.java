package no.rasmantuta.ts7800;

import junit.framework.TestCase;

public class DIOTest extends TestCase {

	protected void setUp() throws Exception {
	}

	public void testToggleValue() {
		short val = 0x5555;
		short mask = 0x0e;
		short expected = 0x555b;

		assertEquals(expected, DIO.toggleValue(mask, val));
	}

	public void testPrepareValue() {
		short oldVal = 0x5555;
		short val = 0xa;
		short mask = 0x0e;
		short expected = 0x555b;

		assertEquals(expected, DIO.prepareValue(mask, val, oldVal));
	}

	public void testCheckMask() {
		short mask = DIO.DIO_01 | DIO.DIO_03 | DIO.DIO_04 | DIO.DIO_05 | DIO.DIO_07 | DIO.DIO_08 | DIO.DIO_09 |
			DIO.DIO_11 | DIO.DIO_13 | DIO.DIO_15 | DIO.SPI_CLK | DIO.SPI_FRAME | DIO.SPI_MISO | DIO.SPI_MOSI;
		DIO.checkMask(mask);

		mask = (short)0x8000;

		try {
			DIO.checkMask(mask);
			fail();
		} catch (IllegalArgumentException e) {
		}

		mask = (short)0x0002;

		try {
			DIO.checkMask(mask);
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

}
