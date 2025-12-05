#!/bin/bash

# =========================================
# SET YOUR CREDENTIALS HERE
# =========================================
export DB_USERNAME=root
export DB_PASSWORD=
# =========================================

echo "========================================="
echo "  Note App - Single Window Mode"
echo "========================================="
echo
echo "Press Ctrl+C to stop everything"
echo

# Build backend
echo "[1/3] Building backend..."
cd backend/api

# Check if gradlew exists and make it executable
if [ -f "./gradlew" ]; then
    chmod +x gradlew
    ./gradlew build -x test
    if [ $? -ne 0 ]; then
        echo "Build failed!"
        cd ../..
        read -p "Press any key to continue..."
        exit 1
    fi
else
    echo "❌ gradlew not found!"
    cd ../..
    read -p "Press any key to continue..."
    exit 1
fi
cd ../..

echo "✅ Backend built"
echo

# Setup frontend
echo "[2/3] Setting up frontend..."
cd frontend/noteapp
if [ -f "package.json" ]; then
    npm install
    echo "✅ Frontend ready"
else
    echo "❌ package.json not found!"
    cd ../..
    read -p "Press any key to continue..."
    exit 1
fi
cd ../..

echo "[3/3] Starting applications..."
echo

# Get JAR file
JAR_FILE=$(find backend/api/build/libs -name "*.jar" ! -name "*-plain.jar" | head -n 1)

if [ -z "$JAR_FILE" ]; then
    echo "❌ No JAR file found!"
    exit 1
fi

echo "Starting backend on port 8080..."

# Start backend in background
cd backend/api/build/libs
java -jar "$JAR_FILE" --server.address=127.0.0.1 --server.port=8080 &
BACKEND_PID=$!
cd ../../../..

echo "Backend started (PID: $BACKEND_PID). Waiting 15 seconds..."
sleep 15

echo "Starting frontend on port 3000..."
cd frontend/noteapp
echo
echo "========================================="
echo "  Frontend running - Press Ctrl+C to stop"
echo "========================================="
echo

# Function to handle cleanup
cleanup() {
    echo
    echo "Stopping all applications..."
    
    # Kill backend process
    if [ -n "$BACKEND_PID" ]; then
        echo "Stopping backend (PID: $BACKEND_PID)..."
        kill $BACKEND_PID 2>/dev/null || true
    fi
    
    # Kill any remaining Java processes for this JAR
    pkill -f "$(basename "$JAR_FILE")" 2>/dev/null || true
    
    echo "All applications stopped."
    echo
    exit 0
}

# Trap Ctrl+C and other termination signals
trap cleanup SIGINT SIGTERM

# Start frontend in foreground (this will block)
npm start

# If we get here, frontend has stopped
cleanup