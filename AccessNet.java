package SOM;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
public class AccessNet {

	public static void main(String[] args) {
		DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
	    Date dateobj = new Date();
	    System.out.println(df.format(dateobj));
	    
	    
	    //Parameters
		double start_radius=5;
		double learnrate=1;
		int iterations=300;
		String filename="iris2.data";
		int[] neus= {30,30};
		
		
		//Do not change Code this code!
	    Network net=new Network(4,neus);
	    net.TrainNet(start_radius, learnrate, iterations,filename);
	    //net.ShowData();
	    net.ShowTrainedNet();
		net.SaveImg("CMYK-Map "+filename+df.format(dateobj));
	    net.ShowClusterMap();
		net.SaveImg("ClusterMap "+filename+df.format(dateobj));
		net.TestDataset("iristest.data");
		net.closelog();
	}

}
