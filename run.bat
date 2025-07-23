@echo off

REM Script to run the Database GUI application on Windows

echo Starting Database GUI Application...

REM Check if virtual environment exists
if not exist "venv" (
    echo Virtual environment not found. Creating...
    python -m venv venv
)

REM Activate virtual environment
echo Activating virtual environment...
call venv\Scripts\activate

REM Run the application
echo Running database_gui.py...
python database_gui.py

echo Application closed.
pause