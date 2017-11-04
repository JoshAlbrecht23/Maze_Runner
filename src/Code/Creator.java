package Code;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

	private static Cell[][] cells ; 
	
	private Cell DECISION_CELL ;
	private Cell START_CELL ; 
	private Cell END_CELL ;
	
	private Deque<Cell> stack ;
	
	public Creator() {
	    init();
	}
		
	public void init(){
		
		Random rand = new Random();
			
		//Initialize window
		thePanel = new myJpanel() ; 
		
		this.add(thePanel) ; 
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Maze Creator");
        this.setResizable(true);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
       
        //Initialize stack
        stack = new ArrayDeque<Cell>() ;      
        
        //Initialize grid of cells
        cells  = new Cell[GRID_WIDTH_SIZE][GRID_HEIGHT_SIZE] ; 
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
                
        pack();
        repaint();
             
	}
		
   @Override
    public void paint(Graphics g) {
	   	   
        
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
        	g.fillRect(END_CELL.get_x_coordinate() + 2, END_CELL.get_y_coordinate() + 2, reduced_width, reduced_width);
    	}
    	
    }
	
 	public void actionPerformed(ActionEvent ev){
 		
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
 	
    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Creator().setVisible(true);
            }
            
        });
        

    } 

    class myJpanel extends JPanel  {
    	
    	public myJpanel(){
    		setPreferredSize(new Dimension((GRID_WIDTH_SIZE * CELL_WIDTH) + FRAME_BUFFER*2, (GRID_HEIGHT_SIZE * CELL_WIDTH) + FRAME_BUFFER*2));
    	} 
    	
    }
}



