# TestRepository
A repository for various tests.
usage: sim.jar command arguments

list of commands:
  help, setCommon, setVars, printParam, setDir, resetDir, printDir, mkDir, 
  loadConfigFile, saveConfigFile, enableConfigFile, disableConfigFile, 
  scanT, scanHx, scanHy, scanHz, enableVis, disableVis, 
      
  -help, Writes this text.
			
  -scanT
    Makes a scan ramping up or down the temp field.
    Arguments after the command should be in the format:
					T_start, dT, T_end

  -scanHx
    Makes a scan ramping up or down the Hx field.
    Arguments after the command should be in the format:
					Hx_start, dHx, nHx, temp, Hy, Hz

  -scanHy
    Makes a scan ramping up or down the Hy field.
    Arguments after the command should be in the format:
      Hy_start, dHz, nHy, temp, Hx, Hz

  -scanH
    Makes a scan ramping up or down the Hz field.
    Arguments after the command should be in the format:
      Hz_start, dHz, nHz, temp, Hx, Hy

  -scanTHx
    Makes a scan ramping up or down the Hz field.
    Arguments after the command should be in the format:
      Hx_start, dHx, Hx_end, T_start, dT, T_end

  -scanTHy
    Makes a scan ramping up or down the Hz field. 
    Arguments after the command should be in the format:
      Hy_start, dHy, Hy_end, T_start, dT, T_end

  -scanTHz
    Makes a scan ramping up or down the Hz field. 
    Arguments after the command should be in the format:
      Hz_start, dHz, Hz_end, T_start, dT, T_end
