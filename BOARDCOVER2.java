import java.util.Scanner;
import java.util.Arrays;
public class BOARDCOVER2 {

	static int[][] board;
	static int[][] cover1;
	static int[][] cover2;
	
	static int[][] xy1;
	static int[][] xy2;
	static int[][] xy3;
	static int[][] xy4;
	
	static int[] check;
	
	static int best;
	static int boardnc;
	static int coverc;
	static int blockcnt;
	static int minx;
	static int T;
	static int H;
	static int W;
	static int R;
	static int C;
	
	public static void main(String args[])
	{
		Scanner sc = new Scanner(System.in);
		
		T = sc.nextInt();
		
		for(int k = 0; k < T; k++)
		{
			H = sc.nextInt();
			W = sc.nextInt();
			R = sc.nextInt();
			C = sc.nextInt();
			
			board = new int[H][W];
			cover1 = new int[R][C];
			cover2 = new int[C][R];
			check = new int[4];
			
			boardnc = 0;
			coverc = 0;
			
			for(int i = 0; i < H; i++)
			{
				String s = sc.next();
				
				for(int j = 0; j < W; j++) 
				{
					if(s.charAt(j) == '#')
						board[i][j] = 1;
					else
					{
						board[i][j] = 0;
						boardnc++;
					}
				}
			}
			
			for(int i = 0; i < R; i++)
			{
				String s = sc.next();
				
				for(int j = 0; j < C; j++) 
				{
					if(s.charAt(j) == '#')
					{
						cover1[i][j] = 1;
						coverc++;
					}
					else
						cover1[i][j] = 0;
					
				}
			}
	
			xy1 = new int[coverc][2];
			xy2 = new int[coverc][2];
			xy3 = new int[coverc][2];
			xy4 = new int[coverc][2];
			
			minx = 0;
			
			get_rotation(R,C);
			
			blockcnt = 0;
			best = 0;
			
			search(0, 0);
			
			System.out.println(best);
		}
	}
	
	public static void rot1(int[][] cv, int R, int C)
	{
		for(int i = 0; i < R; i++)
		{
			for(int j = 0; j < C; j++)
			{
				cover2[j][R - i - 1] = cv[i][j];
			}
		}
	}
	
	public static void rot2(int[][] cv, int R, int C)
	{
		for(int i = 0; i < R; i++)
		{
			for(int j = 0; j < C; j++)
			{
				cover1[j][R - i - 1] = cv[i][j];
			}
		}
	}
	
	public static void get_rotation(int R, int C)
	{
		get_block(cover1, xy1, R, C);
		
		sorting(xy1);
		
		minx = Math.min(xy1[coverc-1][0], minx);
		
		rot1(cover1,R,C);
		
		get_block(cover2, xy2, C, R);
		
		sorting(xy2);
		
		minx = Math.min(xy2[coverc-1][0], minx);
		
		rot2(cover2,C,R);
		
		get_block(cover1, xy3, R, C);
		
		sorting(xy3);
		
		minx = Math.min(xy3[coverc-1][0], minx);
		
		rot1(cover1,R,C);
		
		get_block(cover2, xy4, C, R);
		
		sorting(xy4);
		
		minx = Math.min(xy4[coverc-1][0], minx);
		
		comp(xy1, xy2, 1);
		
		comp(xy1, xy3, 2);
		
		comp(xy2, xy4, 3);
	}
	
	public static void get_block(int[][] cover, int[][] xy, int a, int b)
	{
		int point = 0;
		
		int X = -1, Y = -1;
		
		for(int i = 0; i < a; i++)
		{
			for(int j = 0; j < b; j++)
			{
				if(cover[i][j] == 1)
				{
					if(Y == -1)
					{
						Y = i;
						X = j;
					}
					
					xy[point][0] = i - Y;
					xy[point++][1] = j - X;
				}
			}
		}
	
	}
	
	public static void sorting(int[][] xy)
	{
		Arrays.sort(xy, (o1,o2) -> {
			if(o1[0] == o2[0])
				return Integer.compare(o1[1], o2[1]);
			else
				return Integer.compare(o1[0], o2[0]);
		});
	}
	
	public static void comp(int[][] xy1, int[][] xy2, int ck)
	{
		int cnt = 0;
		
		for(int i = 0; i < coverc; i++)
		{
			if(xy1[i][0] == xy2[i][0] && xy1[i][1] == xy2[i][1]) cnt++;
		}
		
		if(cnt == coverc) check[ck] = 1;
	}
	
	public static void search(int x, int y)
	{	
		if (boardnc < coverc) return;
		
		int a = boardnc/coverc;
		
		if(best >= a + blockcnt) return;
		
		for(int i = x; i < H - minx; i++)
		{
 			for(int j = 0; j < W; j++)
			{
     			if(i == x && j == 0) 
     				j += y;
				
 				if(board[i][j] == 0)
				{
					set(i,j,xy1);
					
 					if(check[1] == 0)
						set(i,j,xy2);
					
					if(check[2] == 0)
 						set(i,j,xy3);
					
					if(check[3] == 0)
						set(i,j,xy4);
					
					board[i][j] = 1;
					boardnc--;
					search(i, j);
					boardnc++;
					board[i][j] = 0;
					
					return;
				}
			}
		}
	}
	
	public static void set(int i, int j, int[][] xy)
	{
		int k;
		int a;
		int b;
		
		for(k = 0; k < coverc; k++)
		{
			a = i + xy[k][0];
			b = j + xy[k][1];
			
			if(a < H && b >= 0 && b < W && board[a][b] == 0)
				continue;
			else
				break;
		}
		
		if(k == coverc)
		{
			boardnc -= coverc;
			blockcnt++;
			best = Math.max(best,blockcnt);
			cov(i,j,xy);
			search(i, j);
			recov(i,j,xy);
			blockcnt--;
			boardnc += coverc;
		}
		
	}
	
	public static void cov(int i, int j, int[][] xy)
	{
		for(int k = 0; k < coverc; k++)
		{
			board[i + xy[k][0]][j + xy[k][1]] = 1;
		}
	}
	public static void recov(int i, int j, int[][] xy)
	{
		for(int k = 0; k < coverc; k++)
		{
			board[i + xy[k][0]][j + xy[k][1]] = 0;
		}
	}
}
