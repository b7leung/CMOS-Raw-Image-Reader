package rawReader;

import java.util.LinkedList;

import javax.swing.JTextArea;

public class HistoryManager {
	
	RawImage image;
	LinkedList<HistoryItem> actionHistory = new LinkedList<HistoryItem>();
	int amt = 0;
	int currentPos = -1;
	// position of last action
	JTextArea historyTextArea;
	
	public HistoryManager(RawImage image, JTextArea historyTextArea ){
		this.image = image;
		this.historyTextArea = historyTextArea;
	}
	
	public void log(HistoryItem action){
		
		historyTextArea.append(image.getFilename()+ " -- " +action.description+"\n");
		actionHistory.add(action);
		if(amt == currentPos+1){
			amt++;
			currentPos++;
		}else{
			currentPos++;
			for(int i = currentPos + 1; i<amt; i++){
				actionHistory.remove(i);
			}
		}
	}
	public void undo(){
		HistoryItem previousAction = actionHistory.get(--currentPos);
		if(previousAction.actionType == previousAction.ROTATE_RIGHT){
			//EditPictureUtils editor = new Ed;
		}
		
	}
	
	public void redo(){
		HistoryItem nextAction = actionHistory.get(++currentPos);
	}
	
	private void parseAction(HistoryItem action){
		
	}
	
	public void undoRotateRight(){
		
	}
}
