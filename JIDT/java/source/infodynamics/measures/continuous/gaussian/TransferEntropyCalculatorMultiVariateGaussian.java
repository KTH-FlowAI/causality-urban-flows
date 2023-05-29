/*
 *  Java Information Dynamics Toolkit (JIDT)
 *  Copyright (C) 2012, Joseph T. Lizier
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package infodynamics.measures.continuous.gaussian;

import infodynamics.measures.continuous.TransferEntropyCalculator;
import infodynamics.measures.continuous.TransferEntropyCalculatorMultiVariate;
import infodynamics.measures.continuous.TransferEntropyCalculatorMultiVariateViaCondMutualInfo;
import infodynamics.measures.continuous.TransferEntropyCalculatorViaCondMutualInfo;
import infodynamics.utils.AnalyticNullDistributionComputer;
import infodynamics.utils.ChiSquareMeasurementDistribution;
import infodynamics.utils.EmpiricalMeasurementDistribution;

/**
 * <p>Computes the differential transfer entropy (TE) between two multivariate
 *  <code>double[][]</code> time-series of observations
 *  (implementing {@link TransferEntropyCalculatorMultiVariate}),
 *  assuming that the probability distribution function for these observations is
 *  a multivariate Gaussian distribution.
 *  TE was defined by Schreiber, extended to multivariate source
 *  and destination by Lizier et al. (2011), and
 *  Kaiser and Schreiber showed how to compute
 *  TE via Gaussian assumption.
 *  This estimator is realised here by plugging in
 *  {@link ConditionalMutualInfoCalculatorMultiVariateGaussian}
 *  as the calculator into the parent class {@link TransferEntropyCalculatorMultiVariateViaCondMutualInfo}.</p>
 *  
 * <p>That is, this class implements a multivariate TE calculator using model of 
 * Gaussian variables with linear interactions, making it equivalent
 * (up to a multiplicative constant) to the multivariate Granger causality (see Barnett et al below).
 * </p> 
 * 
 * <p>Usage is as per the paradigm outlined for {@link TransferEntropyCalculatorMultiVariate},
 * with:
 * <ul>
 * 	<li>The constructor step being a simple call to {@link #TransferEntropyCalculatorMultiVariateGaussian()}.</li>
 * 	<li>{@link #setProperty(String, String)} allowing properties defined for both
 * 		{@link TransferEntropyCalculator#setProperty(String, String)} and
 *      {@link ConditionalMutualInfoCalculatorMultiVariateGaussian#setProperty(String, String)}
 *      as outlined
 *      in {@link TransferEntropyCalculatorViaCondMutualInfo#setProperty(String, String)}).</li>
 * 	<li>The user can call {@link #setCovariance(double[][], int)}
 *     instead of supplying observations via {@link #setObservations(double[][], double[][])} or
 *     {@link #addObservations(double[][], double[][])} etc.</li>
 *  <li>Computed values are in <b>nats</b>, not bits!</li>
 *  <li>Additional method {@link #computeSignificance()} to compute null distribution analytically.</li>
 *  </ul>
 * </p>
 * 
 * <p><b>References:</b><br/>
 * <ul>
 * 	<li>T. Schreiber, <a href="http://dx.doi.org/10.1103/PhysRevLett.85.461">
 * "Measuring information transfer"</a>,
 *  Physical Review Letters 85 (2) pp.461-464, 2000.</li>
 *  <li>J.T. Lizier, J. Heinzle, A. Horstmann, J.-D. Haynes, M. Prokopenko,
 *  <a href="http://dx.doi.org/10.1007/s10827-010-0271-2">
 *  "Multivariate information-theoretic measures reveal directed information
 *  structure and task relevant changes in fMRI connectivity"</a>,
 *  Journal of Computational Neuroscience, vol. 30, pp. 85-107, 2011.</li>
 *  <li>L. Barnett, A. B. Barrett, A. K. Seth, <a href="http://dx.doi.org/10.1103/physrevlett.103.238701">
 *  "Granger Causality and Transfer Entropy Are Equivalent for Gaussian Variables"</a>,
 *  Physical Review Letters 103 (23) 238701, 2009;</li>
 *  <li>A. Kaiser, T. Schreiber, <a href="http://dx.doi.org/10.1016/s0167-2789(02)00432-3">
 *  "Information transfer in continuous processes"</a>,
 *  Physica D, Vol. 166, No. 1-2., pp. 43-62 (2002).</li>
 * </ul>
 *
 * @author Joseph Lizier, <a href="joseph.lizier at gmail.com">email</a>,
 * <a href="http://lizier.me/joseph/">www</a>
 * @see TransferEntropyCalculatorMultiVariate
 */
