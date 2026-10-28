
import { useState } from "react";
import { Link } from "react-router-dom";

const API_URL = "http://localhost:8082";

function Resume() {
  const [file, setFile] = useState<File | null>(null);
  const [message, setMessage] = useState("");

  function handleFileChange(event: {
    target: HTMLInputElement;
  }) {
    const selectedFile = event.target.files?.[0] ?? null;
    setFile(selectedFile);
    setMessage("");
  }

  async function handleUpload(event: { preventDefault: () => void }) {
    event.preventDefault();

    if (!file) {
      setMessage("Please select a PDF resume.");
      return;
    }

    if (file.type !== "application/pdf") {
      setMessage("Only PDF files are allowed.");
      return;
    }

    const token = localStorage.getItem("token");

    if (!token) {
      setMessage("Please login before uploading a resume.");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await fetch(`${API_URL}/api/resume/upload`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: formData,
      });

      const result = await response.text();

      if (!response.ok) {
        throw new Error(result);
      }

      setMessage(result);
      setFile(null);
    } catch (error) {
      console.error("Resume upload error:", error);
      setMessage(
        error instanceof Error
          ? error.message
          : "Resume upload failed."
      );
    }
  }

  return (
    <div>
      <h1>CareerForge</h1>
      <h2>Resume Analysis</h2>

      <p>Upload your resume to analyze your skills, education, and experience.</p>

      <form onSubmit={handleUpload}>
        <label htmlFor="resume">Select Resume (PDF)</label>
        <br />
        <input
          id="resume"
          type="file"
          accept=".pdf,application/pdf"
          onChange={handleFileChange}
        />

        <br />
        <br />

        {file && <p>Selected file: {file.name}</p>}

        <button type="submit">Upload Resume</button>
      </form>

      <p>{message}</p>

      <p>
        <Link to="/">Back to Dashboard</Link>
      </p>
    </div>
  );
}

export default Resume;

