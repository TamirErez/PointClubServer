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
 2. Find pg_hba.conf, which is usually located at <i>/etc/postgresql/ < version > /main</i>
 3. Edit it by changing all 'METHOD' options which are not peer, to 'trust'  
    The lines should look like: host all all 127.0.0.1/32 <b>trust</b>
 4. Add user 'postgres' to sudoers by running <i>sudo usermod -aG sudo postgres</i>
 5. change user 'postgres' password to 'postgres' by running <i>passwd postgres</i>
    and typing the password 'postgres'
 6. Switch to user postgres and edit its password in psql:  
   6.1. sudo -i -u postgres  
   6.2. psql  
   6.3. ALTER USER postgres WITH PASSWORD 'postgres';  
 7. Restart postgres by running: <i>sudo systemctl restart postgresql</i>
 
 ## Allow Remote Connection to Postgre
  1. Find and edit pg_hba.conf by following the previous section
  2. Add the following lines at the end of the file:  
   host all all 0.0.0.0/0 trust   
   host all all ::0/0 trust
  3. Edit the file /etc/postgresql/ < version > /main/postgresql.conf  
     Uncomment the line containing 'listen_addresses', and change the value to '*'
  4. Restart postgres by running: <i>sudo systemctl restart postgresql</i>

## NoIp
### Overview
NoIp allows free DNS of our server.  
See https://www.noip.com/ for more info.  
In order to periodically update the IP address used by our hostname, we need to run NoIp's client in the background.

### Install NoIp  
Run the following Commands:  
 1. cd /usr/local/src
 2. wget http://www.no-ip.com/client/linux/noip-duc-linux.tar.gz
 3. tar xzf noip-duc-linux.tar.gz
 4. cd noip-2.1.9-1
 5. apt install make
 6. apt install gcc
 7. make
 8. make install
 
### Run NoIp on Startup
 1.Create the file /etc/systemd/system/noip2.service with the following content:  
  ```
  [Unit]
  Description=noip2 service

  [Service]
  Type=forking
  ExecStart=/usr/local/bin/noip2
  Restart=always

  [Install]
  WantedBy=default.target
  ```
 2. Run the command <i>sudo systemctl daemon-reload</i>
 3. Run the command <i>sudo systemctl enable noip2</i>
