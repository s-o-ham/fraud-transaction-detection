#!/bin/bash
echo "Starting Fraud Detection Frontend..."
echo
cd Frontend
if [ ! -d "node_modules" ]; then
    echo "📦 Installing dependencies..."
    npm install
    if [ $? -ne 0 ]; then
        echo
        echo "❌ npm install failed! Please check your Node.js installation."
        exit 1
    fi
fi
echo "✅ Dependencies ready!"
echo
echo "🚀 Starting frontend development server..."
echo "Frontend will be available at http://localhost:5173"
echo
npm run dev