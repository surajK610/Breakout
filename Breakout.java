/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;

public class Breakout extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 1920;
	public static final int APPLICATION_HEIGHT = 953;
	
	/**Count down*/
	public static final int COUNTDOWN = 5;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 200;
	private static final int PADDLE_HEIGHT = 20;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 35;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;

	private GRect brick;
	private GRect paddle;
	private GOval ball;
	
	/* Method: init() */
	/** Sets up the Breakout program. */
	public void init() {
		/* You fill this in, along with any subsidiary methods */
		setUpBricks();
		setUpPaddle();
		
		addMouseListeners();
		
			
		}
	

	private void setUpBricks() {
		// TODO Auto-generated method stub
		for(int j = 0; j < NBRICKS_PER_ROW; j++){
			heightFromTop = BRICK_Y_OFFSET;
			for(int k = 1; k < NBRICK_ROWS + 1; k++){
				brick = new GRect(widthFromSide, heightFromTop, BRICK_WIDTH, BRICK_HEIGHT);
				brick.setFilled(true);
				heightFromTop = heightFromTop + BRICK_HEIGHT + BRICK_SEP; 
				switch(k){
					case 1: brick.setColor(R12Color);break;
					case 2: brick.setColor(R12Color);break;
					case 3: brick.setColor(R34Color);break;
					case 4: brick.setColor(R34Color);break;
					case 5: brick.setColor(R56Color);break;
					case 6: brick.setColor(R56Color);break;
					case 7: brick.setColor(R78Color);break;
					case 8: brick.setColor(R78Color);break;
					case 9: brick.setColor(R910Color);break;
					case 10: brick.setColor(R910Color);break;
					default: brick.setColor(Color.black);break;
				}
						
				add(brick);
			}
			widthFromSide = widthFromSide + BRICK_SEP + BRICK_WIDTH;
		}
	}


	private void setUpPaddle() {
		// TODO Auto-generated method stub
		paddle = new GRect((WIDTH -PADDLE_WIDTH)/2, HEIGHT - PADDLE_HEIGHT - PADDLE_Y_OFFSET, PADDLE_WIDTH, PADDLE_HEIGHT  );
		paddle.setFilled(true);
		add(paddle);
	
	}
	public void mousePressed(MouseEvent e){
		GPoint last = new GPoint(e.getPoint());
		gobj = getElementAt(last);
		
		}
	public void mouseDragged(MouseEvent e){
		
			if(gobj == paddle){
				GPoint now = new GPoint(e.getX(), HEIGHT - PADDLE_HEIGHT - PADDLE_Y_OFFSET);
				gobj.setLocation(now);
			}
			if(paddle.getX() + PADDLE_WIDTH > WIDTH){
				paddle.move(-(paddle.getX() + PADDLE_WIDTH - WIDTH), 0);
			}
			
		}
	

	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
		/* You fill this in, along with any subsidiary methods */
		setUpBall();
		for(int i = COUNTDOWN; i>0; i --){
			
			count = new GLabel("" + i, (WIDTH-10)/2, (HEIGHT-100)/2 );
			count.setFont("SansSerif-bold-48");
			add(count);
			pause(1000);
			remove(count);
		}
		vx = rgen.nextDouble(1.0, 10.0);
		if (rgen.nextBoolean(0.5)) vx = -vx;
		
		vy = 24.0;
		
		boolean game = false;
		while(nturns>0 && nbricks>0){
			ball.move(vx, vy);
			bounceOffWall();
			GObject collider = getCollidingObject();
			if(collider != null){
			implementCollider(collider);
			}
			goFaster();
			checkForEnd();
			pause(50);
		}
		
		}

	
	private void implementCollider(GObject collider) {
		if (collider == paddle){
			ball.setLocation(ball.getX(),paddle.getY()-(2 * BALL_RADIUS));
			vy = -vy;
			
		} else{
			vy = -vy;
			remove(collider);
			nbricks -=1;
		}
		
	}


	private void setUpBall() {
		// TODO Auto-generated method stub
		ball = new GOval(WIDTH/2, HEIGHT/2, BALL_RADIUS * 2, BALL_RADIUS * 2);
		ball.setFilled(true);
		add(ball);
		
	}
	
	private void bounceOffWall(){
		if(ball.getX()<= 0){
			ball.move(0-ball.getX(), 0);
			vx = -vx;
		} else if(ball.getY()<=0){
			ball.move(0, 0-ball.getY());;
			vy = -vy;
		}else if(ball.getX() + 2 * BALL_RADIUS>=WIDTH){
			ball.move(ball.getX()-WIDTH, 0);
			vx = -vx;
		}else if(ball.getY() + 2 * BALL_RADIUS>=HEIGHT){
			checkForBottom();
			}
	}
	
	private GObject getCollidingObject(){
		GObject ballCorner1 = getElementAt(ball.getX(), ball.getY());
		GObject ballCorner2 = getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY());
		GObject ballCorner3 = getElementAt(ball.getX() + 2 * BALL_RADIUS, ball.getY() + 2 * BALL_RADIUS);
		GObject ballCorner4 = getElementAt(ball.getX(), ball.getY() + 2 * BALL_RADIUS);
		
		 GObject collider = null; 
		if(ballCorner1 != null){
			collider = ballCorner1;
		} else if(ballCorner2 != null){
				collider = ballCorner2;
		} else if(ballCorner3 != null){
			collider = ballCorner3;
		} else if(ballCorner4 != null){
			collider = ballCorner4;}
		else{
			collider = null;
		}
		return collider;
		
	}
	
	private void checkForBottom(){
		if(ball.getY() + 2 * BALL_RADIUS > HEIGHT){
			pause(2000);
			ball.setLocation(WIDTH/2, HEIGHT/2);
			
			nturns-=1;
			
			if(nturns <= 0){
				remove(ball);
				GLabel end = new GLabel("You Lose!", (WIDTH-400)/2, (HEIGHT-100)/2);
				end.setFont("SansSerif-bold-96");
				end.setColor(R12Color);
				add(end);
				pause(2000);
				game = true;
				removeAll();
		}else{
			GLabel nextTurn = new GLabel("Lives Left: " + nturns, (WIDTH-400)/2, (HEIGHT-100)/2);
			nextTurn.setFont("SansSerif-bold-48");
			add(nextTurn);
			
			pause(2000);
			
			remove(nextTurn);
			for(int i = 3; i> 0; i --){
				
				GLabel count = new GLabel("" + i, (WIDTH-10)/2, (HEIGHT-100)/2 );
				count.setFont("SansSerif-bold-48");
				add(count);
				pause(1000);
				remove(count);
			}	
		}
		}
		}
			
	
	private void checkForEnd(){
		 if (nbricks ==0){
			GLabel win = new GLabel("You Win!", (WIDTH-400)/2, (HEIGHT-100)/2);
			win.setFont("SansSerif-bold-96");
			win.setColor(Color.green);
			add(win);
			pause(4000);
			game = true;
			removeAll();

		 }
			}
	
	private void goFaster(){
		if(nbricks == 50 && s1 == false){
			vy+=6;
			vx+=6;
			s1 = true;
		}
		if(nbricks == 25 && s2 == false){
			vy+=6;
			vx+=6;
			s2 = true;
		}
		if(nbricks == 10 && s1 == false){
			vy+=6;
			vx+=6;
			s3 = true;
		}
			
	}

	private int heightFromTop = BRICK_Y_OFFSET;
	private int widthFromSide = BRICK_SEP;
	
	private Color R12Color = Color.red;
	private Color R34Color = Color.orange;
	private Color R56Color = Color.yellow;
	private Color R78Color = Color.green;
	private Color R910Color = Color.cyan;

	private GObject gobj;
	
	private int nturns = NTURNS;
	private int nbricks = 100;
	
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private double vx, vy;
	
	boolean game;
	boolean s1 = false;
	boolean s2 = false;
	boolean s3 = false;
	
	GLabel count;
}
