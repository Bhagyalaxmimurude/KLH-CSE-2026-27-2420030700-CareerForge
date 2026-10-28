import { useState } from "react";
import { Link } from "react-router-dom";

interface CareerRecommendation {
  career: string;
  matchPercentage: number;
  totalRequiredSkills: number;
  matchedSkills: number;
}

const API_URL = "http://localhost:8082";

function CareerRecommendations() {
  const [recommendations, setRecommendations] = useState<
    CareerRecommendation[]
  >([]);
  const [message, setMessage] = useState("");

  async function handleGetRecommendations() {
    const token = localStorage.getItem("token");

    if (!token) {
      setMessage("Please login before viewing career recommendations.");
      return;
    }

    try {
      const response = await fetch(
        `${API_URL}/api/career-recommendations`,
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
          data.message || "Failed to load career recommendations."
        );
      }

      setRecommendations(data);
      setMessage("Career recommendations loaded successfully.");
    } catch (error) {
      console.error("Career recommendations error:", error);
      setMessage(
        error instanceof Error
          ? error.message
          : "Failed to load career recommendations."
      );
      setRecommendations([]);
    }
  }

  return (
    <div>
      <h1>CareerForge</h1>
      <h2>Career Recommendations</h2>

      <p>
        Discover career roles that match your skills and experience.
      </p>

      <button type="button" onClick={handleGetRecommendations}>
        Get Career Recommendations
      </button>

      <p>{message}</p>

      {recommendations.length > 0 && (
        <div>
          <hr />

          <h3>Recommended Career Roles</h3>

          {recommendations.map((recommendation) => (
            <div key={recommendation.career}>
              <h4>{recommendation.career}</h4>

              <p>
                Match Percentage: {recommendation.matchPercentage}%
              </p>

              <p>
                Matched Skills: {recommendation.matchedSkills}
              </p>

              <p>
                Required Skills: {recommendation.totalRequiredSkills}
              </p>

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

export default CareerRecommendations;