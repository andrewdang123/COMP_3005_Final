# COMP_3005_Final
**Fitness Database**

Andrew Dang 101297865 
Dylan Nakamura 101306225

Final Assignment for COMP 3005

Video Link: https://youtu.be/lsxquLjcUhg

What is needed to run:
Download Maven: https://maven.apache.org/download.cgi 
    Download Binary zip archive, extract to a location of choice. 
    Search Edit the System Environment Variables -> Environment Variables -> System Variables -> Path -> New -> Directory of the "bin" of the extracte Maven Folder

Bash Scripts:
    Running the whole Project       ./project.sh
    Running Main:                   ./run_main.sh
    Running Main with traces:       ./debugging.sh
    Repopulating Database:          ./restart_db.sh
    Viewing Database:               ./show_db.sh

For Viewing the Database:
    Run the command ./show_db.sh
    Open http://localhost:8082
    Leave username as "sa" and Password as "" (should be default)
    In the JDBC URL field, enter the directory of testdb on your device
        For example, my directory is
            jdbc:h2:~/SchoolCode\COMP 3005\COMP_3005_Final\data\testdb

Video Link: https://youtu.be/lsxquLjcUhg