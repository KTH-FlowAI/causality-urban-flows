% Main Script for Calculating Transfer Entropy (TE) from Temporal Signals
%
% This script sets the parameters for the TE calculation and calls the main function.
% The main function (calculateTE.m) performs the actual calculations.
%
% Users should modify the parameters in this script to suit their needs.
% The main function and any helper functions should not need to be modified.

clc; clear all; close all; addpath('functions');

%% Define the data file and variable name
dataFile = '/path/to/my_data.mat';
dataVar = 'my_variable';

%% Define parameters for execution
% Number of modes to calculate causality for. This depends on your data and research question.
numModes = 2; 

% Different lags to consider. It can be a vector of values. These values can be adjusted based on your data and research question.
lags = [1]; 

% Embedding dimension. This is typically set to 1 for time series data, but can be adjusted based on your data and research question.
embedding = 1; 

% Length of past history to consider. This is typically set to the same value as the lag.
len = 'lag'; 

% Number of nearest neighbors for the Kraskov estimator. It can be a vector of values. This is typically set to 4, but can be adjusted based on your data and research question.
numNeighbors =  [5]; 

% Number of permutations for significance testing. This is typically set to 100, but can be adjusted based on your data and research question.
numPermutations = 100; 

% Format for the result filenames
name = 'Lag%d_Embed%d_Length%d_K%d_%dPermutations'; 

%% Define the location of the JIDT library
% This should be the path to your infodynamics.jar file
jarLoc = "/path/to/infodynamics.jar"; 

% Define a location to save results
% This should be the directory where you want to save your results
saveLoc = "/path/to/save/location"; 

% Call the main function
% This function performs the actual TE calculations and saves the results
calculateTE(jarLoc, saveLoc, numModes, lags, embedding, len, numNeighbors, numPermutations, name);
