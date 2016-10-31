
public class Client {

	public static void main(String[] args) {
		int[] a = new int[4];
		int[] b = new int[4];
		for (int i = 0; i < 4; i++) {
			a[i] = 4 - i;
			b[i] = (i+1)*2;
		}
		ClientStub cs = new ClientStub();
		int[] c = cs.multiply(a, b);
		for (int i = 0; i < c.length; i++) {
			System.out.println(c[i]);
		}
		int[] d = cs.sort(a);
		for (int i = 0; i < d.length; i++) {
			System.out.println(d[i]);
		}
		System.out.println(cs.max(a));
		System.out.println(cs.min(b));
		cs.test();
	}
	
}