/*
*ISTE 121 Project
*Team: aGroup >>>Demetri Sakellaropoulos,Zhimin Lin,Roshan Mathew,Becca Dingman
*/


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.awt.geom.*;
import java.util.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class SignomiGameGUI extends JPanel implements MouseListener, ActionListener
{
	public int window_side_length = 880;
   private JButton card; 
   private ImageIcon card_background; 
   private JTextArea chat_message; 
   private JTextArea type_message;
   private JButton send;
	public int Clicked_Grid_X, Clicked_Grid_Y;
	private Client client;   
   private ChatMessage cm;   
   public String card_num = "";
   public String card_info = "";
   public Ellipse2D.Double red_start_circle,blue_start_circle,yellow_start_circle,green_start_circle;
   public Ellipse2D.Double red_home_circle,blue_home_circle,yellow_home_circle,green_home_circle;
   public BufferedImage DrawCardImage = null;

   public enum CardType
   {
      CARD_BLANK, CARD_1,CARD_2,CARD_3,CARD_4,CARD_5,CARD_7,CARD_8,CARD_10,CARD_11,CARD_12,CARD_Signimo
   }   
   public Map<CardType, String> Deck;
   
   public void DrawCard(CardType type)
   {
      if( type == CardType.CARD_BLANK )
      {
         DrawCardImage = null;
         repaint();
         return;
      }
      
      if( Deck.containsKey(type) == true )
      {
         
         String image_path = Deck.get(type);
         try 
         {
            DrawCardImage = ImageIO.read(new File(image_path));            
            repaint(); 
         } 
         catch (IOException e) 
         {
         }
      
      }
   }
   
	public SignomiGameGUI()
   {
      Clicked_Grid_X = -1;
      Clicked_Grid_Y = -1;
      
      Deck = new HashMap<CardType, String>();
      Deck.put(CardType.CARD_1, "Card1.png");
      Deck.put(CardType.CARD_2, "Card2.png");
      Deck.put(CardType.CARD_3, "Card3.png");
      Deck.put(CardType.CARD_4, "Card4.png");
      Deck.put(CardType.CARD_5, "Card5.png");
      Deck.put(CardType.CARD_7, "Card7.png");
      Deck.put(CardType.CARD_8, "Card8.png");
      Deck.put(CardType.CARD_10, "Card10.png");
      Deck.put(CardType.CARD_11, "Card11.png");
      Deck.put(CardType.CARD_12, "Card12.png");
      Deck.put(CardType.CARD_Signimo, "SignomiCard.png");
      
      //DrawCard(CardType.CARD_BLANK);
      DrawCard(CardType.CARD_1);
   
      setLayout(null);
      card = new JButton(); 
      card_background = new ImageIcon(new ImageIcon("card_background.png").getImage().getScaledInstance(200, 130, Image.SCALE_DEFAULT));   
      card.setIcon(card_background); 
      card.setBorderPainted(true);
      card.setBounds(350, 340, 180, 130); 
      add(card);
      
      
      
      
      chat_message = new JTextArea();
      chat_message.setBounds(900, 500, 370, 250); 
      chat_message.setEditable(false);
      add(chat_message);
      
      type_message = new JTextArea();
      type_message.setBounds(900, 780, 280, 50); 
      add(type_message);
      
      send = new JButton("Send"); 
		send.addActionListener(this);      
      send.setBounds(1190, 785, 80, 40);      
      add(send);
      
      addMouseListener(this);
      
		client = new Client("localhost", 10000, "Player", this); 
      client.start();   
	}

	void connectionFailed() 
   {
      System.out.println("Connection Failed");
	}
   
	void append(String str) 
   {
		chat_message.append(str);
		chat_message.setCaretPosition(chat_message.getText().length() - 1);
      type_message.setText("");
	}
   
   public void actionPerformed(ActionEvent e) 
   {
		Object o = e.getSource();
      if(o == send)
      {
         cm = new ChatMessage(1, type_message.getText());
         client.sendMessage(cm);
      }
	}
   
   public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
		g.setColor(Color.black);
		int grid_side_length = window_side_length/16;
		
		for(int i = 0; i<16; i++)//horizontal grid
		{
		   g.drawRect((i*grid_side_length), 0, grid_side_length, grid_side_length);
			g.drawRect((i*grid_side_length),  (window_side_length-grid_side_length) , grid_side_length, grid_side_length);		
      }
      
      for(int i = 1; i<15; i++)//vertical grid
		{
			g.drawRect(0,(i*grid_side_length), grid_side_length, grid_side_length);
			g.drawRect((window_side_length-grid_side_length) ,(i*grid_side_length),  grid_side_length, grid_side_length);
      }
      
      
      g.setFont(new Font("Times New Roman", Font.PLAIN, 25));
      
      
      //-------------------------------------RED-----------------------------------------------------------------------------------------------------------
		
      g.setColor(Color.red);
      for(int i=1; i<6;i++)// save zone
		{
			g.drawRect(grid_side_length*2, (i*grid_side_length) , grid_side_length, grid_side_length);
		}
		
		g.fillOval(90, 330, 100, 100);// home
		
		g.setColor(Color.white);// home
		g.fillOval(100, 340, 80,80);
      red_home_circle = new Ellipse2D.Double(100,340, 80,80);
      g2.draw(red_home_circle);
		
		g.setColor(Color.red);
		g.fillPolygon(new int[] {60, 60, 85}, new int[] {10, 45, 27}, 3);
		
		g.setColor(Color.red);//"Home" text
      g.drawString("Home", 112, 385 );
      
      g.fillOval(210, 55, 80,80);//start
      red_start_circle = new Ellipse2D.Double(210, 55, 80,80);
      g2.draw(red_start_circle);
      
      g.setColor(Color.black);     
      g.drawString("Start", 228, 100 );
      
      //-------------------------------------BLUE--------------------------------------------------------------------------------------------------------------
		g.setColor(new Color(0,0,255));
      
      for(int i=1; i<6;i++)// save zone
		{
         g.drawRect(grid_side_length*(i+9), (2*grid_side_length), grid_side_length, grid_side_length);
		}
		
		g.fillOval(450, 90, 100, 100);// home
		
		g.setColor(Color.white);// home
		g.fillOval(460, 100, 80,80);
      blue_home_circle = new Ellipse2D.Double(460, 100, 80,80);
      g2.draw(blue_home_circle);
		
		g.setColor(new Color(0,0,255));
		g.fillPolygon(new int[] {870, 835, 852}, new int[] {60, 60, 85}, 3);
		
		g.drawString("Home", 472, 145 );//"Home" text
      
      g.fillOval(745, 210, 80,80);//start
      blue_start_circle = new Ellipse2D.Double(745, 210, 80,80);
      g2.draw(blue_start_circle);
      
      g.setColor(Color.black);     
      g.drawString("Start", 758, 257 );//"Start" text
      
      
      //-------------------------------------YELLOW---------------------------------------------------------------------------------------------------------------
		g.setColor(Color.yellow);
      
      for(int i=1; i<6;i++)// save zone
		{
         g.drawRect(grid_side_length*13, ((i+9)*grid_side_length), grid_side_length, grid_side_length);
		}
		
		g.fillOval(693, 450, 100, 100);// home
		
		g.setColor(Color.white);// home
		g.fillOval(703, 460, 80,80);
      yellow_home_circle = new Ellipse2D.Double(703, 460, 80,80);
      g2.draw(yellow_home_circle);
		
		g.setColor(Color.yellow);
		g.fillPolygon(new int[] {820, 820, 795}, new int[] {870, 835, 852}, 3);
		
		g.drawString("Home", 713, 507 );//"Home" text
      
      g.fillOval(590, 743, 80,80);//start
      yellow_start_circle = new Ellipse2D.Double(590, 743, 80,80);
      g2.draw(yellow_start_circle);
      
      g.setColor(Color.black);     
      g.drawString("Start", 605, 790 );//"Start" text
      
      
      //-------------------------------------GREEN----------------------------------------------------------------------------------------------------------------
		g.setColor(new Color(0,255,0));
      
      for(int i=1; i<6;i++)// save zone
		{
         g.drawRect(grid_side_length*i, 13*grid_side_length, grid_side_length, grid_side_length);
		}
		
		g.fillOval(330, 690, 100, 100);// home
		
		g.setColor(Color.white);// home
		g.fillOval(340, 700, 80,80);
      green_home_circle = new Ellipse2D.Double(340, 700, 80,80);
      g2.draw(green_home_circle);
		
		g.setColor(new Color(0,255,0));
		g.fillPolygon(new int[] {10, 45, 25}, new int[] {820, 820, 795}, 3);
		
		g.drawString("Home", 350, 745 );//"Home" text   
      
      g.fillOval(55, 595, 80,80);//start
      green_start_circle = new Ellipse2D.Double(55, 595, 80,80);
      g2.draw(green_start_circle);
      
      g.setColor(Color.black);     
      g.drawString("Start", 68, 640);//"Start" text
      
      
      //-----------------------------------------------center---------------------------------------------------------
      g.setColor(Color.white);
      g.setFont(new Font("Wide Latin", Font.PLAIN, 50));
      g.drawString("Signomi!!", 240, 530 );
      
      g.setColor(Color.gray);
      g.setFont(new Font("Wide Latin", Font.PLAIN, 50));
      g.drawString("Chat", 990, 490 );
      
     // ---------------------------------------------------------pic----------------------------------------------------------------
      if(DrawCardImage != null)
      {
         g.drawImage(DrawCardImage,930,30,300,400,null);
      }

	}

   
	public void mouseClicked(MouseEvent arg0) {
	
		arg0.getX();
      arg0.getY(); 
      for(int i=0;i<15;i++)
      {
         
         if((arg0.getX() >= i*55 && arg0.getX() <(i+1)*55 ) && (arg0.getY() >= 0 && arg0.getY() <55 ))
         {
            System.out.println("grid#" + (i+1) +" is clicked!");
            cm = new ChatMessage(3, (i+1));    
            client.sendMessage(cm);               
         }

      }
      
      for(int i=0;i<15;i++)
      {
         
         if((arg0.getX() >= 825 && arg0.getX() <880 ) && (arg0.getY() >= i*55 && arg0.getY() <(i+1)*55 ))
         {
            System.out.println("grid#" + (i+16) +" is clicked!");
            cm = new ChatMessage(3, (i+16));    
            client.sendMessage(cm);                    
         }

      }
      
      for(int i=15;i>0;i--)
      {
         
         if((arg0.getX() >= (i*55) && arg0.getX() <(i+1)*55 ) && (arg0.getY() >= 825 && arg0.getY() <880 ))
         {
            System.out.println("grid#" + (31 + (15 - i)) +" is clicked!");
            cm = new ChatMessage(3, (31 + (15-i)));    
            client.sendMessage(cm);                         
         }

      }
      
      for(int i=15;i>0;i--)
      {
         
         if((arg0.getX() >= 0 && arg0.getX() < 55 ) && (arg0.getY() >= (i*55) && arg0.getY() <(i+1)*55 ))
         {
            System.out.println("grid#" + (46 + (15 - i)) +" is clicked!");
            cm = new ChatMessage(3, (46 + (15-i)));    
            client.sendMessage(cm);               
         }

      }
      
      for(int i=1;i<6;i++)
      {
         
         if((arg0.getX() >= 110 && arg0.getX() <165 ) && (arg0.getY() >= i*55 && arg0.getY() <(i+1)*55 ))
         {
            System.out.println("red save zone grid#" + (i) +" is clicked!");
            cm = new ChatMessage(4, (i));    
            client.sendMessage(cm);               
         }

      }
      
      for(int i=15;i>10;i--)
      {
         
         if((arg0.getX() >= ((i-1)*55) && arg0.getX() < (i)*55 ) && (arg0.getY() >= 110 && arg0.getY() <165 ))
         {
            System.out.println("blue save zone grid#" + (16 - i) +" is clicked!");
            cm = new ChatMessage(4, (16-i));    
            client.sendMessage(cm);               
         }

      }
      
      for(int i=15;i>10;i--)
      {
         
         if((arg0.getX() >= 715 && arg0.getX() < 770 ) && (arg0.getY() >= (i-1)*55 ) && arg0.getY() <(i*55))
         {
            System.out.println("yellow save zone grid#" + (16 - i) +" is clicked!");
            cm = new ChatMessage(4, (16-i));    
            client.sendMessage(cm);               
         }

      }
      
      for(int i=1;i<6;i++)
      {
         
         if((arg0.getX() >= i*55 && arg0.getX() < (i+1)*55 ) && (arg0.getY() >= 715  && arg0.getY() <770))
         {
            System.out.println("green save zone grid#" + (i) +" is clicked!");
            cm = new ChatMessage(4, (i));    
            client.sendMessage(cm);               
         }

      }

      if (red_start_circle.contains(arg0.getPoint()))
      {
         System.out.println("red start clicked ");
      }
      
      if (blue_start_circle.contains(arg0.getPoint()))
      {
         System.out.println("blue start clicked ");
      }
      
      if (yellow_start_circle.contains(arg0.getPoint()))
      {
         System.out.println("yellow start clicked ");
      }
      
      if (green_start_circle.contains(arg0.getPoint()))
      {
         System.out.println("green start clicked ");
      }
      
      if (red_home_circle.contains(arg0.getPoint()))
      {
         System.out.println("red home clicked!");
      }
      
      if (blue_home_circle.contains(arg0.getPoint()))
      {
         System.out.println("blue home clicked");
      }
      
      if (yellow_home_circle.contains(arg0.getPoint()))
      {
         System.out.println("yellow home clicked");
      }
      
      if (green_home_circle.contains(arg0.getPoint()))
      {
         System.out.println("green home clicked");
      }

      repaint();
      
      
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

   public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
   
   
   
   
   
   
   public static void main(String[] args) 
	{

		SignomiGameGUI sorry = new SignomiGameGUI();
		JFrame jf = new JFrame();
		jf.setTitle("Signomi");
		jf.setSize(1300,940);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.add(sorry);
		jf.setVisible(true);
		jf.setResizable(false);
		
	}

}
