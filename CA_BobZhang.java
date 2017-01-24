// Bob Zhang Culminating Activity
// Top Down Shooter
// June 2, 2016, Last Updated Jan 24, 2017
// Thanks to Bryan Chauvin for providing ideas and inspiration

import java.awt.Image;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.*; //key and actionlisteners
import java.awt.Toolkit; //used to import images into program
import java.awt.Rectangle; //for hitboxes
import java.util.ArrayList; //used for bullet data storage.
import java.util.Random; //used for computer shooting patterns

//used in JButton, GUI, and graphical output of game
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;

//Used to give JButtons function
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;


class CA_BobZhang extends JFrame implements KeyListener
{
    //variables are public so they can be modified in the game menu
    public static int dmg1 = 1; //Player's weapon damage
    public static int dmg2 = 1; //CPU player's weapon damage
    public static int bulletspeed = 1; //speed bullets travel along the x axis
    public static int starthealth1 = 10; //starting health of player
    public static int starthealth2 = 10; //cpu
    public static int speed = 3; //movement speed of player
    public static int speed2 = 1; //cpu
    public static int shotgun = 1; //"hard" mode of program, cpu player shoots 3 bullets in a cone each time he fires.

    public static ArrayList bullets = new ArrayList (); //all bullets created are stored and their path is tracked in this arraylist

    private Player player1; //human player
    private Player player2; //cpu player
    private Player obstacle1; //obstacles placed randomly around the play field
    private Player obstacle2;
    private Player obstacle3;
    private Player obstacle4;

    private Image image;
    private Graphics graphics;

    public static boolean Player1Left = false; //human player movement
    public static boolean Player1Right = false;
    public static boolean Player1Up = false;
    public static boolean Player1Down = false;

    public static boolean Player2Up = false;
    public static boolean Player2Down = false;

    public static void main (String[] args)  //creates the menu screen using menu class
    {
	new menu ();
    }


    public CA_BobZhang ()  //Generates game screen and default paramters of players (health, speed, size, position, etc.)
    {
	Random locationgen = new Random ();
	int randomx = locationgen.nextInt ((200 - 50) + 1) + 50; //x value of obstacle. Max value 200, minimum 50
	int randomy = locationgen.nextInt ((450 - 29) + 1) + 29; //y value of obstacle, max value 450, minimum 29
	setTitle ("Top Down Shooter");
	setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	setResizable (false);
	setSize (800, 600);
	setBackground (Color.BLACK);
	setVisible (true);
	addKeyListener (this); //used for human player movement
	player1 = new Player (0, speed, 150, 17, 41, starthealth1, "p1.png"); //Human player initial stats
	player2 = new Player (757, speed2, 150, 17, 41, starthealth2, "p2.png"); //CPU controlled player
	obstacle1 = new Player (400 - randomx, 0, randomy, 40, 40, 10, "ObstacleA.png"); //generates obstacle 1
	obstacle2 = new Player (400 + randomx, 0, randomy, 40, 40, 10, "ObstacleB.png");
	//obstacle 2 has same y as obstacle1 and is equidistant from the centre of the screen as obstacle1
	//obstacles being mirrored makes the game fair for player and cpu
	randomx = locationgen.nextInt ((200 - 50) + 1) + 50; //new x and y generated for 2 more obstacles
	randomy = locationgen.nextInt ((450 - 29) + 1) + 29;
	obstacle3 = new Player (400 - randomx, 0, randomy, 40, 40, 10, "ObstacleA.png");
	obstacle4 = new Player (400 + randomx, 0, randomy, 40, 40, 10, "ObstacleB.png");
    }


