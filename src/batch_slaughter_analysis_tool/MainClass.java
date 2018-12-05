/*
 * 	Scio+ solution 
 * 	by Benjamin Major
 */

package batch_slaughter_analysis_tool;



public class MainClass {
	
	private static final String pathToData = "/home/ben/Desktop/data";	
	private static final String outputFileName = "analysis_output.json";
	
	public static void main(String[] args) {		

		BatchAnalyser tool = new BatchAnalyser(pathToData);
		tool.getInputFiles();		
		tool.readPredictionInputFiles();
		tool.readSlaughterInputFiles();
		tool.generateSlaughterAnalysis(String.format("%s/%s", pathToData, outputFileName));	
	}
	
	
	

}
