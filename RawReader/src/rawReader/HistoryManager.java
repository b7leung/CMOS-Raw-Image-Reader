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
	Main guiHandler;
	
	public HistoryManager(Main guiHandler, RawImage image, JTextArea historyTextArea ){
		this.guiHandler = guiHandler;
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
			for(int i = amt - 1; i>currentPos; i--){
			//for(int i = currentPos + 1; i<amt; i++){
				actionHistory.remove(i);
				//historyTextArea.append(  );
			}
		}
	}
	public void undo(){
		HistoryItem previousAction = actionHistory.get(currentPos--);
		if(previousAction.actionType == previousAction.ROTATE_RIGHT){
			System.out.println("rotate left");
			guiHandler.rotateLeft();
		}
		
	}
	
	public void redo(){
		HistoryItem nextAction = actionHistory.get(++currentPos);
	}
	
	public int getAmt(){
		return amt;
	}
	
	public int getCurrentPos(){
		return currentPos;
	}
	
	private void parseAction(HistoryItem action){
		
	}
	

}