    public void paint (Graphics g)
    {
	try
	{
	    Thread.sleep (4);
	}
	catch (InterruptedException ex)
	{
	    Thread.currentThread ().interrupt ();
	}
	int allowfire = 1; //determines if cpu player fires or not
	Random rand = new Random ();
	allowfire = rand.nextInt (1001);
	if (allowfire >= 970)
	{
	    Bullet player2BulletB = new Bullet (player1, -(bulletspeed), 0, dmg2, player2.getx () - 5, player2.gety () + 34, 5, 5, "bullet.png", obstacle1, obstacle2, obstacle3, obstacle4);
	    bullets.add (player2BulletB);
	    player2BulletB = null;
	    if (shotgun == 1)
	    {
		Bullet player2Bullet = new Bullet (player1, -(bulletspeed), bulletspeed, dmg2, player2.getx () - 5, player2.gety () + 34, 5, 5, "bullet.png", obstacle1, obstacle2, obstacle3, obstacle4);
		Bullet player2BulletC = new Bullet (player1, -(bulletspeed), -(bulletspeed), dmg2, player2.getx () - 5, player2.gety () + 34, 5, 5, "bullet.png", obstacle1, obstacle2, obstacle3, obstacle4);
		bullets.add (player2Bullet);
		player2Bullet = null;
		bullets.add (player2BulletC);
		player2BulletC = null;
	    }
	}
	image = createImage (getWidth (), getHeight ());
	graphics = image.getGraphics ();
	updateObjects (graphics);
	g.drawImage (image, 0, 0, null);
	aimove ();
	repaint ();
    }


    public void aimove ()  //cpu movement
    {
	if (Math.abs (player1.gety () - player2.gety ()) < speed2)
	{
	    Player2Down = false;
	    Player2Up = false;
	    player2.sety (player1.gety ());
	}
	if (player1.gety () > player2.gety ())
	{
	    Player2Up = false;
	    Player2Down = true;
	}
	else if (player1.gety () < player2.gety ())
	{
	    Player2Down = false;
	    Player2Up = true;
	}
	else if (player1.gety () == player2.gety ())
	{
	    Player2Down = false;
	    Player2Up = false;
	}
    }


    public void updateObjects (Graphics g)
    {
	if (player1.getlife () > 0 && player2.getlife () > 0)
	{
	    for (int x = 0 ; x < bullets.size () ; x++)
	    {
		Bullet drawbullet = (Bullet) bullets.get (x); //drawbullet is the bullet currently being drawn
		drawbullet.draw (g);
		drawbullet.update ();
	    }
	}
	if (obstacle1.getlife () <= 0)
	{
	    obstacle1.remove ();
	}
	if (obstacle2.getlife () <= 0)
	{
	    obstacle2.remove ();
	}
	if (obstacle3.getlife () <= 0)
	{
	    obstacle3.remove ();
	}
	if (obstacle4.getlife () <= 0)
	{
	    obstacle4.remove ();
	}
	if (player1.getlife () <= 0)
	{
	    g.setColor (Color.GRAY);
	    g.fillRect (332, 200, 100, 30);
	    g.setColor (Color.RED);
	    g.drawString ("YOU LOSE!", 350, 218); //lose message
	    stopMovement ();
	}
	if (player2.getlife () <= 0)
	{
	    g.setColor (Color.YELLOW);
	    g.fillRect (332, 200, 100, 30);
	    g.setColor (Color.BLUE);
	    g.drawString ("YOU WIN!!", 350, 218); //win message
	    stopMovement ();
	}
	player1.draw (g); //draws human player
	player1.update (1);
	player2.draw (g); //draws cpu player
	player2.update (2);
	obstacle1.draw (g);
	obstacle2.draw (g);
	obstacle3.draw (g);
	obstacle4.draw (g);
	//draw HUD, displays user information
	g.setColor (Color.GRAY); //HUD background colour
	g.fillRect (0, 500, 800, 100);
	g.setColor (Color.RED);
	g.drawString ("Health: " + player1.getlife (), 20, 550);
	g.setColor (Color.GREEN);
	g.drawString ("Weapon Damage: " + dmg1, 100, 550);
	g.setColor (Color.YELLOW);
	g.drawString ("Movement Speed: " + speed, 240, 550);
    }


