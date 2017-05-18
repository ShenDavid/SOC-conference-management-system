**Purpose of this project:**

Develop a conference management system.


**Prerequisite:**

Make sure java is installed by running ‘java -version’
Install activator and set the path

**how to download and install the software**

Get the source code from github
	git clone -b final git@github.com:cmusv-sc/18655-Spring-2017-Team-3.git
Start your mysql
Change the mysql password to yours
	In backend/conf/application.conf line 16:
	default.password="" -> add you mysql password

 **how to use the software**
 
Create a database named ‘playdbtest’
Run run.sh in backend/:
	cd backend
	./run.sh
	*if run.sh can not run, run the following command:
	Chmod +x run.sh
Run run.sh in frontend/:
cd frontend
./run.sh
Build database first by entering http://localhost:9000 in browser first. An error saying ‘can not get /’ may occur, just ignore it. Enter http://localhost:8080 in browser.