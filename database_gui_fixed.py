#!/usr/bin/env python3
"""
Database GUI Application with Tkinter - Windows Compatible Version
Includes automatic TCL library detection and error handling
"""

import os
import sys

def setup_tcl_environment():
    """Setup TCL/TK environment variables for Windows"""
    python_dir = os.path.dirname(sys.executable)
    
    # Common TCL library locations
    possible_tcl_dirs = [
        os.path.join(python_dir, 'tcl', 'tcl8.6'),
        os.path.join(python_dir, 'tcl', 'tcl8.5'),
        os.path.join(python_dir, 'Lib', 'tcl8.6'),
        os.path.join(python_dir, 'tcl8.6'),
        os.path.join(python_dir, 'lib', 'tcl8.6'),
        os.path.join(os.path.dirname(python_dir), 'tcl', 'tcl8.6'),
        os.path.join(os.path.dirname(python_dir), 'lib', 'tcl8.6')
    ]
    
    possible_tk_dirs = [
        os.path.join(python_dir, 'tcl', 'tk8.6'),
        os.path.join(python_dir, 'tcl', 'tk8.5'),
        os.path.join(python_dir, 'Lib', 'tk8.6'),
        os.path.join(python_dir, 'tk8.6'),
        os.path.join(python_dir, 'lib', 'tk8.6'),
        os.path.join(os.path.dirname(python_dir), 'tcl', 'tk8.6'),
        os.path.join(os.path.dirname(python_dir), 'lib', 'tk8.6')
    ]
    
    # Find TCL library
    for tcl_dir in possible_tcl_dirs:
        if os.path.exists(tcl_dir) and os.path.exists(os.path.join(tcl_dir, 'init.tcl')):
            os.environ['TCL_LIBRARY'] = tcl_dir
            break
    
    # Find TK library
    for tk_dir in possible_tk_dirs:
        if os.path.exists(tk_dir):
            os.environ['TK_LIBRARY'] = tk_dir
            break

# Setup environment before importing tkinter
if sys.platform.startswith('win'):
    setup_tcl_environment()

try:
    import tkinter as tk
    from tkinter import ttk, messagebox
except ImportError as e:
    print(f"Error importing tkinter: {e}")
    print("Please run 'python fix_tkinter.py' to diagnose the issue.")
    sys.exit(1)

import sqlite3
from datetime import datetime