    public void stopMovement ()
    {
	removeKeyListener (this);
	Player1Left = false; //human player movement
	Player1Right = false;
	Player1Up = false;
	Player1Down = false;
    }


    public void keyPressed (KeyEvent e)
    {
	if (e.getKeyCode () == KeyEvent.VK_UP || e.getKeyCode () == KeyEvent.VK_W)
	{
	    Player1Up = true;
	}
	else if (e.getKeyCode () == KeyEvent.VK_DOWN || e.getKeyCode () == KeyEvent.VK_S)
	{
	    Player1Down = true;
	}
	else if (e.getKeyCode () == KeyEvent.VK_LEFT || e.getKeyCode () == KeyEvent.VK_A)
	{
	    Player1Left = true;
	}
	else if (e.getKeyCode () == KeyEvent.VK_RIGHT || e.getKeyCode () == KeyEvent.VK_D)
	{
	    Player1Right = true;
	}
    }


    public void keyReleased (KeyEvent e)
    {
	if (e.getKeyCode () == KeyEvent.VK_UP || e.getKeyCode () == KeyEvent.VK_W)
	{
	    Player1Up = false;
	}
	else if (e.getKeyCode () == KeyEvent.VK_DOWN || e.getKeyCode () == KeyEvent.VK_S)
	{
	    Player1Down = false;
	}
	else if (e.getKeyCode () == KeyEvent.VK_LEFT || e.getKeyCode () == KeyEvent.VK_A)
	{
	    Player1Left = false;
	}
	else if (e.getKeyCode () == KeyEvent.VK_RIGHT || e.getKeyCode () == KeyEvent.VK_D)
	{
	    Player1Right = false;
	}
	else if (e.getKeyCode () == KeyEvent.VK_SPACE)
	{
	    Bullet player1Bullet = new Bullet (player2, bulletspeed, 0, dmg1, player1.getx () + 16, player1.gety () + 34, 5, 5, "bullet.png", obstacle1, obstacle2, obstacle3, obstacle4);
	    bullets.add (player1Bullet);
	}
    }


    public void keyTyped (KeyEvent e) {}
}

class Player extends GameObject
{
    private int speed;

    public Player (final int x, final int speed, final int y, final int w, final int h, final int life, final String img)
    {
	this.speed = speed;
	this.x = x;
	this.y = y;
	this.h = h;
	this.w = w;
	this.rect = new Rectangle (x, y, w, h);
	this.img = getImage (img);
	this.life = life;
    }


    public void draw (Graphics g)
    {
	g.drawImage (img, x, y, w, h, null);
    }


    void update (final int id)
    {
	if (id == 1)
	{
	    if (CA_BobZhang.Player1Up)
	    {
		if (y >= 26)
		{
		    y = y - speed;
		}
	    }
	    if (CA_BobZhang.Player1Down)
	    {
		if (y <= 459)
		{
		    y = y + speed;
		}
	    }
	    if (CA_BobZhang.Player1Left)
	    {
		if (x >= 1)
		{
		    x = x - speed;
		}
	    }
	    if (CA_BobZhang.Player1Right)
	    {
		if (x < 300)
		{
		    x = x + speed;
		}
	    }
	    rect.y = this.y;
	    rect.x = this.x;
	}
	else if (id == 2)
	{
	    if (CA_BobZhang.Player2Up)
	    {
		if (!(y < 26))
		{
		    y = y - speed;
		}
	    }
	    if (CA_BobZhang.Player2Down)
	    {
		if (y <= 459)
		{
		    y = y + speed;
		}
	    }
	    rect.y = this.y;
	}
    }


    public void remove ()
    {
	this.setx (-50);
	this.rect = new Rectangle (-15, 0, 0, 0);
    }


    Image getImage (String img)
    {
	return Toolkit.getDefaultToolkit ().getImage (img);
    }
}

