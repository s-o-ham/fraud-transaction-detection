import { useState } from "react";
import "./App.css";

type ApiResponse = {
  result?: string;
  error?: string;
};

const App: React.FC = () => {
  const [message, setMessage] = useState<string>("");
  const [result, setResult] = useState<"Fraud" | "Not Fraud" | "Not" | "">("");
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string>("");

  const classifyMessage = async (): Promise<void> => {
    if (!message.trim()) {
      setError("Please enter an SMS message");
      return;
    }

    setLoading(true);
    setError("");
    setResult("");

    try {
      const response = await fetch("http://localhost:8001/classify", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ message }),
      });

      const data: ApiResponse = await response.json();

      //  Treat anything unexpected as ERROR
      if (!response.ok) {
        setError(data.error || "Server error. Please try again later.");
        return;
      }

      if (data.result !== "Fraud" && data.result !== "Not Fraud") {
        setError("Unable to classify message. Please try again.");
        return;
      }

      setResult(data.result);

    } catch {
      setError("Server error. Please try again later.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <div className={`card ${loading ? "analyzing" : ""}`}>
        <h1 className="title">Fraud Transaction Detection</h1>

        <div className="textarea-wrapper">
          <textarea
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            placeholder="📩 Paste transaction message here..."
            className="textarea"
          />
        </div>

        <button onClick={classifyMessage} disabled={loading} className="button">
          {loading ? "Analyzing Transaction..." : "Check Fraud"}
        </button>

        {loading && (
          <div className="searching">
            🔍 Scanning patterns... detecting anomalies...
          </div>
        )}

        {/*  Show ONLY valid results */}
        {result === "Fraud" && (
          <div className="result fraud">
            🚨 FRAUD DETECTED! Transaction is Dangerous
          </div>
        )}

        {(result === "Not Fraud" || result === "Not" )&& (
          <div className="result safe">
            ✅ SAFE TRANSACTION — Good to Go!
          </div>
        )}

        {/*  Errors shown instead of Safe */}
        {error && <p className="error">{error}</p>}
      </div>
    </div>
  );
};

export default App;
