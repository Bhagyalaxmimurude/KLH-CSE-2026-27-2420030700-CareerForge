
import mlflow
import pandas as pd

import os

MODEL_URI = os.getenv(
    "CAREERFORGE_MODEL_URI",
    "models:/m-3e1921d69a874a059c2807d4b9b58813"
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
