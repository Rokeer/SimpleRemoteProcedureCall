
public class Client {

	public static void main(String[] args) {
		int[][] a={{1,2},{3,4},{5,6}};
        int[][] b={{1,2,3},{4,5,6}};
		
		int[] c = new int[4];
		for (int i = 0; i < 4; i++) {
			c[i] = (i+1)*2;
		}
		
		
		ClientStub cs = new ClientStub();
		int[][] d = cs.multiply(a, b);
		for(int i = 0;i<d.length;i++)  
        {  
            for(int j = 0;j<d[0].length;j++)  
            {  
                System.out.print(d[i][j]+"\t");  
            }  
            System.out.println();  
        }  
		
		int[] e = cs.sort(c);
		for (int i = 0; i < e.length; i++) {
			System.out.println(c[i]);
		}
		System.out.println(cs.max(c));
		System.out.println(cs.min(c));
		cs.test();
		
		int[][] f = new int[1000][1000];
		for(int i = 0;i<f.length;i++)  
        {  
            for(int j = 0;j<f[0].length;j++)  
            {  
            	f[i][j] = 0;
            } 
        } 
		int[][] g = cs.multiply(f, f);
		for(int i = 0;i<g.length;i++)  
        {  
            for(int j = 0;j<g[0].length;j++)  
            {  
                System.out.print(g[i][j]+"\t");  
            }  
            System.out.println();  
        } 
	}
	
}