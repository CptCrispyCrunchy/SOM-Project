package SOM;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//This is a toolbox where I store all commonly needed functions

public class TB {
	private static FileWriter writer;
	private static boolean log_called;
	public static double activate(double netVal){
		return 1/(1+Math.pow(Math.E, -netVal));
	}
	
	public static void ShowArray(double[] arr){
		System.out.print("{");
		for(int i=0;i<arr.length;i++){
			System.out.print(arr[i]+" ");
		}
		System.out.print("}");
	}
	public static double distance(double[] a,double[] b){
		if(a.length!=b.length){
			System.out.print("Error in Function distance: Arrays have different Dimension");
			return 0;
		}
		double sum=0;
		for(int i=0;i<a.length;i++){
			sum+=Math.pow(a[i]-b[i],2);
		}
		sum=Math.sqrt(sum);
		return sum;
	}
	
	public static double distance(int[] a, int[] b){
		return Math.sqrt(Math.pow(a[0]-b[0],2)+Math.pow(a[1]-b[1],2));
	}

	public static double[] To1D(double[][] in,int dim,int index){
		double[] out=new double[dim];
		for(int i=0;i<dim;i++){
			out[i]=in[index][i];
		}
		return out;
	}
	
	public static double[] Normalize(double[] vec){
		double[] res= new double[vec.length];
		double c=0;
		for(int i=0;i<vec.length;i++){
			c+=vec[i]*vec[i];
		}
		c=1/Math.sqrt(c);
		for(int i=0;i<vec.length;i++){
			res[i]=c*vec[i];
		}
		return res;
	}
	public static void log(String msg){
		Date current;
		DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
		if(!log_called){
			try {
				current=new Date();
				writer = new FileWriter("log-"+df.format(current), true); 
				}
		 	catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log_called=true;
		}
		try {
	
				current=new Date();
				writer.write(current+": "+msg+"\n");

			}
	 	catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void closelog(){
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
