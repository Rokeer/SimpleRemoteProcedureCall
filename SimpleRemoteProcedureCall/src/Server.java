import java.util.Arrays;

public class Server implements HeaderInterface {

	public Server() {

	}

	@Override
	public int[][] multiply(int[][] a, int[][] b) {
		int[][] result = new int[a.length][b[0].length];
		int x, i, j;
		for (i = 0; i < a.length; i++) {
			for (j = 0; j < b[0].length; j++) {
				int temp = 0;
				for (x = 0; x < b.length; x++) {
					temp += a[i][x] * b[x][j];

				}
				result[i][j] = temp;

			}
		}
		return result;

	}

	@Override
	public int[] sort(int[] a) {
		Arrays.sort(a);
		return a;

	}

	@Override
	public int min(int[] a) {
		int result = a[0];
		for (int i = 0; i < a.length; i++) {
			if (result > a[i]) {
				result = a[i];
			}
		}
		return result;

	}

	@Override
	public int max(int[] a) {
		int result = a[0];
		for (int i = 0; i < a.length; i++) {
			if (result < a[i]) {
				result = a[i];
			}
		}
		return result;

	}

	@Override
	public void test() {

	}

}