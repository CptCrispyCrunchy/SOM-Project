package SOM;
import java.util.ArrayList;


public class Neuron {
	private double outval;
	private ArrayList <Connection> in=new ArrayList <Connection>();
	private ArrayList <Connection> out=new ArrayList <Connection>();
	private int x;
	private int y;

	public void SetPos(int a, int b){
		x=a;
		y=b;
	}
	
	public int[] GetPos(){
		int[] pos={x,y};
		return pos;
	}
	
	public void SetOut(double x){
		outval=x;
	}
	public double GetOutVal(){
		return outval;
	}
	public void AddIn(Connection x){
		in.add(x);
	}
	public void AddOut(Connection x){
		out.add(x);
	}
	public ArrayList <Connection> GetOut(){
		return out;
	}
	public ArrayList <Connection> GetIn(){
		return in;
	}
	public double GetNetSum(){
		double netsum=0;
		for(int i=0;i<in.size();i++){
			netsum+=in.get(i).GetInp().GetOutVal();
		}
		return netsum;
	}
	public double[] GetInpValuesArray(){
		double[] InpValues=new double[in.size()];
		for(int i=0;i<in.size();i++){
			InpValues[i]=in.get(i).GetInp().GetOutVal();
		}
		return InpValues;
	}
	public double[] GetInpWeightsArray(){
		double[] InpWeights=new double[in.size()];
		for(int i=0;i<in.size();i++){
			InpWeights[i]=in.get(i).GetWeight();
		}
		return InpWeights;
	}
}