class DatabaseGUI:
    def __init__(self, root):
        self.root = root
        self.root.title("Hệ thống Quản lý Database")
        self.root.geometry("1200x800")
        self.root.configure(bg='#f0f0f0')
        
        # Initialize database
        self.init_database()
        
        # Create GUI
        self.create_widgets()
        
    def init_database(self):
        """Initialize SQLite database for demo purposes"""
        self.conn = sqlite3.connect(':memory:')  # In-memory database for demo
        cursor = self.conn.cursor()
        
        # Create sample table
        cursor.execute('''
            CREATE TABLE card_info (
                id INTEGER PRIMARY KEY,
                so_the TEXT,
                card_token TEXT,
                cif TEXT,
                created_date TEXT,
                status TEXT
            )
        ''')
        
        # Insert sample data
        sample_data = [
            ('123456789', 'TK001ABC123', 'CIF001', '2024-01-15', 'Active'),
            ('987654321', 'TK002DEF456', 'CIF002', '2024-01-16', 'Active'),
            ('555666777', 'TK003GHI789', 'CIF003', '2024-01-17', 'Inactive'),
            ('111222333', 'TK004JKL012', 'CIF004', '2024-01-18', 'Active'),
            ('444555666', 'TK005MNO345', 'CIF005', '2024-01-19', 'Pending')
        ]
        
        cursor.executemany('''
            INSERT INTO card_info (so_the, card_token, cif, created_date, status)
            VALUES (?, ?, ?, ?, ?)
        ''', sample_data)
        
        self.conn.commit()
        
    def create_widgets(self):
        # Header frame
        header_frame = tk.Frame(self.root, bg='#f0f0f0', height=100)
        header_frame.pack(fill='x', padx=10, pady=5)
        header_frame.pack_propagate(False)
        
        # Environment dropdown
        env_frame = tk.Frame(header_frame, bg='#f0f0f0')
        env_frame.pack(side='left', fill='y')
        
        tk.Label(env_frame, text="Môi trường SIT", bg='#f0f0f0', font=('Arial', 10)).pack(anchor='w')
        self.env_combo = ttk.Combobox(env_frame, values=['SIT', 'UAT', 'PROD'], state='readonly', width=15)
        self.env_combo.set('SIT')
        self.env_combo.pack(pady=5)
        
        # Status info
        status_frame = tk.Frame(header_frame, bg='#f0f0f0')
        status_frame.pack(side='right', fill='both', expand=True, padx=(20, 0))
        
        self.status_label = tk.Label(status_frame, 
                                   text="Thông tin trạng thái kết nối MSSQL, DB2 môi trường SIT - UAT", 
                                   bg='lightblue', 
                                   font=('Arial', 10), 
                                   relief='solid', 
                                   borderwidth=1)
        self.status_label.pack(fill='both', expand=True, padx=5, pady=5)
        
        # Search frame
        search_frame = tk.Frame(self.root, bg='#f0f0f0', height=80)
        search_frame.pack(fill='x', padx=10, pady=5)
        search_frame.pack_propagate(False)
        
        # Input fields
        input_frame = tk.Frame(search_frame, bg='#f0f0f0')
        input_frame.pack(side='left', fill='both', expand=True)
        
        # Create input fields in a grid
        fields_frame = tk.Frame(input_frame, bg='#f0f0f0')
        fields_frame.pack(pady=10)
        
        # Số thẻ
        tk.Label(fields_frame, text="Số thẻ", bg='#f0f0f0', font=('Arial', 10)).grid(row=0, column=0, sticky='w', padx=5)
        self.so_the_entry = tk.Entry(fields_frame, width=20, font=('Arial', 10))
        self.so_the_entry.grid(row=1, column=0, padx=5, pady=2)
        
        # Card Token
        tk.Label(fields_frame, text="Card Token", bg='#f0f0f0', font=('Arial', 10)).grid(row=0, column=1, sticky='w', padx=5)
        self.card_token_entry = tk.Entry(fields_frame, width=20, font=('Arial', 10))
        self.card_token_entry.grid(row=1, column=1, padx=5, pady=2)
        
        # CIF
        tk.Label(fields_frame, text="CIF", bg='#f0f0f0', font=('Arial', 10)).grid(row=0, column=2, sticky='w', padx=5)
        self.cif_entry = tk.Entry(fields_frame, width=20, font=('Arial', 10))
        self.cif_entry.grid(row=1, column=2, padx=5, pady=2)
        
        # Search button
        search_button_frame = tk.Frame(search_frame, bg='#f0f0f0')
        search_button_frame.pack(side='right', fill='y', padx=10)
        
        self.search_button = tk.Button(search_button_frame, 
                                     text="TÌM KIẾM", 
                                     bg='lightblue', 
                                     font=('Arial', 10, 'bold'),
                                     width=12,
                                     height=2,
                                     command=self.search_data)
        self.search_button.pack(pady=15)
        
        # Results frame
        results_frame = tk.Frame(self.root, bg='#f0f0f0')
        results_frame.pack(fill='both', expand=True, padx=10, pady=5)
        
        # Results label
        tk.Label(results_frame, 
                text="Table lấy về từ DB", 
                bg='#f0f0f0', 
                font=('Arial', 12, 'bold')).pack(pady=5)
        
        # Create frame for treeview and scrollbars
        tree_frame = tk.Frame(results_frame)
        tree_frame.pack(fill='both', expand=True)
        
        # Treeview for displaying results
        columns = ('ID', 'Số thẻ', 'Card Token', 'CIF', 'Ngày tạo', 'Trạng thái')
        self.tree = ttk.Treeview(tree_frame, columns=columns, show='headings', height=15)
        
        # Define column headings and widths
        for col in columns:
            self.tree.heading(col, text=col)
            self.tree.column(col, width=150, anchor='center')
        
        # Scrollbars
        v_scrollbar = ttk.Scrollbar(tree_frame, orient='vertical', command=self.tree.yview)
        h_scrollbar = ttk.Scrollbar(tree_frame, orient='horizontal', command=self.tree.xview)
        self.tree.configure(yscrollcommand=v_scrollbar.set, xscrollcommand=h_scrollbar.set)
        
        # Grid layout for treeview and scrollbars
        self.tree.grid(row=0, column=0, sticky='nsew')
        v_scrollbar.grid(row=0, column=1, sticky='ns')
        h_scrollbar.grid(row=1, column=0, sticky='ew')
        
        # Configure grid weights
        tree_frame.grid_rowconfigure(0, weight=1)
        tree_frame.grid_columnconfigure(0, weight=1)
        
        # Status bar
        self.status_bar = tk.Label(self.root, 
                                 text="Sẵn sàng", 
                                 relief='sunken', 
                                 anchor='w',
                                 bg='lightgray')
        self.status_bar.pack(side='bottom', fill='x')
        
        # Load all data initially
        self.load_all_data()
        
    def search_data(self):
        """Search data based on input criteria"""
        so_the = self.so_the_entry.get().strip()
        card_token = self.card_token_entry.get().strip()
        cif = self.cif_entry.get().strip()
        
        # Build search query
        query = "SELECT id, so_the, card_token, cif, created_date, status FROM card_info WHERE 1=1"
        params = []
        
        if so_the:
            query += " AND so_the LIKE ?"
            params.append(f"%{so_the}%")
            
        if card_token:
            query += " AND card_token LIKE ?"
            params.append(f"%{card_token}%")
            
        if cif:
            query += " AND cif LIKE ?"
            params.append(f"%{cif}%")
        
        try:
            cursor = self.conn.cursor()
            cursor.execute(query, params)
            results = cursor.fetchall()
            
            # Clear existing data
            for item in self.tree.get_children():
                self.tree.delete(item)
            
            # Insert search results
            for row in results:
                self.tree.insert('', 'end', values=row)
            
            # Update status
            self.status_bar.config(text=f"Tìm thấy {len(results)} kết quả")
            
        except Exception as e:
            messagebox.showerror("Lỗi", f"Lỗi khi tìm kiếm: {str(e)}")
            self.status_bar.config(text="Lỗi tìm kiếm")
    
    def load_all_data(self):
        """Load all data from database"""
        try:
            cursor = self.conn.cursor()
            cursor.execute("SELECT id, so_the, card_token, cif, created_date, status FROM card_info")
            results = cursor.fetchall()
            
            # Clear existing data
            for item in self.tree.get_children():
                self.tree.delete(item)
            
            # Insert all data
            for row in results:
                self.tree.insert('', 'end', values=row)
            
            # Update status
            self.status_bar.config(text=f"Hiển thị {len(results)} bản ghi")
            
        except Exception as e:
            messagebox.showerror("Lỗi", f"Lỗi khi tải dữ liệu: {str(e)}")
            self.status_bar.config(text="Lỗi tải dữ liệu")
    
    def __del__(self):
        """Clean up database connection"""
        if hasattr(self, 'conn'):
            self.conn.close()

def main():
    try:
        root = tk.Tk()
        app = DatabaseGUI(root)
        
        # Add some debugging info
        print(f"Tkinter version: {tk.TkVersion}")
        print(f"TCL_LIBRARY: {os.environ.get('TCL_LIBRARY', 'Not set')}")
        print(f"TK_LIBRARY: {os.environ.get('TK_LIBRARY', 'Not set')}")
        print("Application started successfully!")
        
        root.mainloop()
        
    except Exception as e:
        print(f"Error starting application: {e}")
        print("Please run 'python fix_tkinter.py' to diagnose the issue.")
        input("Press Enter to exit...")

if __name__ == "__main__":
    main()