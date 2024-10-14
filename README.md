
# User Management Service [UMS]

API Web Service that manages and maintains users

## Tech Stack

**Service:** Java SpringBoot

**Database:** Postgres

**Database:** Docker and Kubernetes

## Pre-Req

- Java 21
- Maven
- Docker Engine
- Kubernetes
- Rancher Desktop (Optional)

## Quick Start

### Build

```
mvn clean package
```

### Docker

```
docker compose up
```
### K8s (Deployment)

```
kubectl apply -f k8s/  
```

### Load Testing (Artillery)
```
cd load-tests
npm run load-tests/load-test:create-user
```