class Bullet extends GameObject
{
    private Player player, obs1, obs2, obs3, obs4;
    private int xmove;
    private int ymove;
    private int damage;

    public Bullet (final Player player, final int xmove, final int ymove, final int damage, final int x, final int y, final int w, final int h, final String img, final Player obs1, final Player obs2, final Player obs3, final Player obs4)
    {
	this.player = player;
	this.xmove = xmove;
	this.ymove = ymove;
	this.damage = damage;
	this.x = x;
	this.y = y;
	this.h = h;
	this.w = w;
	this.rect = new Rectangle (x, y, w, h);
	this.img = getImage (img);
	this.obs1 = obs1;
	this.obs2 = obs2;
	this.obs3 = obs3;
	this.obs4 = obs4;
    }


    public void draw (Graphics g)
    {
	g.drawImage (img, x, y, w, h, null);
    }


    void update ()  //updates bullet location and checks for collisions with Player objects
    {
	if (rect.intersects (player.rect)) //bullet hitbox (Rectangle) intersects the opponent player's hitbox
	{
	    player.setlife (player.getlife () - damage);
	    this.x = -10;
	    this.rect = new Rectangle (-15, 0, 0, 0);
	}
	if (rect.intersects (obs1.rect)) //bullet hitting obstacles detection
	{
	    obs1.setlife (obs1.getlife () - damage);
	    this.x = -10;
	    this.rect = new Rectangle (-15, 0, 0, 0);
	}
	if (rect.intersects (obs2.rect))
	{
	    obs2.setlife (obs2.getlife () - damage);
	    this.x = -10;
	    this.rect = new Rectangle (-15, 0, 0, 0);
	}
	if (rect.intersects (obs3.rect))
	{
	    obs3.setlife (obs3.getlife () - damage);
	    this.x = -10;
	    this.rect = new Rectangle (-15, 0, 0, 0);
	}
	if (rect.intersects (obs4.rect))
	{
	    obs4.setlife (obs4.getlife () - damage);
	    this.x = -10;
	    this.rect = new Rectangle (-15, 0, 0, 0);
	}

	else if (x < -2 || x > 805)
	{
	    this.x = -10;
	    this.rect = new Rectangle (-15, 0, 0, 0);
	}
	else
	{
	    x = x + xmove;
	    rect.x = rect.x + xmove;
	    y = y + ymove;
	    rect.y = rect.y + ymove;
	}
    }


    Image getImage (String img)
    {
	return Toolkit.getDefaultToolkit ().getImage (img);
    }
}

abstract class GameObject
{
    protected Rectangle rect; //hitbox of GameObject
    protected int x;
    protected int y;
    protected int h;
    protected int w;
    protected int life;
    protected Image img;

    abstract void draw (Graphics g);
    abstract Image getImage (String img);


    public Image getimg ()
    {
	return img;
    }


    public int getx ()
    {
	return x;
    }


    public void setx (int x)
    {
	this.x = x;
    }


    public int gety ()
    {
	return y;
    }


    public void sety (int y)
    {
	this.y = y;
    }


    public int getlife ()
    {
	return life;
    }


    public void setlife (int life)
    {
	this.life = life;
    }
}

class menu extends JFrame implements ActionListener //Menu screen
{
    JPanel mainmenu = new JPanel ();
    protected JButton start = new JButton ("Start Game");
    protected JButton quit = new JButton ("Quit Game");

    protected JButton damage1 = new JButton ("Your Damage: " + CA_BobZhang.dmg1);
    protected JButton damage2 = new JButton ("CPU Damage: " + CA_BobZhang.dmg2);
    protected JButton hp1 = new JButton ("Your Health: " + CA_BobZhang.starthealth1);
    protected JButton hp2 = new JButton ("CPU Health: " + CA_BobZhang.starthealth2);
    protected JButton move1 = new JButton ("Your Speed: " + CA_BobZhang.speed);
    protected JButton move2 = new JButton ("CPU Speed: " + CA_BobZhang.speed2);
    protected JButton bspeed = new JButton ("Bullet Speed: " + CA_BobZhang.bulletspeed);
    protected JButton hard = new JButton ("Hard Mode: " + CA_BobZhang.shotgun);

