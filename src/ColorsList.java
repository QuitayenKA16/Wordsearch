import java.awt.Color;

public class ColorsList
{
	//Grays
	private Color black = new Color(0,0,0);
	private Color white = new Color(255,255,255);
	private Color gray = new Color(190,190,190);
	private Color slateGray = new Color(112,138,144);
	
	//Blues
	private Color blue = new Color(0,0,255);
	private Color deepSkyBlue = new Color(0,191,255);
	private Color cornflowerBlue = new Color(100,149,237);
	private Color cyan = new Color(0,255,255);
	private Color paleTurquoise = new Color(175,238,238);
	
	//Greens
	private Color aquamarine = new Color(127,255,212);
	private Color darkGreen = new Color(0,100,100);
	private Color mediumSeaGreen = new Color(60,179,113);
	private Color springGreen = new Color(0,255,127);
	private Color limeGreen = new Color(50,205,50);
	private Color forestGreen = new Color(34,139,34);
			
	//Yellows
	private Color yellow = new Color(255,255,0);
	private Color gold = new Color(255,215,0);
	
	//Browns
	private Color indianRed = new Color(205,92,92);
	private Color rosyBrown = new Color(188,143,143);
	private Color sandyBrown = new Color(244,64,96);
	private Color chocolate = new Color(210,105,30);
	private Color firebrick = new Color(128,34,34);
	
	//Oranges
	private Color salmon = new Color(250,128,114);
	private Color darkOrange = new Color(255,140,0);
	private Color lightCoral = new Color(240,128,128);
	private Color red = new Color(255,0,0);
	
	//Pink/Violets
	private Color hotPink = new Color(255,105,180);
	private Color lightPink = new Color(255,182,193);
	private Color plum = new Color(221,160,221);
	private Color darkOrchid = new Color(153,50,204);
	private Color mediumPurple = new Color(147,112,219);
	
	private Color[] clr = {black,white,gray,slateGray,blue,deepSkyBlue,cornflowerBlue,cyan,paleTurquoise,
			aquamarine,darkGreen,mediumSeaGreen,springGreen,limeGreen,forestGreen,yellow,gold,indianRed,
			rosyBrown,sandyBrown,chocolate,firebrick,salmon,darkOrange,lightCoral,red,hotPink,lightPink,
			plum,darkOrchid,mediumPurple};
	
	private String[] clrName = {"black","white","gray","slateGray","blue","deepSkyBlue","cornflowerBlue","cyan","paleTurquoise",
			"aquamarine","darkGreen","mediumSeaGreen","springGreen","limeGreen","forestGreen","yellow","gold","indianRed",
			"rosyBrown","sandyBrown","chocolate","firebrick","salmon","darkOrange","lightCoral","red","hotPink","lightPink",
			"plum","darkOrchid","mediumPurple"};
	
	private String[] clrHTML;
	
	ColorsList()
	{	
		clrHTML = new String[31];
		for (int i = 0; i < 31; i++)
			clrHTML[i] = getHTML(i) + clrName[i];
		
	}
	public String getHTML(int i)
	{
		String html = "<html><font color=rgb(";
		html += clr[i].getRed() + ",";
		html += clr[i].getGreen() + ",";
		html += clr[i].getBlue() + ")>";
		return html;
	}
	
	public Color getColor(int i){
		return clr[i];
	}
	
	public String getStr(int i){
		return clrHTML[i];
	}
	
	public String[] getList(){
		return clrHTML;
	}
	
	public static void main(String[] args)
	{
		new ColorsList();
	}
}
