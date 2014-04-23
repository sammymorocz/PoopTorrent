package poopTorrent;

/**
 * @author Derek Poirier
 * @author Evan Bass
 * @author Stewart Reive
 * 
 * Contains a byte array 
 */

public class Bitfield
{
	private byte[] bitfield;
	private int numBits;
	//private boolean complete;

	public Bitfield(int numBits)
	{
		this(numBits, false);
	}

	public Bitfield(int numBits, boolean hasFile)
	{
		createBitfield(numBits, hasFile);
		this.numBits = numBits;
		//complete = hasFile;
	}

	// Getters/Setters
	/**
	 * Getter for the bitfield
	 * @return the bitfield
	 */
	public byte[] getBitfield()
	{
		return bitfield;
	}

	/**
	 * Setter for the bitfield
	 * @param bitfield the bitfield to set
	 */
	public void setBitfield(byte[] bitfield)
	{
		this.bitfield = bitfield;
	}

	/**
	 * Getter for the number of bits
	 * @return the numBits
	 */
	public int getNumBits()
	{
		return numBits;
	}

	/**
	 * Setter for the number of bits
	 * @param numBits the numBits to set
	 */
	public void setNumBits(int numBits)
	{
		this.numBits = numBits;
	}

	/**
	 * Gets whether or not it has the entire file
	 * @return the hasEntireFile
	 */
	public boolean isComplete()
	{
		boolean complete = true;
		
		for(byte b : bitfield)
		{
			if (b == 0)
				complete = false;
		}
		
		return complete;
	}

	/**
	 * Initializes the bitfield property.
	 * @param size Size of the bitfield to create
	 * @param hasFile 
	 */
	private void createBitfield(int size, boolean hasFile)
	{
		bitfield = new byte[size];
		
		for(int i = 0; i < bitfield.length; i++)
		{
			bitfield[i] = (byte) (hasFile ? 1 : 0);
		}
	}

	public void receivePiece(int index) {
		bitfield[index] = (byte)1;
	}
}