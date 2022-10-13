import io.jbotsim.core.Node;
import io.jbotsim.core.Color;
import io.jbotsim.ui.icons.Icons;
import java.util.*;
import java.lang.*;
import io.jbotsim.core.Point;


public class ColorNode extends Node{
	double step = 1;
	Point destination = new Point(Math.random()*400, Math.random()*400);

	// array of predefined jbotsim colors
	private static final Color all_colors[] = {Color.black, Color.white, Color.gray, Color.
	lightGray, Color.pink, Color.red, Color.darkGray, Color.orange, Color.yellow,
	Color.magenta, Color.cyan, Color.blue, Color.green };
	private static final int nb_colors = all_colors.length;

	// the node current color
	private int color = 0; 

	// Convert internal color to jBotSim color
	Color toColor(int i) {
		return all_colors[i%nb_colors];
	}

	// change node color and adjust graphics
	void setColor(int i) {
		color = i%nb_colors;
		setColor(toColor(i));
	}

	// returns true if the node has a color conflict with a neighbor
	boolean isConflict(List<Node>l) {
		for(Node node: l) {
			if(color == ((ColorNode)node).color){
				return true;
			}
		}
		return false;
	}


	// finds a new color for the node among available colors
	// if the node has more neighbors than max nb of colors, the node may not change its color
	void findColor(List<Node>l) {
		
		//Selecting color modulo size of color list
		/*
		int last = color;
		while(isConflict(l)) {
			color += 1 % nb_colors;
			if(color == last) {
				break;
			}
		}
		*/
		
		//Selecting random color
		/*
		Random ran = new Random();
		color = ran.nextInt(nb_colors);
		*/
		
		
		//Selecting the minimal available color
		color = 0;
		for(int i=0; i < nb_colors; i++){
			if(isConflict(l)) color++;
			else if (isConflict(l) && color == nb_colors -1) {color = 0; break;}
			else break;
		}
		
		setColor(color);
	}

	@Override
	public void onStart() {
		// JBotSim executes this method on each node upon initialization
		this.setProperty("byzantine", false);
		setColor(0);
	}

	@Override
	public void onSelection() {
		// JBotSim executes this method on a selected node (middle-click)
		this.setProperty("byzantine",!(boolean)this.getProperty("byzantine"));
	}
		
	@Override
	public void onClock() {
		// JBotSim executes this method on each node in each round
		if((boolean)this.getProperty("byzantine") == false && isConflict(getNeighbors())){
			findColor(getNeighbors());
		}
		else if((boolean)this.getProperty("byzantine") == true && isConflict(getNeighbors()) == false ){
			
			if(this.getNeighbors().size()!=0){
				Random randomizer = new Random();
				Node random = this.getNeighbors().get(randomizer.nextInt(this.getNeighbors().size()));
				color = ((ColorNode)random).color;
				setColor(color);
			}
		}
	}
	
	@Override
	public void onPreClock() {
		if((boolean)this.getProperty("byzantine")){
			setDirection(destination);
			if (distance(destination) > step)
				move(step);
			else {
				move(distance(destination));
				destination = new Point(Math.random()*600, Math.random()*600);
			}
		}
	}
}
