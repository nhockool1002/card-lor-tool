#!/usr/bin/env python3
"""
Script to diagnose and fix tkinter TCL library issues on Windows
"""

import os
import sys
import platform

def diagnose_tkinter():
    print("=== Tkinter Diagnostic Tool ===")
    print(f"Python version: {sys.version}")
    print(f"Platform: {platform.platform()}")
    print(f"Python executable: {sys.executable}")
    print(f"Python path: {sys.path}")
    print()
    
    # Check if tkinter is available
    try:
        import tkinter
        print("✓ tkinter module is available")
    except ImportError as e:
        print(f"✗ tkinter module not available: {e}")
        return False
    
    # Check TCL/TK environment variables
    print("\n=== Environment Variables ===")
    tcl_vars = ['TCL_LIBRARY', 'TK_LIBRARY', 'TIX_LIBRARY']
    for var in tcl_vars:
        value = os.environ.get(var)
        if value:
            print(f"{var}: {value}")
            if not os.path.exists(value):
                print(f"  ✗ Path does not exist!")
        else:
            print(f"{var}: Not set")
    
    # Try to find TCL library
    python_dir = os.path.dirname(sys.executable)
    possible_tcl_dirs = [
        os.path.join(python_dir, 'tcl'),
        os.path.join(python_dir, 'Lib', 'tcl'),
        os.path.join(python_dir, 'tcl8.6'),
        os.path.join(python_dir, 'lib', 'tcl8.6'),
        os.path.join(os.path.dirname(python_dir), 'tcl'),
        os.path.join(os.path.dirname(python_dir), 'lib', 'tcl8.6')
    ]
    
    print(f"\n=== Looking for TCL libraries ===")
    tcl_found = None
    for tcl_dir in possible_tcl_dirs:
        print(f"Checking: {tcl_dir}")
        if os.path.exists(tcl_dir):
            init_tcl = os.path.join(tcl_dir, 'init.tcl')
            if os.path.exists(init_tcl):
                print(f"  ✓ Found init.tcl at: {init_tcl}")
                tcl_found = tcl_dir
                break
            else:
                print(f"  - Directory exists but no init.tcl")
        else:
            print(f"  - Directory does not exist")
    
    if tcl_found:
        print(f"\n=== Setting up environment ===")
        os.environ['TCL_LIBRARY'] = tcl_found
        tk_dir = tcl_found.replace('tcl', 'tk')
        if os.path.exists(tk_dir):
            os.environ['TK_LIBRARY'] = tk_dir
            print(f"Set TK_LIBRARY to: {tk_dir}")
        
        print(f"Set TCL_LIBRARY to: {tcl_found}")
        return True
    else:
        print("\n✗ Could not find TCL library directory")
        return False

def try_tkinter():
    """Try to create a simple tkinter window"""
    try:
        import tkinter as tk
        root = tk.Tk()
        root.title("Tkinter Test")
        root.geometry("300x200")
        
        label = tk.Label(root, text="Tkinter is working!", font=("Arial", 14))
        label.pack(expand=True)
        
        button = tk.Button(root, text="Close", command=root.quit)
        button.pack(pady=10)
        
        print("✓ Tkinter test window created successfully!")
        print("Close the test window to continue...")
        root.mainloop()
        root.destroy()
        return True
        
    except Exception as e:
        print(f"✗ Failed to create tkinter window: {e}")
        return False

def main():
    print("Diagnosing tkinter issues...\n")
    
    # First, try to diagnose and fix
    if diagnose_tkinter():
        print("\n=== Testing tkinter ===")
        if try_tkinter():
            print("\n✓ Tkinter is working! You can now run the main application.")
            return True
    
    print("\n=== Troubleshooting Suggestions ===")
    print("1. Reinstall Python from python.org (make sure to check 'Add Python to PATH')")
    print("2. Try using Anaconda or Miniconda instead of standard Python")
    print("3. Install tkinter separately: conda install tk (if using conda)")
    print("4. Set environment variables manually:")
    print("   - TCL_LIBRARY=C:\\Python\\tcl\\tcl8.6")
    print("   - TK_LIBRARY=C:\\Python\\tcl\\tk8.6")
    print("5. Try running Python from Command Prompt as Administrator")
    
    return False

if __name__ == "__main__":
    main()