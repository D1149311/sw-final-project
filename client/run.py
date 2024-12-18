# server.py
from flask import Flask, render_template

app = Flask(__name__, template_folder="./dist/", static_folder="./dist/assets/")

@app.route("/")
def index():
    return render_template("index.html")

if __name__ == "__main__":
    app.run(port = 5000)
