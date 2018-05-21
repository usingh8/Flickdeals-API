# flickdeals-api
Flickdeals api is used to get movies data from the database.
Running Fuseki

	1- Run jar file-
		java -Xmx6g -jar fuseki-server.jar
		
	2- Goto- localhost:3030
	
	3- Create dataset with 'Flickdeals-API' name
	
	4- Upload all RDFs to newly created dataset.


Instructions to run Flickdeals-API project-
	
	1- Clone this project on your local disk
	
	2- Import cloned project as maven project in eclipse
	
	3- Go To src/main/java/edu/asu/ser594/flickdeals/service/impl
	
		Open system.properties
		Update Fuseki server url, port and dataset name
		
	4- Maven Build (clean install)
	
	5- Run on Server
	
