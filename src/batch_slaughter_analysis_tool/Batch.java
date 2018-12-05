package batch_slaughter_analysis_tool;

import java.util.ArrayList;
import java.util.List;

public class Batch {

	private String batchId;
	private int slaughterWeight;
	private int slaughterAge;
	private List<SlaughterPrediction> slaughterPredictions;	


	public Batch(String id) {		
		this.batchId = id;
		slaughterPredictions = new ArrayList<SlaughterPrediction>();
	}


	public Batch(String batchId, int slaughterWeight, int slaughterAge) {
		this.batchId = batchId;
		this.slaughterWeight = slaughterWeight;
		this.slaughterAge = slaughterAge;
	}


	public String getId() {

		return this.batchId;		
	}


	public void setSlaughterWeight(int weight) {

		this.slaughterWeight = weight;
	}


	public int getSlaughterWeight() {

		return this.slaughterWeight;
	}	


	public void setSlaughterAge(int age) {

		this.slaughterAge = age;
	}


	public int getSlaughterAge() {

		return this.slaughterAge;		
	}


	public void addPrediction(SlaughterPrediction p) {

		this.slaughterPredictions.add(p);
	}


	public List<SlaughterPrediction> getPredictions() {

		return this.slaughterPredictions;
	}


	public int getAverageDifference() {

		int difference = 0;

		for (SlaughterPrediction  sP : slaughterPredictions) {		
			difference += Math.abs(this.slaughterWeight - sP.getPredictedWeight());
		}

		return difference/slaughterPredictions.size();
	}


	public SlaughterPrediction  getBestPrediction() {
		SlaughterPrediction sP = new SlaughterPrediction("unknown", 0, 0);
		int lowestDifference = 1000000;

		for (SlaughterPrediction p : slaughterPredictions) 
		{
			int diff = Math.abs(this.slaughterWeight - p.getPredictedWeight());

			if (diff < lowestDifference) {
				lowestDifference = diff;
				sP = p;
			}
		}

		return sP;
	}


}
