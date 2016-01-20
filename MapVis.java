package SOM;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MapVis extends Frame
{       
	private double 	Map[][][];
	private int[] 	dim;
	private double	x_dis;
	private double	y_dis;
	private Panel dp;
	private Image img;
	private Polygon pi,po;
	
  public MapVis (int[] dimensions) 
  {               
	dim=new int[2];
	dim[0]=dimensions[0];
	dim[1]=dimensions[1];
	dp=new Panel();
    setTitle("Self Organizing Map 2D"); 
    addWindowListener(new TestWindowListener());
    setSize(1800,800);
    setVisible(true);
    img = createImage(1800, 800);
    x_dis=1800/dim[0];
    y_dis=1800/dim[1];
    po=new Polygon();
    pi=new Polygon();
  }
  public void AddLabel(int[] pos, String label){
	  double posx,posy;
		posx=pos[0]*x_dis*3/2;
		posy=pos[1]*y_dis*Math.sqrt(3)/4;	
		if(pos[1]%2==1){
			posx+=(x_dis*3/4);
		}
  		Graphics g=img.getGraphics();
  		g.setColor(Color.WHITE);
  		g.drawString(label, (int)Math.round(posx), (int)Math.round(posy));
  }

  public void Save(String fname){
	  save(toBufferedImage(img),"bmp",fname);
  }
/*Obsolete:
  public void paintrec(double[][][] MapValues){
		Map=MapValues;
  		Graphics g=img.getGraphics();
  		for(int x=1;x<=dim[0];x++){
  			for(int y=1;y<=dim[1];y++){
  				g.setColor(MapValToColor(x,y));
  				g.fillRect(x*x_dis, y*y_dis, x_dis, y_dis);
  			}
  		}
  }
  */
  public void paint(double[][][] MapValues) {
	Map=MapValues;
	Graphics g=img.getGraphics();
	double posx=0;
	double posy=0;

	for(int x=0;x<dim[0];x++){
		for(int y=0;y<dim[1];y++){
			
				posx=x*x_dis*3/2;
				posy=y*y_dis*Math.sqrt(3)/4;	
				if(!(y%2==1)){
					posx+=(x_dis*3/4);
				}
				
		      // draw outline
				/*
		      for (int i = 0; i < 6; i++) {
		          po.addPoint((int) (posx + x_dis/2 * Math.cos(i * 2 * Math.PI / 6)),
		                      (int) (posy + y_dis/2 * Math.sin(i * 2 * Math.PI / 6)));
		      }
		     
		      g.setColor(Color.BLACK);
		      g.fillPolygon(po);
		    	*/
		      // draw inside
			
		      for (int i = 0; i < 6; i++){
		          pi.addPoint((int) Math.round(posx + (x_dis/2) * Math.cos(i * 2 * Math.PI / 6)),
		                      (int) Math.round(posy + (y_dis/2) * Math.sin(i * 2 * Math.PI / 6)));}
		      g.setColor(MapValToColor(x,y));
		      g.fillPolygon(pi);
		      pi.reset();
		      po.reset();
		}
	}
  }
  
  public void paint_CMap(int[][] CMap) {
	  Color[] colors = {
			  	Color.yellow, Color.green, Color.magenta, Color.cyan,
	            Color.white, Color.lightGray, Color.gray, Color.darkGray,
	            Color.black, Color.red, Color.pink, Color.orange,
	            Color.blue
	        };
	Graphics g=img.getGraphics();
	double posx=0;
	double posy=0;
	
	for(int x=0;x<dim[0];x++){
		for(int y=0;y<dim[1];y++){
			if(CMap[x][y]>colors.length){
				System.out.println("Error: Index out of Color Range. Abadoning Cluster Map");
				return;
			}
				posx=x*x_dis*3/2;
				posy=y*y_dis*Math.sqrt(3)/4;	
				if(!(y%2==1)){
					posx+=(x_dis*3/4);
				}
				
		      // draw inside
			
		      for (int i = 0; i < 6; i++){
		          pi.addPoint((int) Math.round(posx + (x_dis/2) * Math.cos(i * 2 * Math.PI / 6)),
		                      (int) Math.round(posy + (y_dis/2) * Math.sin(i * 2 * Math.PI / 6)));}
		      g.setColor(colors[CMap[x][y]]);
		      g.fillPolygon(pi);
		      pi.reset();
		      po.reset();
		}
	}
  }
  
  public void ShowImg(){
	  Graphics g=getGraphics();
	  g.drawImage(img, 0, 0, dp);
  }
 
  class TestWindowListener extends WindowAdapter
  {
	  public void windowClosing(WindowEvent e)
	  {
		  e.getWindow().dispose();                  
	      System.exit(0);                            
	  }         
  }
  	
  	private Color MapValToColor(int x, int y){
  		float c=(float) Math.abs(Math.sqrt(Math.pow(Map[x][y][0],2)+Math.pow(Map[x][y][1],2)+Math.pow(Map[x][y][2],2)+Math.pow(Map[x][y][3],2)));
  		if(c==0){
  			System.out.print("Error: c=0!");
  			return Color.WHITE;
  		}
  		c=1/c;
  		
  		float[] CMYK={Math.abs(c*(float)Map[x][y][0]),Math.abs(c*(float)Map[x][y][1]),Math.abs(c*(float)Map[x][y][2]),Math.abs(c*(float)Map[x][y][3])};
  		float[] rgb=CMYKToRGB(CMYK);
  		
  		if(rgb[0]>1 || rgb[1]>1 || rgb[2]>1 || rgb[0]<0 || rgb[1]<0 || rgb[2]<0){
  			System.out.println("Error: RGB Color not in Range for Neuron at Position: x="+x+" y="+y);
  			System.out.println(rgb[0]+" "+rgb[1]+" "+rgb[2]);
  			return Color.WHITE;
  		}
  		Color color=new Color(rgb[0],rgb[1],rgb[2]);
  		return color;
  	}
  	
  	private float[] CMYKToRGB(float[] CMYK){
  		float[] RGB=new float[3];
  		RGB[0]=(1-CMYK[0])*(1-CMYK[3]);
  		RGB[1]=(1-CMYK[1])*(1-CMYK[3]);
  		RGB[2]=(1-CMYK[2])*(1-CMYK[3]);
  		return RGB;
  	}
  	
    private static void save(BufferedImage image, String ext, String fname) {
        String fileName = fname;
        File file = new File(fileName + "." + ext);
        try {
            ImageIO.write(image, ext, file);
        } catch(IOException e) {
            System.out.println("Write error for " + file.getPath() +
                               ": " + e.getMessage());
        }
    }
 
    private static BufferedImage toBufferedImage(Image src) {
        int w = src.getWidth(null);
        int h = src.getHeight(null);
        int type = BufferedImage.TYPE_INT_RGB;
        BufferedImage dest = new BufferedImage(w, h, type);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return dest;
    }

 
}
