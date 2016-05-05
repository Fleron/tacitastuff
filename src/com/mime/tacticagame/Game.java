package com.mime.tacticagame;

import java.awt.event.KeyEvent;

import com.mime.tacticagame.input.Controller;

public class Game {
	public int time;
	public Controller controls;
	
	public Game(){
		controls = new Controller();
	}
	
	public void tick(boolean[] key){
		time++;
		boolean forward = key[KeyEvent.VK_W];
		boolean back = key[KeyEvent.VK_S];
		boolean strafeLeft = key[KeyEvent.VK_Q];
		boolean strafeRight = key[KeyEvent.VK_E];
		boolean jump = key[KeyEvent.VK_SPACE];
		boolean crouch = key[KeyEvent.VK_CONTROL];
		boolean sprint = key[KeyEvent.VK_SHIFT];
		
		controls.tick(forward, back, strafeLeft, strafeRight,jump, crouch, sprint);
	}
}
