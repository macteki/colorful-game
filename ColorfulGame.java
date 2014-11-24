/******************************************************************************
* File : ColorfulGame.java
* Author : http://java.macteki.com/
* Description :
*   Step by Step walk-through for building a game like tetris
* To compile:
*   javac ColorfulGame.java
* To run:
*   java ColorfulGame
* Tested with : JDK 1.8
******************************************************************************/
 
// Step 1 : draw a green background
// Step 2 : draw a red cell with black border
// Step 3 : create a drawCell method()
// Step 4 : draw a token
// Step 5 : move the factor 24 into drawCell(), remove "*24" from caller
// Step 6 : create a drawToken() method
// Step 7 : create a eraseCell() method, create an occupied array
// Step 8 : modify drawCell() so that only fill the occupied array,
//          move the actual drawing code to paint()
// Step 9 : modify drawToken() to accept an array of relative position
// Step 10 : define rotation array of the first token
// Step 11 : define rotation array of the second token
// Step 12 : create global array to hold the rotation array
// Step 13 : create rotation array for all the seven tokens
// Step 14 : random token test
// Step 15 : add validation check
// Step 16 : add a falling token
// Step 17 : game over check
// Step 18 : add keyboard control
// Step 19 : erase filled rows
// Step 20 : shift down everything above a completed row
// Step 21 : add blinking effect, add score, add level
 
import java.awt.Color;

