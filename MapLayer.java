package SOM;
import java.util.ArrayList;
import java.awt.*;
public class MapLayer {

	private int			nNeurons;
	private Neuron[][]	Neurons;
	private int[] dim=new int[2];
	//Insert int array to determine dimensions of map
	public MapLayer(int[] ncount){
		nNeurons=ncount[0]*ncount[1];
		dim[0]=ncount[0];
		dim[1]=ncount[1];
		Neurons=new Neuron[ncount[0]][ncount[1]];

		for(int i=0;i<dim[0];i++){
			for(int j=0;j<dim[1];j++){
				Neurons[i][j]=new Neuron();
				Neurons[i][j].SetPos(i,j);
			}
		}
	}
	
	public ArrayList <Neuron> GetInRange(double r,Neuron a){
		int x=0;
		int y=0;
		int[] center=a.GetPos();
		ArrayList<Neuron> inRange=new ArrayList<Neuron>();
		inRange.add(a);
		for(double i=0;i<r;i=i+0.5){
			for(int h=0;h<360;h++){
				x=(int) (center[0]+Math.round(i*Math.cos(h)));
				y=(int) (center[1]+Math.round(i*Math.sin(h)));
				if(x<dim[0] && x>=0 && y<dim[1] && y>=0){
					inRange.add(Neurons[x][y]);
				}
			}
		}
		return inRange;
	}
	

	public int GetnNeurons(){ return nNeurons; }
	
	public int[] GetDim(){ return dim; }
	
	public Neuron GetNeuron(int x,int y){ return Neurons[x][y]; }
	
	public Neuron[][] GetNeurons(){ return Neurons; }

}
