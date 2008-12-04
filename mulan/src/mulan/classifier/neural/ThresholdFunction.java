
package mulan.classifier.neural;

import java.util.Arrays;

import weka.core.Utils;
import weka.core.matrix.Matrix;

	
/**
 * Implementation of a threshold function. 
 *  
 * @author Jozef Vilcek
 */
public class ThresholdFunction {

	private double[] parameters;

	/**
	 * Creates a new instance of {@link ThresholdFunction} and 
	 * build the function based on input parameters.
	 * 
	 * @param idealLabels the ideal output for each input patterns, which a model should output
	 * @param modelOutLabels the real output of a model for each input pattern 
	 * @see ThresholdFunction#build(double[][], double[][])
	 */
	public ThresholdFunction(final double[][] idealLabels, final double[][] modelOutLabels){
		this.build(idealLabels, modelOutLabels);
	}
	
	/**
	 * Computes a threshold value, based on learned parameters, for given labels confidences.
	 * 
	 * @param labelsConfidences the labels confidences
	 * @return
	 * @throws IllegalArgumentException if the dimension of labels confidences does not match 
	 * 		   							the dimension of learned parameters of threshold function.
	 */
	public double computeThreshold(final double[] labelsConfidences){
		
		int expectedDim = parameters.length - 1;
		if(labelsConfidences.length != expectedDim){
			throw new IllegalArgumentException("The array of label confidences has wrong dimension." +
					"The function expect parameters of length : " + expectedDim);
		}
		
		double threshold = 0;
		for(int index = 0; index < expectedDim; index++){
			threshold += labelsConfidences[index]*parameters[index];
		}
		threshold += parameters[expectedDim];
		
		return threshold;
	}
	
	/**
	 * Build a threshold function for based on input data. 
	 * The threshold function is build for a particular model.
	 *  
	 * @param idealLabels the ideal output for each input patterns, which a model should output. 
	 * 					  First index is expected to be number of examples and second is the label index.
	 * @param modelOutLabels the real output of a model for each input pattern.
	 * 						 First index is expected to be number of examples and second is the label index. 
	 * @throws IllegalArgumentException if dimensions of input arrays does not match
	 */
	public void build(final double[][] idealLabels, final double[][] modelOutLabels){
		
		if(idealLabels == null || modelOutLabels == null){
			throw new IllegalArgumentException("Non of the input parameters can be null.");
		}
		
		int numExamples = idealLabels.length;
		int numLabels = idealLabels[0].length;
		
		if (modelOutLabels.length != numExamples || 
			modelOutLabels[0].length != numLabels) {
			throw new IllegalArgumentException("Matrix dimensions of input parameters does not agree.");
		}
		
		double[] thresholds = new double[numExamples];
		double[] isLabelModelOuts = new double[numLabels];
		double[] isNotLabelModelOuts = new double[numLabels];
		for(int example = 0; example < numExamples; example++){
			Arrays.fill(isLabelModelOuts, Double.MAX_VALUE);
			Arrays.fill(isNotLabelModelOuts, -Double.MAX_VALUE);
			for (int label = 0; label < numLabels; label++) {
				if (idealLabels[example][label] == 1)
					isLabelModelOuts[label] = modelOutLabels[example][label];
				else
					isNotLabelModelOuts[label] = modelOutLabels[example][label];
			}
			double isLabelMin = isLabelModelOuts[Utils.minIndex(isLabelModelOuts)];
			double isNotLabelMax = isNotLabelModelOuts[Utils.maxIndex(isNotLabelModelOuts)];
			if(isLabelMin != isNotLabelMax)
				thresholds[example] = (isLabelMin + isNotLabelMax) / 2;
			else
				thresholds[example] = isLabelMin;
		}
		
		Matrix modelMatrix = new Matrix(numExamples, numLabels + 1, 1.0);
		modelMatrix.setMatrix(0, numExamples - 1, 0, numLabels - 1, new Matrix(modelOutLabels));
		Matrix weights = modelMatrix.solve(new Matrix(thresholds, thresholds.length));
		double[][] weightsArray = weights.transpose().getArray();
		
		parameters = Arrays.copyOf(weightsArray[0], weightsArray[0].length);
	}
	
}
	
