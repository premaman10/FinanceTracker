# 💰 FinanceTracker Application

FinanceTracker is a **full-stack finance management application** that helps users track **income, expenses, and financial data** securely.  
The application is built using **Spring Boot** for the backend, serves the **frontend as static resources**, and uses **MySQL** as the database.  
It is containerized using **Docker** and deployed on **Kubernetes (Minikube)**.

---

## 🚀 Features

- User authentication (JWT-based)
- Manage income and expenses
- Categorized financial records
- RESTful API architecture
- Frontend served via Spring Boot static resources
- Dockerized application
- Kubernetes deployment with services
- MySQL database integration

---

## 🛠️ Tech Stack

### Backend
- Java
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA

### Frontend
- HTML
- CSS
- JavaScript  
*(served from `src/main/resources/static`)*

### Database
- MySQL

### DevOps / Deployment
- Docker
- Docker Compose
- Kubernetes
- Minikube

---

## 📂 Project Structure

```

FinanceTracker/
├── src/
│   ├── main/
│   │   ├── java/com/example/FinanceTracker/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repo/
│   │   │   ├── model/
│   │   │   ├── filters/
│   │   │   └── config/
│   │   └── resources/
│   │       ├── static/
│   │       └── application.properties
│   └── test/
├── k8s/
│   ├── backend-deployment.yaml
│   ├── backend-service.yaml
│   ├── mysql-statefulset.yaml
│   └── mysql-service.yaml
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md

````

---

## 🐳 Docker Setup (Optional – Local)

### Build Docker Image
```bash
docker build -t financetracker:latest .
````

### Run with Docker Compose

```bash
docker-compose up
```

---

## ☸️ Kubernetes Deployment (Minikube)

### Prerequisites

* Docker
* Minikube
* kubectl

### Start Minikube

```bash
minikube start
```

### Apply Kubernetes Manifests

```bash
kubectl apply -f k8s/mysql-service.yaml
kubectl apply -f k8s/mysql-statefulset.yaml
kubectl apply -f k8s/backend-deployment.yaml
kubectl apply -f k8s/backend-service.yaml
```

### Check Pod Status

```bash
kubectl get pods
```

---

## 🌐 Access the Application

### Using Minikube Service

```bash
minikube service finance-backend-service
```

OR manually:

```bash
minikube ip
```

Open in browser:

```
http://<minikube-ip>:30007
```

---

---

## 🔐 Environment Configuration

Database configuration is handled via Kubernetes environment variables:

* `SPRING_DATASOURCE_URL`
* `SPRING_DATASOURCE_USERNAME`
* `SPRING_DATASOURCE_PASSWORD`

---

## 📈 Scaling the Application

You can scale the backend easily:

```bash
kubectl scale deployment finance-backend --replicas=3
```

---

## 🧠 Key Learning Outcomes

* Containerization using Docker
* Kubernetes Deployments and Services
* Handling MySQL with Kubernetes
* Managing application configuration via environment variables
* Real-world DevOps troubleshooting on Windows + Minikube

---

## 👤 Author

**Prem Aman**
Computer Science Engineering Student
GitHub: [https://github.com/premaman10](https://github.com/premaman10)

---

## 📜 License

This project is for **educational and academic purposes**.

```

---

