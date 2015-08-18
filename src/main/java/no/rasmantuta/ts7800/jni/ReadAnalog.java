package no.rasmantuta.ts7800.jni;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is designed for giving access to the ADC's available in the ADC frame of the TS-7800 Single Board Computer.
 * <p/> It depends upon the lib_rasmantuta_ts7800.so library to be present on the TS-7800 card.
 * <br/> For now, no rasmantuta_ts7800.dll is provided for testing on PC platform.
 * <p/> For information on ADC on the TS-7800 Single Board Computer, see the <a href="http://www.embeddedarm.com/about/resource.php?item=303#adc">TS-7800 Preliminary Manual</a>.
 *
 * @author kristian
 *
 */
public class ReadAnalog {
	private static Log log = LogFactory.getLog(ReadAnalog.class);

	/**
	 * Native method for sampling one or more channels on the TS-7800 on board ADC.
	 * <p/>The resulting values are averaged and will have a value between 0 and 4092.
	 * The value 0 represents 0 Volts and 4092 represents 3.3 Volts.
	 * <p/> The return values for channels that are not sampled is -1.
	 * <p/> This method will not return until 2048 samples has been performed. This will take at least 1100ms.
	 *
	 * @param channels the channels to sample. This should be a combination of the constants CHANNEL_0-7.
	 * @return a short array containing the average samples for all channels. channels with no samples will be -1.
	 */
	protected static native short[] tsReadAndAverage(int channels);

	/**
	 * Native method for sampling one or more channels on the TS-7800 on board ADC.
	 * <p/>The resulting values are raw and will have a the channel number in bit 12-15 and a 10 bit shifted two bits left in bit 0-11.
	 * If shifted back, the value 0 will represent 0 Volts and 1023 represent 3.3 Volts.
	 * The channels are sampled in a round-robin fashion, and will be placed in the returned array in the sequence they where sampled.
	 * <p/> The return values for samples that for a reason are not sampled is -1.
	 * <p/> This method will not return until 2048 samples has been performed. This will take at least 1100ms.
	 *
	 * @param channels the channels to sample. This should be a combination of the constants CHANNEL_0-7.
	 * @return a short array with 2048 samples.
	 */
	protected static native short[] tsRawRead(int channels);

	/** The bit used to address the ADC channel 0. */
	public static final int CHANNEL_0 = 0x1;
	/** The bit used to address the ADC channel 1. */
	public static final int CHANNEL_1 = 0x2;
	/** The bit used to address the ADC channel 2. */
	public static final int CHANNEL_2 = 0x4;
	/** The bit used to address the ADC channel 3. */
	public static final int CHANNEL_3 = 0x8;
	/** The bit used to address the ADC channel 6. This channel does not have a pin in the TS-7800 ADC frame. Where this channel is sampled is not documented.*/
	public static final int CHANNEL_6 = 0x10;
	/** The bit used to address the ADC channel 7. */
	public static final int CHANNEL_7 = 0x20;

	/** The index for channel 0 sample in the array returned from {@link #readAndAverage(int)} and {@link #tsReadAndAverage(int)}. */
	public static final int CHANNEL_0_INDEX = 0;
	/** The index for channel 1 sample in the array returned from {@link #readAndAverage(int)} and {@link #tsReadAndAverage(int)}. */
	public static final int CHANNEL_1_INDEX = 1;
	/** The index for channel 2 sample in the array returned from {@link #readAndAverage(int)} and {@link #tsReadAndAverage(int)}. */
	public static final int CHANNEL_2_INDEX = 2;
	/** The index for channel 3 sample in the array returned from {@link #readAndAverage(int)} and {@link #tsReadAndAverage(int)}. */
	public static final int CHANNEL_3_INDEX = 3;
	/** The index for channel 6 sample in the array returned from {@link #readAndAverage(int)} and {@link #tsReadAndAverage(int)}. */
	public static final int CHANNEL_6_INDEX = 4;
	/** The index for channel 7 sample in the array returned from {@link #readAndAverage(int)} and {@link #tsReadAndAverage(int)}. */
	public static final int CHANNEL_7_INDEX = 5;

	/** The base for calculating the ADC voltage based upon the averaged value. */
	public static final float ADC_BASE = 3.3f/4092;

	static {
		try{
			System.loadLibrary("_rasmantuta_ts7800");
		}catch(UnsatisfiedLinkError e){
			log.error("Failed to load peekpoke library", e);
		}
     }

	/**
	 * Constructs a new <code>ReadAnalog</code> object.
	 * <p/>Making the <code>ReadAnalog default constructor</code> protected makes this a static class, applications cannot call this constructor directly.
	 */
	protected ReadAnalog(){}


	/**
	 * Method for sampling one or more channels on the TS-7800 on board ADC.
	 * <p/>The resulting values are averaged and will have a value between 0 and 4092.
	 * The value 0 represents 0 Volts and 4092 represents 3.3 Volts.
	 * <p/> The return values for channels that are not sampled is -1.
	 * <p/> This method will not return until 2048 samples has been performed. This will take at least 1100ms.
	 *
	 * @param channels the channels to sample. This should be a combination of the constants CHANNEL_0-7.
	 * @return a short array containing the average samples for all channels. channels with no samples will be -1.
	 * @throws IllegalArgumentException thrown if the <code>channels</code> parameter is more than 63, zero or negative.
	 */
	public static short[] rawRead(int channels) throws IllegalArgumentException{
		checkChannels(channels);
		return tsRawRead(channels);
	}

	private static void checkChannels(int channels) {
		if((channels & 0xFFFFFFC0) != 0)
			throw new IllegalArgumentException("The 'channels' parameter must be less than 63, non-zero and non-negative.");

	}

	/**
	 * Method for sampling one or more channels on the TS-7800 on board ADC.
	 * <p/>The resulting values are raw and will have a the channel number in bit 12-15 and a 10 bit shifted two bits left in bit 0-11.
	 * If shifted back, the value 0 will represent 0 Volts and 1023 represent 3.3 Volts.
	 * The channels are sampled in a round-robin fashion, and will be placed in the returned array in the sequence they where sampled.
	 * <p/> The return values for samples that for a reason are not sampled is -1.
	 * <p/> This method will not return until 2048 samples has been performed. This will take at least 1100ms.
	 *
	 * @param channels the channels to sample. This should be a combination of the constants CHANNEL_0-7.
	 * @return a short array with 2048 samples.
	 * @throws IllegalArgumentException thrown if the <code>channels</code> parameter is more than 63, zero or negative.
	 */
	public static short[] readAndAverage(int channels) throws IllegalArgumentException{
		checkChannels(channels);
		return tsReadAndAverage(channels);
	}

}
