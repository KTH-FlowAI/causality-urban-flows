% Helper Function to Calculate the TE for a Single Pair of Source and Destination Arrays
%
% This function calculates the TE for a single pair of source and destination arrays, given the specified parameters.
%
% This function should not need to be modified by the user.

function [result, pVal, effecResult] = calculateSingleTE(teCalc, sourceArray, destArray, condData, len, embedding, lag, numCond, k, numPermutations)

% Prepare the TE calculator
teCalc.initialise(len, embedding, len, embedding, lag, ones(1,numCond)*len, ones(1,numCond)*embedding, ones(1,numCond)*lag);
teCalc.setProperty('k', string(k));

% Set the observations
teCalc.setObservations(sourceArray, destArray, condData);

% Calculate the TE
result = teCalc.computeAverageLocalOfObservations();

% Perform significance testing
nullDistrib = teCalc.computeSignificance(numPermutations);
pVal = nullDistrib.pValue;
distrib = nullDistrib.distribution;
effecResult = mean(distrib);

end
