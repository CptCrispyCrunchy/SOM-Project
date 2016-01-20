package SOM;
import java.util.ArrayList;
import java.util.Scanner;

public class Network {
	
	private MapLayer	Map;
	private InpLayer	InpLayer;
	private double[]	TrainInp;
	private double[][]	Data;
	private boolean		img_created=false;
	private boolean		trained=false;
	private int[] 		dim;
	private FM			fn;
	private MapVis 		panel;
	private int[][]		CMap;
	private boolean		CMap_created=false;
	//1. Arg: Int Array for Dimensions of Input| 2.Arg: int[2] Array for Dimensions of Map
	public Network(int NCountI,int[] NCountM){
		Map=new MapLayer(NCountM);
		InpLayer=new InpLayer(NCountI,Map);
		InpLayer.InitConnects();
		dim=new int[2];
		dim[0]=NCountM[0];
		dim[1]=NCountM[1];
	}
	//Find best fitting candidate
	private Neuron BFC(){
		Neuron n;
		double min=1000000;
		double temp=0;
		int outind[]={-1,-1};
		for(int x=0;x<dim[0];x++){
			for(int y=0;y<dim[1];y++){
				n=Map.GetNeuron(x, y);
				temp=TB.distance(n.GetInpValuesArray(),n.GetInpWeightsArray());
				if(Double.isNaN(temp)){
					
					System.out.println("Distance could not be calculated");
					TB.ShowArray(n.GetInpValuesArray());
					TB.ShowArray(n.GetInpWeightsArray());
					break;
					
				}
				//System.out.println("dotp: "+x+","+y+"="+temp);
				if(temp<min){
					min=temp;
					outind[0]=x;
					outind[1]=y;

				}
			}
		}
		if(outind[0]==-1 || outind[1]==-1){
			System.out.println("Error: No BFC found");
			return Map.GetNeuron(0, 0);
		}
		return Map.GetNeuron(outind[0], outind[1]);
	}
	
	private void learn(double r, double l, double[] inp){
		//Set Inputvalues
		InpLayer.SetInput(inp);
		//Get BFC and store position
		Neuron c_neuron=BFC();
		int[] c_pos=c_neuron.GetPos();
		double w=0;
		double x=0;
		
		//Get all Neurons in range of radius with center BFC 
		ArrayList<Neuron> NinRange=Map.GetInRange(r,c_neuron);
		
		//Change all weights of neurons in range
		for(int i=0;i<NinRange.size();i++){
			Neuron a=NinRange.get(i);
			ArrayList<Connection> c=a.GetIn();
			for(int j=0;j<c.size();j++){
				Connection con=c.get(j);
				w=con.GetWeight();
				x=TB.distance(c_pos,(a.GetPos()));
				con.SetWeight(w+l*g(x,r)*(con.GetInp().GetOutVal()-w));
			}
		}	
	}
	
	private double g(double x, double radius){
		double res=Math.pow(Math.E, -x*x/(2*radius*radius));
		if(Double.isNaN(res)){
			System.out.println("Error calculating Gauss Bell Curve for Values: x="+x+" radius="+radius);
		}
		return res;
	}
	
	/*Obsolete:
	private double dotproduct(double[] a,double[] b){
		if(a.length!=b.length){
			System.out.print("Error in Function Dotproduct: Arrays have different Dimension");
			return 0;
		}
		double sum=0;
		for(int i=0;i<a.length;i++){
			sum+=a[i]*b[i];
		}
		
		return sum;
	}*/
	public void TestDataset(String fname){
		fn = new FM(fname, InpLayer.GetDim());
		double[] testdata;
		String[] names={"Iris setos", "Iris versicolor", "Iris virginica"};
		int[]	error={0,0,0};
		int temp1,temp2;
		//Initializing Training Data
		InitTrainingData(fn);
		System.out.println("Test-Data read!");
		//CheckAssignments without knowing which index is corresponding to which group...zZz
		//Check for 20/20/20 Testdata
		for(int i=0;i<3;i++){
			temp2=AssignCluster(GetData(20*i));
			for(int j=1+20*i;j<20*(1+i);j++){
				testdata=GetData(j);
	
				temp1=AssignCluster(testdata);
		
				if(temp1-temp2!=0){error[i]++;}
				temp2=temp1;
			}
			System.out.println("Deviating Datasets in Class "+names[i]+": "+error[i]);
			TB.log("Deviating Datasets in Class "+names[i]+": "+error[i]);
		}
		float errpercent=100-100*(error[0]+error[1]+error[2])/60;
		System.out.println("Performance overall: "+errpercent+"% successfully clustered");
		TB.log("Performance overall: "+errpercent+"% successfully clustered");
	}
	
	
	public void TrainNet(double radius, double learnrateinp, int iter,String filename){
		fn = new FM(filename, InpLayer.GetDim());
		double[] traindata;
		double r_start=radius;
		double l_start=learnrateinp;
		double r=r_start;
		double l=l_start;
		
		//Change time decay here:
		double r_end=1;
		double l_end=0.5;
		
		//logging
		TB.log("Starting training process with parameters: learnrate:"+l+"  -  radius: "+r+"  -  iterations: "+iter+"  -  on file: "+filename);
		//Initializing Training Data
		InitTrainingData(fn);
		System.out.println("Training Data read!");
		TB.log("Training Data read!");
		
		for(int i=0;i<iter;i++){
			for(int j=0;j<fn.getdatacount();j++){
				//pick random training sample
				int num=(int)(Math.round(Math.random()*(fn.getdatacount()-1)));
				traindata=GetData(num);
				
				//start learning algorithm
				learn(r,l,traindata);
			}
			
			//Decrease learnrate and radius
			r=r*Math.pow(r_start/r_end,i/iter);
			l=l*Math.pow(l_start/l_end,i/iter);
			
			//Progress output
			if(i%100==0) {
				float progress=(float) i/iter*100;
				System.out.println("Progress: "+progress+"%");
				TB.log("Progress: "+progress+"%");
			}

		}
		System.out.println("Progress: 100% - SOM trained!");
		TB.log("Progress: 100% - SOM trained!");
		trained=true;
	}


