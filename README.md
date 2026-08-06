# Employee Plot Management Microservice System

This project is a distributed microservice-based system designed to manage Employee data, their associated Accounts, and Plots. It leverages **Spring Boot** and **Spring Cloud** to ensure modularity, scalability, and ease of maintenance.

## Architecture Overview

The system is composed of five distinct Spring Boot applications that work together:

1. **eureka-server**: Service Registry
2. **micro-apiGateway**: API Gateway
3. **micro-employee**: Employee Management Service
4. **micro-account**: Account Management Service
5. **micro-plot**: Plot Management Service

### How Things Are Moving (Overall Workflow)

1. **Service Registration**: When the individual microservices (`micro-employee`, `micro-account`, `micro-plot`, and `micro-apiGateway`) start up, they register themselves with the **eureka-server**.
2. **Request Routing**: All incoming client requests are directed to the **micro-apiGateway**. The gateway looks up the registered services in Eureka and routes the request to the appropriate microservice based on the URL path.
3. **Inter-Service Communication**: Microservices communicate with each other using **OpenFeign** clients. For complex operations (like creating a complete employee profile), one service can synchronously call other services to perform related tasks.
4. **Data Isolation**: Each microservice maintains its own data domain. The `micro-employee` service holds core employee data, while `micro-account` and `micro-plot` hold domain-specific data mapped via an `employeeId`.

---

## Microservices Detailed Breakdown

### 1. Eureka Server (`eureka-server`)
- **Role**: Service Registry.
- **Description**: Acts as a central directory for the microservices. Instead of hardcoding IP addresses and ports, services register with Eureka. The API Gateway and other services use Eureka to discover where a specific service is running dynamically.

### 2. API Gateway (`micro-apiGateway`)
- **Role**: Single Entry Point / Reverse Proxy.
- **Description**: Built using **Spring Cloud Gateway WebMVC**. It sits in front of all the microservices, handling routing, load balancing, and potentially cross-cutting concerns like authentication or rate limiting. 

### 3. Employee Service (`micro-employee`)
- **Role**: Core orchestration and Employee domain management.
- **Description**: Manages `EmployeeEntity` (creation, retrieval, deletion). 
- **Key Workflow**: It acts as an orchestrator for composite requests. For example, the `saveFullEmployee` endpoint:
  1. Saves the basic employee details to its own database.
  2. Uses `AccountClient` (Feign) to send a request to `micro-account` to save account details, passing the generated `employeeId`.
  3. Uses `PlotClient` (Feign) to send a request to `micro-plot` to save plot details, linking them to the `employeeId`.
- Similarly, for deletion, it deletes the employee locally and triggers delete operations on the Account and Plot services.

### 4. Account Service (`micro-account`)
- **Role**: Account domain management.
- **Description**: Exposes REST endpoints (`/api/account`) to perform CRUD operations on `AccountEntity`. It can function independently but is primarily called by the Employee service to associate account details with a specific employee.

### 5. Plot Service (`micro-plot`)
- **Role**: Plot domain management.
- **Description**: Exposes REST endpoints (`/api/plot`) to perform CRUD operations on `PlotEntity`. Similar to the Account service, it maintains plot assignments linked to an employee.

---

## Setup and Execution

To run the system locally, ensure you start the services in the following order:

1. **eureka-server** (Wait for it to start completely)
2. **micro-account**, **micro-plot**, and **micro-employee** (Order does not matter as long as Eureka is up)
3. **micro-apiGateway**

Each service uses its respective `pom.xml` configurations and can be containerized using the integrated `jib-maven-plugin`.
