import { useState } from "react";
import { Link } from "react-router-dom";

interface RoadmapItem {
  skill: string;
  duration: string;
  topics: string[];
}

const API_URL = "http://localhost:8082";

function LearningRoadmap() {
  const [targetRole, setTargetRole] = useState("Software Engineer");
  const [roadmap, setRoadmap] = useState<RoadmapItem[]>([]);
  const [message, setMessage] = useState("");

  async function handleGenerateRoadmap(
    event: { preventDefault: () => void }
  ) {
    event.preventDefault();

    const token = localStorage.getItem("token");

    if (!token) {
      setMessage("Please login before generating your learning roadmap.");
      return;
    }

    try {
      const response = await fetch(
        `${API_URL}/api/learning-roadmap?targetRole=${encodeURIComponent(
          targetRole
        )}`,
        {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      const data = await response.json();

      if (!response.ok) {
        throw new Error(
          data.message || "Failed to generate learning roadmap."
        );
      }

      setRoadmap(data);
      setMessage("Learning roadmap generated successfully.");
    } catch (error) {
      console.error("Learning roadmap error:", error);
      setMessage(
        error instanceof Error
          ? error.message
          : "Failed to generate learning roadmap."
      );
      setRoadmap([]);
    }
  }

  return (
    <div>
      <h1>CareerForge</h1>
      <h2>Learning Roadmap</h2>

      <p>
        Generate a personalized learning roadmap based on your skill gaps.
      </p>

      <form onSubmit={handleGenerateRoadmap}>
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

        <button type="submit">Generate Roadmap</button>
      </form>

      <p>{message}</p>

      {roadmap.length > 0 && (
        <div>
          <hr />

          <h3>Your Learning Roadmap</h3>

          {roadmap.map((item) => (
            <div key={item.skill}>
              <h4>{item.skill}</h4>

              <p>
                <strong>Duration:</strong> {item.duration}
              </p>

              <p>
                <strong>Topics:</strong>
              </p>

              <ul>
                {item.topics.map((topic) => (
                  <li key={topic}>{topic}</li>
                ))}
              </ul>

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

export default LearningRoadmap;