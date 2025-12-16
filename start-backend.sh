#!/bin/bash
echo "Starting Fraud Detection Backend Server..."
echo
echo "Make sure you have set your GEMINI_API_KEY environment variable:"
echo "export GEMINI_API_KEY=your_api_key_here"
echo
cd Backend
javac Server.java
if [ $? -ne 0 ]; then
    echo
    echo "❌ Compilation failed! Please check your Java installation."
    exit 1
fi
echo "✅ Java server compiled successfully!"
echo
echo "🚀 Starting server on http://localhost:8001"
echo
java Server