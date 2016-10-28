
public class Server implements HeaderInterface {
	
	public Server() {

	}

	@Override
	public int[] multiply(int[] a, int[] b){
		return b;
		
	}

	@Override
	public int[] sort(int[] a){
		return a;
		
	}

	@Override
	public int min(int[] a){
		int result = a[0];
		for (int i = 0; i < a.length; i++) {
			if (result > a[i]) {
				result = a[i];
			}
		}
		return result;
		
	}

	@Override
	public int max(int[] a){
		int result = a[0];
		for (int i = 0; i < a.length; i++) {
			if (result < a[i]) {
				result = a[i];
			}
		}
		return result;
		
	}

	@Override
	public void test(){
		
	}

}