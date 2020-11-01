# BOARDCOVER2

https://algospot.com/judge/problem/read/BOARDCOVER2

# 구현 방법
```
 i)   입력 받은 블록의 모양을 바탕으로 4가지 블록 모양을 만든다. ( 3번의 90° 회전 )
      이때 블록의 모양은 상대적인 좌표로 나타내고 (0,0) 은 항상 가장 윗줄, 가장 왼쪽 블록으로 한다.
 
 ii)  기준 좌표가 항상 가장 윗줄, 가장 왼쪽 블록으로 정해져 있기 때문에 같은 형태의 블록은 좌표가 똑같이 나올 수 밖에 없다.
      모양이 같은 경우는 첫 번째 블록과 두 번째 블록, 두 번째 블록과 네 번째 블록, 첫 번째 블록과 세 번째 블록이 같은 경우만 존재하므로 이 세 경우를 비교하며 중복되는 모양은 제거한다.
      
 iii) 블록의 모양을 모두 얻었으면 게임판의 빈 칸을 맨 윗줄, 맨 왼쪽 칸부터 차례대로 탐색하고 만약 빈 칸을 발견할 경우 그 빈칸에 블록을 놓을 수 있는지 체크한다.
      블록을 놓을 수 있는 경우 블록을 놓고 그 다음 빈 칸을 탐색한다. 블록을 놓을 수 없는 경우 이 빈칸을 막아버린다.
      
 iv)  (마지막 줄 - 블록의 가장 짧은 세로 길이) 줄의 맨 오른쪽 칸까지 탐색을 마친 후 나온 놓은 블록의 개수를 최선의 값으로 두고 이 이후의 탐색은 이 값을 기준으로 탐색을 진행한다.
      (마지막 줄 - 블록의 가장 짧은 세로 길이) 줄까지 탐색을 하는 이유는 그보다 아래 있는 줄은 빈 칸이 있어도 어차피 블록을 절대로 놓을 수 없기 때문이다.
 
 v)   (현재 놓은 블록의 수 + 남은 빈칸 / 블록의 칸 수) 가 현재 최선의 값보다 작거나 같은 경우 이 방향으로의 탐색은 의미가 없으므로 탐색을 중단한다.
      그렇지 않은 경우 탐색을 끝까지 진행하며 현재 최선의 값보다 놓은 블록의 개수가 클 경우 최선의 값을 교체한다.
 
 vi)  위의 과정을 반복하며 최선의 값을 찾는다.
```

# 구현 코드
```java
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
```
