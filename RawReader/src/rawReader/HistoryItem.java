package rawReader;

public class HistoryItem {
	
	public static final int ROTATE_RIGHT = 0;
	
	int actionType;
	int x;
	int y;
	int pixelValue;
	String filename;
	String description;
	
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
}
