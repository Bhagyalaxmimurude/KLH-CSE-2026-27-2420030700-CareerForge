# CareerForge ML Service

A proof-of-concept machine learning API for CareerForge. It uses a Random Forest classifier, scikit-learn, MLflow, FastAPI, and pandas.

**Important:** The current model is trained on synthetic illustrative data. Its predictions are not validated career-readiness or hiring assessments.

## Requirements

- Python 3.13 or a compatible Python version
- pip

## 1. Set up the environment

Run these commands from the CareerForge project root:

```powershell
py -3.13 -m venv ml-service\.venv
.\ml-service\.venv\Scripts\Activate.ps1
python -m pip install --upgrade pip
pip install -r .\ml-service\requirements.txt
```

If PowerShell blocks environment activation, use the environment's Python executable directly:

```powershell
.\ml-service\.venv\Scripts\python.exe -m pip install -r .\ml-service\requirements.txt
```

## 2. Configure MLflow tracking

The training script and API must use the same MLflow tracking database.

For the existing local development setup, run:

```powershell
$env:MLFLOW_TRACKING_URI = "sqlite:///C:/Users/Vaishnavi/OneDrive/Desktop/CareerForge/mlflow.db"
```

If you are using another computer, replace this with the absolute path to your own `mlflow.db` file.

The local MLflow database and generated model artifacts should not be committed to Git.

## 3. Train the model

From the CareerForge project root, run:

```powershell
python .\ml-service\train_model.py
```

This script:

- Creates a reproducible synthetic dataset.
- Splits the dataset into training and testing sets.
- Trains a Random Forest classifier.
- Calculates accuracy, precision, recall, and F1 score.
- Logs model parameters, metrics, and the model to MLflow.

The dataset is illustrative and is not representative of real hiring outcomes.

## 4. Configure the model URI

The API reads its model URI from the `CAREERFORGE_MODEL_URI` environment variable.

After training, configure this variable with a model ID that exists in your MLflow environment:

```powershell
$env:CAREERFORGE_MODEL_URI = "models:/<your-model-id>"
```

Replace `<your-model-id>` with a valid model ID logged by your MLflow version.

If this variable is not set, `app.py` uses its existing default model ID. That ID belongs to the original local MLflow environment and may not exist on another computer.

**Important:** The API must be able to access the selected model before it starts.

## 5. Start the ML API

From the CareerForge project root, run:

```powershell
uvicorn app:app --app-dir .\ml-service --reload --port 8000
```

The API will be available at:

- Base endpoint: `http://127.0.0.1:8000/`
- Health check: `http://127.0.0.1:8000/health`
- Prediction endpoint: `http://127.0.0.1:8000/predict`
- Interactive API documentation: `http://127.0.0.1:8000/docs`

Keep this terminal open while testing the API.

## 6. Test the health endpoint

Open another PowerShell terminal and run:

```powershell
Invoke-RestMethod -Uri "http://127.0.0.1:8000/health" -Method Get
```

Expected response:

```json
{
  "status": "healthy",
  "model_loaded": true
}
```

## 7. Test a prediction

Run this command in the second PowerShell terminal:

```powershell
$body = @{
    coding = 7
    dsa = 6
    sql = 8
    projects = 7
    communication = 8
} | ConvertTo-Json

Invoke-RestMethod `
    -Uri "http://127.0.0.1:8000/predict" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body
```

Each skill score must be an integer between 0 and 10.

The response contains:

- `predicted_label`: the predicted class.
- `label_meaning`: an explanation of the illustrative class.
- `note`: a disclaimer about the synthetic dataset.

The predicted class is not a validated measure of career readiness.

## 8. Open the MLflow UI

Use a separate terminal with the same Python environment and tracking configuration.

Start the UI:

```powershell
mlflow ui --backend-store-uri "$env:MLFLOW_TRACKING_URI" --host 127.0.0.1 --port 5000
```

Open:

`http://127.0.0.1:5000`

You can inspect the training experiment, parameters, metrics, and logged model.

## 9. Run the standalone prediction script

After configuring MLflow and the model URI, run:

```powershell
python .\ml-service\predict_model.py
```

This script loads the configured model and performs a sample prediction.

Note that `predict_model.py` currently uses the model URI defined inside that script. If you use a different MLflow model ID, update its `MODEL_URI` to match the model you want to test.

## 10. Verify dependencies

Run:

```powershell
python -m pip check
```

Expected output:

```text
No broken requirements found.
```

## Limitations

- The dataset contains only 20 synthetic examples.
- Evaluation metrics from this small dataset do not establish real-world model accuracy.
- The model is a proof of concept for MLflow-based model training and API integration.
- Predictions must not be interpreted as actual hiring recommendations or validated career-readiness assessments.
