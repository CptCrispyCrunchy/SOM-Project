package SOM;

public class InpLayer {

	private int			nNeurons;
	private Neuron[]	Neurons;
	private int dim;
	private MapLayer	Map;
	//Insert int array to determine dimensions of map
	public InpLayer(int ncount,MapLayer m){
		nNeurons=ncount;
		dim=ncount;
		Neurons=new Neuron[dim];
		Map=m;
	}
	
	
	
	public void InitConnects(){
		int[] dimmap=Map.GetDim();
		for(int i=0;i<dim;i++){
			Neurons[i]=new Neuron();
					for(int j=0;j<dimmap[0];j++){
						for(int k=0;k<dimmap[1];k++){
							Connection a=new Connection(Neurons[i],Map.GetNeuron(j,k),Math.random());
							Neurons[i].AddOut(a);
							Map.GetNeuron(j,k).AddIn(a);
						}
					}
		}
	}
	
	public boolean SetInput(double[] inputvalues){
		if(inputvalues.length!=dim){
			System.out.print("Error: Inputvalues are in the wrong dimension!");
			return false;
		}
		for(int i=0;i<dim;i++){
			Neurons[i].SetOut(TB.activate(inputvalues[i]));
		}
		return true;
	}

	public int GetnNeurons(){ return nNeurons; }
	
	public int GetDim(){ return dim; }
	
	public Neuron GetNeuron(int x){ return Neurons[x]; }
	
	public Neuron[] GetNeurons(){ return Neurons; }
	
}
