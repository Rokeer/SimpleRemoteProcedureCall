
public class Client {

	public static void main(String[] args) {
		int[] a = new int[4];
		int[] b = new int[4];
		for (int i = 0; i < 4; i++) {
			a[i] = i;
			b[i] = (i+1)*2;
		}
		ClientStub cs = new ClientStub();
		System.out.println(cs.max(a));
		System.out.println(cs.min(b));
	}
	
}