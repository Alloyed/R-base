package client;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PConstants;

import controlP5.*;
/* 
 * Allows the Client to make and print chat messages.
 * TODO: escape chars for line color, moveable insert point, maybe make messages expire
 */
public class Chat extends OutputStream {
	Textlabel labels[];
	Textlabel input;
	boolean isChatting;
	String out, in;
	Random r;
	PApplet p;
	
	public Chat(Client c, int size) {
		p = c.p;
		labels = new Textlabel[size];
		r = new Random();
		for (int i = 0; i < labels.length; ++i) {
			Textlabel l = c.gooey.addTextlabel("chatlabel-"+i, " ", 110, c.p.height-40-(10*i));
			labels[i] = l;
		}
		input = c.gooey.addTextlabel("inlabel", "", 110, c.p.height-30);
		out = "";
		in = "";
	}
	
	public void keyPressed() {
		char key = p.key;
		if (isChatting) {
			if (key == PConstants.ENTER || key == PConstants.RETURN) {
				physics.Console.chat("me", 2, in.substring(1));
				in = "";
				input.setStringValue(in);
				isChatting = false;
			} else if (key == PConstants.BACKSPACE || key == PConstants.DELETE) {
				in = in.substring(0, in.length()-1);
				input.setStringValue(in+"_");
			} else if (key > 31 && key != PConstants.CODED) {
				in += key;
				input.setStringValue(in+"_");
			}
		} else if (key == 't') { 
			in += '>';
			input.setStringValue(in+"_");
			isChatting = true;
		}	
	}
	
	@Override
	public void write(int b) throws IOException {
		char c = (char) b;
		if (c == '\n') {
			for (int i = labels.length-1; i > 0 ; --i) {
				Textlabel oldL = labels[i], newL = labels[i-1];
				oldL.setStringValue(newL.getStringValue());
				oldL.setColor(newL.getColor());
			}
			labels[0].setStringValue(out);
			labels[0].setColorValueLabel(r.nextInt(0xffffff)); //Fabulous!
			out = "";
		} else {
			out += c;
		}
	}

}