    protected JButton damage1r = new JButton ("Reduce Value");
    protected JButton damage2r = new JButton ("Reduce Value");
    protected JButton hp1r = new JButton ("Reduce Value");
    protected JButton hp2r = new JButton ("Reduce Value");
    protected JButton move1r = new JButton ("Reduce Value");
    protected JButton move2r = new JButton ("Reduce Value");
    protected JButton bspeedr = new JButton ("Reduce Value");

    public menu ()
    {
	start.setActionCommand ("A");
	quit.setActionCommand ("B");

	damage1.setActionCommand ("C");
	damage2.setActionCommand ("D");
	hp1.setActionCommand ("E");
	hp2.setActionCommand ("F");
	move1.setActionCommand ("G");
	move2.setActionCommand ("H");
	bspeed.setActionCommand ("I");
	hard.setActionCommand ("J");

	damage1r.setActionCommand ("Cr");
	damage2r.setActionCommand ("Dr");
	hp1r.setActionCommand ("Er");
	hp2r.setActionCommand ("Fr");
	move1r.setActionCommand ("Gr");
	move2r.setActionCommand ("Hr");
	bspeedr.setActionCommand ("Ir");
	//adding the buttons to JFrame
	this.getContentPane ().add (start);
	this.getContentPane ().add (quit);

	this.getContentPane ().add (damage1);
	this.getContentPane ().add (damage2);
	this.getContentPane ().add (hp1);
	this.getContentPane ().add (hp2);
	this.getContentPane ().add (move1);
	this.getContentPane ().add (move2);
	this.getContentPane ().add (bspeed);
	this.getContentPane ().add (hard);

	this.getContentPane ().add (damage1r);
	this.getContentPane ().add (damage2r);
	this.getContentPane ().add (hp1r);
	this.getContentPane ().add (hp2r);
	this.getContentPane ().add (move1r);
	this.getContentPane ().add (move2r);
	this.getContentPane ().add (bspeedr);

	this.getContentPane ().add (mainmenu);

	mainmenu.setBounds (800, 600, 200, 100);

	start.setBounds (230, 0, 340, 100);
	quit.setBounds (530, 500, 250, 50);

	damage1.setBounds (20, 100, 220, 30);
	damage2.setBounds (20, 145, 220, 30);
	hp1.setBounds (20, 190, 220, 30);
	hp2.setBounds (20, 235, 220, 30);
	move1.setBounds (20, 280, 220, 30);
	move2.setBounds (20, 325, 220, 30);
	bspeed.setBounds (20, 370, 220, 30);
	hard.setBounds (20, 415, 220, 30);

	damage1r.setBounds (20 + 250, 100, 220 - 100, 30);
	damage2r.setBounds (20 + 250, 145, 220 - 100, 30);
	hp1r.setBounds (20 + 250, 190, 220 - 100, 30);
	hp2r.setBounds (20 + 250, 235, 220 - 100, 30);
	move1r.setBounds (20 + 250, 280, 220 - 100, 30);
	move2r.setBounds (20 + 250, 325, 220 - 100, 30);
	bspeedr.setBounds (20 + 250, 370, 220 - 100, 30);

	setTitle ("Top Down Shooter");
	setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	setResizable (false);
	setSize (800, 600);
	setBackground (Color.BLACK);
	setVisible (true);

	start.addActionListener (this);
	quit.addActionListener (this);

	damage1.addActionListener (this);
	damage2.addActionListener (this);
	hp1.addActionListener (this);
	hp2.addActionListener (this);
	move1.addActionListener (this);
	move2.addActionListener (this);
	bspeed.addActionListener (this);
	hard.addActionListener (this);

	damage1r.addActionListener (this);
	damage2r.addActionListener (this);
	hp1r.addActionListener (this);
	hp2r.addActionListener (this);
	move1r.addActionListener (this);
	move2r.addActionListener (this);
	bspeedr.addActionListener (this);
    }


