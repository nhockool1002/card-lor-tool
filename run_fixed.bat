@echo off

REM Script to run the Database GUI application (Windows Fixed Version)

echo Starting Database GUI Application (Windows Compatible)...

REM Check if virtual environment exists
if not exist "venv" (
    echo Virtual environment not found. Creating...
    python -m venv venv
    if errorlevel 1 (
        echo Failed to create virtual environment. Please check your Python installation.
        pause
        exit /b 1
    )
)

REM Activate virtual environment
echo Activating virtual environment...
call venv\Scripts\activate
if errorlevel 1 (
    echo Failed to activate virtual environment.
    pause
    exit /b 1
)

REM First, try to diagnose tkinter issues
echo Checking tkinter compatibility...
python fix_tkinter.py
if errorlevel 1 (
    echo Please check tkinter installation issues above.
    pause
    exit /b 1
)

REM Run the fixed application
echo Running database_gui_fixed.py...
python database_gui_fixed.py

echo Application closed.
pause