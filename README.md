# Transfer Entropy Calculation for Temporal Signals

This repository contains MATLAB scripts for calculating Transfer Entropy (TE) from temporal signals. The scripts are designed to be easy to use and modify for your specific needs. The main script sets the parameters for the TE calculation and calls the main function, which performs the actual calculations.

## Introduction

This code is based on the research paper titled ["Causality analysis of large-scale structures in the flow around a wall-mounted square cylinder", Martínez-Sánchez *et al.*](https://arxiv.org/abs/2209.15356) (2023, *Journal of Fluid Mechanics*). In this work, the authors conducted a causality analysis of the large-scale structures in the flow around a wall-mounted square cylinder. The aim was to analyze the formation mechanisms of these coherent structures and their impact on pollutant transport within cities. The authors assessed the causal relations between the modes of a reduced-order model obtained by applying proper-orthogonal decomposition to high-fidelity-simulation data of the flow case under study. The causal relations were identified using conditional transfer entropy, which is an information-theoretical quantity that estimates the amount of information contained in the past of one variable about another. This allowed for an understanding of the origins and evolution of different phenomena in the flow and the identification of the modes responsible for the formation of the main vortical structures.

Watch the video below for a brief introduction to the project:

[![Introduction Video](https://i.imgur.com/oBtpQQv.png)](http://www.youtube.com/watch?v=6FSBU9wrqkY)

## Getting Started

To get started with using the scripts in this repository, follow the steps below:

1. **Install MATLAB**: The scripts in this repository are written in MATLAB. Make sure to also install the Signal Processing Toolbox, as some of the scripts may require it. The scripts have been tested and confirmed to work on MATLAB version R2021a.

2. **Download the Java Information Dynamics Toolkit (JIDT)**: The scripts in this repository use the JIDT for the calculation of Transfer Entropy. The JIDT is a Java library that provides a set of tools for the computation of information-theoretic measures of distributed computation in complex systems. You can download the JIDT from its GitHub repository [here](https://github.com/jlizier/jidt/).

3. **Set up the MATLAB environment**: Open MATLAB and navigate to the directory where you saved the scripts from this repository. Before running the scripts, you will need to add the path to the JIDT library to the MATLAB environment. You can do this by using the `javaaddpath` function in MATLAB. For example, if you saved the JIDT library in a folder named "JIDT" in your home directory, you would add the path like this: `javaaddpath('~/JIDT/infodynamics.jar')`.

4. **Run the scripts**: You are now ready to run the scripts. You can start by running the `main.m` script, which sets the parameters for the TE calculation and calls the main function to perform the calculations.

Remember to always check the MATLAB command window for any error messages or warnings when running the scripts.

## Usage

1. Set the parameters for the TE calculation in the `main.m` script. These include the number of modes, lags, embedding dimension, length of past history, number of nearest neighbors for the Kraskov estimator, and number of permutations for significance testing.

2. Define the location of the JIDT library in the `main.m` script. This should be the path to your `infodynamics.jar` file.

3. Define a location to save results in the `main.m` script. This should be the directory where you want to save your results.

4. Call the main function from the `main.m` script. This function performs the actual TE calculations and saves the results.

## Example

An example script (`example.m`) is provided to demonstrate how to use the scripts. This script creates five time-varying variables, saves them to a .mat file, and then calculates the transfer entropy between them. The results are saved in a folder named "example".

## Citation

If you use this code in your research, please cite the following paper:

Á. Martínez-Sánchez, E. López, S. Le Clainche, A. Lozano-Durán, A. Srivastava & R. Vinuesa (2023). "Causality analysis of large-scale structures in the flow around a wall-mounted square cylinder". *Journal of Fluid Mechanics*, DOI:[10.1017/jfm.2023.423](https://doi.org/10.1017/jfm.2023.423) (pre-print: [arXiv:2209.15356](https://arxiv.org/abs/2209.15356))

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

## Acknowledgments

* The code in this repository uses the Java Information Dynamics Toolkit (JIDT) for the calculation of Transfer Entropy. The JIDT is available [here](https://github.com/jlizier/jidt/).
* This work was inspired by the research conducted at KTH-FlowAI. You can find more of their work [here](https://github.com/KTH-FlowAI).
