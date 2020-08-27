# GUI  Testing Documentation 

Authors: Marco Bellavia (s280130), Gabriel Ganzer (s271961), Gianluca Morabito (s277943), Francesco Xia (s277509)

Date: 02/06/2020

Version: 1

# GUI testing

This part of the document reports about testing at the GUI level. Tests are end to end, so they should cover the Use Cases, and corresponding scenarios.

## Coverage of Scenarios and FR

### 

| Scenario ID | Functional Requirements covered | GUI Test(s) |
| ----------- | ------------------------------- | ----------- | 
| UC1         | FR1.1 - FR1.2  |  UC1.sikuli --> UC1.py    |             
| UC2         | FR1.1 - FR1.2  |  UC2.sikuli --> UC2.py    | 
| UC3         | FR1.1 - FR1.2  |  UC3_Admin.sikuli --> UC3_Admin.py    |              
| UC4         | FR3.1 - FR3.2  |  UC4.sikuli --> UC4.py    |           
| UC5         | FR3.1 - FR3.2  |  UC5.sikuli --> UC5.py    |               
| UC6         | FR3.1 - FR3.2  |  UC6.sikuli --> UC6.py    |            
| UC7         | FR1.1 - FR1.2 - FR3.1 - FR3.2 - FR5.1 |  UC7.sikuli --> UC7.py    |               
| UC8         | FR3.1 - FR3.2 - FR3.1 - FR3.2 - FR4.3 - FR4.4 - FR5.1 |  UC8.sikuli --> UC8.py    | 
| UC10        | FR1.1 - FR1.2 - FR3.1 - FR3.2 - FR5.1 - FR5.2 - FR5.3    |  UC10.sikuli --> UC10.py    |             


# REST  API  Testing

This part of the document reports about testing the REST APIs of the back end. The REST APIs are implemented by classes in the Controller package of the back end. 
Tests should cover each function of classes in the Controller package

## Coverage of Controller methods


<Report in this table the test cases defined to cover all methods in Controller classes >

| class.method name | Functional Requirements covered |REST  API Test(s) | 
| ----------- | ------------------------------- | ----------- | 
| UserController.getUserById() 		  	       | FR1.4         | TestController.testGetUserById() 		|     
| UserController.getAllUsers() 		               | FR1.3         | TestController.testGetAllUsers() 		| 
| UserController.saveUser()    		               | FR1.1         | TestController.testSaveUser() 			| 
| UserController.deleteUser()  		               | FR1.2         | TestController.testDeleteUser() 		| 
| UserController.login()       		   	       | FR2           | TestController.testLogin() 			| 
| UserController.decreaseUserReputation()  	       | FR5.2 - FR5.3 | TestController.testDecreaseUserReputation() 	| 
| UserController.increaseUserReputation()              | FR5.2 - FR5.3 | TestController.testIncreaseUserReputation() 	|
| GasStationController.getGasStationById()             | FR3.1 - FR3.2 - FR5 | TestController.testGetGasStationById() 		|
| GasStationController.getAllGasStations()             | FR3.3         | TestController.testGetAllGasStations() 		|
| GasStationController.saveGasStation()                | FR3.1         | TestController.testSaveGasStation() 		|
| GasStationController.deleteGasStation()              | FR3.2         | TestController.testDeleteGasStation() 		|
| GasStationController.getGasStationsByGasolineType()  | FR4.4 - FR4.5 | TestController.testGetGasStationsByGasolineType() |
| GasStationController.getGasStationsByProximity()     | FR4.1         | TestController.testGetGasStationsByProximity() 	|
| GasStationController.getGasStationsWithCoordinates() | FR4.2         | TestController.testGetGasStationsWithCoordinates()|
| GasStationController.setGasStationReport()           | FR5.1 - FR5.2 | TestController.testSetGasStationReport() 		|

