package batch_slaughter_analysis_tool;

public class SlaughterPrediction {
	
	private String batchId;
	private int predictedWeight;
	private int birdAge;
	
	public SlaughterPrediction(String id, int weight, int age) {
		this.batchId = id;
		this.predictedWeight = weight;
		this.birdAge = age;
	}
	
	public String getId() {
		
		return this.batchId;
	}
	
	
	public int getPredictedWeight() {
		
		return this.predictedWeight;
	}
	
	
	public int getBirdAge() {
		
		return this.birdAge;
	}
	
}
