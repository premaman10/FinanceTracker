const API_BASE_URL = window.location.origin;

// Redirect if already logged in
if (localStorage.getItem("token")) {
    window.location.href = "dashboard.html";
}


const loginTab = document.getElementById("showLogin");
const signupTab = document.getElementById("showSignup");
const loginForm = document.getElementById("loginForm");
const signupForm = document.getElementById("signupForm");
const message = document.getElementById("message");

// 🔀 Tab switching
loginTab.onclick = () => {
    loginTab.classList.add("active");
    signupTab.classList.remove("active");
    loginForm.classList.remove("hidden");
    signupForm.classList.add("hidden");
};

signupTab.onclick = () => {
    signupTab.classList.add("active");
    loginTab.classList.remove("active");
    signupForm.classList.remove("hidden");
    loginForm.classList.add("hidden");
};

// 🔐 LOGIN
loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const email = document.getElementById("loginEmail").value;
    const password = document.getElementById("loginPassword").value;

    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        const data = await response.json();

        if (response.ok) {
            localStorage.setItem("token", data.token);
            window.location.href = "dashboard.html";
        } else {
            message.style.color = "red";
            message.textContent = data.message || "Login failed";
        }
    } catch (err) {
        message.textContent = "Server error";
    }
});

// 📝 SIGNUP
signupForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const name = document.getElementById("signupName").value;
    const email = document.getElementById("signupEmail").value;
    const password = document.getElementById("signupPassword").value;

    try {
        const response = await fetch(`${API_BASE_URL}/auth/signup`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ name, email, password })
        });

        const data = await response.json();

        if (response.ok) {
            message.style.color = "green";
            message.textContent = "Signup successful! Please login.";
            loginTab.click();
        } else {
            message.style.color = "red";
            message.textContent = data.message || "Signup failed";
        }
    } catch (err) {
        message.textContent = "Server error";
    }
});
