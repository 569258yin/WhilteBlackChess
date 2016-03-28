package com.chessman;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Label;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javafx.scene.layout.CornerRadii;


public class GameFram extends JFrame{

	private static int GRID_SIZE = 8;
	private static int GRID_WIDTH = 40; //棋盘格距地宽度
	private static int StartX = 20;
	private static int StartY = 100;
	private static final int Width = StartX*4+GRID_SIZE*GRID_WIDTH;
	private static final int Height= StartY*2+GRID_SIZE*GRID_WIDTH;
	private Image img,blackImage,WhilteImage;
	GameFram(){
		setTitle("黑白棋游戏");
		CenteredFrame();
		setSize(Width, Height);
		GamePanel Gpanel = new GamePanel();
		add(Gpanel);
		

	}

	void CenteredFrame(){
		//获取系统信息
		Toolkit kit = Toolkit.getDefaultToolkit();
		img = Toolkit.getDefaultToolkit().getImage("image/image.jpg");  
		blackImage = Toolkit.getDefaultToolkit().getImage("image/bcm.png"); 
		WhilteImage = Toolkit.getDefaultToolkit().getImage("image/wcm.png"); 
		//	    Image shadows = Toolkit.getDefaultToolkit().getImage("shadows.jpg");
		//将系统属性信息进行封装
		Dimension screenSize = kit.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		int Xposition = (screenWidth-Width)/2;
		int Yposition = (screenHeight-Height)/2;
		setLocation(Xposition,Yposition);
	}

	class GamePanel extends JPanel implements ActionListener{


		private int ChessSumCount = 0;
		private int BlackCount =0;
		private int WhiteCount = 0;
		private int si =0;
		private boolean SaveStatus = false;
		private int[][] save = new int[20][2];
		
		private Point cursor = new Point(60, 60); //棋盘坐标
		private int ChessManNumberX = Width/40;
		private int ChessManNumberY = (Height)/40;

		private int[][] saveCurrentChess = new int[ChessManNumberX][ChessManNumberY];
		private int[][] isChessSate = new int[ChessManNumberX][ChessManNumberY]; //目前棋盘能下的位置
		private int [][] ChessState = new int[ChessManNumberX][ChessManNumberY];//棋盘状态
		private int i=0;//横坐标
		private int j=0;//纵坐标
		private JTextField textBalck = new JTextField("", 5);//黑房文字提示框
		private JTextField textWhite = new JTextField("",5);
		private Label labelBlack = new Label("黑子：2");      //黑子实时计分文字提示框；
		private Label labelWhite = new Label("白子：2");
		private String Nothing = "";
		private String textblack = "请黑方线下子";
		private String textwhite = "请白方下子";
		private Player Black = new Player(1, Color.BLACK, "黑方");
		private Player White = new Player(2, Color.WHITE, "白方");
		private Player Cplayer = null; //当前用户的引用
		private Color backGroung = new Color(153, 217, 234);
		private int isCount = 4;
		public GamePanel() {
			setLayout(null);
			Initialization();//初始化画布
			setFocusable(true);
			setResizable(false);//禁止改变窗体大小

			setBackground(backGroung);//设置背景色为橘黄色
			//加入开始按钮
			
			JButton btnStart = new JButton("重新开始");
			btnStart.setBounds(20, 14, 100, 26);
			btnStart.addActionListener(this);
			add(btnStart);

			//加入黑方白方提示框
			textBalck.setHorizontalAlignment(JTextField.CENTER);
			textBalck.setBounds(150,14,110,26);
			textBalck.setEditable(false);
			add(textBalck);
			textWhite.setHorizontalAlignment(JTextField.CENTER);
			textWhite.setBounds(290,14,110,26);
			textWhite.setEditable(false);
			add(textWhite);
			//加入提示框
			labelBlack.setForeground(Color.RED);
			labelBlack.setBackground(Color.white);
			labelBlack.setBounds(20, 54, 110, 30);
			add(labelBlack);
			labelWhite.setBackground(Color.white);
			labelWhite.setForeground(Color.RED);
			labelWhite.setBounds(150, 54, 110, 30);
			add(labelWhite);

			ChessManMouseListener mlistener = new ChessManMouseListener();
			addMouseListener(mlistener);
			CheckAvalible();
			repaint();

		}


