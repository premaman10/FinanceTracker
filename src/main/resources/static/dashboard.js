// ================= AUTH =================
let token = localStorage.getItem("token");

// Google OAuth redirect token
const params = new URLSearchParams(window.location.search);
const googleToken = params.get("token");
if (googleToken) {
    localStorage.setItem("token", googleToken);
    token = googleToken;
    window.history.replaceState({}, document.title, "/dashboard.html");
}

if (!token) window.location.href = "index.html";

// ================= API =================
const API = window.location.origin;

// ================= ELEMENTS =================
const incomeList = document.getElementById("incomeList");
const expenseList = document.getElementById("expenseList");
const totalIncomeEl = document.getElementById("totalIncome");
const totalExpensesEl = document.getElementById("totalExpenses");
const balanceEl = document.getElementById("balance");

// Filter controls
const typeFilterEl = document.getElementById("typeFilter");
const categorySourceFilterEl = document.getElementById("categorySourceFilter");
const sortByEl = document.getElementById("sortBy");
const fromDateEl = document.getElementById("fromDate");
const toDateEl = document.getElementById("toDate");

// Form elements
const incomeModal = document.getElementById("incomeModal");
const expenseModal = document.getElementById("expenseModal");
const incomeDesc = document.getElementById("incomeDesc");
const incomeSource = document.getElementById("incomeSource");
const incomeAmount = document.getElementById("incomeAmount");
const incomeDate = document.getElementById("incomeDate");
const expenseDesc = document.getElementById("expenseDesc");
const expenseCategory = document.getElementById("expenseCategory");
const expenseAmount = document.getElementById("expenseAmount");
const expenseDate = document.getElementById("expenseDate");

// In-memory store for current user data
let allIncomes = [];
let allExpenses = [];

// ================= LOGOUT =================
document.addEventListener("DOMContentLoaded", () => {

    const logoutBtn = document.getElementById("logoutBtn");

    if (!logoutBtn) {
        console.error("Logout button not found");
        return;
    }

    logoutBtn.addEventListener("click", () => {
        console.log("Logout clicked");
        localStorage.removeItem("token");
        window.location.href = "index.html";
    });

    // Limit all date pickers so user cannot select future dates
    const today = new Date();
    const todayStr = today.toISOString().split("T")[0]; // yyyy-mm-dd

    [incomeDate, expenseDate, fromDateEl, toDateEl].forEach(input => {
        if (!input) return;
        input.setAttribute("max", todayStr);

        // Pre-fill add-income / add-expense with today's date if empty
        if (!input.value && (input === incomeDate || input === expenseDate)) {
            input.value = todayStr;
        }
    });

});


// ================= HEADERS =================
function headers() {
    return {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
    };
}

// ================= TABS =================
document.querySelectorAll(".tab").forEach(tab => {
    tab.onclick = () => {
        document.querySelectorAll(".tab").forEach(t => t.classList.remove("active"));
        document.querySelectorAll(".section").forEach(s => s.classList.remove("active"));
        tab.classList.add("active");
        document.getElementById(tab.dataset.tab).classList.add("active");

        // Keep filters in sync with selected tab
        if (typeFilterEl) {
            if (tab.dataset.tab === "incomeSection") {
                typeFilterEl.value = "income";
            } else if (tab.dataset.tab === "expenseSection") {
                typeFilterEl.value = "expense";
            }
            populateFilterOptions();
            applyFilters();
        }
    };
});

// ================= MODALS =================
function openIncomeModal() { incomeModal.classList.remove("hidden"); }
function closeIncomeModal() { incomeModal.classList.add("hidden"); }
function openExpenseModal() { expenseModal.classList.remove("hidden"); }
function closeExpenseModal() { expenseModal.classList.add("hidden"); }

document.querySelector("#incomeSection .primary-btn").onclick = openIncomeModal;
document.querySelector("#expenseSection .primary-btn").onclick = openExpenseModal;

// ================= FETCH DATA =================
async function loadDashboard() {
    const [incomes, expenses] = await Promise.all([
        fetch(`${API}/income`, { headers: headers() }).then(r => r.json()),
        fetch(`${API}/expense`, { headers: headers() }).then(r => r.json())
    ]);

    allIncomes = incomes || [];
    allExpenses = expenses || [];

    renderSummary(allIncomes, allExpenses);
    populateFilterOptions();
    applyFilters(); // render initial lists with default filters
}

// ================= SUMMARY / P&L =================
function renderSummary(incomes, expenses) {
    const totalIncome = incomes.reduce((s, i) => s + (i.amount || 0), 0);
    const totalExpense = expenses.reduce((s, e) => s + (e.amount || 0), 0);
    const net = totalIncome - totalExpense;

    totalIncomeEl.textContent = `₹ ${totalIncome.toFixed(2)}`;
    totalExpensesEl.textContent = `₹ ${totalExpense.toFixed(2)}`;
    balanceEl.textContent = `${net >= 0 ? "+" : "-"}₹ ${Math.abs(net).toFixed(2)}`;

    // Toggle profit / loss styling on the Net P&L card
    const balanceCard = balanceEl.closest(".card");
    if (balanceCard) {
        balanceCard.classList.remove("profit", "loss");
        balanceCard.classList.add(net >= 0 ? "profit" : "loss");
    }
}

// ================= RENDER LISTS =================
function renderIncomeList(items) {
    incomeList.innerHTML = items.length
        ? items.map(i => `
            <div class="list-row">
                <span class="cell date">${i.date || ""}</span>
                <span class="cell desc">${i.description}</span>
                <span class="cell tag income-tag">${i.source}</span>
                <span class="cell amount income-amount">+₹ ${Number(i.amount || 0).toFixed(2)}</span>
                <button class="delete-btn" onclick="deleteIncome(${i.id})">✕</button>
            </div>
        `).join("")
        : `<p class="empty">No income added</p>`;
}

