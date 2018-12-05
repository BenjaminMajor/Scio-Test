/*
 * 	Scio+ solution 
 * 	by Benjamin Major
 */

package batch_slaughter_analysis_tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class MainClass {
	
	private static final String pathToData = "/home/ben/Desktop/data";	
	private static final String outputFileName = "analysis_output.json";
	private static final String lastModifiedFileName = "last-modified.json";
	
	
	public static void help() {
		System.out.println("usage: analyse_batch_slaughter --path-to-data [path to data dir] --output-file [full path to output file]");
	}
	
	
/*	public static boolean isUpToDate() {
		boolean resp = false;
		
		File folder = new File(pathToData);
		File lastMod = new File(String.format("%s/%s", pathToData, lastModifiedFileName));
		
		try (BufferedReader br = new BufferedReader(new FileReader(lastMod))) {
			long lastTime = Long.parseLong(br.readLine());			
			if (lastTime == folder.lastModified()) {
				System.out.println("No new input received, analysis_output.json is up to date");
				return true;
			}
		} catch (FileNotFoundException e1) {
			System.out.println("No last-modified file was found, a new one will be generated");		
		} catch (IOException e1) {			
			System.out.println("an error occured while reading file");
		}
		return resp;
	}
	
	
	public static void writeLastModified() {
		File folder = new File(pathToData);		
		try (FileWriter file = new FileWriter(String.format("%s/%s", pathToData, lastModifiedFileName))) {						
			file.write(String.valueOf(folder.lastModified()));
			file.flush();

		} catch (IOException e) {
			System.out.println("An error occured printing output file");
		}
	}
*/

	public static void main(String[] args) {		



		BatchAnalyser tool = new BatchAnalyser(pathToData);
		tool.getInputFiles();		
		tool.readPredictionInputFiles();
		tool.readSlaughterInputFiles();
		tool.generateSlaughterAnalysis(String.format("%s/%s", pathToData, outputFileName));	
		
		//writeLastModified();
	}
	
	
	

}
