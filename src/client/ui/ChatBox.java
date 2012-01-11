package client.ui;

import TWLSlick.RootPane;
import client.CNet;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.TextArea;

public class ChatBox {
	TextArea output;
	EditField input;
	ScrollPane sp;
	
	class chatback implements EditField.Callback {
		CNet n;
		public chatback(CNet n) {
			this.n = n;
		}
		@Override
		public void callback(int evt) {
			if (evt == Event.KEY_RETURN) {
				input.setVisible(false);
				n.say(input.getText());
				input.setText("");
			}
		}
	}
	
	public ChatBox(Loop c) {
		input = new EditField();
		input.addCallback(new chatback(c.net));
		
		output = new TextArea();
		
		output.setModel(c.chat.outModel);
		sp = new ScrollPane(output);
		sp.setTheme("ChatWin");
	}
	
	public void add(RootPane rootPane) {
		input.setVisible(false);
		rootPane.add(input);
		rootPane.add(sp);
	}
	
	public void startChat() {
		input.setVisible(true);
		input.requestKeyboardFocus();
	}
	
	public void layout(RootPane rootPane) {
		input.setSize(rootPane.getWidth(), 20);
    	input.setPosition(0, rootPane.getHeight()-20);
    	output.setSize((int)(rootPane.getWidth()*.75), 60);
    	output.setPosition(0, rootPane.getHeight()-100);
    	sp.setSize(rootPane.getWidth(), 80);
    	//sp.adjustSize();
    	sp.setPosition(0, rootPane.getHeight()-100);
	}
}
