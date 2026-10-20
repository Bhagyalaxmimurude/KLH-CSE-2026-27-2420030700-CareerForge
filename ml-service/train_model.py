
import mlflow
import mlflow.sklearn
import pandas as pd

from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score, precision_score, recall_score, f1_score

# Reproducible sample dataset for CareerForge.
# Features: coding, DSA, SQL, projects, communication (scores 0-10).
data = pd.DataFrame({
    "coding":        [8, 3, 7, 4, 9, 5, 6, 2, 8, 4, 7, 3, 9, 6, 5, 2, 8, 4, 7, 5],
    "dsa":           [7, 2, 8, 3, 9, 4, 5, 2, 8, 3, 7, 4, 9, 6, 4, 1, 7, 3, 6, 5],
    "sql":           [8, 4, 6, 3, 8, 5, 7, 2, 9, 4, 6, 3, 9, 5, 6, 2, 8, 4, 7, 5],
    "projects":      [8, 3, 7, 4, 9, 5, 6, 2, 8, 4, 8, 3, 9, 6, 5, 1, 9, 4, 7, 5],
    "communication": [7, 5, 8, 4, 8, 6, 7, 3, 9, 5, 7, 4, 9, 6, 5, 3, 8, 4, 7, 6],
    # Illustrative labels only—not validated real-world hiring outcomes.
    "ready":         [1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0]
})

X = data.drop(columns=["ready"])
y = data["ready"]

X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.30, random_state=42, stratify=y
)

n_estimators = 100
max_depth = 4

mlflow.set_experiment("CareerForge-Career-Readiness")

with mlflow.start_run(run_name="random-forest-baseline"):
    model = RandomForestClassifier(
        n_estimators=n_estimators,
        max_depth=max_depth,
        random_state=42
    )
    model.fit(X_train, y_train)

    predictions = model.predict(X_test)

    accuracy = accuracy_score(y_test, predictions)
    precision = precision_score(y_test, predictions, zero_division=0)
    recall = recall_score(y_test, predictions, zero_division=0)
    f1 = f1_score(y_test, predictions, zero_division=0)

    mlflow.log_param("model_type", "RandomForestClassifier")
    mlflow.log_param("n_estimators", n_estimators)
    mlflow.log_param("max_depth", max_depth)
    mlflow.log_param("random_state", 42)
    mlflow.log_param("training_rows", len(X_train))
    mlflow.log_param("test_rows", len(X_test))

    mlflow.log_metrics({
        "accuracy": accuracy,
        "precision": precision,
        "recall": recall,
        "f1_score": f1
    })


    mlflow.sklearn.log_model(
        sk_model=model,
        name="career_readiness_model",
        input_example=X_test.head(2),
        skops_trusted_types=["sklearn.tree._tree.Tree"]
    )


    print("CareerForge MLflow experiment completed!")
    print(f"Accuracy:  {accuracy:.2f}")
    print(f"Precision: {precision:.2f}")
    print(f"Recall:    {recall:.2f}")
    print(f"F1 score:  {f1:.2f}")
    print("Experiment: CareerForge-Career-Readiness")
    print("Model and metrics logged to MLflow.")
