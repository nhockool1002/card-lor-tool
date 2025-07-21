# Database Connection Status Monitor

Ứng dụng Spring Boot để giám sát trạng thái kết nối của các database DB2 và MSSQL.

## Các tính năng

- Giám sát kết nối đến nhiều database (MSSQL, DB2)
- Hỗ trợ 2 môi trường: SIT và UAT
- Giao diện web hiển thị trạng thái real-time
- REST API để lấy thông tin trạng thái
- Auto-refresh mỗi 30 giây
- Hiển thị response time và error messages

## Cấu hình Database

### Môi trường SIT
- **MSSQL**: 172.20.17.48 (portalusr/portal@usr)
- **DB2 BOSPROD**: 172.20.17.21:50000/BOSPROD (cardpro/sacombank@123456789)
- **DB2 FEP**: 172.20.17.21:50000/FEPPROD (cardpro/sacombank@123456789)

### Môi trường UAT
- **MSSQL**: 172.20.15.84 (portalusr/portal@usr)
- **DB2 BOSPROD**: 172.20.15.52:50000/BOSPROD (cardpro/sacombank@123456789)
- **DB2 FEP**: 172.20.15.52:50000/FEPPROD (cardpro/cardpro)

## Cách chạy ứng dụng

### Yêu cầu
- Java 21+
- Maven 3.6+
- Kết nối mạng đến các database servers

### Quick Start

#### Chạy môi trường SIT
```bash
chmod +x run-sit.sh
./run-sit.sh
```

#### Chạy môi trường UAT  
```bash
chmod +x run-uat.sh
./run-uat.sh
```

### Manual Commands

#### Chạy với môi trường SIT (mặc định)
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=sit
```

#### Chạy với môi trường UAT
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=uat
```

#### Build và chạy JAR
```bash
mvn clean package
java -jar target/db-connection-status-0.0.1-SNAPSHOT.jar --spring.profiles.active=sit
```

## Endpoints

### Web Interface
- **Dashboard**: http://localhost:8080/
  - Hiển thị giao diện web với trạng thái tất cả database
  - Auto-refresh mỗi 30 giây
  - Responsive design với Bootstrap

### REST API
- **Status API**: http://localhost:8080/api/status
  - Trả về JSON với thông tin chi tiết của tất cả database
  
- **Health Check**: http://localhost:8080/api/health
  - Trả về trạng thái tổng thể (UP/DOWN)

### Actuator Endpoints
- **Health**: http://localhost:8080/actuator/health
- **Info**: http://localhost:8080/actuator/info

## Cấu trúc project

```
src/
├── main/
│   ├── java/
│   │   └── com/example/dbconnectionstatus/
│   │       ├── config/
│   │       │   └── DatabaseConfig.java          # Cấu hình multiple datasources
│   │       ├── controller/
│   │       │   ├── DatabaseStatusController.java # REST API endpoints
│   │       │   └── WebController.java           # Web interface controller
│   │       ├── model/
│   │       │   └── ConnectionStatus.java        # Model cho connection status
│   │       ├── service/
│   │       │   └── DatabaseHealthService.java   # Service kiểm tra health
│   │       └── DbConnectionStatusApplication.java
│   └── resources/
│       ├── application.yml                      # Cấu hình database
│       └── templates/
│           └── dashboard.html                   # Giao diện web
└── pom.xml                                     # Maven dependencies
```

## Screenshots API Response

### Status API Response
```json
{
  "timestamp": 1703123456789,
  "databases": [
    {
      "name": "MSSQL Primary (SIT)",
      "type": "MSSQL",
      "host": "172.20.17.48",
      "database": "",
      "username": "portalusr",
      "connected": true,
      "status": "CONNECTED",
      "errorMessage": null,
      "lastChecked": "2023-12-21T10:30:45",
      "responseTimeMs": 125
    },
    {
      "name": "DB2 BOSPROD (SIT)",
      "type": "DB2",
      "host": "172.20.17.21",
      "database": "BOSPROD",
      "username": "cardpro",
      "connected": true,
      "status": "CONNECTED",
      "errorMessage": null,
      "lastChecked": "2023-12-21T10:30:45",
      "responseTimeMs": 89
    }
  ],
  "totalDatabases": 3,
  "connectedDatabases": 2
}
```

## Thay đổi môi trường

Để thay đổi giữa môi trường SIT và UAT, sửa file `application.yml`:

```yaml
spring:
  profiles:
    active: uat  # Thay đổi thành 'sit' hoặc 'uat'
```

Hoặc sử dụng environment variable:
```bash
export SPRING_PROFILES_ACTIVE=uat
```

## Troubleshooting

1. **Lỗi kết nối database**: Kiểm tra network connectivity và credentials
2. **Missing driver**: Đảm bảo DB2 và MSSQL drivers được thêm vào classpath
3. **Memory issues**: Tăng heap size với `-Xmx512m` hoặc cao hơn

## Dependencies chính

- Spring Boot 3.2.0
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Actuator
- Spring Boot Starter Thymeleaf
- Microsoft SQL Server JDBC Driver
- IBM DB2 JDBC Driver (jcc 11.5.8.0)
- Bootstrap 5.1.3 (CDN)
- Font Awesome 6.0.0 (CDN)