public class TransferEntropyCalculatorMultiVariateGaussian
	extends TransferEntropyCalculatorMultiVariateViaCondMutualInfo
	implements AnalyticNullDistributionComputer {
	
	/**
	 * Name of the Gaussian conditional MI calculator we will use 
	 */
	public static final String COND_MI_CALCULATOR_GAUSSIAN = ConditionalMutualInfoCalculatorMultiVariateGaussian.class.getName();
	
	/**
	 * Property name for the number of surrogates to use in computing the bias correction
	 *  if required for the auto embedding method in {@link TransferEntropyCalculatorViaCondMutualInfo}.
	 *  Defaults to 0 meaning that we use analytic bias correction rather than empirical
	 *  surrogates. Note: This is not used for bias correction of the raw values, only for auto-embedding
	 */
	public static final String PROP_MAX_CORR_NUM_SURROGATES = "AUTO_EMBED_MAX_CORR_SURROGATES";
	/**
	 * Internal variable for storing the number of surrogates to use for the
	 * auto-embedding in {@link TransferEntropyCalculatorViaCondMutualInfo}.
	 * 0 mean we use analytic approaches rather than surrogates.
	 */
	protected int auto_embed_num_surrogates = 0;

	/**
	 * Creates a new instance of the Gaussian-estimate style transfer entropy calculator
	 * 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 *
	 */
	public TransferEntropyCalculatorMultiVariateGaussian() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		super(COND_MI_CALCULATOR_GAUSSIAN);
	}

	/**
	 * <p>Set the joint covariance of the distribution for which we will compute the
	 *  transfer entropy.</p>
	 *  
	 * <p>Note that without setting any observations, you cannot later
	 *  call {@link #computeLocalOfPreviousObservations()}, and without
	 *  providing the means of the variables, you cannot later call
	 *  {@link #computeLocalUsingPreviousObservations(double[][], double[][])}.</p>
	 * 
	 * @param covariance joint covariance matrix of the multivariate
	 *  source, dest, dest history
	 *  variables, considered together.
	 * @param numObservations the number of observations that the covariance
	 *  was determined from. This is used for later significance calculations
	 * @throws Exception for covariance matrix not matching the expected dimensions,
	 *  being non-square, asymmetric or non-positive definite
	 */
	public void setCovariance(double[][] covariance, int numObservations) throws Exception {
		((ConditionalMutualInfoCalculatorMultiVariateGaussian) condMiCalc).
				setCovariance(covariance, numObservations);
	}

	/**
	 * Sets properties for the TE Gaussian MultiVariate calculator.
	 *  New property values are not guaranteed to take effect until the next call
	 *  to an initialise method.
	 *
	 * <p>Valid property names, and what their
	 * values should represent, include:</p>
	 * <ul>
	 * 		<li>{@link #PROP_MAX_CORR_NUM_SURROGATES} -- number of surrogates to use
	 * 		to compute the bias correction
	 * 		in the auto-embedding if the property {@link #PROP_AUTO_EMBED_METHOD}
	 * 		has been set to one of the bias-corrected maximisation methods. Defaults to 0
	 * 		meaning that we use analytic bias correction.
	 * 		Note: this is not used for other bias-correction, only inside auto-embedding</li>
	 * 		<li>Any properties accepted by {@link super#setProperty(String, String)}</li>
	 * 		<li>Or properties accepted by the underlying
	 * 		{@link MutualInfoCalculatorMultiVariateGaussian#setProperty(String, String)} implementation.</li>
	 * </ul>
	 * 
	 * @param propertyName name of the property
	 * @param propertyValue value of the property.
	 * @throws Exception if there is a problem with the supplied value).
	 */
	@Override
	public void setProperty(String propertyName, String propertyValue)
			throws Exception {
		boolean propertySet = true;
		if (propertyName.equalsIgnoreCase(PROP_MAX_CORR_NUM_SURROGATES)) {
			auto_embed_num_surrogates = Integer.parseInt(propertyValue);
		} else {
			propertySet = false;
			// Assume it was a property for the parent class or underlying MI calculator
			super.setProperty(propertyName, propertyValue);
		}
		if (debug && propertySet) {
			System.out.println(this.getClass().getSimpleName() + ": Set property " + propertyName +
					" to " + propertyValue);
		}
	}

	@Override
	public String getProperty(String propertyName)
			throws Exception {
		
		if (propertyName.equalsIgnoreCase(PROP_MAX_CORR_NUM_SURROGATES)) {
			return Integer.toString(auto_embed_num_surrogates);
		} else {
			// Assume it was a property for the parent class or underlying MI calculator
			return super.getProperty(propertyName);
		}
	}

	@Override
	protected double computeAdditionalBiasToRemove() throws Exception {
		boolean biasCorrected = Boolean.getBoolean(getProperty(ConditionalMutualInfoCalculatorMultiVariateGaussian.PROP_BIAS_CORRECTION));
		if (auto_embed_num_surrogates == 0) {
			// Analytic bias correction:
			if (!biasCorrected) {
				ChiSquareMeasurementDistribution analyticMeasDist =
						((ConditionalMutualInfoCalculatorMultiVariateGaussian)condMiCalc).computeSignificance();
				return analyticMeasDist.getMeanOfDistribution();
			} else {
				return 0;
			}
		} else {
			// Empirical bias correction with auto_embed_num_surrogates surrogates:
			EmpiricalMeasurementDistribution measDist =
					condMiCalc.computeSignificance(auto_embed_num_surrogates);
			return measDist.getMeanOfDistribution();
		}
	}
	
	/**
	 * Generate an <b>analytic</b> distribution of what the TE would look like,
	 * under a null hypothesis that our variables had no relation
	 * (in the context of the conditional value).
	 * This is performed without bootstrapping (which is done in
	 * {@link #computeSignificance(int, int)} and {@link #computeSignificance(int, int[][])}).
	 * The method is implemented using the corresponding method of the
	 *  underlying {@link ConditionalMutualInfoCalculatorMultiVariateGaussian}
	 * 
	 * <p>See Section II.E "Statistical significance testing" of 
	 * the JIDT paper below, and the other papers referenced in
	 * {@link AnalyticNullDistributionComputer#computeSignificance()}
	 * (in particular Geweke),
	 * for a description of how this is done for TE and conditional MI.
	 * Basically, the null distribution is a chi-square distribution.
	 * </p>
	 * 
	 * @return ChiSquareMeasurementDistribution object which describes
	 * the proportion of TE scores from the null distribution
	 *  which have higher or equal conditional MIs to our actual value.
	 * @see {@link ConditionalMutualInfoCalculatorMultiVariateGaussian#computeSignificance()}
	 * @see "J.T. Lizier, 'JIDT: An information-theoretic
	 *    toolkit for studying the dynamics of complex systems', 2014."
	 * @throws Exception
	 */
	public ChiSquareMeasurementDistribution computeSignificance()
			throws Exception {
		return ((ConditionalMutualInfoCalculatorMultiVariateGaussian) condMiCalc).computeSignificance();
	}

}
