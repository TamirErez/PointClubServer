# PointClubServer
The Main Server for the Official Point Club.

# Server Main Features:
 1. Handle all communication between users.
 2. Save all data as backup and pass it to users.

# Set Up Server on Raspberry-Pi
## Install Java 11
 Run the command <i>sudo apt-get install openjdk-11-jdk</i>

## Install Postgresql
 1. Run the command <i>sudo apt-get install postgresql</i>
 2. Find pg_hba.conf, which is usually located at <i>/etc/postgresql/<number>/main</i>
 3. Edit it by changing all 'METHOD' options which are not peer, to 'trust'  
    The lines should look like: host all all 127.0.0.1/32 <b>trust</b>
 4. Add user 'postgres' to sudoers by running <i>sudo usermod -aG sudo postgres</i>
 5. Switch to user postgres and edit its password in psql:  
   5.1. sudo -i -u postgres  
   5.2. psql  
   5.3. ALTER USER postgres WITH PASSWORD 'postgres';  
 6. Restart postgres by running: <i>sudo systemctl restart postgresql</i>
 
 ## Allow Remote Connection to Postgre
 1. Find and edit pg_hba.conf by following the previous section
 2. Add the following lines at the end of the file:  
  host all all 0.0.0.0/0 trust   
  host all all ::0/0 trust
 3. Restart postgres by running: <i>sudo systemctl restart postgresql</i>
