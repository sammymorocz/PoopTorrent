package poopTorrent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class MessageUtils
{
	/**
	 * Sends message to the designated peer.
	 * @param socket ID of the peer to send the message to
	 * @param message Message with payload to send
	 */
	private static void sendMessage(Socket socket, Message message)
	{
		 OutputStream out = null;
		 
		 try {
			out = socket.getOutputStream();
		 } catch (IOException e) {
			e.printStackTrace();
		 }
		 
		 DataOutputStream dos = new DataOutputStream(out);
		 try {
			dos.write(message.getBytes(), 0, message.getBytes().length);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates a message based on the data received
	 * @param messageData The byte array received
	 * @return The generated message
	 */
	public static Message receiveMessage(Socket s)
	{
		try {
			InputStream in = s.getInputStream();
		    DataInputStream dis = new DataInputStream(in);
		    
		    byte[] header = new byte[5];
		    dis.readFully(header);
	
		    PeerProcess.log.info("header is: " + new String(header));
		    
			if ( new String(header).equals("HELLO") )
			{
				ByteBuffer buffer = ByteBuffer.allocate(27);
				dis.readFully(buffer.array());
				
				int peerID = buffer.getInt(23);
	
				return new HandshakeMessage(peerID);
			}
			else
			{
				ByteBuffer buffer = ByteBuffer.allocate(5);
				buffer.put(header);
				buffer.position(0); // reset position
				int length = buffer.getInt();
				byte messageType = buffer.get();
				
				// length includes type, so the remaining bytes are length - 1
				PeerProcess.log.info("We are allocating a buffer of length=" + length);
				byte[] payload = new byte[length - 1];
				dis.readFully(payload);
				
				return new NormalMessage(messageType, payload);
			}
		} catch (IOException e) {
			e.printStackTrace();
			PeerProcess.log.info("SHIT GOT FUCKED YO :(");
			return null;
		}
	}

	/**
	 * Creates and sends a handshake message to the designated peer.
	 * @param socket Socket of the peer FUCK MY LIFE
	 * @param peerID ID of the sender to put in the message
	 */
	public static void handshake(Socket socket, int peerID)
	{
		Message message = new HandshakeMessage( peerID );

		sendMessage(socket, message);
	}

	/**
	 * Creates and sends a choke message to the designated peer.
	 * @param socket ID of the peer to send the message to
	 */
	public static void choke(Socket socket)
	{
		Message message = new NormalMessage( (byte)0, new byte[0] );

		sendMessage(socket, message);
	}

	/**
	 * Creates and sends an unchoke message to the designated peer.
	 * @param socket ID of the peer to send the message to
	 */
	public static void unchoke(Socket socket)
	{
		Message message = new NormalMessage( (byte)1, new byte[0] );

		sendMessage(socket, message);
	}

	/**
	 * Creates and sends an interested message to the designated peer.
	 * @param socket ID of the peer to send the message to
	 */
	public static void interested(Socket socket)
	{
		Message message = new NormalMessage( (byte)2, new byte[0] );

		sendMessage(socket, message);
	}

	/**
	 * Creates and sends a not interested message to the designated peer.
	 * @param socket ID of the peer to send the message to
	 */
	public static void notInterested(Socket socket)
	{
		Message message = new NormalMessage( (byte)3, new byte[0] );

		sendMessage(socket, message);
	}

	/**
	 * Sends a have message to the designated peer along with the index of the possesed piece
	 * @param socket ID of the peer to send the message to
	 * @param index Index of the possesed piece
	 */
	public static void have(Socket socket, int index)
	{
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(index);
		Message message = new NormalMessage( (byte)4, buffer.array() );

		sendMessage(socket, message);
	}

	/**
	 * Sends a bitfield message to the designated peer. To be sent after the handshake message
	 * @param socket ID of the peer to send the message to
	 * @param bitfield Bitfield of pieces possesed
	 */
	public static void bitfield(Socket socket, byte[] bitfield)
	{
		Message message = new NormalMessage( (byte)5, bitfield );

		sendMessage(socket, message);
	}

	/**
	 * Sends a request message to the designated peer along with the index of the desired piece
	 * @param socket ID of the peer to send the message to
	 * @param index Index of the desired piece
	 */
	public static void request(Socket socket, int index)
	{
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(index);
		Message message = new NormalMessage( (byte)6, buffer.array() );

		sendMessage(socket, message);
	}

	/**
	 * Sends a piece of the file to the designated peer.
	 * @param socket ID of the peer to send the message to
	 * @param index Index of the piece being sent
	 * @param payload Piece of the file
	 */
	public static void piece(Socket socket, int index, byte[] payload)
	{
		ByteBuffer buffer = ByteBuffer.allocate(payload.length + 4);
		buffer.putInt(index);
		buffer.put(payload);
		Message message = new NormalMessage( (byte)7, buffer.array() );

		sendMessage(socket, message);
	}
}