    public void actionPerformed (ActionEvent e)
    {
	if ("A".equals (e.getActionCommand ()))
	{
	    new CA_BobZhang ();
	}
	else if ("B".equals (e.getActionCommand ()))
	{
	    System.exit (0);
	}
	//changing of game variables
	else if ("C".equals (e.getActionCommand ()))
	{
	    CA_BobZhang.dmg1++;
	    damage1.setText ("Your Damage: " + CA_BobZhang.dmg1);
	}
	else if ("D".equals (e.getActionCommand ()))
	{
	    CA_BobZhang.dmg2++;
	    damage2.setText ("CPU Damage: " + CA_BobZhang.dmg2);
	}
	else if ("E".equals (e.getActionCommand ()))
	{
	    CA_BobZhang.starthealth1++;
	    hp1.setText ("Your Health: " + CA_BobZhang.starthealth1);
	}
	else if ("F".equals (e.getActionCommand ()))
	{
	    CA_BobZhang.starthealth2++;
	    hp2.setText ("CPU Health: " + CA_BobZhang.starthealth2);
	}
	else if ("G".equals (e.getActionCommand ()))
	{
	    CA_BobZhang.speed++;
	    move1.setText ("Your Speed: " + CA_BobZhang.speed);
	}
	else if ("H".equals (e.getActionCommand ()))
	{
	    CA_BobZhang.speed2++;
	    move2.setText ("CPU Speed: " + CA_BobZhang.speed2);
	}
	else if ("I".equals (e.getActionCommand ()))
	{
	    CA_BobZhang.bulletspeed++;
	    bspeed.setText ("Bullet Speed: " + CA_BobZhang.bulletspeed);
	}
	else if ("J".equals (e.getActionCommand ()) && CA_BobZhang.shotgun == 1) //"hard" mode button. Hard mode can only be set to on (1) or off (0)
	{
	    CA_BobZhang.shotgun = 0;
	    hard.setText ("Hard Mode: " + CA_BobZhang.shotgun);
	}
	else if ("J".equals (e.getActionCommand ()) && CA_BobZhang.shotgun == 0)
	{
	    CA_BobZhang.shotgun = 1;
	    hard.setText ("Hard Mode: " + CA_BobZhang.shotgun);
	}
	else if ("Cr".equals (e.getActionCommand ())) //buttons that decrease a game variable
	{
	    CA_BobZhang.dmg1--;
	    damage1.setText ("Your Damage: " + CA_BobZhang.dmg1);
	}
	else if ("Dr".equals (e.getActionCommand ()))
	{
	    CA_BobZhang.dmg2--;
	    damage2.setText ("CPU Damage: " + CA_BobZhang.dmg2);
	}
	else if ("Er".equals (e.getActionCommand ()))
	{
	    CA_BobZhang.starthealth1--;
	    hp1.setText ("Your Health: " + CA_BobZhang.starthealth1);
	}
	else if ("Fr".equals (e.getActionCommand ()))
	{
	    CA_BobZhang.starthealth2--;
	    hp2.setText ("CPU Health: " + CA_BobZhang.starthealth2);
	}
	else if ("Gr".equals (e.getActionCommand ()))
	{
	    CA_BobZhang.speed--;
	    move1.setText ("Your Speed: " + CA_BobZhang.speed);
	}
	else if ("Hr".equals (e.getActionCommand ()))
	{
	    CA_BobZhang.speed2--;
	    move2.setText ("CPU Speed: " + CA_BobZhang.speed2);
	}
	else if ("Ir".equals (e.getActionCommand ()))
	{
	    CA_BobZhang.bulletspeed--;
	    bspeed.setText ("Bullet Speed: " + CA_BobZhang.bulletspeed);
	}
    }
}
