package Code;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Creator extends JFrame implements ActionListener{

    private myJpanel thePanel ;
	
	private static int CELL_WIDTH = 50 ;
	private static int GRID_WIDTH_SIZE = 10 ; 
	private static int GRID_HEIGHT_SIZE = 10 ; 	
	
	private static int FRAME_BUFFER = 20 ; 
	private static int FRAME_WIDTH = 300 ;
	private static int FRAME_HEIGHT = 300 ;
	
	private static Cell[][] cells ; 
	
	private Cell DECISION_CELL ;
	private Cell START_CELL ; 
	private Cell END_CELL ;
	private Cell FINISH_LINE_CELL ;
	
	private static int COUNTER = 0 ;
	private int[][] distFromStart ;
	
	private Player PLAYER ;
	
	private Deque<Cell> stack ;
	
	public Creator() {
	    init();
	    
	    while ( is_unvisited_cells() ){
	    	pick_frontier_cell();
	    }
	    
    	clearVisited() ;
    	fillDistanceGrid(START_CELL);
    	FINISH_LINE_CELL = getEndCell() ;
	    
	}
		
	public void init(){
		
		Random rand = new Random();
			
        addKeyListener(new TAdapter());
		
		//Initialize window
		thePanel = new myJpanel() ; 
		
		this.add(thePanel) ; 
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Maze Runner");
        this.setResizable(true);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
       
        //Initialize stack
        stack = new ArrayDeque<Cell>() ;      
        
        //Initialize grid of cells
        cells  = new Cell[GRID_WIDTH_SIZE][GRID_HEIGHT_SIZE] ; 
        distFromStart = new int [GRID_WIDTH_SIZE][GRID_HEIGHT_SIZE] ;

        //Create cells    
        for (int i=0 ; i < GRID_HEIGHT_SIZE ; i++ ){
        	for (int j=0 ; j < GRID_WIDTH_SIZE ; j++ ){
        		cells[i][j] = new Cell((i*CELL_WIDTH)+FRAME_BUFFER, (j*CELL_WIDTH)+FRAME_BUFFER*2) ;        		
        		cells[i][j].set_down(true) ;
        		cells[i][j].set_left(true) ;
        		cells[i][j].set_right(true) ;
        		cells[i][j].set_up(true) ;
        		cells[i][j].set_x(i);
        		cells[i][j].set_y(j);
        	}
        } 
       
        //Pick the starting cell in the first row.
        int  start_pos = rand.nextInt(GRID_WIDTH_SIZE);        
        DECISION_CELL = cells[start_pos][0] ;
        cells[start_pos][0].set_marked(true);
        
        //Init Start Cell
        START_CELL = new Cell(DECISION_CELL.get_x_coordinate(),DECISION_CELL.get_y_coordinate());  
        
        //Init player and set player at starting cell position.
        PLAYER = new Player(DECISION_CELL.get_x_coordinate() + 10, DECISION_CELL.get_y_coordinate() + 10, CELL_WIDTH/2, Color.GREEN);
                
        pack();
        repaint();
             
	}
		
   @Override
    public void paint(Graphics g) {
	   	
	    //Set the Camera Origin based on the players position.
        g.translate((PLAYER.get_x_location() - ( FRAME_WIDTH / 2 )) * -1, (PLAYER.get_y_location() - (FRAME_HEIGHT / 2)) * -1);
        //Clear the possible viewing area.
        g.clearRect(FRAME_WIDTH * -1,FRAME_HEIGHT * -1 , GRID_WIDTH_SIZE*CELL_WIDTH+(FRAME_WIDTH*2), GRID_HEIGHT_SIZE*CELL_WIDTH+(FRAME_HEIGHT*2));
        
    	//Draw the Cell border lines.
    	for (int i=0; i<cells.length; i++){
    		for (int j=0; j<cells.length; j++){
    			Cell myCell = cells[i][j] ; 
    		
    			//Fill rectangle with white/
    			g.setColor(Color.WHITE);
    			g.fillRect(myCell.get_x_coordinate(), myCell.get_y_coordinate(), CELL_WIDTH, CELL_WIDTH);
    			
    			//Draw cell borders
    	    	g.setColor(Color.BLACK);
    			
	    		if (myCell.get_down() == true){
		    		g.drawLine(myCell.get_x_coordinate(), myCell.get_y_coordinate() + CELL_WIDTH, myCell.get_x_coordinate() + CELL_WIDTH, myCell.get_y_coordinate() + CELL_WIDTH);
		    	}
		    	if (myCell.get_left() == true){
		    		g.drawLine(myCell.get_x_coordinate(), myCell.get_y_coordinate(), myCell.get_x_coordinate(), myCell.get_y_coordinate() + CELL_WIDTH);
		    	}
		    	if (myCell.get_right() == true){
		    		g.drawLine(myCell.get_x_coordinate()+CELL_WIDTH, myCell.get_y_coordinate(), myCell.get_x_coordinate()+CELL_WIDTH, myCell.get_y_coordinate() + CELL_WIDTH);
		    	}
		    	if (myCell.get_up() == true){
		    		g.drawLine(myCell.get_x_coordinate(), myCell.get_y_coordinate(), myCell.get_x_coordinate()+CELL_WIDTH, myCell.get_y_coordinate());
		    	}
    		}	
    	}
    	
    	//Draw Start
    	g.setColor(Color.BLUE);
    	
    	int reduced_width = (int) ((int) CELL_WIDTH - (CELL_WIDTH*.10)) ;
    	if( START_CELL != null ){
        	g.fillRect(START_CELL.get_x_coordinate() + 2, START_CELL.get_y_coordinate() + 2, reduced_width , reduced_width) ;
    	}
    	
    	//Draw End
    	if( END_CELL != null ){
        	
    		g.setColor(Color.white);
    		g.fillRect(END_CELL.get_x_coordinate() + 2, END_CELL.get_y_coordinate() + 2, reduced_width, reduced_width);
        	//clearVisited() ;
        	//fillDistanceGrid(START_CELL);
        	//FINISH_LINE_CELL = getEndCell() ;
        	
    	}
    	
    	//Draw Finish Line
    	if( FINISH_LINE_CELL != null ){
    		g.setColor(Color.RED);
    		g.fillRect(FINISH_LINE_CELL.get_x_coordinate() + 2, FINISH_LINE_CELL.get_y_coordinate() + 2, reduced_width, reduced_width);
    	}
    	
    	//Draw Player
    	if( PLAYER != null ){
        	g.setColor(PLAYER.get_color());
        	g.fillRect(PLAYER.get_x_location(), PLAYER.get_y_location(), PLAYER.get_length(), PLAYER.get_length());
    	}
    	        
    }
	
   	public boolean check_move(int x, int y){
   		boolean check = false ;
   		
   		Rectangle rect = new Rectangle(x,y,PLAYER.get_length(),PLAYER.get_length());
   		
   		for (int i=0; i<cells.length; i++){
    		for (int j=0; j<cells.length; j++){
    			Cell myCell = cells[i][j] ; 
    			    			
	    		if (myCell.get_down() == true){
	    			if ( rect.intersectsLine(new Line2D.Double(myCell.get_x_coordinate(), myCell.get_y_coordinate()+CELL_WIDTH, myCell.get_x_coordinate()+CELL_WIDTH,myCell.get_y_coordinate()+CELL_WIDTH))){
	    				return true ;
	    			}
		    	}
		    	if (myCell.get_left() == true){
	    			if ( rect.intersectsLine(new Line2D.Double(myCell.get_x_coordinate(), myCell.get_y_coordinate(), myCell.get_x_coordinate(),myCell.get_y_coordinate()+CELL_WIDTH))){
	    				return true ;
	    			}
		    	}
		    	if (myCell.get_right() == true){
	    			if ( rect.intersectsLine(new Line2D.Double(myCell.get_x_coordinate()+CELL_WIDTH, myCell.get_y_coordinate(), myCell.get_x_coordinate()+CELL_WIDTH,myCell.get_y_coordinate()+CELL_WIDTH))){
	    				return true ;
	    			}
		    	}
		    	if (myCell.get_up() == true){
	    			if ( rect.intersectsLine(new Line2D.Double(myCell.get_x_coordinate(), myCell.get_y_coordinate(), myCell.get_x_coordinate()+CELL_WIDTH,myCell.get_y_coordinate()))){
	    				return true ;
	    			}
		    	}
    		}	
    	}

   		return check ;
   	}
   
   	private void pick_frontier_cell(){
   		   		
   		ArrayList<Cell> front_cells = new ArrayList<Cell>(); 
   			
   		//Add current cell's neighbors
   		//Right
   		if (DECISION_CELL.get_x() != GRID_WIDTH_SIZE - 1 ){
   			if (cells[DECISION_CELL.get_x() + 1][DECISION_CELL.get_y()].is_marked() == false){
   	   			front_cells.add(cells[DECISION_CELL.get_x() + 1][DECISION_CELL.get_y()]);
   			}
   		}
   		//Left
   		if (DECISION_CELL.get_x() != 0 ){
   			if (cells[DECISION_CELL.get_x() - 1][DECISION_CELL.get_y()].is_marked() == false){
   	   			front_cells.add(cells[DECISION_CELL.get_x() - 1][DECISION_CELL.get_y()]);
   			}
   		}
   		//Down
   		if (DECISION_CELL.get_y() != GRID_HEIGHT_SIZE - 1 ){
   			if (cells[DECISION_CELL.get_x()][DECISION_CELL.get_y() + 1].is_marked() == false){
   	   			front_cells.add(cells[DECISION_CELL.get_x()][DECISION_CELL.get_y() + 1]);
   			}
   		}
   		//Up
   		if (DECISION_CELL.get_y() != 0 ){
   			if (cells[DECISION_CELL.get_x()][DECISION_CELL.get_y() - 1].is_marked() == false){
   	   			front_cells.add(cells[DECISION_CELL.get_x()][DECISION_CELL.get_y() - 1]);
   			}
   		}
   		  		
   		if (front_cells.isEmpty() == false){
   	   		//Push current cell to the stack
   	   		stack.push(DECISION_CELL);
   			
   			//Randomly choose a neighbor
   	   		Random rand = new Random();
   	   		int  rand_pick = rand.nextInt(front_cells.size());
   	   		
   	   		Cell rand_front_cell = front_cells.get(rand_pick) ; 
   	   		
   	   		//Remove the wall between the chosen cell and the current cell.
   	   		//Right
   	   		if (DECISION_CELL.get_x() + 1 == rand_front_cell.get_x() && DECISION_CELL.get_y() == rand_front_cell.get_y()){
   	   			cells[DECISION_CELL.get_x()][DECISION_CELL.get_y()].set_right(false);
   	   			cells[rand_front_cell.get_x()][rand_front_cell.get_y()].set_left(false);
   	   		//Left	
   	   		}else if (DECISION_CELL.get_x() - 1 == rand_front_cell.get_x() && DECISION_CELL.get_y() == rand_front_cell.get_y()){
   	   			cells[DECISION_CELL.get_x()][DECISION_CELL.get_y()].set_left(false);
   	   			cells[rand_front_cell.get_x()][rand_front_cell.get_y()].set_right(false);
   	   		//Down	
   	   		}else if (DECISION_CELL.get_x() == rand_front_cell.get_x() && DECISION_CELL.get_y() + 1 == rand_front_cell.get_y()){
   	   			cells[DECISION_CELL.get_x()][DECISION_CELL.get_y()].set_down(false);
   	   			cells[rand_front_cell.get_x()][rand_front_cell.get_y()].set_up(false);		
   	   		//Up
   	   		}else{
   	   			cells[DECISION_CELL.get_x()][DECISION_CELL.get_y()].set_up(false);
   	   			cells[rand_front_cell.get_x()][rand_front_cell.get_y()].set_down(false);	
   	   		}
   	   		
   	   		cells[rand_front_cell.get_x()][rand_front_cell.get_y()].set_marked(true);
   	   		DECISION_CELL = rand_front_cell ;
   	   		
   		}else{
   			if (stack.isEmpty() == false){
   	   			DECISION_CELL = stack.pop() ;
   			}
   		}
   		
   		
   	}
   
   	private boolean is_unvisited_cells(){
   		boolean b = false;
   		
   		for (int i=0; i<GRID_WIDTH_SIZE; i++){
   			for (int j=0; j<GRID_HEIGHT_SIZE; j++){
   				if ( cells[i][j].is_marked() == false)
   					return true ; 
   			}
   		}
   		
   		return b ;
   	}
 	
public void fillDistanceGrid( Cell currCell ){
   		
   		Cell myCell = cells[currCell.get_x()][currCell.get_y()] ;
   		   		
   		if( myCell.get_x() ==  START_CELL.get_x() && myCell.get_y() ==  START_CELL.get_y() ){
   			distFromStart[myCell.get_x()][myCell.get_y()] = 0 ;
   		}else if( distFromStart[myCell.get_x()][myCell.get_y()] == -1 ){
   			
   			COUNTER++;
   			distFromStart[myCell.get_x()][myCell.get_y()] = COUNTER ;
   		}else{
   			COUNTER = distFromStart[myCell.get_x()][myCell.get_y()] ;
   		}
   		
   		
   		if ( myCell.get_left() == false ){
   			if (distFromStart[myCell.get_x() - 1][myCell.get_y()] == -1)
   				stack.push(cells[myCell.get_x() - 1][myCell.get_y()]);
   		}
   			
   		if ( myCell.get_right() == false ){
   			if ( distFromStart[myCell.get_x() + 1][myCell.get_y()] == -1 )
   				stack.push(cells[myCell.get_x() + 1][myCell.get_y()]);
   		}
   		
   		if ( myCell.get_down() == false ){
   			if ( distFromStart[myCell.get_x()][myCell.get_y() + 1] == -1 )
   				stack.push(cells[myCell.get_x()][myCell.get_y() + 1]);
   		}
   				
   		if ( myCell.get_up() == false ){
   			if (distFromStart[myCell.get_x()][myCell.get_y() -1] == -1)
   				stack.push(cells[myCell.get_x()][myCell.get_y() - 1]);
   		}
   			
   		if ( stack.isEmpty() == false ){
   			Cell temp = stack.pop() ;
   			//Get new current count
   			COUNTER = getNewInt(temp);
   			fillDistanceGrid( temp ) ;
   		}
   	}
   	
public int getNewInt(Cell curr){
		int newCount = 0 ;
		int x = curr.get_x();
		int y = curr.get_y();
		
		/* Returns the cell's count based on the neighbor's count. */
		
		if ( distFromStart[x][y] == COUNTER )
			return COUNTER ;
		
		if ( curr.get_left() == false ){
			if (distFromStart[curr.get_x() - 1][curr.get_y()] != -1)
				newCount = distFromStart[curr.get_x() - 1][curr.get_y()] ;
		}
			
		if ( curr.get_right() == false ){
			if ( distFromStart[curr.get_x() + 1][curr.get_y()] != -1 )
				newCount = distFromStart[curr.get_x() + 1][curr.get_y()] ;
		}
		
		if ( curr.get_down() == false ){
			if ( distFromStart[curr.get_x()][curr.get_y() + 1] != -1 )
				newCount = distFromStart[curr.get_x()][curr.get_y() + 1] ;
		}
				
		if ( curr.get_up() == false ){
			if (distFromStart[curr.get_x()][curr.get_y() -1] != -1)
				newCount = distFromStart[curr.get_x()][curr.get_y() -1] ;
		}
		
		return newCount ;
	}
	
	public void clearVisited(){
		//Clear stack.
		stack.clear() ; 
		//Clear the marked flags.
		for (int i=0; i<GRID_WIDTH_SIZE; i++){
			for (int j=0; j<GRID_HEIGHT_SIZE; j++){
				cells[i][j].set_marked(false);
			}
		}
		//Fill the distance grid with -1
		for (int i=0; i<GRID_WIDTH_SIZE; i++){
			for (int j=0; j<GRID_HEIGHT_SIZE; j++){
				distFromStart[i][j] = -1 ;
			}
		}
	}
	
   	public Cell getEndCell(){
   		
   		Cell lastCell = null ;
   		
   		for ( int i=0; i<GRID_WIDTH_SIZE; i++ ){
   			for (int j=0; j<GRID_HEIGHT_SIZE; j++){
   				   				
   				if ( lastCell == null ){
   					lastCell = cells[i][j];
   				}else{
   					if ( distFromStart[i][j] > distFromStart[lastCell.get_x()][lastCell.get_y()] )
   						lastCell = cells[i][j] ;
   				}
   			}
   		}
   		
   		return lastCell ;
   	}

    public static void main(String[] args) {
    	
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Creator().setVisible(true);
            }
            
        });
    } 

    class myJpanel extends JPanel  {
    	public myJpanel(){
    		setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
    	} 	
    }
    
    private class TAdapter extends KeyAdapter {
    	
        @Override
        public void keyReleased(KeyEvent e) {
        	int keyCode = e.getKeyCode();

     	   if (keyCode == KeyEvent.VK_UP){
     		   if (!check_move(PLAYER.get_x_location(), PLAYER.get_y_location() - PLAYER.get_dy())){
     	        	PLAYER.keyReleased(e);
     		   }
     	   }
     	   if (keyCode == KeyEvent.VK_DOWN){
     		   if (!check_move(PLAYER.get_x_location(), PLAYER.get_y_location() + PLAYER.get_dy())){
     	        	PLAYER.keyReleased(e);
     		   }
     	   }
     	   if (keyCode == KeyEvent.VK_LEFT){
     		   if (!check_move(PLAYER.get_x_location() - PLAYER.get_dx(), PLAYER.get_y_location())){
     	        	PLAYER.keyReleased(e);
     		   }
     	   }
     	   if (keyCode == KeyEvent.VK_RIGHT){
     		   if (!check_move(PLAYER.get_x_location() + PLAYER.get_dx(), PLAYER.get_y_location() - PLAYER.get_dy())){
     	        	PLAYER.keyReleased(e);
     		   }
     	   }
        	
            repaint();
        }
        
        @Override
        public void keyPressed(KeyEvent e) {
        	int keyCode = e.getKeyCode();

     	   if (keyCode == KeyEvent.VK_UP){
     		   if (!check_move(PLAYER.get_x_location(), PLAYER.get_y_location() - PLAYER.get_dy())){
     	        	PLAYER.keyReleased(e);
     		   }
     	   }
     	   if (keyCode == KeyEvent.VK_DOWN){
     		   if (!check_move(PLAYER.get_x_location(), PLAYER.get_y_location() + PLAYER.get_dy())){
     	        	PLAYER.keyReleased(e);
     		   }
     	   }
     	   if (keyCode == KeyEvent.VK_LEFT){
     		   if (!check_move(PLAYER.get_x_location() - PLAYER.get_dx(), PLAYER.get_y_location())){
     	        	PLAYER.keyReleased(e);
     		   }
     	   }
     	   if (keyCode == KeyEvent.VK_RIGHT){
     		   if (!check_move(PLAYER.get_x_location() + PLAYER.get_dx(), PLAYER.get_y_location() - PLAYER.get_dy())){
     	        	PLAYER.keyReleased(e);
     		   }
     	   }
        	
        	repaint();
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}



