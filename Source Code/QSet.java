public class QSet {

	private int pos[], boardSz, fitness;
	
	public QSet(int[] pos) {
		this.pos = pos;
		boardSz = pos.length;
		fitness = findFitness();
	}
	
	public int getFitness() {
		return fitness;
	}
	
	public int[] getPos() {
		return pos;
	}
	
	private int[] delete(int[] arr, int x, int newSz) {
		int idx = 0, brr[] = new int[newSz], sz = arr.length;
		
		for(int i=0; i<sz; i++)
			if(arr[i] != x)
				brr[idx++] = arr[i];
		
		return brr;
	}
	
	private int count(int[] arr, int x) {
		int cnt = 0, sz = arr.length;
		for(int i=0; i<sz; i++)
			if(arr[i] == x)
				cnt++;
		
		return cnt;
	}
	
	public int findFitness() {
		int sum = 0;
		int[] positionCopy = pos.clone();
		
		while(positionCopy.length > 0) {
			int frequency = count(positionCopy, positionCopy[0]);
			if(frequency > 1)
				sum += ((frequency-1)*(frequency))/2;
			
			positionCopy = delete(positionCopy, positionCopy[0], positionCopy.length - frequency);
		}
		
		positionCopy = pos.clone();
		int[][] diagonals = new int[2][boardSz];
		
		for(int i=0; i<2; i++) {
			for(int j=0; j<boardSz; j++) {
				if(i == 0)
					diagonals[i][j] = positionCopy[j] + j;
				else
					diagonals[i][j] = positionCopy[j] - j;
			}
		}
		
		for(int i=0; i<2; i++) {
			while(diagonals[i].length > 0) {
				int frequency = count(diagonals[i], diagonals[i][0]);
				if(frequency > 1)
					sum += ((frequency-1)*(frequency))/2;
				
				diagonals = delete(diagonals[i], diagonals[i][0], diagonals[i].length - frequency);
			}
		}
		
		return sum;
	}
	
}
