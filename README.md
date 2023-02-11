# PointClubServer
The Main Server for the Official Point Club.

# Server Main Features:
 1. Handle all communication between users.
 2. Save all data as backup and pass it to users.

# Set Up Server on Raspberry-Pi
## Install Postgresql
 1. Run the command <i>sudo apt-get install postgresql</i>
 2. Find pg_hba.conf, which is usually located at <i>/etc/postgresql/13/main</i>
 3. Edit it by changing all 'md5' options to 'trust'  
    The lines should look like: host all all 127.0.0.1/32 <b>trust</b>
 4. Switch to user postgres and edit its password in psql:
   1. sudo -u -i postgres
   2. psql
   3. ALTER USER postgres WITH PASSWORD 'postgres';
 5. Restart postgres by running: <i>sudo systemctl restart postgresql</i>
   
