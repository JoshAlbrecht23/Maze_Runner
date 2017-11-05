package Code;

import java.awt.Color;

public class Player {

	private int X_LOCATION ; 
	private int Y_LOCATION ;
	private int LENGTH ; 
	private Color COLOR ;
	
	public Player(int x, int y, int length, Color color){
		X_LOCATION = x ;
		Y_LOCATION = y ;
		LENGTH = length ;
		COLOR = color ;
	}
	
	//Gets
	public int get_x_location(){
		return X_LOCATION ;
	}
	
	public int get_y_location(){
		return Y_LOCATION ; 
	}
	
	public int get_length(){
		return LENGTH ; 
	}
	
	public Color get_color(){
		return COLOR ; 
	}
	
	//Sets
	public void set_x(int x){
		X_LOCATION = x ;
	}
	
	public void set_y(int y){
		Y_LOCATION = y ;
	}	
	
}
