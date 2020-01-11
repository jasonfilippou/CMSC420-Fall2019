% A demo to investigate efficiency of column - based 
% operations in MATLAB as opposed to row - based operations.
% You will need to run this in MATLAB or Octave.

% Some variables we can change at will:
N = 10000; % Might need to tweak biggest array size through Home->Preferences->MATLAB->Workspace
WAIT=2;

% N X N matrix with 8 byte doubles
% drawn uniformally from (0, 1) (non-inclusive):
M = rand(N) * 12000;


fprintf('Summing matrix column-wise...\n');
% Busy - wait for WAIT seconds 
pause(WAIT);

% Measure column-wise (vertical) summation time in seconds:
% We expect this to be fast!
t = cputime;
sum(M, 1);
fprintf('Summing matrix column-wise (across rows) took %.4f seconds. \n', cputime - t);

fprintf('Summing matrix row-wise...\n');
pause(WAIT);

% Measure row-wise (horizontal) summation time in seconds:
% We expect this to be slow!
t = cputime;
sum(M, 2);
fprintf('Summing matrix row-wise (across columns) took %.4f seconds. \n', cputime - t);
