package com.chessman;

import java.awt.Color;


public class Player{
		
		private int identify; //黑方白方标示，1代表黑方，2代表白方
		private ChessMan pChessMan; //棋子
		private String PlayerStringIdentify;//黑方白方
		
		public Player(int idetify,Color c,String sIdetify) {
			this.identify = idetify;
			pChessMan = new ChessMan(c);
			this.PlayerStringIdentify =  sIdetify;
		}
		int getCurrentIdentify(){
			return identify;
		}
		String getIdentify() {
			return PlayerStringIdentify;
		}
		void PerformChessman(){
			
		}
		Color getplayerChessManColor(){
			return pChessMan.getChessManColor();
		}
}
