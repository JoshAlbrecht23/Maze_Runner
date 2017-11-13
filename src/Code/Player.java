package Code;

import java.awt.Color;
import java.awt.event.KeyEvent;

public class Player {

	private int X_LOCATION ; 
	private int Y_LOCATION ;
	private int LENGTH ; 
	private Color COLOR ;
	private int dx = 4 ;
	private int dy = 4 ;
	
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
	
	public int get_dx(){
		return dx ;
	}
	
	public int get_dy(){
		return dy ;
	}
	
	//Sets
	public void set_x(int x){
		X_LOCATION = x ;
	}
	
	public void set_y(int y){
		Y_LOCATION = y ;
	}	
	
	
   public void keyReleased(KeyEvent e) {
	   
	   int keyCode = e.getKeyCode();

	   if (keyCode == KeyEvent.VK_UP){
		   set_y(get_y_location() - get_dy()); 
	   }
	   if (keyCode == KeyEvent.VK_DOWN){
           set_y(get_y_location() + get_dy()); 
	   }
	   if (keyCode == KeyEvent.VK_LEFT){
		   set_x(get_x_location() - get_dx()); 
	   }
	   if (keyCode == KeyEvent.VK_RIGHT){
           set_x(get_x_location() + get_dx());
	   }
	} 
	
}
