import { useState } from "react";
import { Link } from "react-router-dom";

interface InterviewQuestion {
  topic: string;
  question: string;
  expectedAnswer: string;
}

const API_URL = "http://localhost:8082";

function InterviewPreparation() {
  const [role, setRole] = useState("Software Engineer");
  const [questions, setQuestions] = useState<InterviewQuestion[]>([]);
  const [message, setMessage] = useState("");

  async function handleGenerateQuestions(
    event: { preventDefault: () => void }
  ) {
    event.preventDefault();

    const token = localStorage.getItem("token");

    if (!token) {
      setMessage("Please login before preparing for the interview.");
      return;
    }

    try {
      const response = await fetch(
        `${API_URL}/api/interview/questions?role=${encodeURIComponent(role)}`,
        {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      const responseText = await response.text();

      if (!response.ok) {
        throw new Error(
          responseText || "Failed to generate interview questions."
        );
      }

      const data: InterviewQuestion[] = JSON.parse(responseText);

      setQuestions(data);
      setMessage("Interview questions generated successfully.");
    } catch (error) {
      console.error("Interview preparation error:", error);

      setMessage(
        error instanceof Error
          ? error.message
          : "Failed to generate interview questions."
      );

      setQuestions([]);
    }
  }

  return (
    <div>
      <h1>CareerForge</h1>

      <h2>Interview Preparation</h2>

      <p>
        Practice interview questions based on your target career role.
      </p>

      <form onSubmit={handleGenerateQuestions}>
        <label htmlFor="role">Target Role</label>
        <br />

        <input
          id="role"
          type="text"
          value={role}
          onChange={(event) => setRole(event.target.value)}
          placeholder="Enter target role"
          required
        />

        <br />
        <br />

        <button type="submit">Generate Questions</button>
      </form>

      <p>{message}</p>

      {questions.length > 0 && (
        <div>
          <hr />

          <h3>Interview Questions</h3>

          {questions.map((item, index) => (
            <div key={`${item.topic}-${index}`}>
              <h4>
                {index + 1}. {item.topic}
              </h4>

              <p>
                <strong>Question:</strong>
              </p>

              <p>{item.question}</p>

              <p>
                <strong>Expected Answer:</strong>
              </p>

              <p>{item.expectedAnswer}</p>

              <hr />
            </div>
          ))}
        </div>
      )}

      <p>
        <Link to="/">Back to Dashboard</Link>
      </p>
    </div>
  );
}

export default InterviewPreparation;