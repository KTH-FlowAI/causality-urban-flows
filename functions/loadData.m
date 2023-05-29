% Helper Function to Load the Data
%
% This function loads the data from the specified file and selects the specified number of modes.
%
% This function should not need to be modified by the user.

function data = loadData(file, var, numModes)
    % Load the data
    ALL_data = load(file);
    
    % Select the specified number of modes
    data = ALL_data.(var);
    data = data(:,1:numModes);
end