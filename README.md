# Ứng dụng Quản lý Database với Tkinter

Đây là ứng dụng giao diện đồ họa được xây dựng bằng Python Tkinter để quản lý và tìm kiếm thông tin thẻ từ cơ sở dữ liệu.

## Tính năng

- **Giao diện thân thiện**: Sử dụng Tkinter với layout đẹp mắt
- **Tìm kiếm linh hoạt**: Tìm kiếm theo số thẻ, card token, hoặc CIF
- **Hiển thị dữ liệu**: Bảng hiển thị kết quả với thanh cuộn
- **Môi trường**: Chọn môi trường SIT/UAT/PROD
- **Cơ sở dữ liệu demo**: Sử dụng SQLite với dữ liệu mẫu

## Cài đặt và Chạy

### 1. Tạo và kích hoạt virtual environment

```bash
# Tạo virtual environment
python3 -m venv venv

# Kích hoạt virtual environment (Linux/Mac)
source venv/bin/activate

# Kích hoạt virtual environment (Windows)
venv\Scripts\activate
```

### 2. Chạy ứng dụng

#### Cho Windows (Recommended):
```bash
# Chạy phiên bản tương thích Windows với auto-fix
python database_gui_fixed.py

# Hoặc sử dụng batch file
run_fixed.bat
```

#### Nếu gặp lỗi tkinter trên Windows:
```bash
# Chạy công cụ chẩn đoán trước
python fix_tkinter.py

# Sau đó chạy ứng dụng
python database_gui_fixed.py
```

#### Cho Linux/Mac:
```bash
python database_gui.py
# hoặc
./run.sh
```

## Hướng dẫn sử dụng

### Giao diện chính

1. **Môi trường SIT**: Dropdown để chọn môi trường (SIT, UAT, PROD)
2. **Thông tin kết nối**: Hiển thị trạng thái kết nối database
3. **Các trường tìm kiếm**:
   - **Số thẻ**: Nhập số thẻ cần tìm
   - **Card Token**: Nhập token thẻ
   - **CIF**: Nhập mã CIF khách hàng
4. **Nút TÌM KIẾM**: Thực hiện tìm kiếm theo điều kiện
5. **Bảng kết quả**: Hiển thị dữ liệu từ database

### Cách tìm kiếm

- Có thể tìm kiếm theo một hoặc nhiều tiêu chí
- Để trống tất cả các trường và nhấn "TÌM KIẾM" để hiển thị toàn bộ dữ liệu
- Tìm kiếm hỗ trợ tìm kiếm gần đúng (partial match)

### Dữ liệu mẫu

Ứng dụng bao gồm dữ liệu mẫu với các thông tin:
- ID: 1-5
- Số thẻ: 123456789, 987654321, v.v.
- Card Token: TK001ABC123, TK002DEF456, v.v.
- CIF: CIF001, CIF002, v.v.
- Trạng thái: Active, Inactive, Pending

## Cấu trúc dự án

```
├── database_gui.py      # File chính chứa ứng dụng GUI
├── requirements.txt     # Danh sách dependencies
├── README.md           # Hướng dẫn sử dụng
└── venv/               # Virtual environment
```

## Các tính năng có thể mở rộng

- Kết nối với database thực (MSSQL, PostgreSQL, MySQL)
- Xuất dữ liệu ra Excel/CSV
- Thêm chức năng thêm/sửa/xóa dữ liệu
- Đồ thị và báo cáo
- Xác thực người dùng
- Ghi log hoạt động

## Yêu cầu hệ thống

- Python 3.7+
- Tkinter (đã bao gồm trong Python)
- SQLite3 (đã bao gồm trong Python)

## Khắc phục sự cố

### Lỗi tkinter trên Windows
Nếu gặp lỗi `_tkinter.TclError: Can't find a usable init.tcl`:

1. **Chạy công cụ chẩn đoán**: `python fix_tkinter.py`
2. **Sử dụng phiên bản fixed**: `python database_gui_fixed.py`
3. **Cài đặt lại Python** từ python.org (đảm bảo tick "Add Python to PATH")
4. **Dùng Anaconda**: `conda install tk`
5. **Set biến môi trường thủ công**:
   - TCL_LIBRARY=C:\Python\tcl\tcl8.6
   - TK_LIBRARY=C:\Python\tcl\tk8.6

### Files trong dự án
- `database_gui.py`: Phiên bản gốc (Linux/Mac)
- `database_gui_fixed.py`: Phiên bản tương thích Windows
- `fix_tkinter.py`: Công cụ chẩn đoán tkinter
- `run_fixed.bat`: Script chạy cho Windows

## Ghi chú

- Ứng dụng hiện sử dụng SQLite in-memory database để demo
- Dữ liệu sẽ bị mất khi tắt ứng dụng
- Có thể dễ dàng chỉnh sửa để kết nối với database thực