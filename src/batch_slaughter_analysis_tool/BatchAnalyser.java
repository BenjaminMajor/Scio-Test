package batch_slaughter_analysis_tool;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BatchAnalyser {

	private Map<String, Batch> batchMap;
	private File folder;
	private File[] files;
	private String pathToDir;
	private JSONArray anaysisReportArray;
	
/*	@Param String pathToDir
 * 	constructor
 */
	public BatchAnalyser(String pathToDir) {
		this.pathToDir = pathToDir;
		this.batchMap = new HashMap<String, Batch>();
	}

/*
 * 	gets the names of all files in the data dir
 */
	public void getInputFiles() {
		try {
			this.folder = new File(pathToDir);	
			this.files = folder.listFiles();
		} catch (NullPointerException e) {
			System.out.println("Path to folder does not exist");
		}
	}
	
/*
 * 	reads the prediction input files in the data dir and creates a Batch object using the batchId
 * 	Batch objects are stored in the Map 'batchMap'
 */
	
	public void readPredictionInputFiles() {
		System.out.print("reading prediction input files.......");
		JSONParser parser = new JSONParser();

		try {
			for (File f : files) {
				if (f.getName().startsWith("prediction")) {
					JSONObject jObject = (JSONObject) parser.parse(new FileReader(f.getPath()));

					String id = jObject.get("batchId").toString();
					int weight = Integer.parseInt(jObject.get("predictedWeight").toString());
					int age = Integer.parseInt(jObject.get("birdAge").toString());

					if (batchMap.containsKey(id)) {
						Batch b = batchMap.get(id);
						b.getPredictions().add(new SlaughterPrediction(id, weight, age));
					}
					else {
						Batch b = new Batch(id);
						b.getPredictions().add(new SlaughterPrediction(id, weight, age));
						this.batchMap.put(id, b);
					}					
				}

			}
		} catch (NullPointerException n) {
			System.out.println("Folder Path does not exist");
		} catch (IOException io) {
			System.out.println("Error while reading or parsing file");
		} catch (ParseException p) {
			System.out.println("Error while parsing json");
		}
		
		System.out.print("OK");
	}

	
	/*
	 * 	reads the slaughter input files, updates the Batch objects in the Map with the slaughter data
	 */
	public void readSlaughterInputFiles() {
		System.out.println();
		System.out.print("reading slaughter input files.......");
		JSONParser parser = new JSONParser();

		try {
			for (File f : files) {
				if (f.getName().startsWith("slaughter")) {
					JSONObject jObject = (JSONObject) parser.parse(new FileReader(f.getPath()));

					String id = jObject.get("batchId").toString();
					int weight = Integer.parseInt(jObject.get("slaughterWeight").toString());
					int age = Integer.parseInt(jObject.get("slaughterAge").toString());

					Batch b = this.batchMap.get(id);
					b.setSlaughterWeight(weight);
					b.setSlaughterAge(age);
				}
			}
		} catch (NullPointerException e) {
			System.out.println("Folder Path does not exist");
		} catch (IOException io) {
			System.out.println("Error while reading file");
		} catch (ParseException p) {
			System.out.println("Error while parsing json");
		}
		System.out.print("OK");

	}
	

	/*
	 * 	generates a report from the data of all batches with slaughter input data
	 */
	
	@SuppressWarnings("unchecked")
	public void generateSlaughterAnalysis(String outputFilePath) {
		System.out.println();
		System.out.println("generating slaughter analyses report for all batches");
		anaysisReportArray = new JSONArray();
		
		batchMap.forEach((key, batch) -> {
			if (batch.getSlaughterAge() > 20) {

				SlaughterPrediction sp = batch.getBestPrediction();
				JSONObject topJObj = new JSONObject();

				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("batchId", batch.getId());
				map.put("averageDifference", batch.getAverageDifference());
				topJObj.putAll(map);

				Map<String, Integer> map2 = new LinkedHashMap<String, Integer>();
				map2.put("predictedWeight", sp.getPredictedWeight());
				map2.put("birdAge", sp.getBirdAge());			

				topJObj.put("bestPrediction", map2);
				anaysisReportArray.add(topJObj);
			}
			
		});
		
		try (FileWriter file = new FileWriter(outputFilePath)) {
			file.write(anaysisReportArray.toJSONString());
			file.flush();

		} catch (IOException e) {
			System.out.println("An error occured printing output file");
		}
		
		System.out.println("Analysis output file has been generated");
	}



}
