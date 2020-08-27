# Unit Testing Documentation

Authors: Marco Bellavia (s280130), Gabriel Ganzer (s271961), Gianluca Morabito (s277943), Francesco Xia (s277509)

Date: 26/05/2020

Version: 1.1

Changelog v1.1:
* change the reference class for the tests from EZGasApplicationTests to EntityAndDtoUnitTests


# Contents

- [Black Box Unit Tests](#black-box-unit-tests)

- [White Box Unit Tests](#white-box-unit-tests)


# Black Box Unit Tests

### **Class *GasStation* - methods *setGasStationId* and *getGasStationId***

**Criteria for method *setGasStationId* and *getGasStationId*:**

 - gasStationId isNull
 - Value of gasStationId

**Predicates for method *name*:**

| Criteria | Predicate |
|----------|-----------|
| gasStationId isNull | null |
|                     | not null |
| Value of gasStationId | [minInt, maxInt] |

**Boundaries**:

| Criteria | Boundary values |
|----------|-----------------|
| Value of gasStationId | minInt |               
|                       | maxInt |

**Combination of predicates**:

| gasStationId isNull | Value of gasStationId | Valid / Invalid | Description of the test case | JUnit test case |
|---------------------|-----------------------|-----------------|------------------------------|-----------------|
| null | - | Valid | Id is null | EntityAndDtoUnitTests.testCase1()(null, null) |
| not null | (minInt, maxInt) | Valid | Id is a valid number | EntityAndDtoUnitTests.testCase2()(1, 1) |
|          | < minInt or > maxInt | Valid | Id has a value outside the Integer class limits | Not feasible |
|          | minInt | Valid | Id has value minInt | EntityAndDtoUnitTests.testCase3()(minInt, minInt) |
|          | maxInt | Valid | Id has value maxInt | EntityAndDtoUnitTests.testCase4()(maxInt, maxInt) |

### **Class *PriceReport* - methods *setDieselPrice* and *getDieselPrice***

**Criteria for method *setDieselPrice* and *getDieselPrice*:**

 - Value of dieselPrice

**Predicates for method *name*:**

| Criteria | Predicate |
|----------|-----------|
| Value of dieselPrice | [minDouble, maxDouble] |

**Boundaries**:

| Criteria | Boundary values |
|----------|-----------------|
| Value of dieselPrice | minDouble |               
|                      | maxDouble |

**Combination of predicates**:

| Value of dieselPrice | Valid / Invalid | Description of the test case | JUnit test case |
|----------------------|-----------------|------------------------------|-----------------|
| (minDouble, maxDouble) | Valid | Price is a valid number | EntityAndDtoUnitTests.testCase5()(1.0, 1.0) |
| < minDouble or > maxDouble | Valid | Price has a value outside the Double class limits | Not feasible |
| minDouble | Valid | Price has value minDouble | EntityAndDtoUnitTests.testCase6()(minDouble, minDouble) |
| maxDouble | Valid | Price has value maxDouble | EntityAndDtoUnitTests.testCase7()(maxDouble, maxDouble) |

### **Class *User* - methods *setUserName* and *getUserName***

**Criteria for method *setUserName* and *getUserName*:**

 - userName isNull
 - userName isEmpty

**Predicates for method *name*:**

| Criteria | Predicate |
|----------|-----------|
| userName isNull | null |
|                 | not null |
| userName isEmpty | "" |
|                  | "userName" |

**Boundaries**:

| Criteria | Boundary values |
|----------|-----------------|

**Combination of predicates**:

| userName isNull | userName isEmpty | Valid / Invalid | Description of the test case | JUnit test case |
|-----------------|------------------|-----------------|------------------------------|-----------------|
| null | - | Valid | userName is null | EntityAndDtoUnitTests.testCase8()(null, null) |
| not null | "" | Valid | userName is an empty string | EntityAndDtoUnitTests.testCase9()("", "") |
|          | "userName" | Valid | userName is a not-empty string | EntityAndDtoUnitTests.testCase10()("userName", "userName")

### **Class *GasStationDto* - methods *setUserDto* and *getUserDto***

**Criteria for method *setUserDto* and *getUserDto*:**

 - userDto isNull

**Predicates for method *name*:**

| Criteria | Predicate |
|----------|-----------|
| userDto isNull | null |
|                | not null |

**Boundaries**:

| Criteria | Boundary values |
|----------|-----------------|

**Combination of predicates**:

| userDto isNull | Valid / Invalid | Description of the test case | JUnit test case |
|-----------------|-----------------|------------------------------|-----------------|
| null | Valid | userDto is null | EntityAndDtoUnitTests.testCase11()(null, null) |
| not null | Valid | userDto is not null | EntityAndDtoUnitTests.testCase12()((UserDto)u, (UserDto)u) |

### **Class *LoginDto* - methods *setAdmin* and *getAdmin***

**Criteria for method *setAdmin* and *setAdmin*:**

 - admin isNull
 - Value of admin

**Predicates for method *name*:**

| Criteria | Predicate |
|----------|-----------|
| admin isNull | null |
|              | not null |
| Value of admin | true |
|                | false |

**Boundaries**:

| Criteria | Boundary values |
|----------|-----------------|

**Combination of predicates**:

| admin isNull | Value of admin | Valid / Invalid | Description of the test case | JUnit test case |
|--------------|----------------|-----------------|------------------------------|-----------------|
| null | - | Valid | admin is null | EntityAndDtoUnitTests.testCase13()(null, null) |
| not null | true | Valid | admin is true | EntityAndDtoUnitTests.testCase14()(true, true) |
|          | false | Valid | admin is false | EntityAndDtoUnitTests.testCase15()(false, false) |

### **Class *PriceReportDto* - methods *setUser* and *getUser***

**Criteria for method *setUser* and *getUser*:**

 - user isNull

**Predicates for method *name*:**

| Criteria | Predicate |
|----------|-----------|
| user isNull | null |
|             | not null |

**Boundaries**:

| Criteria | Boundary values |
|----------|-----------------|

**Combination of predicates**:

| userDto isNull | Valid / Invalid | Description of the test case | JUnit test case |
|-----------------|-----------------|------------------------------|-----------------|
| null | Valid | user is null | EntityAndDtoUnitTests.testCase16()(null, null) |
| not null | Valid | user is not null | EntityAndDtoUnitTests.testCase17()((User)u, (User)u) |


# White Box Unit Tests

### Test cases definition

| Unit name | JUnit test case |
|-----------|-----------------|
| GasStation.setGasStationId(), GasStation.getGasStationId() | EntityAndDtoUnitTests.testCase1(), EntityAndDtoUnitTests.testCase2(), EntityAndDtoUnitTests.testCase3(), EntityAndDtoUnitTests.testCase4() |
| PriceReport.setDieselPrice(), PriceReport.getDieselPrice() | EntityAndDtoUnitTests.testCase5(), EntityAndDtoUnitTests.testCase6(), EntityAndDtoUnitTests.testCase7() |
| User.setUserName(), User.getUserName() | EntityAndDtoUnitTests.testCase8(), EntityAndDtoUnitTests.testCase9(), EntityAndDtoUnitTests.testCase10() |
| GasStationDto.setUserDto(), GasStationDto.getUserDto() | EntityAndDtoUnitTests.testCase11(), EntityAndDtoUnitTests.testCase12() |
| LoginDto.setAdmin(), LoginDto.getAdmin() | EntityAndDtoUnitTests.testCase13(), EntityAndDtoUnitTests.testCase14(), EntityAndDtoUnitTests.testCase15() |
| PriceReportDto.setUser(), PriceReportDto.getUser() | EntityAndDtoUnitTests.testCase16(), EntityAndDtoUnitTests.testCase17() |


### Code coverage report

![](https://raw.githubusercontent.com/s277943/EZGas_images/master/EclEmma_tool.png)

### Loop coverage analysis

    <Identify significant loops in the units and reports the test cases
    developed to cover zero, one or multiple iterations >

|Unit name | Loop rows | Number of iterations | JUnit test case |
|----------|-----------|----------------------|-----------------|
|||||
|||||
|||||

