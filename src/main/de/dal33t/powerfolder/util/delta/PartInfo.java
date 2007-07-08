package de.dal33t.powerfolder.util.delta;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Info for a frame of bytes.
 * A partinfo contains only enough information to check for matches and reconstruct
 * the location in a file.
 * 
 * @author Dennis "Dante" Waldherr
 * @version $Revision: $ 
 */
public class PartInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private long index;
	private long checksum;
	private byte[] digest;
	
	public PartInfo(long index, long checksum, byte[] digest) {
		super();
		this.index = index;
		this.checksum = checksum;
		this.digest = digest;
	}
	/**
	 * Returns the checksum calculated for this part.
	 * @return
	 */
	public long getChecksum() {
		return checksum;
	}
	/**
	 * Returns the message digest calculated for this part.
	 * @return
	 */
	public byte[] getDigest() {
		return digest;
	}
	/**
	 * Returns the index of this part.
	 * @return
	 */
	public long getIndex() {
		return index;
	}
	
	@Override
	public String toString() {
		return "{" + getIndex() + ": " + getChecksum() + "}";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PartInfo) {
			PartInfo pi = (PartInfo) obj;
			return index == pi.index 
				&& checksum == pi.checksum 
				&& Arrays.equals(digest, pi.digest);
		}
		return false;
	}
	@Override
	public int hashCode() {
		return (int) index ^ (int) (index >> 32) ^ (int) checksum ^ (int) (checksum >> 32) ^ Arrays.hashCode(digest);
	}
}
