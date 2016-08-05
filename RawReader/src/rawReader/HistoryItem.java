package rawReader;

import java.awt.Color;

public class HistoryItem {
	
	public static final int ROTATE_RIGHT = 0;
	
	private int actionType;
	private int x;
	private int y;
	private int pixelValue;
	private String filename;
	private String description;
	
	// used for changing pixel values
	public HistoryItem(int actionType, String description, int x, int y, int pixelValue){
		this.actionType = actionType;
		this.x = x;
		this.y = y;
		this.pixelValue = pixelValue;
		this.description = description;
	}
	
	// used for transformations
	public HistoryItem(int actionType, String description){
		this.actionType = actionType;
		this.description = description;
	}

	public int getActionType(){
	    return actionType;
	}

	public String getDescription(){
	    return description;
	}
}
