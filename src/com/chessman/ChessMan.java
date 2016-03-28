package com.chessman;

import java.awt.Color;

//¶¨ÒåÆå×Ó
	public class ChessMan{
		private static final int ChessManSize = 40;
		private Color ChessManColor;
		
		public ChessMan(Color c) {
			ChessManColor = c;
		}
		public static int getChessmansize() {
			return ChessManSize;
		}
		public Color getChessManColor() {
			return ChessManColor;
		}
}