		private void Initialization() {
			for (int i = 0; i < ChessState.length; i++) {
				for (int j = 0; j < ChessState.length; j++) {
					ChessState[i][j] = 0;
					isChessSate[i][j] = 0;
				}
			}
			ChessState[3][3] =1;
			ChessState[4][4] =1;
			ChessState[3][4] =2;
			ChessState[4][3] =2;
			cursor.setLocation(60, 60);
			Cplayer = Black;
			textBalck.setText(textblack);
			textWhite.setText(Nothing);
//			CheckAvalible(); 
		}
		//画棋盘和初始化棋盘状态	
		protected void paintComponent(Graphics g){
			//绘制图形抽象类
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			// 			//设置五子棋的背景颜色
			// 			int imgWidth = img.getWidth(this);
			// 			int imageHieght = img.getHeight(this);
			// 			int x = (Width - imgWidth)/2;
			// 			int y = (Height - imageHieght)/2;
			// 			g2.drawImage(img, x, y, null);


			//画棋盘
			for (int i = 0; i < GRID_SIZE; i++) {
				int x = i*GRID_WIDTH+StartX;
				//					int y = j*GRID_WIDTH+StartY;
				int width = GRID_WIDTH;
				int height = GRID_SIZE*GRID_WIDTH;
				g2.drawRect(x,StartY, width, height);
			}
			for (int i = 0; i < GRID_SIZE; i++) {
				//				int x = i*GRID_WIDTH+StartX;
				int y = i*GRID_WIDTH+StartY;
				int width = GRID_SIZE*GRID_WIDTH;
				int height = GRID_WIDTH;
				g2.drawRect(StartX,y, width, height);
			}


			//画棋盘的外框

			g2.setColor(Color.RED);
			// 			g2.setFont(new Font("serif", Font.BOLD, 600));
			g2.drawRect(StartX,StartY
					,GRID_SIZE*GRID_WIDTH
					,GRID_SIZE*GRID_WIDTH);
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					
					g2.setPaint(Color.RED);
					
					if (isChessSate[i][j] == 1) {
						g2.drawOval(i*GRID_WIDTH+StartX+2, j*GRID_WIDTH+StartY+2
								,ChessMan.getChessmansize()-5
								, ChessMan.getChessmansize()-5);
				}
			}
			}
			for (int i = 0; i < ChessState.length; i++) {
				for (int j = 0; j < ChessState.length; j++) {
					
					if(ChessState[i][j] != 0){
						if (ChessState[i][j] == 1) {
							//							g2.drawImage(blackImage, i*GRID_WIDTH+StartX, j*GRID_WIDTH+StartY
							//							,ChessMan.getChessmansize()
							//							, ChessMan.getChessmansize(), null);
							g2.setPaint(Black.getplayerChessManColor());
						}
						//加载棋子图片
						if (ChessState[i][j] == 2) {
							//							g2.drawImage(WhilteImage, i*GRID_WIDTH+StartX, j*GRID_WIDTH+StartY
							//									,ChessMan.getChessmansize()
							//									, ChessMan.getChessmansize(), null);
							g2.setPaint(White.getplayerChessManColor());
						}
//						绘制圆
						g2.fillOval(i*GRID_WIDTH+StartX, j*GRID_WIDTH+StartY
									,ChessMan.getChessmansize()
									, ChessMan.getChessmansize());
					}
					
					
				}
			}
		}



		//判断当前落子后是否赢了
		void JudgeWin(){
			
			if (isFull()) {
				//提示当前玩家已经获胜
				
				if (BlackCount > WhiteCount) {
					ShowWinView("黑方获胜！");
				}
				else if(BlackCount < WhiteCount){
					ShowWinView("白方获胜！");
				}
				else {
					ShowWinView("平局！");
				}

			}
			//如果当前没有赢，双方交换
			//				count = 0;
			ChangeCurrentPlayer();
		}
		void ShowWinView(String message){
			try {
				String Ginformation = "Gameinformation";
				String Message = "恭喜玩家"+message
						+"\n"+"继续游戏，还是退出？";
				String [] options = {"继续","退出"};
				int selection = JOptionPane.showOptionDialog(null
						, Message, Ginformation
						, JOptionPane.YES_NO_OPTION
						, JOptionPane.INFORMATION_MESSAGE,null
						, options, options[0]);
				//继续游戏
				if (selection == JOptionPane.OK_OPTION) {
					reStart();
					return;
				}
				//退出程序
				if (selection == JOptionPane.NO_OPTION) {
					System.exit(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}		
		}
		//交换玩家
		private void ChangeCurrentPlayer() {
			if(Cplayer == Black){
				Cplayer = White;
				textBalck.setText(Nothing);
				textWhite.setText(textwhite);
			}
			else {
				Cplayer = Black;
				textBalck.setText(textblack);
				textWhite.setText(Nothing);
			}

		}
		//计算此时棋盘中黑白棋的个数
		void ChessCount(){
			BlackCount = 0;
			WhiteCount = 0;
			for (int i = 0; i < ChessState.length; i++) {
				for (int j = 0; j < ChessState.length; j++) {
					if (ChessState[i][j] == 1) {
						BlackCount++;
					}
					if (ChessState[i][j] == 2) {
						WhiteCount++;
					}
				}
			}
		}
		//计算此时棋盘中黑白棋的个数
				void ChessisCount(){
					isCount  = 0;
					
					for (int i = 0; i < isChessSate.length; i++) {
						for (int j = 0; j < isChessSate.length; j++) {
							if (ChessState[i][j] == 1) {
								isCount++;
							}
						}
					}
				}
		//记录落子后棋盘的当前的状态
		void RecordChessState(){
			// 			this.j = (cursor.x - 60)/GRID_WIDTH;
			// 			this.i = (cursor.y - 60)/GRID_WIDTH;
			ChessState[this.i][this.j] = Cplayer.getCurrentIdentify();
			ChessSumCount++;
		}

		//判断是否下满
		boolean isFull(){
			int sum = GRID_SIZE*GRID_SIZE;
			if (ChessSumCount >= sum - 4) {
				return true;
			}
			return false;
		}
		void ChangeChessSave(){
			for (int i = 0; i < si; i++) {
				ChessState[save[i][0]][save[i][1]] = Cplayer.getCurrentIdentify();
			}
		}
		//判断是否赢
		private void JudgeWinLine() {
			//定义下标
			int di,dj;
			int [] a = {-1,-1,-1,0,0,1,1,1};
			int [] b = {-1,0,1,-1,1,-1,0,1};
			//			int [] a = {-1,-1,-1,0,  1,1,1,0};
			//			int [] b = {-1,0,1,-1,   1,0,-1,1};	
			for (int i = 0; i < 8; i++) {
				di = this.i + a[i];
				dj = this.j + b[i];
				si = 0;
				SaveStatus =false;
				ChecekManCount(di,dj,a[i],b[i]);
				if (SaveStatus) {
					ChangeChessSave();
				}

			}

		}
		//递归判断棋子连线共有几个

		void ChecekManCount(int di,int dj,int i,int j){
			if (di >= 0 && di <ChessState.length && dj >= 0 && dj < ChessState.length) {	
				if(ChessState[di][dj] != Cplayer.getCurrentIdentify() && ChessState[di][dj] != 0){
					save[si][0] = di;
					save[si][1] = dj;
					si++;

					di = di + i;
					dj = dj + j;
					if (di >= 0 && di <ChessState.length && dj >= 0 && dj < ChessState.length){
						if (ChessState[di][dj] == Cplayer.getCurrentIdentify()) {
							SaveStatus = true;
							return;
						}
						else{
							ChecekManCount(di,dj,i,j);
						}
					}

				}
			}	
		}

		//判断棋盘当前位置是否有棋子
		boolean isCheckState(){
			if (ChessState[this.i][this.j] != 0) {
				return true;
			}
			else
				return false;
		}
		
//-------------------------------------------------------------------------------------
		
		
		void CheckAvalible(){
			
			int currentPlay = 0;
			if (Cplayer.getCurrentIdentify() == 1 ) {
				currentPlay= 2;
			}
			else if (Cplayer.getCurrentIdentify() == 2|| Cplayer.getCurrentIdentify() == 1 ) {
				currentPlay = 1;
			}
			for (int i = 0; i < ChessState.length; i++) {
				for (int j = 0; j < ChessState.length; j++) {
					if (ChessState[i][j] != 0) {
						saveCurrentChess[i][j] = ChessState[i][j];
					}
				}
			}
			
			for (int i = 0; i < ChessState.length; i++) {
				for (int j = 0; j < ChessState.length; j++) {
					
					if (ChessState[i][j] == 0) {
//						saveCurrentChess[i][j] = currentPlay;
						saveCurrentChess[i][j] = Cplayer.getCurrentIdentify();
					}
					if(JudgeWinCount(i,j)){
						isChessSate[i][j] = 1;
					}		
				}
			}
		}
		//判断是否赢
				@SuppressWarnings("unused")
				private boolean JudgeWinCount(int ci,int cj) {
					//定义下标
					int di,dj;
					int [] a = {-1,-1,-1,0,0,1,1,1};
					int [] b = {-1,0,1,-1,1,-1,0,1};

					for (int i = 0; i < 8; i++) {
						di = ci + a[i];
						dj = cj + b[i];
						si = 0;
						SaveStatus =false;
						ChecekManCount(di,dj,a[i],b[i]);
						if (SaveStatus) {
							return true;
						}
						
					}
					return false;

				}
		
		//递归判断棋子连线共有几个

		void ChecekAcalibleCount(int di,int dj,int i,int j){
			if (di >= 0 && di <saveCurrentChess.length && dj >= 0 && dj < saveCurrentChess.length) {	
				if(saveCurrentChess[di][dj] != Cplayer.getCurrentIdentify() && saveCurrentChess[di][dj] != 0){
					save[si][0] = di;
					save[si][1] = dj;
					si++;

					di = di + i;
					dj = dj + j;
					if (di >= 0 && di <saveCurrentChess.length && dj >= 0 && dj < saveCurrentChess.length){
						if (saveCurrentChess[di][dj] == Cplayer.getCurrentIdentify()) {
							SaveStatus = true;
							return;
						}
						else{
							ChecekManCount(di,dj,i,j);
						}
					}

				}
			}	
		}
		boolean pointAvalible(){
			this.i = (cursor.x )/GRID_WIDTH;
			this.j = (cursor.y )/GRID_WIDTH;
			if (isChessSate[this.i][this.j] ==1) {
				return true;
			}
			return false;
			
		}

	//----------------------------------------------------------------------------------	
		public class ChessManMouseListener implements MouseListener{

			@Override
			public void mouseClicked(MouseEvent e) {
				int x = (int) (Math.round((e.getX()-StartX)));
				int y = (int) (Math.round((e.getY()-StartY)));
				if (x >= 0 && x <=GRID_SIZE*GRID_WIDTH && y>=0 && y <=GRID_SIZE*GRID_WIDTH) {
					cursor.x = x;
					cursor.y = y;
					
					if (pointAvalible()) {  //判断该点是否符合规则
						if (!isCheckState()) {
							Cplayer.PerformChessman();
							RecordChessState();
							ChessCount();
							outString();
							JudgeWinLine();
							repaint();
							JudgeWin();
						}
					}
					
				}
				for (int i = 0; i < ChessState.length; i++) {
					for (int j = 0; j < ChessState.length; j++) {
						isChessSate[i][j] = 0;
					}
				}
				CheckAvalible();                  // bug 无法在双方都没有棋子的情况下，判断是否输赢、
				ChessisCount();
				if (isCount == 0) {
					//提示当前玩家已经获胜
					
					if (BlackCount > WhiteCount) {
						ShowWinView("黑方获胜！");
					}
					else if(BlackCount < WhiteCount){
						ShowWinView("白方获胜！");
					}
					else {
						ShowWinView("平局！");
					}
					
				}
				CheckAvalible();
//				repaint();
				repaint();
			}

			@Override
			public void mousePressed(MouseEvent e) {


			}

			@Override
			public void mouseReleased(MouseEvent e) {


			}

			@Override
			public void mouseEntered(MouseEvent e) {


			}

			@Override
			public void mouseExited(MouseEvent e) {


			}

		}
		@Override
		public void actionPerformed(ActionEvent e) {

			reStart();
			return;
		}  
		public void outString() {
			labelBlack.setText("黑子："+BlackCount);
			labelWhite.setText("白子："+WhiteCount);
		}
		//重新开始  
		public void reStart(){
			Initialization();
			si = 0;
			ChessSumCount =0;
			SaveStatus = false;
			BlackCount =2;
			WhiteCount = 2;
			outString();
			CheckAvalible();
			repaint();
		}

	}

}


