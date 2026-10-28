import { useState } from "react";
import { Link } from "react-router-dom";

interface SkillGapData {
  currentSkills: string[];
  missingSkills: string[];
  targetRole: string;
}

const API_URL = "http://localhost:8082";

function SkillGap() {
  const [targetRole, setTargetRole] = useState("Software Engineer");
  const [result, setResult] = useState<SkillGapData | null>(null);
  const [message, setMessage] = useState("");

  async function handleAnalyze(event: { preventDefault: () => void }) {
    event.preventDefault();

    const token = localStorage.getItem("token");

    if (!token) {
      setMessage("Please login before analyzing your skill gap.");
      return;
    }

    try {
      const response = await fetch(
        `${API_URL}/api/skill-gap?targetRole=${encodeURIComponent(targetRole)}`,
        {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.message || "Skill gap analysis failed.");
      }

      setResult(data);
      setMessage("Skill gap analysis completed successfully.");
    } catch (error) {
      console.error("Skill gap analysis error:", error);
      setMessage(
        error instanceof Error
          ? error.message
          : "Skill gap analysis failed."
      );
      setResult(null);
    }
  }

  return (
    <div>
      <h1>CareerForge</h1>
      <h2>Skill Gap Analysis</h2>

      <p>
        Compare your current skills with the skills required for your target
        career role.
      </p>

      <form onSubmit={handleAnalyze}>
        <label htmlFor="targetRole">Target Role</label>
        <br />

        <input
          id="targetRole"
          type="text"
          value={targetRole}
          onChange={(event) => setTargetRole(event.target.value)}
          placeholder="Enter target role"
          required
        />

        <br />
        <br />

        <button type="submit">Analyze Skill Gap</button>
      </form>

      <p>{message}</p>

      {result && (
        <div>
          <hr />

          <h3>Target Role</h3>
          <p>{result.targetRole}</p>

          <h3>Current Skills</h3>

          {result.currentSkills.length > 0 ? (
            <ul>
              {result.currentSkills.map((skill) => (
                <li key={skill}>{skill}</li>
              ))}
            </ul>
          ) : (
            <p>No current skills found.</p>
          )}

          <h3>Missing Skills</h3>

          {result.missingSkills.length > 0 ? (
            <ul>
              {result.missingSkills.map((skill) => (
                <li key={skill}>{skill}</li>
              ))}
            </ul>
          ) : (
            <p>No skill gaps found for this role.</p>
          )}
        </div>
      )}

      <p>
        <Link to="/">Back to Dashboard</Link>
      </p>
    </div>
  );
}

export default SkillGap;