# Design Document 

Authors: Marco Bellavia (s280130), Gabriel Ganzer (s271961), Gianluca Morabito (s277943), Francesco Xia (s277509)

Date:    18/05/2020

Version: 1.1

Changelog v1.1:
* rewrite uml diagrams to reflect changes in the code base 
    * update traceability matrix
* add class dependencies


# Contents

- [High level design](#high-level-design)
- [Low level design](#low-level-design)
- [Verification traceability matrix](#verification-traceability-matrix)
- [Verification sequence diagrams](#verification-sequence-diagrams)

# Instructions

The design must satisfy the Official Requirements document (see EZGas Official Requirements.md ). <br>
The design must comply with interfaces defined in package it.polito.ezgas.service (see folder ServicePackage ) <br>
UML diagrams **MUST** be written using plantuml notation.

# High level design 

The style selected is client - server. Clients can be smartphones, tablets, PCs.
The choice is to avoid any development client side. The clients will access the server using only a browser. 

The server has two components: the frontend, which is developed with web technologies (JavaScript, HTML, Css) and is in charge of collecting user inputs to send requests to the backend; the backend, which is developed using the Spring Framework and exposes API to the front-end.
Together, they implement a layered style: Presentation layer (front end), Application logic and data layer (back end). 
Together, they implement also an MVC pattern, with the V on the front end and the MC on the back end.



```plantuml
@startuml
package "Backend" {

}

package "Frontend" {

}


Frontend -> Backend
@enduml


```


## Front End

The Frontend component is made of: 

Views: the package contains the .html pages that are rendered on the browser and that provide the GUI to the user. 

Styles: the package contains .css style sheets that are used to render the GUI.

Controller: the package contains the JavaScript files that catch the user's inputs. Based on the user's inputs and on the status of the GUI widgets, the JavaScript controller creates REST API calls that are sent to the Java Controller implemented in the back-end.


```plantuml
@startuml
package "Frontend" {

    package "it.polito.ezgas.resources.views" {

    }


package "it.polito.ezgas.resources.controller" {

    }


package "it.polito.ezgas.resources.styles" {

    }



it.polito.ezgas.resources.styles -down-> it.polito.ezgas.resources.views

it.polito.ezgas.resources.views -right-> it.polito.ezgas.resources.controller


}
@enduml

```

## Back End

The backend  uses a MC style, combined with a layered style (application logic, data). 
The back end is implemented using the Spring framework for developing Java Entrerprise applications.

Spring was selected for its popularity and relative simplicity: persistency (M and data layer) and interactions are pre-implemented, the programmer needs only to add the specific parts.

See in the package diagram below the project structure of Spring.

For more information about the Spring design guidelines and naming conventions:  https://medium.com/the-resonant-web/spring-boot-2-0-project-structure-and-best-practices-part-2-7137bdcba7d3



```plantuml
@startuml
package "Backend" {

package "it.polito.ezgas.service"  as ps {
   interface "GasStationService"
   interface "UserService"
} 


package "it.polito.ezgas.controller" {

}

package "it.polito.ezgas.converter" {

}

package "it.polito.ezgas.dto" {

}

package "it.polito.ezgas.entity" {

}

package "it.polito.ezgas.repository" {

}

    
}
note "see folder ServicePackage" as n
n -- ps
```



The Spring framework implements the MC of the MVC pattern. The M is implemented in the packages Entity and Repository. The C is implemented in the packages Service, ServiceImpl and Controller. The packages DTO and Converter contain classes for translation services.



**Entity Package**

Each Model class should have a corresponding class in this package. Model classes contain the data that the application must handle.
The various models of the application are organised under the model package, their DTOs(data transfer objects) are present under the dto package.

In the Entity package all the Entities of the system are provided. Entities classes provide the model of the application, and represent all the data that the application must handle.




**Repository Package**

This package implements persistency for each Model class using an internal database. 

For each Entity class, a Repository class is created (in a 1:1 mapping) to allow the management of the database where the objects are stored. For Spring to be able to map the association at runtime, the Repository class associated to class "XClass" has to be exactly named "XClassRepository".

Extending class JpaRepository provides a lot of CRUD operations by inheritance. The programmer can also overload or modify them. 



**DTO package**

The DTO package contains all the DTO classes. DTO classes are used to transfer only the data that we need to share with the user interface and not the entire model object that we may have aggregated using several sub-objects and persisted in the database.

For each Entity class, a DTO class is created (in a 1:1 mapping).  For Spring the Dto class associated to class "XClass" must be called "XClassDto".  This allows Spring to find automatically the DTO class having the corresponding Entity class, and viceversa. 




**Converter Package**

The Converter Package contains all the Converter classes of the project.

For each Entity class, a Converter class is created (in a 1:1 mapping) to allow conversion from Entity class to DTO class and viceversa.

For Spring to be able to map the association at runtime, the Converter class associated to class "XClass" has to be exactly named "XClassConverter".




**Controller Package**

The controller package is in charge of handling the calls to the REST API that are generated by the user's interaction with the GUI. The Controller package contains methods in 1:1 correspondance to the REST API calls. Each Controller can be wired to a Service (related to a specific entity) and call its methods.
Services are in packages Service (interfaces of services) and ServiceImpl (classes that implement the interfaces)

The controller layer interacts with the service layer (packages Service and ServieImpl) 
 to get a job done whenever it receives a request from the view or api layer, when it does it should not have access to the model objects and should always exchange neutral DTOs.

The service layer never accepts a model as input and never ever returns one either. This is another best practice that Spring enforces to implement  a layered architecture.



**Service Package**


The service package provides interfaces, that collect the calls related to the management of a specific entity in the project.
The Java interfaces are already defined (see file ServicePackage.zip) and the low level design must comply with these interfaces.


**ServiceImpl Package**

Contains Service classes that implement the Service Interfaces in the Service package.

# Low level design

### it.polito.ezgas.entity
```plantuml
@startuml
'class diagram for "it.polito.ezgas.entity"
left to right direction
package "it.polito.ezgas.entity"{
class User{
--private fields--
-userId : Integer
-username : String
-password : String
-email : String
-reputation : Integer
-admin: Boolean
--public methods--
+getters()
+setters()
}

class GasStation{
--private fields--
-gasStationId : Integer
-gasStationName : String
-gasStationAddress : String
-lat : double
-long : double
-hasDiesel : Boolean
-hasSuper : Boolean
-hasSuperPlus : Boolean
-hasGas : Boolean
-hasMethane : Boolean
-carSharing : String
-lat : double
-lon : double
-dieselPrice : double 
-superPrice : double 
-superPlusPrice : double 
-gasPrice : double 
-methanePrice : double 
-reportUser : Integer
-reportTimestamp : String
-reportDependability : double
-user : User
--public methods--
+getters()
+settters()
}

class PriceReport{
-priceReportId: Integer
-user: User
-dieselPrice : double
-superPrice : double
-superPlusPrice : double
-gasPrice : double
-methanePrice : double
--public methods--
+getters()
+settters()
}
}
@enduml
```

### it.polito.ezgas.dto
```plantuml
@startuml
left to right direction
'class diagram for "it.polito.ezgas.dto"

package "it.polito.ezgas.dto"{
class UserDto{
-userId : Integer
-username : String
-password : String
-email : String
-reputation : Integer
-admin : Boolean
--public methods--
+getters()
+setters()
}

class GasStationDto{
--private fields--
-gasStationId : Integer
-gasStationName : String
-gasStationAddress : String
-lat : double
-log : double
-hasDiesel : Boolean
-hasSuper : Boolean
-hasSuperPlus : Boolean
-hasGas : Boolean
-hasMethane : Boolean
-carSharing : String
-reportUser : Integer
-reportTimestamp : String
-reportDependability: Integer
-dieselPrice : double
-superPrice : double
-superPlusPrice : double
-gasPrice : double
-methanePrice : double
-userDto : userDto
--public methods--
+getters()
+setters()
}

class LoginDto{
--private fields--
-token : String
-userId : Integer
-username : String
-email : String
-reputation : Integer
-admin : Boolean
--public methods--
+getters()
+setters()
}

class IdPw{
--private fields--
-email: String
-password : String
--public methods--
+getters()
+setters()
}

class PriceReportDto{
--private fields--
priceReportId : Integer
user: User
dieselPrice: double
superPrice: double
superPlusPrice: double
gasPrice: double
methanePrice: double
--public methods--
+getters()
+setters()
}
}
@enduml
```

### it.polito.ezgas.converter
```plantuml
@startuml
'class diagram for "it.polito.ezgas.converter"
left to right direction
package "it.polito.ezgas.converter"{
interface CustomConverter<E, D> {
convertToDto(E aEntity): D
convertToEntity(D aDto): E
convertToDtos(List<E> aListOfEntities): List<D> 
}

class UserConverter{
}

class GasStationConverter{
}

UserConverter ..|> CustomConverter: <<implement>>
GasStationConverter..|> CustomConverter: <<implement>>
note top of UserConverter :  E = User, D = UserDto
note top of GasStationConverter: E = GasStation, D = GasStationDto
}
@enduml
```

### it.polito.ezgas.service + it.polito.ezgas.service.impl
```plantuml
@startuml
left to right direction
package it.polito.ezgas.service{
'class diagram for “it.polito.ezgas.service”
interface UserService{
+getUserById(Integer userId) : UserDto
+getAllUsers() : List<UserDto>
+saveUser(UserDto userDto) : UserDto
+deleteUser(Integer userId) : Boolean
+increaseUserReputation(Integer userId) : Integer
+decreaseUserReputation(Integer userId) : Integer
+login(IdPw credentials) : LoginDto
}

interface GasStationService {
+getGasStationById(Integer gasStationId) : GasStationDto
+getAllGasStations() : List<GasStationDto>
+saveGasStation(GasStationDto gasStationDto) : GasStationDto
+deleteGasStation(Integer gasStationId) : Boolean
+getGasStationsByGasolineType(String gasolinetype) : List<GasStationDto>
+getGasStationByCarSharing(String carSharing) :  List<GasStationDto>
+getGasStationsWithoutCoordinates(String gasolinetype, String carsharing) :  List<GasStationDto>
+getGasStationsByProximity(double lat, double lon) : List<GasStationDto>
+getGasStationsWithCoordinates(double lat, double lon, String gasolinetype, String carsharing) :  List<GasStationDto>
+setReport(Integer gasStationId, double , double , double , double , double , Integer userId) : void
}
}

package it.polito.ezgas.service.impl{
class UserServiceImpl{
--private fields--
-userRepository: UserRepository
-userConverter: CustomConverter<User, UserDto>
}
class GasStationServiceImpl{
--private fields--
-gasStationRepository: GasStationRepository
-userConverter : CustomConverter<User, UserDto>
-gasStationConverter: CustomConverter<GasStation, GasStationDto>
-userService: UserService
--private method--
-updateDependability(GasStation gS): GasStation
-isAPriceNegative(GasStationDto gasStationDto): Boolean
}

}
UserServiceImpl ..|> UserService : <<implement>>
GasStationServiceImpl ..|> GasStationService : <<implement>>
@enduml
```

### it.polito.ezgas.controller
```plantuml
@startuml
'class diagram for it.polito.ezgas.controller
left to right direction
package it.polito.ezgas.controller{

class UserController{
--private fields--
-userService: UserService
--public methods--
+getUserById(Integer userId) : UserDto
+List<UserDto> getAllUsers() : List<UserDto>
+saveUser(UserDto userDto) : UserDto
+deleteUser(Integer userId) : Boolean
+increaseUserReputation(Integer userId) : Integer
+decreaseUserReputation(Integer userId) : Integer
+login(IdPw credentials) : LoginDto
}

class GasStationController{
--private fields--
-gasStationService: GasStationService
--public methods--
+getGasStationById(Integer gasStationId) : GasStationDto
+getAllGasStations() : List<GasStationDto>
+saveGasStation(GasStationDto gasStationDto) : GasStationDto
+deleteGasStation(Integer gasStationId) : void
+getGasStationsByGasolineType(String gasolinetype) : List<GasStationDto>
+getGasStationsByProximity(double lat, double lon) : List<GasStationDto>
+getGasStationsWithCoordinates(double lat, double lon, String gasolinetype, String carsharing) :  List<GasStationDto>
+setGasStationReport(Integer gasStationId, double , double , double , double , double , Integer userId) : void
}

class HomeController{
--public methods--
+admin(): String
+index(): String
+map(): String
+login(): String
+update(): String
+signup(): String
}

}
@enduml
```

### it.polito.ezgas.repository
```plantuml
@startuml
'class diagram for “it.polito.ezgas.repository”
left to right direction
package "it.polito.ezgas.repository"{
interface UserRepository{
+findByEmail(String email): User
}

interface GasStationRepository{
}

interface CustomizedGasStationRepository {
+findAllByGasolineTypeAndCarSharing(String gasolineType, String carSharing, SortOrder order) :  List<GasStation>
+findAllByProximity(double lat, double lon, SortOrder order) : List<GasStation>
}

class GasStationRepositoryImpl{
--private fields--
-radius: Integer
-entityManager: EntityManager
}

interface CrudRepository<T,ID>{
+count() : long 
+delete(T entity) : void
+deleteAll(T entity) : void
+deleteAll(Iterable<T> entities): void
+deleteById(ID id) : void
+existsById(ID id) : Boolean
+findAll() : Iterable<T>
+findAllById(Iterable<ID> ids) : Iterable<T>
+findById(ID id) : Optional<T>
+save(T entity) : T
+saveAll(Iterable<T> entities) : Iterable<T>
}

GasStationRepository --|> CrudRepository : <<extend>>
GasStationRepository --|> CustomizedGasStationRepository : <<extend>>
GasStationRepositoryImpl ..|> CustomizedGasStationRepository : <<implement>>
UserRepository --|> CrudRepository : <<extend>>
note top of UserRepository :  T = User, ID = Integer
note top of GasStationRepository : T = GasStation, ID = Integer
}
@enduml
```

```plantuml
@startuml
top to bottom direction
package it.polito.ezgas.controller {
class UserController
class GasStationController
class HomeController
}


package it.polito.ezgas.service {
interface UserService
interface GasStationService
}

package it.polito.ezgas.service.impl{
class UserServiceImpl
class GasStationServiceImpl
UserServiceImpl .up.|> UserService : <<implement>>
GasStationServiceImpl .up.|> GasStationService: <<implement>>
}

package it.polito.ezgas.repository {
interface CrudRepository
interface CustomizedGasStationRepository
class GasStationRepositoryImpl
interface GasStationRepository
interface UserRepository
UserRepository --|> CrudRepository : <<extend>>
GasStationRepository --|> CrudRepository: <<extend>>
GasStationRepository --|> CustomizedGasStationRepository: <<extend>>
GasStationRepositoryImpl ..|> CustomizedGasStationRepository: <<implement>>
}

package it.polito.ezgas.converter {
class UserConverter
class GasStationConverter
interface CustomConverter
UserConverter ..|> CustomConverter: <<implement>>
GasStationConverter ..|> CustomConverter: <<implement>>
}
package it.polito.ezgas.dto{
class UserDto
class GasStationDto
class IdPw
class LoginDto
class PriceReportDto
}

package it.polito.ezgas.entity {
class User
class GasStation
class PriceReport
}

UserController "0..1" o-down- "1" UserService
UserController .up.> UserDto
UserController .up.> LoginDto
UserController .up.> IdPw
GasStationController "0..1" o-down- "1" GasStationService
GasStationController .up.> GasStationDto
UserService .up..> UserDto
UserService .up..> LoginDto
UserService .up..> IdPw
GasStationService .up..> GasStationDto

UserServiceImpl .up...> UserDto
UserServiceImpl .up...> LoginDto
UserServiceImpl .up...> IdPw
UserServiceImpl .up...> User
UserServiceImpl "0..1" o-down- "1" UserConverter
UserServiceImpl "0..1" o-down- "1" UserRepository 

GasStationServiceImpl "0..1" o-down- "1" GasStationConverter
GasStationServiceImpl "0..1" o-down- "1" UserConverter
GasStationServiceImpl "0..1" o-down- "1" GasStationRepository
GasStationServiceImpl "0..1" -up-o "1" UserService

UserRepository "1" *-up---- "0..*" User
GasStationRepository "1" *-up---- "0..*" GasStation

UserConverter .up....> User
UserConverter .up....> UserDto
GasStationConverter .up....> GasStation
GasStationConverter .up....> GasStationDto

GasStation "0..*" o-right-- "1" User
GasStationDto "0..*" o-right- "1" UserDto
GasStationDto .> PriceReportDto
PriceReport "0..*" o-right- "1" User
PriceReportDto "0..*" o--right-- "1" User
@enduml
```

# Verification traceability matrix

|ID FR | UserController  | GasStationController | UserService | GasStationService | UserServiceImpl | GasStationImpl | UserRepository | GasStationRepository| CustomizedGasStationRepository | GasStationRepositoryImpl |  UserConverter | GasStationConverter | CustomConverter| User | GasStation |UserDto |GasStationDto | LoginDto | IdPw |
| :--  |:--:| :--:|:--:|:--:|:--:|:--:|:--:|:--:|:--:|:--:|:--:|:--:|:--:|:--:|:--:|:--:|:--:|:--:|:--:|
|FR1   |x | |x | |x | |x | | | |x | |x |x | |x | |x |x |
|FR1.1 |x | |x | |x | |x | | | |x | |x |x | |x | |x |x |
|FR1.2 |x | |x | |x | |x | | | |x | |x |x | |x | |x |x |
|FR1.3 |x | |x | |x | |x | | | |x | |x |x | |x | |x |x |
|FR1.4 |x | |x | |x | |x | | | |x | |x |x | |x | |x |x |
|FR2 || || || ||| ||| | || | | | | |
|FR3   | |x | |x | |x | |x |x |x | |x |x | |x | |x |x |x |
|FR3.1 | |x | |x | |x | |x |x |x | |x |x | |x | |x |x |x |
|FR3.2 | |x | |x | |x | |x |x |x | |x |x | |x | |x |x |x |
|FR3.3 | |x | |x | |x | |x |x |x | |x |x | |x | |x |x |x |
|FR4   | |x | |x | |x | |x |x |x | |x |x | |x | |x |  |  |
|FR4.1 | |x | |x | |x | |x |x |x | |x |x | |x | |x |  |  |
|FR4.2 | |x | |x | |x | |x |x |x | |x |x | |x | |x |  |  |
|FR4.3 | |x | |x | |x | |x |x |x | |x |x | |x | |x |  |  |
|FR4.4 | |x | |x | |x | |x |x |x | |x |x | |x | |x |  |  |
|FR4.5 | |x | |x | |x | |x |x |x | |x |x | |x | |x |  |  |
|FR5   |x|x |x |x |x |x |x |x |x |x |x |x |x |x |x |x |x |x |x |
|FR5.1 | |x | |x | |x | |x |x |x | |x |x | |x | |x |x |x |
|FR5.2 | |x | |x | |x | |x |x |x | |x |x | |x | |x |x |x |
|FR5.3 |x | |x | |x | x | |x |x |x | |x |x | |x | |x |x |

# Verification sequence diagrams 
![SD1](https://raw.githubusercontent.com/MarcoBll/SoftEng__1/master/New_SD1.PNG)

![SD4](https://raw.githubusercontent.com/MarcoBll/SoftEng__1/master/New_SD4.PNG)

![SD7](https://raw.githubusercontent.com/MarcoBll/SoftEng__1/master/New_SD7.PNG)

![SD10](https://raw.githubusercontent.com/MarcoBll/SoftEng__1/master/New_SD10.PNG)
