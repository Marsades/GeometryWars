# TestRepository
A repository for various tests.
usage: java -jar LiMPO4_sim.jar

list of commands:
  help, setCommon, setVars, printParam, setDir, resetDir, printDir, mkDir, 
  loadConfigFile, saveConfigFile, enableConfigFile, disableConfigFile, 
  scanT, scanHx, scanHy, scanHz, enableVis, disableVis, 

help
	Writes this document.

setParam MCS Nx Ny Nz element
    This command sets MCS, the number of monte carlo steps to use for the simulations, Nx, Ny, Nz, the dimensions of the system to simulate, and El, the initial element to use in the simulations.
    Example: setParam 10000 4 4 6 Mn
    
setVars T Bx By Bz
    This command sets the default variables to use for scanning through the (T,Bx,By,Bz), that is the values which are not scanned, will be set to these default values.
    Example: setVars 4 0 0 0
    
printParam
    This command prints the current parameters for the simulations, MCS, Nx, Ny, Nz, Element. 
    
printVars
    This command prints the current 
    
scanT T_i dT T_f
    Appends a list of points in (B,T) space, along the T-axis, to the current simulation queue. T_i and T_f, are the initial and final temperature respectively and dT is the size of the temperature steps.

scanT T_i dT T_f
    Appends a list of points in (B,T) space, along the T-axis, to the current simulation queue. T_i and T_f, are the initial and final temperature respectively and dT is the size of the temperature steps.
    
scanBx B_i dB B_f
    Similar to scanT, it just scans along the Bx axis instead
    
scanBy B_i dB B_f
    Similar to scanT, it just scans along the By axis instead
    
scanBz B_i dB B_f
    Similar to scanT, it just scans along the Bz axis instead
    
runSim outfile
    Starts the simulation with the current queue, and prints results to the file outfile. 
