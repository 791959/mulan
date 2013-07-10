/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
 *    TransformationBasedMultiTargetLearner.java
 *    Copyright (C) 2009-2012 Aristotle University of Thessaloniki, Greece
 */
package mulan.regressor.transformation;

import mulan.classifier.MultiLabelLearnerBase;
import weka.classifiers.Classifier;
import weka.classifiers.rules.ZeroR;
import weka.core.TechnicalInformation;
import weka.core.TechnicalInformation.Field;
import weka.core.TechnicalInformation.Type;

/**
 * 
 <!-- globalinfo-start --> Base class for multi-target regressors, which use a single-target
 * transformation to handle multi-target data.<br/>
 * <br/>
 * For more information, see<br/>
 * <br/>
 * E. Spyromitros-Xioufis, W. Groves, G. Tsoumakas, I. Vlahavas (2012). Multi-label Classification
 * Methods for Multi-target Regression. ArXiv e-prints.
 * 
 * <p/>
 * <!-- globalinfo-end -->
 * 
 * <!-- technical-bibtex-start --> BibTeX:
 * 
 * <pre>
 * &#64;article{spyromitros.mtr:2012,
 *    author = {E. Spyromitros-Xioufis and W. Groves and G. Tsoumakas and I. Vlahavas},
 *    journal = {ArXiv e-prints},
 *    title = {Multi-label Classification Methods for Multi-target Regression},
 *    url = {http://arxiv.org/abs/1211.6581}
 *    year = {2012}
 * }
 * </pre>
 * <p/>
 * <!-- technical-bibtex-end -->
 * 
 * 
 * @author Eleftherios Spyromitros-Xioufis
 * @version 2012.11.21
 */
@SuppressWarnings("serial")
public abstract class TransformationBasedMultiTargetRegressor extends MultiLabelLearnerBase {

    /**
     * The underlying single-target regressor.
     */
    protected Classifier baseRegressor;

    /**
     * Creates a new instance of {@link TransformationBasedMultiTargetRegressor} with default
     * {@link ZeroR} base regressor.
     */
    public TransformationBasedMultiTargetRegressor() {
        this(new ZeroR());
    }

    /**
     * Creates a new instance.
     * 
     * @param baseRegressor the base regressor which will be used internally to handle the data.
     */
    public TransformationBasedMultiTargetRegressor(Classifier baseRegressor) {
        this.baseRegressor = baseRegressor;
    }

    /**
     * Returns the {@link Classifier} which is used internally by the learner.
     * 
     * @return the internally used regressor
     */
    public Classifier getBaseRegressor() {
        return baseRegressor;
    }

    /**
     * Returns an instance of a TechnicalInformation object, containing detailed information about
     * the technical background of this class, e.g., paper reference or book this class is based on.
     * 
     * @return the technical information about this class
     */
    public TechnicalInformation getTechnicalInformation() {
        TechnicalInformation result = new TechnicalInformation(Type.INCOLLECTION);
        result
                .setValue(
                        Field.AUTHOR,
                        "Spyromitros-Xioufis, Eleftherios and Groves, William and Tsoumakas, Grigorios and Vlahavas, Ioannis");
        result.setValue(Field.TITLE,
                "Multi-label Classification Methods for Multi-target Regression");
        result.setValue(Field.JOURNAL, "ArXiv e-prints");
        result.setValue(Field.URL, "http://arxiv.org/abs/1211.6581");
        result.setValue(Field.YEAR, "2012");
        return result;
    }

    /**
     * Returns a string describing the classifier.
     * 
     * @return a string description of the classifier
     */
    public String globalInfo() {
        return "Base class for multi-target regressors, which use a single-target transformation to handle multi-target data."
                + "For more information, see\n\n" + getTechnicalInformation().toString();
    }

    /**
     * Returns a string representation of the multi-target regression model by calling
     * {@link #getModelForTarget(int)} for each target. Should always by called after the model has
     * been initialized.
     * 
     * @return a string representation of the multi-target regression model
     */
    public String getModel() {
        if (!isModelInitialized()) {
            return "No model built yet!";
        }
        String modelSummary = "";
        // get the model built for each target
        for (int i = 0; i < numLabels; i++) {
            modelSummary += "\n-- Model for target " + labelNames[i] + ":\n";
            modelSummary += getModelForTarget(i);
        }
        return modelSummary;
    }

    /**
     * Returns a string representation of the single-target regression model build for the target
     * with this targetIndex.
     * 
     * @param targetIndex
     * @return a string representation of the single-target regression model
     */
    protected abstract String getModelForTarget(int targetIndex);
}