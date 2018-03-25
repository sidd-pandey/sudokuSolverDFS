import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SudokuSolver {

	public static final int m = 3;
	public static final int n = m * m;
	
	public static void main(String[] args){
		new SudokuSolver().run();
	}
	
	public void run(){
		int[][] sudoku = readSudokuFromFile("ex1.txt");
		System.out.println("Problem sudoku: ");
		print(sudoku);
		int[][] solvedSudoku = solve(sudoku);
		System.out.println("Solution: ");
		print(solvedSudoku);
				
	}
	
	private int[][] solve(int[][] sudoku){
		
		if(isDone(sudoku)) return sudoku;
		else{
			Cell c = nextCell(sudoku);
			if(c != null){
				List<Integer> possibilities = getPossibilities(c.x, c.y, sudoku);
				for (Integer val : possibilities){
					int[][] newSudoku = copy(sudoku);
					newSudoku[c.x][c.y] = val;
					int[][] sol = solve(newSudoku);
					if(sol != null) return sol;
				}
			}
		}
		return null;
	}
	
	private boolean isDone(int[][] sudoku){
		if(sudoku == null) return false;
		
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				if(sudoku[i][j] == 0) return false;
				if(rowCnt(i, j, sudoku) == 0 && colCnt(i, j, sudoku)==0
						&& sqCnt(i, j, sudoku)==0)
					continue;
				else return false;
			}
		}
		return true;
	}
	
	private List<Integer> getPossibilities(int r, int c, int[][] sudoku){
		Set<Integer> rowVals = new HashSet<>();
		for(int i = 0; i < n ; i++){
			if(sudoku[r][i] != 0) rowVals.add(sudoku[r][i]);
		}
		
		Set<Integer> colVals = new HashSet<>();
		for(int i = 0; i < n ; i++){
			if(sudoku[i][c] != 0) colVals.add(sudoku[i][c]);
		}
		
		Set<Integer> sqVals = new HashSet<>();
		int rlow = r - r%m, rup = rlow+3;
		int clow = c - c%m, cup = clow+3;

		for(int i = rlow; i < rup; i++){
			for(int j = clow; j < cup; j++){
				if(sudoku[i][j] != 0) sqVals.add(sudoku[i][j]);
			}
		}
				
		HashSet<Integer> allVals = new HashSet<>();
		for(int i = 1; i <= n ; i++){
			allVals.add(i);
		}
	
		rowVals.addAll(colVals);
		rowVals.addAll(sqVals);
		allVals.removeAll(rowVals);
		
		return new ArrayList<Integer>(allVals);
	}
	
	private Cell nextCell(int[][] sudoku){

		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				if(sudoku[i][j] == 0){
					Cell c = new Cell();
					c.x = i;
					c.y = j;
					return c;
				}
			}
		}
		return null;
	}
	
	private int[][] copy(int[][] old){
		int[][] newMat = new int[old.length][];
		for (int i = 0; i < old.length; i++){
			newMat[i] = new int[old[i].length];
			for (int j = 0; j < old[i].length; j++){
				newMat[i][j] = old[i][j];
			}
		}
		return newMat;
	}
	
	private void print(int[][] sudoku){
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				System.out.print(sudoku[i][j]+" ");
			}
			System.out.println();
		}
	}

	private int[][] readSudokuFromFile(String fileName) {
		int[][] sudoku = new int[n][n];
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));

			for(int i = 0; i < n; i++){
				String line = reader.readLine().replace(" ", "");
				for(int j = 0; j < n; j++){
					sudoku[i][j] = Integer.parseInt(""+line.charAt(j));
				}
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sudoku;
	}
	
	private static class Cell {
		int x, y;
	}
	
	private int rowCnt(int r, int c, int[][] sdk){
		int target = sdk[r][c];
		int count = 0;
		for(int i = 0; i < n; i++)
			if(sdk[r][i] == target && i!=c) count++;
		return count;
	}
	
	private int colCnt(int r, int c, int[][] sdk){
		int target = sdk[r][c];
		int count = 0;
		for(int i = 0; i < n; i++)
			if(sdk[i][c] == target && i!=r) count++;
		return count;
	}
	
	private int sqCnt(int r, int c, int[][] sdk){
		int rlow = r - r%m, rup = rlow+3;
		int clow = c - c%m, cup = clow+3;
		
		int target = sdk[r][c];
		int count = 0;
		for(int i = rlow; i < rup; i++){
			for(int j = clow; j < cup; j++){
				if(i == r && j == c) continue;
				if(sdk[i][j] == target) count++;
			}
		}
		return count;
	}
	

}
