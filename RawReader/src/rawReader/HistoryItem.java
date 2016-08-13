package rawReader;

import java.awt.Color;

public class HistoryItem {
	
	public static final int SELECT_IMAGE = 0;
	public static final int ROTATE_RIGHT = 1;
	
	private int actionType;
	private int x;
	private int y;
	private int pixelValue;
	private String filename;
	private String description;
	private boolean redo;
	
	// used for changing pixel values
	public HistoryItem(int actionType, String description, int x, int y, int pixelValue, boolean redo){
	    this.redo = redo;
		this.actionType = actionType;
		this.x = x;
		this.y = y;
		this.pixelValue = pixelValue;
		this.description = description;
	}
	
	// used for transformations
	public HistoryItem(int actionType, String description, boolean undo){
		this.actionType = actionType;
		this.description = description;
	}

	public int getActionType(){
	    return actionType;
	}

	public String getDescription(){
	    return description;
	}
	
	public boolean isRedo(){
	    return redo;
	}
}
