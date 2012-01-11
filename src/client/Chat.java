package client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Random;

import de.matthiasmann.twl.textarea.SimpleTextAreaModel;

import physics.Console;

/**
 * A stream that should print to an onscreen chat console.
 * TODO: all of this
 * 
 * @author kyle
 *
 */
public class Chat extends OutputStream {
	public SimpleTextAreaModel outModel;
	
	boolean isChatting;
	String out, in, line;
	Random r;

	public Chat(int size) {
		r = new Random();
		out = "";
		in = "";
		line = "";

		outModel = new SimpleTextAreaModel();
		outModel.setText(out);
		
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
