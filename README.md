# Transfer Entropy Calculation for Temporal Signals

This repository contains MATLAB scripts for calculating Transfer Entropy (TE) from temporal signals. The scripts are designed to be easy to use and modify for your specific needs. The main script sets the parameters for the TE calculation and calls the main function, which performs the actual calculations.

## Introduction

This code is based on the research paper titled ["Causality analysis of large-scale structures in the flow around a wall-mounted square cylinder", Martínez-Sánchez *et al.*](https://arxiv.org/abs/2209.15356) (2023, *Journal of Fluid Mechanics*). In this work, the authors conducted a causality analysis of the large-scale structures in the flow around a wall-mounted square cylinder. The aim was to analyze the formation mechanisms of these coherent structures and their impact on pollutant transport within cities. The authors assessed the causal relations between the modes of a reduced-order model obtained by applying proper-orthogonal decomposition to high-fidelity-simulation data of the flow case under study. The causal relations were identified using conditional transfer entropy, which is an information-theoretical quantity that estimates the amount of information contained in the past of one variable about another. This allowed for an understanding of the origins and evolution of different phenomena in the flow and the identification of the modes responsible for the formation of the main vortical structures.

Watch the video below for a brief introduction to the project:

[![](https://markdown-videos.deta.dev/youtube/6FSBU9wrqkY)](https://youtu.be/6FSBU9wrqkY)


## Getting Started

To get started, you will need to have MATLAB installed on your computer. You will also need to download the Java Information Dynamics Toolkit (JIDT) from [here](https://github.com/jlizier/jidt/).

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
