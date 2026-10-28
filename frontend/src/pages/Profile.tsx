import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getProfile, updateProfile } from "../api/profile";

interface ProfileData {
  name: string;
  email: string;
  education: string;
  skills: string;
  careerGoal: string;
  experience: string;
  bio: string;
}

function Profile() {
  const [profile, setProfile] = useState<ProfileData>({
    name: "",
    email: "",
    education: "",
    skills: "",
    careerGoal: "",
    experience: "",
    bio: "",
  });

  const [message, setMessage] = useState("");

  useEffect(() => {
    async function loadProfile() {
      try {
        const data = await getProfile();
        setProfile(data);
      } catch (error) {
        console.error("Profile loading error:", error);
        setMessage("Failed to load profile.");
      }
    }

    loadProfile();
  }, []);

  function handleChange(
    event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) {
    const { name, value } = event.target;

    setProfile((currentProfile) => ({
      ...currentProfile,
      [name]: value,
    }));
  }

  async function handleUpdate(event: { preventDefault: () => void }) {
    event.preventDefault();

    try {
      await updateProfile({
        name: profile.name,
        education: profile.education,
        skills: profile.skills,
        careerGoal: profile.careerGoal,
        experience: profile.experience,
        bio: profile.bio,
      });

      setMessage("Profile updated successfully!");
    } catch (error) {
      console.error("Profile update error:", error);
      setMessage("Failed to update profile.");
    }
  }

  return (
    <div>
      <h1>CareerForge</h1>
      <h2>My Profile</h2>

      <form onSubmit={handleUpdate}>
        <div>
          <label htmlFor="name">Name</label>
          <br />
          <input
            id="name"
            name="name"
            type="text"
            value={profile.name}
            onChange={handleChange}
            required
          />
        </div>

        <br />

        <div>
          <label htmlFor="email">Email</label>
          <br />
          <input
            id="email"
            name="email"
            type="email"
            value={profile.email}
            readOnly
          />
        </div>

        <br />

        <div>
          <label htmlFor="education">Education</label>
          <br />
          <input
            id="education"
            name="education"
            type="text"
            value={profile.education}
            onChange={handleChange}
          />
        </div>

        <br />

        <div>
          <label htmlFor="skills">Skills</label>
          <br />
          <input
            id="skills"
            name="skills"
            type="text"
            value={profile.skills}
            onChange={handleChange}
          />
        </div>

        <br />

        <div>
          <label htmlFor="careerGoal">Career Goal</label>
          <br />
          <input
            id="careerGoal"
            name="careerGoal"
            type="text"
            value={profile.careerGoal}
            onChange={handleChange}
          />
        </div>

        <br />

        <div>
          <label htmlFor="experience">Experience</label>
          <br />
          <input
            id="experience"
            name="experience"
            type="text"
            value={profile.experience}
            onChange={handleChange}
          />
        </div>

        <br />

        <div>
          <label htmlFor="bio">Bio</label>
          <br />
          <textarea
            id="bio"
            name="bio"
            value={profile.bio}
            onChange={handleChange}
          />
        </div>

        <br />

        <button type="submit">Update Profile</button>
      </form>

      <p>{message}</p>

      <p>
        <Link to="/">Back to Dashboard</Link>
      </p>
    </div>
  );
}

export default Profile;