package client;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import org.newdawn.slick.Input;

import client.ui.Loop;
import de.matthiasmann.twl.TextArea;
import physics.Console;
import physics.actors.Actor;

/**
 * A stream that should print to an onscreen chat console.
 * TODO: all of this
 * 
 * @author kyle
 *
 */
public class Chat extends OutputStream {
	//Textlabel labels[];
	//Textlabel input;
	boolean isChatting;
	String out, in;
	Random r;
	Loop c;
	//PApplet p;
	
	public Chat(Loop c, int size) {
		this.c = c;
		//p = c.p;
		//labels = new Textlabel[size];
		r = new Random();
		/*
		for (int i = 0; i < labels.length; ++i) {
			Textlabel l = c.gooey.addTextlabel("chatlabel-"+i, 
												" ", 
												110, 
												c.p.height-40-(10*i));
			labels[i] = l;
		}
		input = c.gooey.addTextlabel("inlabel", "", 110, c.p.height-30);
		*/
		out = "";
		in = "";
	}

	public void keyPressed(int code, char key) {
		/*
		if (isChatting) {
			Console.dbg.print(key);
			if (key == '\n') {
				String name = ((Actor)c.stage.get(0)).label;
				Console.out.println("I AM DONE CHATY");
				//c.net.call(c.stage, "chat", 
				//		new Object[] {name, System.currentTimeMillis(), in});
				in = "";
				//input.setStringValue(in);
				isChatting = false;
			} else if (in.length() > 0 && 
					code == Input.KEY_BACK) {
				in = in.substring(0, in.length()-1);
				//input.setStringValue("> "+in+"_");
			} else if (key > 31) {
				in += key;
				//input.setStringValue("> "+in+"_");
			}
		} else if (code == c.settings.CHAT) { 
			in = "";
//			input.setStringValue("> "+in+"_");
			isChatting = true;
			System.out.println("SO CHATY");
		}
		*/
	}

	@Override
	public void write(int b) throws IOException {
		char c = (char) b;
		
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
		
	}
}