	//Visualize net after it is trained by applying to each neuron a value based on its dotp for each class
	//Set text according to max value
	public void ShowTrainedNet(){
		double[] traindata;
		double[][][] MapValues=new double[dim[0]][dim[1]][InpLayer.GetnNeurons()];
		panel=new MapVis(dim);

		//Calculate MapValues for each Dataset and add it to img
		for(int i=0;i<fn.getdatacount();i++){
			traindata=GetData(i);
			Vis_ApplyDataSet(MapValues,traindata);
		}
		
		panel.paint(MapValues);
		int count=fn.getdatacount();
		//Calculate BFC for each Dataset and add label to img
		for(int i=0;i<count;i++){
			traindata=GetData(i);
			InpLayer.SetInput(traindata);
			Neuron c_neuron=BFC();
			int[] c_pos=c_neuron.GetPos();
			
			if(i<=count/3){
				panel.AddLabel(c_pos,"setosa");
			}
			else if(i<=count*2/3){
				panel.AddLabel(c_pos,"versicolor");
			}
			else{
				panel.AddLabel(c_pos,"virginica");
			}
		}
		
		//Show img created
		System.out.println("Showing colored Map - CMYK Mode");
		panel.ShowImg();

		img_created=true;
	}


	private double[][][] Vis_ApplyDataSet(double[][][] MapValues,double[] inp){
		InpLayer.SetInput(inp);
		Neuron n;
		for(int x=0;x<dim[0];x++){
			for(int y=0;y<dim[1];y++){
				n=Map.GetNeuron(x, y);
				//Test;
				
				
				for(int i=0;i<InpLayer.GetnNeurons();i++){
					MapValues[x][y][i]=n.GetInpValuesArray()[i]-n.GetInpWeightsArray()[i];
					//MapValues[x][y][i]=activate(n.GetInpValuesArray()[i]-n.GetInpWeightsArray()[i]);
				}
			}
		}
		return MapValues;
	}
	
	private boolean CreateCMap(){
		if(!CMap_created){
			CMap=KMeans.Lloyd(Map, 3);
			CMap_created=true;
			return true;
		}
		return true;
	}
	
	public void ShowClusterMap(){
		panel=new MapVis(dim);
		if(CreateCMap()){ panel.paint_CMap(CMap);}
		//Show img created
		System.out.println("Showing colored Cluster-Map");
		panel.ShowImg();
		img_created=true;

	}

	private int AssignCluster(double[] inp){
		if(CreateCMap()){
			InpLayer.SetInput(inp);
			Neuron c_neuron=BFC();
			int[] c_pos=c_neuron.GetPos();
			int i=CMap[c_pos[0]][c_pos[1]];
			return i;
		}
		return -1;
	}
	//Get and Set Training Data
	public void InitTrainingData(FM fn){
		Data=fn.readData();
	}
	
	public double[] GetData(int num){
		int inpl=InpLayer.GetDim();
		double[] Inp = new double[inpl];
		for(int i=0;i<inpl;i++){
			Inp[i]=Data[num][i];
			}
		return Inp;
	}	

	public void ShowData(){
		fn.ShowData();
	}
	public void SaveImg(String filename){
		if(img_created){
			panel.Save(filename);
			System.out.println("Image successfully saved!");
			TB.log("Image successfully saved with name_"+filename);
		}
		else System.out.println("Error: Image of map not yet created!");
		img_created=false;
	}
	public void closelog(){
		TB.closelog();
	}
	

}
