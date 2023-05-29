% Main Function for Calculating Transfer Entropy (TE) from Temporal Signals
%
% This function performs the actual TE calculations based on the parameters set in the main script.
% The results are saved in the specified location.
%
% This function should not need to be modified by the user. All user-configurable parameters should be set in the main script.

function [resultMatrix, significanceMatrix, effecMatrix] = calculateTE(jarLoc, saveLoc, numModes, lags, embedding, len, numNeighbors, numPermutations, name, dataFile, dataVar)

% Start timing the execution of the entire script
fullScriptTime = tic; 

% Create the save location if it doesn't exist
mkdir(saveLoc);

% Get a list of files already completed
contents = dir(saveLoc);
names_completed = {contents.name};

% Save a backup of this execution file
filename = [mfilename];
t = datetime('now');
t_str = datestr(t);
t_str = strrep(t_str, ':', '_');
backup = char(compose('%s/%s %s.txt', saveLoc, filename, t_str));
currentfile = strcat(filename, '.m');
copyfile(currentfile, backup);

% Load the JIDT library
javaaddpath(jarLoc)

% Create the TE calculator
teCalc = javaObject('infodynamics.measures.continuous.kraskov.ConditionalTransferEntropyCalculatorKraskov');

% Loop over all specified numbers of neighbors
for k_index = 1:length(numNeighbors)
    % Loop over all specified lags
    for lag_index = 1:length(lags)
   
       % Get the current number of neighbors and lag
       k = numNeighbors(k_index);
       lag = lags(lag_index);
       
       % If the length of past history is set to 'lag', use the current lag as the length
       if strcmp(len,"lag")
           len=lag;
           len_is_lag = true;
       else
           len_is_lag = false;
       end

       % Compose the filename for the current run
       thisName = compose(name,lag,embedding,len,k,numPermutations);

       % Check if this file has been calculated for already
       if any(contains(names_completed,thisName))
           % If it has, skip this run
           msg = ["Skipping sequence filename: ", thisName];
           disp(msg)
           if len_is_lag
               len="lag";
           end
           continue
       end

       % Load the data
       data = loadData(dataFile, dataVar, numModes);
       
       % Get the size of the data
       [numRow, numCol] = size(data);
       numCond = numCol - 2; % Number of conditions

       % Initialize matrices to store the results
       resultMatrix = zeros(numCol);
       significanceMatrix = zeros(numCol);
       effecMatrix = zeros(numCol);

       % Start timing this run
       singleMatrixRun = tic;

       % Iterate through each column
        for i = 1:numCol % Source Array
            for j = 1:numCol % Destination Array
                if i~=j % Ignoring self-causal interactions
                    % Start timing this TE calculation
                    singleTE = tic;
                    disp('Beginning TE calc...');
                    sourceArray = data(:,i);
                    destArray = data(:,j);
                    colsRemove = [i j];
                    condData = data;
                    condData(:, colsRemove) = [];

                    % Calculate the TE for this pair of source and destination arrays
                    [result, pVal, effecResult] = calculateSingleTE(teCalc, sourceArray, destArray, condData, len, embedding, lag, numCond, k, numPermutations);

                    % Store the result in the appropriate matrix
                    resultMatrix(i,j) = result;
                    significanceMatrix(i,j) = pVal;
                    effecMatrix(i,j) = effecResult;

                    % End timing this TE calculation
                    endTE = toc(singleTE);
                    fprintf('Single TE Calc time: %4.2f seconds\n', endTE);
                end
            end
        end % End of the matrix

        % If the length of past history was set to 'lag', reset it to 'lag'
        if len_is_lag
            len='lag';
        end

        % Store the results
        % The results are saved in the specified save location with the current filename
        fileLoc = append(saveLoc, thisName);
        fileLoc = char(fileLoc);
        save(fileLoc, 'resultMatrix', 'significanceMatrix', 'effecMatrix');

        % End timing this run
        endMatrixRun = toc(singleMatrixRun);
        message = 'Runtime for most recent causal matrix: %4.2f seconds\n';
        fprintf(message, endMatrixRun);
           
   end
end

% End timing the execution of the entire script
endScriptTime = toc(fullScriptTime);
message = 'Runtime for entire script: %4.2f seconds\n';
fprintf(message,endScriptTime);

end
