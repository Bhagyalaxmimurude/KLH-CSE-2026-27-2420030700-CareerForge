const API_URL = "http://localhost:8082";

export async function getProfile() {
  const token = localStorage.getItem("token");

  const response = await fetch(`${API_URL}/api/profile`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    throw new Error("Failed to load profile");
  }

  return response.json();
}

export async function updateProfile(profile: {
  name: string;
  education: string;
  skills: string;
  careerGoal: string;
  experience: string;
  bio: string;
}) {
  const token = localStorage.getItem("token");

  const response = await fetch(`${API_URL}/api/profile`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(profile),
  });

  if (!response.ok) {
    throw new Error("Failed to update profile");
  }

  return response.json();
}