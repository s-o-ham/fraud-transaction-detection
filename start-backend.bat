@echo off
echo Starting Fraud Detection Backend Server...
echo.
echo Make sure you have set your GEMINI_API_KEY environment variable:
echo set GEMINI_API_KEY=your_api_key_here
echo.
cd Backend
javac Server.java
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ❌ Compilation failed! Please check your Java installation.
    pause
    exit /b 1
)
echo ✅ Java server compiled successfully!
echo.
echo 🚀 Starting server on http://localhost:8001
echo.
java Server
pause