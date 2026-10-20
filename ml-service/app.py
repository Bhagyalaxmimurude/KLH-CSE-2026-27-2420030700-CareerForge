
from fastapi import FastAPI
from pydantic import BaseModel, Field
import mlflow
import pandas as pd
import os

app = FastAPI(
    title="CareerForge ML API",
    description="Proof-of-concept career readiness prediction service",
    version="1.0.0"
)

MODEL_URI = os.getenv(
    "CAREERFORGE_MODEL_URI",
    "models:/m-3e1921d69a874a059c2807d4b9b58813"
)
model = mlflow.pyfunc.load_model(MODEL_URI)


class StudentSkills(BaseModel):
    coding: int = Field(ge=0, le=10)
    dsa: int = Field(ge=0, le=10)
    sql: int = Field(ge=0, le=10)
    projects: int = Field(ge=0, le=10)
    communication: int = Field(ge=0, le=10)


@app.get("/")
def home():
    return {"message": "CareerForge ML API is running!"}


@app.get("/health")
def health():
    return {"status": "healthy", "model_loaded": True}


@app.post("/predict")
def predict(skills: StudentSkills):
    student_data = pd.DataFrame([skills.model_dump()])
    prediction = int(model.predict(student_data)[0])

    return {
        "predicted_label": prediction,
        "label_meaning": (
            "Positive class in illustrative training data"
            if prediction == 1
            else "Negative class in illustrative training data"
        ),
        "note": "Proof of concept using synthetic data; not a validated career assessment."
    }
