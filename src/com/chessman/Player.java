package com.chessman;

import java.awt.Color;


public class Player{
		
		private int identify; //�ڷ��׷���ʾ��1����ڷ���2����׷�
		private ChessMan pChessMan; //����
		private String PlayerStringIdentify;//�ڷ��׷�
		
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
