package Code;

public class Cell {

	private int X_COORDINATE ; 
	private int Y_COORDINATE ;
	
	private int x ; 
	private int y ;
	
	private boolean[] Walls ; 
	private boolean marked ;
	private boolean frontier_cell ;
	
	public Cell (int x, int y){
		
		X_COORDINATE = x ; 
		Y_COORDINATE = y ; 
		Walls = new boolean[4] ;
		//Initialize all walls to be filled (True).
		//Left Wall
		Walls[0] = true ; 
		//Up Wall
		Walls[1] = true ; 
		//Right Wall
		Walls[2] = true ; 
		//Down Wall
		Walls[3] = true ; 

		marked = false ; 
		frontier_cell = false ; 
	}
	
	//Set Methods
	public void set_left(boolean myBool){
		Walls[0] = myBool ; 
	}
	
	public void set_up(boolean myBool){
		Walls[1] = myBool ;
	}
	
	public void set_right(boolean myBool){
		Walls[2] = myBool ;
	}
	
	public void set_down(boolean myBool){
		Walls[3] = myBool ;
	}
	
	public void set_marked(boolean m){
		marked = m ;
		frontier_cell = false ;
	}
	
	public void set_frontier(boolean m){
		frontier_cell = m ; 
	}
	
	public void set_x(int m){
		x = m;
	}
	
	public void set_y(int m){
		y = m ;
	}
	
	//Get Methods
	public boolean get_left(){
		return Walls[0] ; 
	}
	
	public boolean get_up(){
		return Walls[1] ;
	}
	
	public boolean get_right(){
		return Walls[2] ;
	}
	
	public boolean get_down(){
		return Walls[3] ;
	}
	
	public int get_x_coordinate(){
		return X_COORDINATE ; 
	}
	
	public int get_y_coordinate(){
		return Y_COORDINATE ; 
	}
	
	public int get_x(){
		return x ;
	}
	
	public int get_y(){
		return y ; 
	}
	
	public boolean is_marked(){
		return marked ; 
	}
	
	public boolean is_frontier(){
		return frontier_cell ; 
	}
	
}
