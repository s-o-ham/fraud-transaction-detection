@echo off
echo Starting Fraud Detection Frontend...
echo.
cd Frontend
if not exist node_modules (
    echo 📦 Installing dependencies...
    npm install
    if %ERRORLEVEL% NEQ 0 (
        echo.
        echo ❌ npm install failed! Please check your Node.js installation.
        pause
        exit /b 1
    )
)
echo ✅ Dependencies ready!
echo.
echo 🚀 Starting frontend development server...
echo Frontend will be available at http://localhost:5173
echo.
npm run dev