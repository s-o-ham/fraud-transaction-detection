# Fraud Transaction Detection System

A full-stack application that uses AI (Google Gemini) to detect fraudulent transactions in SMS messages. The system analyzes transaction messages and classifies them as either "Fraud" or "Not Fraud".

**✅ TESTED & WORKING** - Successfully tested with real API integration!

## 🏗️ Architecture

- **Backend**: Java HTTP Server with Google Gemini AI integration
- **Frontend**: React + TypeScript + Vite
- **AI Engine**: Google Gemini 2.5 Flash for fraud classification

## 🚀 Features

- Real-time fraud detection using AI
- Clean and modern web interface
- Fast analysis and response
- CORS enabled for cross-origin requests
- Responsive design with visual feedback

## 📋 Prerequisites

- **Java 8+** (for backend)
- **Node.js 20.19+ or 22.12+** (for frontend)
- **Google Gemini API Key** (for AI integration)

## ⚙️ Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/s-o-ham/fraud-transaction-detection.git
cd fraud-transaction-detection
```

### 2. Get Google Gemini API Key

1. Go to [Google AI Studio](https://aistudio.google.com/)
2. Sign in with your Google account
3. Create a new API key
4. Copy the API key for use in the next step

### 3. Set Environment Variable

**Option 1: Environment Variable (Recommended for Development)**

**Windows (PowerShell):**
```powershell
$env:GEMINI_API_KEY="your_api_key_here"
```

**Windows (Command Prompt):**
```cmd
set GEMINI_API_KEY=your_api_key_here
```

**Linux/Mac:**
```bash
export GEMINI_API_KEY=your_api_key_here
```

**Option 2: Configuration File (Alternative)**
```bash
# Copy the template and add your key
cp config.properties.template config.properties
# Edit config.properties and replace 'your_api_key_here' with your actual key
```

### 4. Start the Backend

```bash
cd Backend
javac Server.java
java Server
```

The server will start on `http://localhost:8001`

**Windows PowerShell (Quick Start):**
```powershell
cd Backend; $env:GEMINI_API_KEY="your_api_key_here"; javac Server.java; java Server
```

### 5. Start the Frontend

Open a new terminal:

```bash
cd Frontend
npm install
npm run dev
```

The frontend will start on `http://localhost:5173`

## 🖥️ Usage

1. Open your browser and go to `http://localhost:5173`
2. Paste a transaction message in the text area
3. Click "Check Fraud" to analyze the message
4. View the result:
   - 🚨 **FRAUD DETECTED** - for suspicious transactions
   - ✅ **SAFE TRANSACTION** - for legitimate transactions

## 📱 Example Messages to Test

**✅ TESTED - These work with real API:**

**Fraud Examples:**
- "URGENT! Your account will be closed. Click here immediately: suspicious-link.com" → **Result: Fraud** ✅
- "Congratulations! You've won $10,000! Send $500 processing fee to claim." → **Result: Fraud** ✅
- "Your card is blocked. Verify details at fake-bank-site.com" → **Result: Fraud** ✅

**Safe Examples:**
- "Transaction alert: $25.50 spent at Starbucks on 12/17/2025" → **Result: Not Fraud** ✅
- "Your Amazon order #123456 has been shipped" → **Result: Not Fraud** ✅
- "Bank balance: $1,250.00 after recent transaction" → **Result: Not Fraud** ✅

## 🧪 Test Results

### API Test (Confirmed Working)
```powershell
# Test command used:
Invoke-RestMethod -Uri "http://localhost:8001/classify" -Method Post -ContentType "application/json" -Body '{"message":"Your account has been compromised. Click here: suspicious-link.com"}'

# Response:
result
------
Fraud
```

✅ **Status**: All tests passed - AI correctly identifies fraud patterns!

## 🛠️ Project Structure

```
fraud-transaction-detection/
├── Backend/
│   ├── Server.java          # Java HTTP server with Gemini AI
│   └── Server.class         # Compiled Java class
├── Frontend/
│   ├── src/
│   │   ├── App.tsx          # Main React component
│   │   ├── App.css          # Styling
│   │   └── main.tsx         # App entry point
│   ├── package.json         # Dependencies and scripts
│   └── vite.config.ts       # Vite configuration
└── README.md               # Project documentation
```

## 🔧 Development

### Backend Development
- The server runs on port 8001
- Modify `Server.java` and recompile with `javac Server.java`
- Restart with `java Server`

### Frontend Development
- Uses Vite for hot module replacement
- Automatically reloads on file changes
- Build production version with `npm run build`

## 🚦 API Endpoints

### POST `/classify`
Classifies a transaction message as fraud or not.

**Request:**
```json
{
  "message": "Your transaction message here"
}
```

**Response:**
```json
{
  "result": "Fraud" // or "Not Fraud"
}
```

## 🛡️ Security Notes

- Keep your Gemini API key secure and never commit it to version control
- The application analyzes text content only - no personal data is stored
- All communication uses HTTPS when deployed to production

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

This project is open source and available under the MIT License.

## 🆘 Troubleshooting

**Backend won't start:**
- Ensure GEMINI_API_KEY environment variable is set
- Check that Java 8+ is installed
- Verify port 8001 is available

**Frontend build issues:**
- Update Node.js to version 20.19+ or 22.12+
- Delete `node_modules` and run `npm install` again
- Check for TypeScript errors with `npm run lint`

**API calls failing:**
- Verify backend is running on localhost:8001
- Check CORS settings if running on different domains
- Ensure API key is valid and has credits

## 🚀 Deployment

For production deployment:

1. **Backend**: Deploy Java application to your preferred cloud service
2. **Frontend**: Build with `npm run build` and deploy the `dist` folder
3. **Environment**: Set GEMINI_API_KEY in production environment
4. **CORS**: Update CORS settings for your production domain