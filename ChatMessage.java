import java.io.*;
public class ChatMessage implements Serializable
{
	static final int WHOISIN = 0; // Is the signal to return a list of users 
   static final int MESSAGE = 1; // Is the signal to display a message
   static final int DRAWCARD = 2; //Is the signal to draw a card
   static final int MOVEPAWN = 3; //Is the signal that a pawn has been moved.
   static final int INSAFEZONE = 4; //Is the signal that a pawn is in a safe zone
   static final int BORDERINGSAFEZONE = 5; //Is the signal that a pawn is bordering the safe zone
   static final int INSTART = 6; //Is the signal that a pawn is in start
   static final int HOME = 7; //Is the signal that a pawn is home
   
	private int type;
	private String message;
   private int move;
   private static final long serialVersionUID = 7526472295622777L;

	
	// constructor
	ChatMessage(int type, String message) 
   {
		this.type = type;
		this.message = message;
	}
   ChatMessage(int type, int move)
   {
      this.type = type;
      this.move = move;
   }
	
	// getters
	int getType() 
   {
		return type;
	}
	String getMessage() 
   {
		return message;
	} 
   int getMove()
   {
      return move;
   }
}
