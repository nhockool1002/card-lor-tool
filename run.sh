#!/bin/bash

# Script to run the Database GUI application

echo "Starting Database GUI Application..."

# Check if virtual environment exists
if [ ! -d "venv" ]; then
    echo "Virtual environment not found. Creating..."
    python3 -m venv venv
fi

# Activate virtual environment
echo "Activating virtual environment..."
source venv/bin/activate

# Run the application
echo "Running database_gui.py..."
python database_gui.py

echo "Application closed."