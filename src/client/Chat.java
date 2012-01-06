package client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Random;

import physics.Console;


import TWLSlick.RootPane;

import client.ui.Loop;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.textarea.SimpleTextAreaModel;

/**
 * A stream that should print to an onscreen chat console.
 * TODO: all of this
 * 
 * @author kyle
 *
 */
public class Chat extends OutputStream {
	SimpleTextAreaModel outModel;
	TextArea output;
	EditField input;
	ScrollPane sp;
	boolean isChatting;
	String out, in, line;
	Random r;
	Loop c;
	class chatback implements EditField.Callback {
		Net n;
		public chatback(Net n) {
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
	public Chat(Loop c, int size) {
		this.c = c;
		r = new Random();
		out = "";
		in = "";
		line = "";
		input = new EditField();
		input.addCallback(new chatback(c.net));
		
		output = new TextArea();
		outModel = new SimpleTextAreaModel();
		outModel.setText(out);
		output.setModel(outModel);
		sp = new ScrollPane(output);
		sp.setTheme("ChatWin");
	}
	
	public void createRootPane(RootPane rootPane) {
		input.setVisible(false);
		rootPane.add(input);
		rootPane.add(sp);
	}
	
	public void layoutRootPane(RootPane rootPane) {
		input.setSize(rootPane.getWidth(), 20);
    	input.setPosition(0, rootPane.getHeight()-20);
    	output.setSize(rootPane.getWidth(), 80);
    	output.setPosition(0, rootPane.getHeight()-100);
    	sp.setSize(rootPane.getWidth(), 80);
    	sp.setPosition(0, rootPane.getHeight()-100);
	}
	
	public void keyPressed(int code, char key) {
		if (code == c.settings.CHAT) {
			input.setVisible(true);
			input.requestKeyboardFocus();
		}
	}
	
	public static void openWebSight(String u) {
		final String url = u;
		new Thread () {
			public void run() {
				try {
					URI sight = new URI(url);
					java.awt.Desktop.getDesktop().browse(sight);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	@Override
	public void write(int b) throws IOException {
		char c = (char) b;
		out += c;
		line += c;
		outModel.setText(out);
		if (c == '\n') {
			if (line.contains("ytraw{")) {
				String st = line.split("ytraw\\{")[1].split("\\}")[0];
				openWebSight("http://www.youtube.com/apiplayer?video_id=" + st + "&version=3&autoplay=1");
			}
			/* This is the feature I am most proud of. */
			if (line.contains("chocolate") && !line.contains("\\Did")) {
				Console.chat.println("\\Did you say CHOCOLATE?");
				openWebSight("http://www.youtube.com/apiplayer?video_id=8LXinl_vP90");
			}
			if (line.contains("gogogo")) {
				openWebSight("http://www.youtube.com/apiplayer?video_id=1EKTw50Uf8M&version=3&autoplay=1");
			}
			line = "";
		}
		
		
		
		/*
		if (c == '\n') {
			Console.dbg.println(out);
//			for (int i = labels.length-1; i > 0 ; --i) {
//				Textlabel oldL = labels[i], newL = labels[i-1];
//				oldL.setStringValue(newL.getStringValue());
//				oldL.setColor(newL.getColor());
//			}
//			labels[0].setStringValue(out);
			int color = 0xffffff;
			if (out.contains(">")) {
				Console.dbg.println("VLAD THE IMPLIER");
				color = 0x00ff00;
			} else if (out.contains("]")) {
				//Orange team only
			} else if (out.contains("}")) {
				//Blue team only
			} else if (out.charAt(0) == '\\') {
				//This is a system message.
//				labels[0].setStringValue(out.substring(1));
			}
//			labels[0].setColorValueLabel(color);
			out = "";
		} else {
			out += c;
		}
		*/
	}
}
