# CS327 Project 4 setup

Install Python 3 if you need it. From the repository root on Windows, run:

```powershell
python project4/setup_project.py
```

On macOS, run `python3 project4/setup_project.py` from the repository root.

Enter the course download password when prompted. The setup creates
`cs327-p4-env`, installs
NetworkX, Matplotlib, and pygeoip, downloads the course examples and datasets,
and extracts `GeoIP.dat`. Allow at least 1 GB of free disk space. If setup is
interrupted, rerun it. Files already downloaded are reused.

To activate the environment later, open a terminal in `project4` and run
`.\cs327-p4-env\Scripts\Activate.ps1` in PowerShell or
`source cs327-p4-env/bin/activate` in the macOS terminal.

Commit the setup files and your own source code. The virtual environment,
downloaded course files, and example outputs are ignored by Git.
