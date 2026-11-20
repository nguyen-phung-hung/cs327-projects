"""Set up the CS327 Project 4 environment and download course files."""

import argparse
from contextlib import contextmanager
from getpass import getpass
import gzip
import os
from pathlib import Path
import shutil
import subprocess
import sys
from tempfile import NamedTemporaryFile
from urllib.request import (
    HTTPBasicAuthHandler,
    HTTPPasswordMgrWithPriorAuth,
    build_opener,
)


PROJECT = Path(__file__).resolve().parent
COURSE = "https://users.cs.jmu.edu/wangxx/Web/Tools/code/graphproject/bitcoin"
DATA = "https://crypto.cs.jmu.edu/bitcoin"
GRAPH = "700579-bitcoingraph-cleaned.graphml"
GRAPH_URL = f"{DATA}/127.0.1.1_l14340.cs.jmu.edu_2018-03-18-06-45-12-{GRAPH}"
EXAMPLES = {
    "script01.py": f"{COURSE}/script01.py",
    "nxexample01.py": f"{COURSE}/nxexample01.py.txt",
    "geoipexample01.py": f"{COURSE}/geoipexample01.py.txt",
}


def installed(path):
    return path.is_file() and path.stat().st_size > 0


@contextmanager
def write_atomically(destination):
    output = NamedTemporaryFile(
        dir=destination.parent, prefix=destination.name + ".", suffix=".part", delete=False
    )
    temporary = Path(output.name)
    try:
        with output:
            yield output
        temporary.replace(destination)
    finally:
        if temporary.exists():
            temporary.unlink()


def download(opener, url, destination):
    if installed(destination):
        print(f"Already present: {destination.name}")
        return

    print(f"Downloading {destination.name}...", flush=True)
    with opener.open(url, timeout=60) as source, write_atomically(destination) as output:
        shutil.copyfileobj(source, output, length=1024 * 1024)
        expected_size = source.headers.get("Content-Length")
        if output.tell() == 0 or (
            expected_size is not None and output.tell() != int(expected_size)
        ):
            raise OSError(f"Incomplete download: {destination.name}")


def install_environment():
    environment = PROJECT / "cs327-p4-env"
    python = environment / ("Scripts/python.exe" if os.name == "nt" else "bin/python")
    if not python.is_file():
        print(f"Creating {environment.name}...", flush=True)
        subprocess.run([sys.executable, "-m", "venv", str(environment)], check=True)
    subprocess.run(
        [str(python), "-m", "pip", "install", "wheel", "networkx", "matplotlib", "pygeoip"],
        check=True,
    )
    return python


def install_files():
    geoip = PROJECT / "GeoIP.dat"
    archive = PROJECT / "GeoIP.dat.gz"

    course_files = {PROJECT / name: url for name, url in EXAMPLES.items()}
    if not installed(geoip):
        course_files[archive] = f"{COURSE}/GeoIP.dat.gz"

    if any(not installed(path) for path in course_files):
        password = getpass("JMU course download password for cs327: ")
        passwords = HTTPPasswordMgrWithPriorAuth()
        for url in course_files.values():
            passwords.add_password(None, url, "cs327", password, is_authenticated=True)
        opener = build_opener(HTTPBasicAuthHandler(passwords))
        for path, url in course_files.items():
            download(opener, url, path)

    if not installed(geoip):
        print(f"Extracting {geoip.name}...", flush=True)
        with gzip.open(archive, "rb") as source, write_atomically(geoip) as output:
            shutil.copyfileobj(source, output, length=1024 * 1024)

    public = build_opener()
    download(public, f"{DATA}/small-bitcoingraph.graphml", PROJECT / "small-bitcoingraph.graphml")
    download(public, GRAPH_URL, PROJECT / GRAPH)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.parse_args()
    try:
        python = install_environment()
        install_files()
    except (OSError, EOFError, subprocess.CalledProcessError, KeyboardInterrupt) as error:
        parser.exit(1, f"Setup did not finish: {error or 'interrupted'}\n")

    print(f"Setup complete. Project directory: {PROJECT}")
    print(f"Project Python: {python}")


if __name__ == "__main__":
    main()
