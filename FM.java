package SOM;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class FM {
	private static DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
	private static Date dateobj = new Date();
	private Scanner scanner; //= new Scanner(new File("tall.txt"));
	private FileWriter writer;
	private double [][] TestData;
	private boolean log_called=false;
	String fname;
	int linecount;
	int IOSize;
	int datasets;
	public FM(String filename, int iosize){
		
		IOSize=iosize;
		fname=filename;
		datasets=validateData();
		System.out.println("Dataset Count: "+datasets);
		TB.log("Reading Dataset "+filename);
		TB.log("Dataset Count: "+datasets);
		TestData = new double[datasets][iosize];
		df = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
	    dateobj = new Date();

	}
	public double[][] readData(){
		boolean intfound=false;
		Pattern delimiter=Pattern.compile("[,\n]");
		try {
			scanner = new Scanner(new File(fname),"UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scanner.useDelimiter(delimiter);
		for(int j = 0;j<datasets;j++){
			
			for(int i=0;i<IOSize;i++){
				intfound=false;
				while(intfound==false && scanner.hasNext()){
						try{
							String pattern = "[a-zA-Z]*";
							scanner.skip(pattern);
							if(scanner.hasNextDouble()){
								TestData[j][i] = scanner.nextDouble();
								intfound=true;
							}
							else { scanner.next(); }
	
							}

						catch (NumberFormatException ex) { continue; }
			
				}
			}
		}
		scanner.close();
		return TestData;
	}


	private int validateData(){
		int setcount=0;
		
		try {
			scanner = new Scanner(new File(fname),"UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scanner.useDelimiter(",");
		while(scanner.hasNext()){
			try{
				String pattern = "[a-zA-Z\n]*";
				scanner.skip(pattern);
				if(scanner.hasNextDouble()){
					
				scanner.nextDouble();
				}
				else { scanner.next(); }
				setcount++;
				}
			
			catch (NumberFormatException ex) { continue; }
		}
		System.out.println("Incomplete Integers: "+(setcount%IOSize));
		TB.log("Incomplete Integers: "+(setcount%IOSize));
		scanner.close();
		return ((setcount-(setcount%IOSize))/IOSize);
	}
	public int getdatacount(){ return datasets;}
	
	public void ShowData(){
		for(int j = 0;j<datasets;j++){
			for(int i=0;i<IOSize;i++){
				System.out.print(TestData[j][i]);
				System.out.print(" ");
			}
			System.out.print("\n");
		}
	}
}
