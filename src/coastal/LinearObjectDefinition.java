package coastal;

import java.util.ArrayList;

import fr.unistra.pelican.util.detection.MHMTDetectionParameters;

public class LinearObjectDefinition {
	
	private String name;
	private double resolution;
	private ArrayList<MHMTDetectionParameters> strictParameters;
	private ArrayList<MHMTDetectionParameters> softParameters;
	
	public LinearObjectDefinition(String name, double resolution, ArrayList<MHMTDetectionParameters> strictParameters, ArrayList<MHMTDetectionParameters> softParameters)
	{
		this.name=name;
		this.resolution=resolution;
		this.strictParameters=strictParameters;
		this.softParameters=softParameters;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<MHMTDetectionParameters> getStrictParameters() {
		return strictParameters;
	}

	public void setStrictParameters(ArrayList<MHMTDetectionParameters> strictParameters) {
		this.strictParameters = strictParameters;
	}
	
	public ArrayList<MHMTDetectionParameters> getSoftParameters() {
		return softParameters;
	}

	public void setSoftParameters(ArrayList<MHMTDetectionParameters> softParameters) {
		this.softParameters = strictParameters;
	}

	public double getResolution() {
		return resolution;
	}

	public void setResolution(double resolution) {
		this.resolution = resolution;
	}
	
	public String toString()
	{
		String str = new String();
		str=str.concat(name);
		str=str.concat("\n");
		str=str.concat(String.valueOf(resolution));
		str=str.concat("\n");
		for(int i=0;i<strictParameters.size();i++)
		{
			str=str.concat(String.valueOf(strictParameters.get(i).getBand()+1));
			str=str.concat(";");
			str=str.concat(String.valueOf((int)Math.round(strictParameters.get(i).getThresh()*255)));
			str=str.concat(";");
			if(strictParameters.get(i).isErosion())
				str=str.concat("true");
			else
				str=str.concat("false");
			str=str.concat(";");
			str=str.concat(String.valueOf(strictParameters.get(i).getSegmentShift()));
			str=str.concat(";");
			str=str.concat(String.valueOf(strictParameters.get(i).getSegmentLength()));
			str=str.concat("\n");
		}
		str=str.concat("\n");
		for(int i=0;i<softParameters.size();i++)
		{
			str=str.concat(String.valueOf(softParameters.get(i).getBand()+1));
			str=str.concat(";");
			str=str.concat(String.valueOf((int)Math.round(softParameters.get(i).getThresh()*255)));
			str=str.concat(";");
			if(strictParameters.get(i).isErosion())
				str=str.concat("true");
			else
				str=str.concat("false");
			str=str.concat(";");
			str=str.concat(String.valueOf(softParameters.get(i).getSegmentShift()));
			str=str.concat(";");
			str=str.concat(String.valueOf(softParameters.get(i).getSegmentLength()));
			str=str.concat("\n");
		}
		System.out.println(str);
		return str;
	}
	
	public static LinearObjectDefinition fromString(String str)
	{
		String[] splited = str.split("\n\n");
		System.out.println(splited.length);
		String[] splited2 = splited[0].split("\n");
		String tname = splited2[0];
		double tresolution = Double.parseDouble(splited2[1]);
		ArrayList<MHMTDetectionParameters> stparameters = new ArrayList<MHMTDetectionParameters>(); 
		for(int i=2;i<splited2.length;i++)
		{
			String[] splited3 = splited2[i].split(";");
			int band = Integer.parseInt(splited3[0])-1;
			double thresh = Integer.parseInt(splited3[1])/255.;
			boolean erosion=false;
			if(splited3[2].equalsIgnoreCase("true"))
				erosion=true;
			int shift = Integer.parseInt(splited3[3]);
			int length = Integer.parseInt(splited3[4]);
			stparameters.add(new MHMTDetectionParameters(band,thresh,erosion,shift,length));
		}
		ArrayList<MHMTDetectionParameters> sfparameters = new ArrayList<MHMTDetectionParameters>(); 
		
		String[] splited4= splited[1].split("\n");
		for(int i=0;i<splited4.length;i++)
		{
			String[] splited5 = splited4[i].split(";");
			int band = Integer.parseInt(splited5[0])-1;
			double thresh = Integer.parseInt(splited5[1])/255.;
			boolean erosion=false;
			if(splited5[2].equalsIgnoreCase("true"))
				erosion=true;
			int shift = Integer.parseInt(splited5[3]);
			int length = Integer.parseInt(splited5[4]);
			sfparameters.add(new MHMTDetectionParameters(band,thresh,erosion,shift,length));
		}
		return new LinearObjectDefinition(tname,tresolution,stparameters,sfparameters);		
	}
	

}
