import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Profile from "./pages/Profile";
import Resume from "./pages/Resume";
import SkillGap from "./pages/SkillGap";
import CareerRecommendations from "./pages/CareerRecommendations";
import LearningRoadmap from "./pages/LearningRoadmap";
import InterviewPreparation from "./pages/InterviewPreparation";

function Home() {
  return (
    <div>
      <h1>CareerForge</h1>

      <p>AI-Powered Career Development Platform</p>

      <hr />

      <h2>Career Dashboard</h2>

      <p>
        Welcome to CareerForge! Manage your career development activities
        from one place.
      </p>

      <h3>Career Development Modules</h3>

      <ul>
        <li>
          <Link to="/resume">Resume Analysis</Link>
        </li>

        <li>
          <Link to="/skill-gap">Skill Gap Analysis</Link>
        </li>

        <li>
          <Link to="/career-recommendations">
            Career Recommendations
          </Link>
        </li>

        <li>
          <Link to="/learning-roadmap">
            Learning Roadmap
          </Link>
        </li>

        <li>
          <Link to="/interview">
            Interview Preparation
          </Link>
        </li>
      </ul>

      <hr />

      <p>
        <Link to="/profile">My Profile</Link>
      </p>

      <button
        type="button"
        onClick={() => {
          localStorage.removeItem("token");
          window.location.href = "/login";
        }}
      >
        Logout
      </button>
    </div>
  );
}

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />

        <Route path="/login" element={<Login />} />

        <Route path="/register" element={<Register />} />

        <Route path="/profile" element={<Profile />} />

        <Route path="/resume" element={<Resume />} />

        <Route path="/skill-gap" element={<SkillGap />} />

        <Route
          path="/career-recommendations"
          element={<CareerRecommendations />}
        />

        <Route
          path="/learning-roadmap"
          element={<LearningRoadmap />}
        />

        <Route
          path="/interview"
          element={<InterviewPreparation />}
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;

