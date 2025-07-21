# Database Connection Status Monitor

Ứng dụng Java Spring Boot để giám sát trạng thái kết nối đến nhiều database đồng thời trên cả hai môi trường SIT và UAT.

## ✨ Tính năng mới

### 🌐 Multi-Environment Monitoring
- **Không tách môi trường** - Kết nối đồng thời đến cả SIT và UAT
- **Environment Dropdown** - Cho phép người dùng chọn môi trường để hiển thị
- **Status Bar** - Hiển thị tổng quan trạng thái kết nối của tất cả môi trường

### 📊 Dashboard Features
- **Real-time Status Bar** - Hiển thị số lượng database connected/disconnected cho từng môi trường
- **Environment Filter** - Dropdown để chọn hiển thị ALL/SIT/UAT
- **Visual Indicators** - Icons và màu sắc trực quan cho từng trạng thái
- **Auto-refresh** - Tự động làm mới mỗi 30 giây với giữ nguyên filter đã chọn

## 🔧 Cấu hình Database

### SIT Environment
| Database | Host | Port | Database | Username | Password |
|----------|------|------|----------|----------|----------|
| MSSQL | 172.20.17.48 | 1433 | Default | portalusr | portal@usr |
| DB2 BOSPROD | 172.20.17.21 | 50000 | BOSPROD | cardpro | sacombank@123456789 |
| DB2 FEP | 172.20.17.21 | 50000 | FEPPROD | cardpro | sacombank@123456789 |

### UAT Environment  
| Database | Host | Port | Database | Username | Password |
|----------|------|------|----------|----------|----------|
| MSSQL | 172.20.15.84 | 1433 | Default | portalusr | portal@usr |
| DB2 BOSPROD | 172.20.15.52 | 50000 | BOSPROD | cardpro | sacombank@123456789 |
| DB2 FEP | 172.20.15.52 | 50000 | FEPPROD | cardpro | cardpro |

## 🚀 Cách sử dụng

### Quick Start
```bash
# Chạy ứng dụng (monitoring cả SIT và UAT)
./start.sh

# Hoặc sử dụng Maven trực tiếp
mvn spring-boot:run
```

### Legacy Scripts (vẫn hoạt động)
```bash
./run-sit.sh   # Same as start.sh
./run-uat.sh   # Same as start.sh
```

### Endpoints

#### Web Interface
- **Dashboard**: http://localhost:8080/
- **Filter SIT**: http://localhost:8080/?env=SIT
- **Filter UAT**: http://localhost:8080/?env=UAT

#### API Endpoints
```bash
# Tất cả databases
curl http://localhost:8080/api/status

# Chỉ SIT environment
curl http://localhost:8080/api/status?env=SIT

# Chỉ UAT environment  
curl http://localhost:8080/api/status?env=UAT

# Environment summary
curl http://localhost:8080/api/summary

# Health check
curl http://localhost:8080/actuator/health
```

## 📱 Giao diện Web

### Status Bar
- **Màu xanh**: Tất cả databases connected
- **Màu đỏ**: Có database disconnected
- Hiển thị số lượng connected/total cho từng môi trường
- Hiển thị thời gian cập nhật cuối cùng

### Environment Dropdown
- **All Environments**: Hiển thị tất cả 6 databases
- **SIT Environment**: Chỉ hiển thị 3 databases SIT
- **UAT Environment**: Chỉ hiển thị 3 databases UAT

### Database Cards
- Badge màu xanh (SIT) hoặc xanh lá (UAT)
- Icons trạng thái: ✅ Connected, ❌ Disconnected
- Response time indicators: 🟢 Fast, 🟡 Slow, 🔴 Very Slow

## 🔍 API Response Example

```json
{
  "timestamp": 1642781234567,
  "selectedEnvironment": "ALL",
  "databases": [
    {
      "name": "MSSQL Primary (SIT)",
      "type": "MSSQL",
      "host": "172.20.17.48",
      "database": "",
      "username": "portalusr",
      "environment": "SIT",
      "connected": true,
      "status": "CONNECTED",
      "errorMessage": null,
      "lastChecked": "2025-01-21T08:44:04.186",
      "responseTimeMs": 145
    }
  ],
  "totalDatabases": 6,
  "connectedDatabases": 6,
  "environmentSummary": {
    "sitConnected": 3,
    "sitDisconnected": 0,
    "uatConnected": 3,
    "uatDisconnected": 0,
    "totalConnected": 6,
    "totalDisconnected": 0
  }
}
```

## 📁 Cấu trúc dự án

```
db-connection-status/
├── src/main/java/com/example/dbconnectionstatus/
│   ├── DbConnectionStatusApplication.java    # Main application
│   ├── config/DatabaseConfig.java           # Multi-environment datasource config
│   ├── controller/
│   │   ├── DatabaseStatusController.java    # REST API với env filtering
│   │   └── WebController.java              # Web controller với dropdown
│   ├── model/ConnectionStatus.java          # Model với environment field
│   └── service/DatabaseHealthService.java   # Service với dual-env support
├── src/main/resources/
│   ├── application.yml                      # Simplified configuration
│   └── templates/dashboard.html            # Enhanced UI với status bar
├── start.sh                                # Unified startup script
├── run-sit.sh                              # Legacy script (redirects to start.sh)
├── run-uat.sh                              # Legacy script (redirects to start.sh)
├── pom.xml                                 # Maven dependencies
└── README.md                               # This documentation
```

## ⚙️ Technical Stack

- **Java 21** - Programming language
- **Spring Boot 3.2.0** - Application framework
- **Spring JDBC** - Database connectivity
- **HikariCP** - Connection pooling
- **Thymeleaf** - Template engine
- **Bootstrap 5** - UI framework với Font Awesome icons
- **Maven** - Build tool
- **Microsoft JDBC Driver** - SQL Server connectivity
- **IBM DB2 JCC Driver** - DB2 connectivity

## 🎯 Các thay đổi chính

### ✅ Hoàn thành
1. **Multi-Environment Support** - Đồng thời kết nối SIT và UAT
2. **Environment Dropdown** - UI selector cho SIT/UAT/ALL
3. **Status Bar** - Real-time overview của tất cả connections
4. **Enhanced API** - Support environment filtering
5. **Improved UX** - Visual indicators, animations, responsive design

### 🔄 Architecture Changes
- Loại bỏ Spring Profiles (sit/uat)
- Tạo datasources cho cả 2 môi trường đồng thời
- Thêm environment field vào ConnectionStatus model
- Enhanced service methods cho filtering
- Cải thiện web interface với status bar

## 🎉 Kết quả

Ứng dụng đã được cập nhật thành công với các tính năng mới:
- ✅ Monitoring đồng thời cả SIT và UAT environments
- ✅ Environment dropdown selector
- ✅ Real-time status bar với color coding
- ✅ Enhanced API với environment filtering
- ✅ Improved user experience với visual indicators
- ✅ Backward compatibility với existing scripts

Ứng dụng sẵn sàng để sử dụng với khả năng monitoring toàn diện cả hai môi trường.