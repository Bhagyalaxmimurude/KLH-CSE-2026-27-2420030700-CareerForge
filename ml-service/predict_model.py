
import mlflow

import os
from pathlib import Path

PROJECT_ROOT = Path(__file__).resolve().parent.parent
DEFAULT_TRACKING_URI = "sqlite:///" + (PROJECT_ROOT / "mlflow.db").as_posix()
mlflow.set_tracking_uri(
    os.getenv("MLFLOW_TRACKING_URI", DEFAULT_TRACKING_URI)
)
import pandas as pd

import os

MODEL_URI = os.getenv(
    "CAREERFORGE_MODEL_URI",
    str(Path(__file__).resolve().parent / "model")
)

model = mlflow.pyfunc.load_model(MODEL_URI)

# Illustrative student skill scores, each from 0 to 10.
student = pd.DataFrame([{
    "coding": 7,
    "dsa": 6,
    "sql": 8,
    "projects": 7,
    "communication": 8
}])

prediction = model.predict(student)

print("CareerForge model loaded successfully!")
print("Input skill scores:")
print(student.to_string(index=False))
print("Predicted readiness label:", int(prediction[0]))
print("Note: This is a proof-of-concept prediction, not a validated career assessment.")
