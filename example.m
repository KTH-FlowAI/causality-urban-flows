% Example Script for Calculating Transfer Entropy (TE) from Temporal Signals
%
% This script creates five time-varying variables, saves them to a .mat file, and then calculates the transfer entropy between them.
% The results are saved in a folder named "example".
%
% Users should modify the parameters in this script to suit their needs.
% The main function and any helper functions should not need to be modified.

clc; clear all; close all; addpath('functions');

mkdir('example')

%% Create five time-varying variables of length 10,000
% The data should be a 2D matrix where each column represents a different variable (mode) and each row represents a different time point.
% The shape of the data should be Nt x Nm, where Nt is the number of time points and Nm is the number of modes.
% In this example, we create a matrix of random numbers with 10000 rows (time points) and 5 columns (modes).
data = rand(10000, 5);

% Save the data to a .mat file
% The data is saved as a variable named 'data' in a .mat file. The name of the variable and the file can be changed as needed.
dataVar = 'data';
dataFile = 'example/my_data.mat';
save(dataFile, 'data');

%% Plot the variables
figure('Position', [10 10 1200 400]); % Increased figure width
hold on;
for i = 1:2
    plot(data(1:50,i));
end
legend('Variable 1', 'Variable 2');
title('Time-varying variables');
xlabel('Time');
set(gca,'FontSize',14,'FontName','Times')

%% Define parameters for execution
numModes = 5; % Number of modes to calculate causality for.
lags = [1]; % Different lags to consider.
embedding = 1; % Embedding dimension.
len = 'lag'; % Length of past history to consider.
numNeighbors =  [5]; % Number of nearest neighbors for the Kraskov estimator.
numPermutations = 10; % Number of permutations for significance testing.
name = 'Lag%d_Embed%d_Length%d_K%d_%dPermutations'; % Format for the result filenames.

%% Define the location of the JIDT library
jarLoc = "JIDT/infodynamics.jar"; 

% Define a location to save results
saveLoc = "example/"; 

% Call the main function
[resultMatrix, significanceMatrix, effecMatrix] = calculateTE(jarLoc, saveLoc, numModes, lags, embedding, len, numNeighbors, numPermutations, name, dataFile, dataVar);

%% Plot the results
figure;
% Normalize the resultMatrix
normalizedResultMatrix = resultMatrix / max(resultMatrix(:));
imagesc(normalizedResultMatrix');
colormap(flipud(bone)); % Use bone colormap
c = colorbar;
caxis([0 1]); % Set colorbar limits from 0 to 1
c.Label.String = 'Normalized Transfer Entropy';
title('Transfer Entropy Results');
xlabel('Effect');
ylabel('Cause');
axis equal tight;
set(gca,'FontSize',14,'FontName','Times')

% Set the x and y tick labels to integers
xticks(1:numModes);
yticks(1:numModes);
