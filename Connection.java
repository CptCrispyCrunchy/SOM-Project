package SOM;

public class Connection {
	private Neuron inp;
	private Neuron out;
	private double weight;
	
	public Connection(Neuron i, Neuron o, double w){
		inp=i;
		out=i;
		weight=w;
	}
	
	public void SetNeurons(Neuron a,Neuron b){
		inp=a;
		out=b;
	}
	public void SetWeight(double x){
		weight=x;
	}
	public double GetWeight(){
		return weight;
	}
	public Neuron GetInp(){
		return inp;
	}
	public Neuron GetOut(){
		return out;
	}
}
