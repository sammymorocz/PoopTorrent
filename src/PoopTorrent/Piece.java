package poopTorrent;

public class Piece
{
	private byte[] pieceData;
	private boolean hasData;

	public Piece()
	{
		pieceData = new byte[PeerProcess.myConfig.PieceSize];
		hasData = false;
	}	

	public Piece(byte[] pieceData)
	{
		this.pieceData = pieceData;
		hasData = true;
	}

	public byte[] getPieceData() {
		if (hasData)
			return pieceData;
		else 
			return null;
	}
	
	public void setPieceData(byte[] pieceData) {
		this.pieceData = pieceData;
		hasData = true;
	}
	
	public boolean hasData() {
		return hasData;
	}
}