class ColorfulGame extends javax.swing.JPanel 
implements java.awt.event.KeyListener,
java.awt.event.ActionListener
{
  int[][] occupied = new int[10][20];
 
    // [seven tokens] [ four rotations ] [ four cells]
    static int[][][] xRotationArray = {
       { {0,0,1,2}, {0,0,0,1}, {2,0,1,2}, {0,1,1,1} },  // token number 0
       { {0,0,1,1}, {1,2,0,1}, {0,0,1,1}, {1,2,0,1} },  // token number 1
       { {1,1,0,0}, {0,1,1,2}, {1,1,0,0}, {0,1,1,2} },  // token number 2
       { {0,1,2,2}, {0,1,0,0}, {0,0,1,2}, {1,1,0,1} },  // token number 3
       { {1,0,1,2}, {1,0,1,1}, {0,1,1,2}, {0,0,1,0} },  // token number 4
       { {0,1,0,1}, {0,1,0,1}, {0,1,0,1}, {0,1,0,1} },  // token number 5
       { {0,1,2,3}, {0,0,0,0}, {0,1,2,3}, {0,0,0,0} }   // token number 6
    };
 
    static int[][][] yRotationArray = {
       { {0,1,0,0}, {0,1,2,2}, {0,1,1,1}, {0,0,1,2} },  // token number 0
       { {0,1,1,2}, {0,0,1,1}, {0,1,1,2}, {0,0,1,1} },  // token number 1
       { {0,1,1,2}, {0,0,1,1}, {0,1,1,2}, {0,0,1,1} },  // token number 2
       { {0,0,0,1}, {0,0,1,2}, {0,1,1,1}, {0,1,2,2} },  // token number 3
       { {0,1,1,1}, {0,1,1,2}, {0,0,1,0}, {0,1,1,2} },  // token number 4
       { {0,0,1,1}, {0,0,1,1}, {0,0,1,1}, {0,0,1,1} },  // token number 5
       { {0,0,0,0}, {0,1,2,3}, {0,0,0,0}, {0,1,2,3} }   // token number 6
    };

    // define a color index for each of the seven tokens
    static int[] colorCodeTable = {1,2,3,4,5,6,7}; 

    // map color index into actual color
    static Color colorTable[] = {Color.BLACK, // 0 => black
         Color.RED, // token type 1 => RED
         Color.GREEN, // token type 2 = GREEN
         Color.YELLOW, // token type 3 => YELLOW
         Color.PINK, // token type 4 => PINK
         Color.CYAN, // token type 5 => CYAN
         Color.BLUE, // token type 6 => BLUE
         Color.MAGENTA, // token type 7 => MAGENTA
    };
 
  java.awt.image.BufferedImage image;

  int score=0;  // score
  int lineCompleted = 0;   // number of lines completed
  int level=0;
 
  javax.swing.JLabel scoreLabel = new javax.swing.JLabel("SCORE : 0");
  javax.swing.JLabel levelLabel = new javax.swing.JLabel("LEVEL : 0");
 
  public void init()
  {
    this.setPreferredSize(new java.awt.Dimension(640,480));
    this.setBackground(java.awt.Color.GREEN);          
 
    this.setLayout(null);    // absolute coordinate system
    
    scoreLabel.setBounds(300,50,100,30);  // x,y,w,h (in pixels)
    this.add(scoreLabel);
 
    levelLabel.setBounds(300,100,100,30);
    this.add(levelLabel);
 
    image = new java.awt.image.BufferedImage(24*10,24*20,
      java.awt.image.BufferedImage.TYPE_INT_ARGB);

    javax.swing.Timer timer=new javax.swing.Timer(30,this);
    timer.start();
  }

  @Override //ActionListener
  public void actionPerformed(java.awt.event.ActionEvent e)
  {
    // handle timer event only
    if (e.getSource() instanceof javax.swing.Timer)
    {
      refreshPanel();
    }
  }

  void redrawImage()
  {
    if (image==null) return;
    java.awt.Graphics gr=image.getGraphics();
    for (int x=0;x<occupied.length;x++)
      for (int y=0;y<occupied[0].length;y++)
       {
          int colorCode=occupied[x][y];
          // draw black border
          gr.setColor(java.awt.Color.BLACK);
          gr.fillRect(x*24,y*24,24,24);
          // draw cell
          gr.setColor(colorTable[colorCode]);
          gr.fillRect(x*24+1,y*24+1,22,22);
        }
  }

  void refreshPanel()
  {
    // draw to off screen buffer
    redrawImage();

    // draw to screen
    java.awt.Graphics gr=this.getGraphics();
    if (gr==null) return;
    gr.drawImage(image,0,0,this);
  }
 
  public void drawCell(int x,int y,int colorCode)
  {
    occupied[x][y] = colorCode;
  }
 
  public void eraseCell(int x,int y)
  {
    occupied[x][y] = 0;
  }
 
  public void drawToken(int x, int y, int[] xArray, int[] yArray,int colorCode)
  {
    for (int i=0;i<4;i++)
    {
      drawCell(x+xArray[i],y+yArray[i],colorCode);
    }
  }
 
  public void eraseToken(int x, int y, int[] xArray, int[] yArray)
  {
    for (int i=0;i<4;i++)
    {
      eraseCell(x+xArray[i],y+yArray[i]);
    }
  }
 
  @Override // JPanel
  public void paint(java.awt.Graphics gr)
  {
    super.paint(gr);
    if (image!=null) gr.drawImage(image,0,0,this);
 
  }
 
  public boolean isValidPosition(int x,int y, int tokenNumber, int rotationNumber)
  {
    int[] xArray = xRotationArray[tokenNumber][rotationNumber];
    int[] yArray = yRotationArray[tokenNumber][rotationNumber];
     
    for (int i=0;i<4;i++)  // loop over the four cells 
    {
      int xCell = x+xArray[i];
      int yCell = y+yArray[i];
 
      // range check
      if (xCell<0) return false;
      if (xCell>=10) return false;
      if (yCell<0) return false;
      if (yCell>=20) return false;
 
      // occupancy check
      if (occupied[xCell][yCell]!=0) return false;
    }
    return true;
  }
 
  public void randomTokenTest()
  {
    try { Thread.sleep(1000); } catch (Exception ignore) {}
 
    int x,y,tokenNumber,rotationNumber;
 
    while (true)  // loop until position is valid
    {
      x=(int) (10*Math.random());    // random x: 0 to 9
      y=(int) (20*Math.random());    // random y: 0 to 19
 
      tokenNumber = (int) (7*Math.random());
      rotationNumber = (int) (4*Math.random());
 
      if (isValidPosition(x,y,tokenNumber,rotationNumber)) break;
    }
 
 
    int[] xArray = xRotationArray[tokenNumber][rotationNumber];
    int[] yArray = yRotationArray[tokenNumber][rotationNumber];
 
    drawToken(x,y,xArray,yArray,colorCodeTable[tokenNumber]);
    repaint();
  }
 
 
 
  public void clearCompleteRow(int[] completed)
  {
    // blinking : 1,2,3,4
    // color toggle : 1,0,1,0
    for (int blinking=1;blinking<5;blinking++)
    {
      for (int i=0;i<completed.length;i++)
      {
        if (completed[i]==1)
        {
          for (int x=0;x<10;x++)
          {
            // toggle the occupancy array
            occupied[x][i]=blinking%2;
          }
        }
      }
      repaint();
      try { Thread.sleep(100); } catch (Exception ignore) {}
    }
  }
 
  public void shiftDown(int[] completed)
  {
    for (int row=0;row<completed.length;row++)
    {
      if (completed[row]==1)
      {
        for (int y=row;y>=1;y--)
        {
          for (int x=0;x<10;x++)
          {
            occupied[x][y] = occupied[x][y-1];
          }
        }
      }
    }
  }
 
  public void checkRowCompletion()
  {
    int[] complete = new int[20];
    for (int y=0;y<20;y++)  // 20 rows
    {
      int filledCell = 0;
      for (int x=0;x<10;x++)  // 10 columns
      {
        if (occupied[x][y]!=0) filledCell++;
        if (filledCell==10) // row completed 
        {
          complete[y]=1;
        }
      }
    }
 
    clearCompleteRow(complete);
 
    shiftDown(complete);   
 
    addScore(complete);
  }
 
  void addScore(int[] complete)
  {
    int bonus=10;  // score for the first completed line
    for (int row=0;row<complete.length;row++)
    {
      if (complete[row]==1)
      {
        lineCompleted += 1;
        score+=bonus;
        bonus*=2;  // double the bonus for every additional line
      }
    }
 
    // advance level for every 3 completed lines
    level = lineCompleted/3;  
    if (level>30) { lineCompleted=0; level=0; }  // MAX LEVEL
 
    scoreLabel.setText("SCORE : "+score);
    levelLabel.setText("LEVEL : "+level);
  }
 
  boolean gameOver=false;
  public void addFallingToken()
  {
    int x=5,y=0;
    int tokenNumber, rotationNumber;
 
 
      tokenNumber = (int) (7*Math.random());
      rotationNumber = (int) (4*Math.random());
 
 
 
    int[] xArray = xRotationArray[tokenNumber][rotationNumber];
    int[] yArray = yRotationArray[tokenNumber][rotationNumber];
    int colorCode = colorCodeTable[tokenNumber];
 
    if (!isValidPosition(x,y,tokenNumber,rotationNumber)) 
    {
      gameOver=true;
      drawToken(x,y,xArray,yArray,colorCode);
      repaint();
      return;
    }
 
    drawToken(x,y,xArray,yArray,colorCode);
    repaint();
 
    int delay=50;  // mini second
    int frame=0;
    boolean reachFloor=false;
    while (!reachFloor)
    {
      try { Thread.sleep(delay); } catch (Exception ignore) {}
      eraseToken(x,y,xArray,yArray);
 
      // add keyboard control
      if (leftPressed && isValidPosition(x-1,y,tokenNumber,rotationNumber)) x -= 1;
      if (rightPressed && isValidPosition(x+1,y,tokenNumber,rotationNumber)) x += 1;
      if (downPressed && isValidPosition(x,y+1,tokenNumber,rotationNumber)) y += 1;
      if (spacePressed && isValidPosition(x,y,tokenNumber,(rotationNumber+1)%4)) 
      {
        rotationNumber = (rotationNumber+1)%4;
        xArray = xRotationArray[tokenNumber][rotationNumber];
        yArray = yRotationArray[tokenNumber][rotationNumber];
        spacePressed=false;  
      }
 
      int f=31-level;   // fall for every 31 frames, this value is decreased when level up
      if (frame % f==0) y += 1;  
      if (!isValidPosition(x,y,tokenNumber,rotationNumber)) // reached floor
      {
        reachFloor=true;
        y -= 1;  // restore position
      }
      drawToken(x,y,xArray,yArray,colorCode);
      repaint();
      frame++;
    }
 
  }
 
  public void printGameOver()
  {
    javax.swing.JLabel gameOverLabel = new javax.swing.JLabel("GAME OVER");
    gameOverLabel.setBounds(300,300,100,30);
    add(gameOverLabel);
    repaint();
  }
 
 
  boolean leftPressed=false;
  boolean rightPressed=false;
  boolean downPressed=false;
  boolean spacePressed=false;
 
  // must implements this method for KeyListener
  public void keyPressed(java.awt.event.KeyEvent event)
  {
//    System.out.println(event);
    if (event.getKeyCode()==37) // left arrow
    {
      leftPressed=true;
    }
    if (event.getKeyCode()==39) // right arrow
    {
      rightPressed=true;
    }
    if (event.getKeyCode()==40) // down arrow
    {
      downPressed=true;
    }
    if (event.getKeyCode()==32) // space
    {
      spacePressed=true;
    }
 
  }
 
  // must implements this method for KeyListener
  public void keyReleased(java.awt.event.KeyEvent event)
  {
//    System.out.println(event);
 
    if (event.getKeyCode()==37) // left arrow
    {
      leftPressed=false;
    }
    if (event.getKeyCode()==39) // right arrow
    {
      rightPressed=false;
    }
    if (event.getKeyCode()==40) // down arrow
    {
      downPressed=false;
    }
    if (event.getKeyCode()==32) // space
    {
      spacePressed=false;
    }
 
  }
 
  // must implements this method for KeyListener
  public void keyTyped(java.awt.event.KeyEvent event)
  {
//    System.out.println(event);
  }
 
 
  public static void main(String[] args) throws Exception
  {
    javax.swing.JFrame window = new javax.swing.JFrame("Macteki");
    window.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
 
    ColorfulGame game = new ColorfulGame();
    game.init();
 
    window.add(game);
    window.pack();
    window.setVisible(true);
 
    try { Thread.sleep(1000); } catch (Exception ignore) {}
 
    window.addKeyListener(game);  // listen to keyboard event
 
    game.gameOver=false;
    while (!game.gameOver)
    {
      game.addFallingToken();
      game.checkRowCompletion();
    }
 
    game.printGameOver();
 
  } 
 
}