function renderExpenseList(items) {
    expenseList.innerHTML = items.length
        ? items.map(e => `
            <div class="list-row">
                <span class="cell date">${e.date || ""}</span>
                <span class="cell desc">${e.description}</span>
                <span class="cell tag expense-tag">${e.category}</span>
                <span class="cell amount expense-amount">-₹ ${Number(e.amount || 0).toFixed(2)}</span>
                <button class="delete-btn" onclick="deleteExpense(${e.id})">✕</button>
            </div>
        `).join("")
        : `<p class="empty">No expense added</p>`;
}

// ================= FILTERS =================
function populateFilterOptions() {
    if (!categorySourceFilterEl) return;

    const type = typeFilterEl?.value || "income";
    const items = type === "income" ? allIncomes : allExpenses;
    const key = type === "income" ? "source" : "category";

    const uniqueValues = Array.from(new Set(items.map(i => i[key]).filter(Boolean)));

    categorySourceFilterEl.innerHTML = `
        <option value="">All</option>
        ${uniqueValues.map(v => `<option value="${v}">${v}</option>`).join("")}
    `;
}

function applyFilters() {
    const type = typeFilterEl?.value || "income";
    const selectedCategory = categorySourceFilterEl?.value || "";
    const sortBy = sortByEl?.value || "";
    const from = fromDateEl?.value ? new Date(fromDateEl.value) : null;
    const to = toDateEl?.value ? new Date(toDateEl.value) : null;

    if (type === "income") {
        let list = [...allIncomes];

        if (selectedCategory) {
            list = list.filter(i => i.source === selectedCategory);
        }

        if (from) {
            list = list.filter(i => i.date && new Date(i.date) >= from);
        }
        if (to) {
            list = list.filter(i => i.date && new Date(i.date) <= to);
        }

        if (sortBy === "amountAsc") {
            list.sort((a, b) => (a.amount || 0) - (b.amount || 0));
        } else if (sortBy === "amountDesc") {
            list.sort((a, b) => (b.amount || 0) - (a.amount || 0));
        }

        renderIncomeList(list);
    } else {
        let list = [...allExpenses];

        if (selectedCategory) {
            list = list.filter(e => e.category === selectedCategory);
        }

        if (from) {
            list = list.filter(e => e.date && new Date(e.date) >= from);
        }
        if (to) {
            list = list.filter(e => e.date && new Date(e.date) <= to);
        }

        if (sortBy === "amountAsc") {
            list.sort((a, b) => (a.amount || 0) - (b.amount || 0));
        } else if (sortBy === "amountDesc") {
            list.sort((a, b) => (b.amount || 0) - (a.amount || 0));
        }

        renderExpenseList(list);
    }
}

// Keep category/source options in sync with type selection
if (typeFilterEl) {
    typeFilterEl.addEventListener("change", () => {
        populateFilterOptions();
        applyFilters();
    });
}

// ================= SAVE =================
async function saveIncome() {
    const amount = Number(incomeAmount.value);

    if (Number.isNaN(amount) || amount <= 0) {
        alert("Please enter a valid numeric income amount greater than 0.");
        return;
    }
    if (!incomeDate.value) {
        alert("Please choose a date for this income.");
        return;
    }

    const selected = new Date(incomeDate.value);
    const today = new Date();
    if (selected > today) {
        alert("Income date cannot be in the future.");
        return;
    }

    await fetch(`${API}/income`, {
        method: "POST",
        headers: headers(),
        body: JSON.stringify({
            description: incomeDesc.value,
            source: incomeSource.value,
            amount,
            date: incomeDate.value
        })
    });
    closeIncomeModal();
    await loadDashboard();

    // After adding income, make sure filters are set to income and list is refreshed
    if (typeFilterEl) {
        typeFilterEl.value = "income";
        populateFilterOptions();
        applyFilters();
    }
}

async function saveExpense() {
    const amount = Number(expenseAmount.value);

    if (Number.isNaN(amount) || amount <= 0) {
        alert("Please enter a valid numeric expense amount greater than 0.");
        return;
    }
    if (!expenseDate.value) {
        alert("Please choose a date for this expense.");
        return;
    }

    const selected = new Date(expenseDate.value);
    const today = new Date();
    if (selected > today) {
        alert("Expense date cannot be in the future.");
        return;
    }

    await fetch(`${API}/expense`, {
        method: "POST",
        headers: headers(),
        body: JSON.stringify({
            description: expenseDesc.value,
            category: expenseCategory.value,
            amount,
            date: expenseDate.value
        })
    });
    closeExpenseModal();
    await loadDashboard();

    // After adding expense, make sure filters are set to expense and list is refreshed
    if (typeFilterEl) {
        typeFilterEl.value = "expense";
        populateFilterOptions();
        applyFilters();
    }
}

// ================= DELETE =================
async function deleteIncome(id) {
    if (!confirm("Are you sure you want to delete this income entry?")) {
        return;
    }

    await fetch(`${API}/income/${id}`, {
        method: "DELETE",
        headers: headers()
    });
    loadDashboard();
}

async function deleteExpense(id) {
    if (!confirm("Are you sure you want to delete this expense entry?")) {
        return;
    }

    await fetch(`${API}/expense/${id}`, {
        method: "DELETE",
        headers: headers()
    });
    loadDashboard();
}

// ================= INIT =================
loadDashboard();
