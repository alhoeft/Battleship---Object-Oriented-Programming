// Battleship front end.
// Every endpoint returns the full game state, so after each request we call
// render(state) and redraw everything from it. The page keeps no game logic.

const SIZE = 10;
const ROW_LABELS = "ABCDEFGHIJ";
const BOT_NAME = "Bot"; // the name PlayerFactory.createBot() gives the bot

let orientation = "HORIZONTAL";
let current = null;   // the last state the server sent
let busy = false;     // true while a request is in flight (stops double clicks)

const el = {
    status: document.getElementById("status"),
    error: document.getElementById("error"),
    orientation: document.getElementById("orientation"),
    playerBoard: document.getElementById("player-board"),
    enemyBoard: document.getElementById("enemy-board"),
    messages: document.getElementById("messages"),
    gameOver: document.getElementById("game-over"),
    gameOverText: document.getElementById("game-over-text"),
};

// ---------- talking to the server ----------

async function send(method, path, body) {
    if (busy) return;
    busy = true;
    try {
        const response = await fetch("/api/game/" + path, {
            method,
            headers: body ? { "Content-Type": "application/json" } : {},
            body: body ? JSON.stringify(body) : undefined,
        });
        if (!response.ok) {
            throw new Error("Server returned " + response.status);
        }
        render(await response.json());
    } catch (err) {
        showError("Can't reach the game server. Is it running?");
        console.error(err);
    } finally {
        busy = false;
    }
}

const newGame = () => send("POST", "new");
const loadState = () => send("GET", "state");

function place(row, col) {
    const ship = current.shipsToPlace[0]; // ships are placed in a fixed order
    send("POST", "place", { ship, row, col, orientation });
}

function fire(row, col) {
    send("POST", "fire", { row, col });
}

// ---------- drawing ----------

function render(state) {
    current = state;
    drawBoard(el.playerBoard, state.playerBoard, state.phase === "PLACING");
    drawBoard(el.enemyBoard, state.enemyBoard, state.phase === "IN_PROGRESS");
    drawStatus(state);
    drawMessages(state.messages);
    showError(state.error);
}

// Builds an 11x11 grid: a header row of 1-10, then rows A-J.
function drawBoard(container, cells, clickable) {
    container.replaceChildren();
    container.classList.toggle("clickable", clickable);

    container.append(labelCell(""));
    for (let col = 0; col < SIZE; col++) {
        container.append(labelCell(col + 1));
    }

    for (let row = 0; row < SIZE; row++) {
        container.append(labelCell(ROW_LABELS[row]));
        for (let col = 0; col < SIZE; col++) {
            const value = cells[row][col];
            const cell = document.createElement("button");
            cell.type = "button";
            cell.className = "cell " + value.toLowerCase();
            cell.dataset.row = row;
            cell.dataset.col = col;
            cell.title = ROW_LABELS[row] + (col + 1);
            cell.setAttribute("aria-label", cell.title + " " + value.toLowerCase());
            // Only empty water can take a ship; only unknown cells can be fired at.
            cell.disabled = !clickable || (value !== "EMPTY" && value !== "UNKNOWN");
            container.append(cell);
        }
    }
}

function labelCell(text) {
    const div = document.createElement("div");
    div.className = "label";
    div.textContent = text;
    return div;
}

function drawStatus(state) {
    const placing = state.phase === "PLACING";
    el.orientation.hidden = !placing;
    el.gameOver.hidden = state.phase !== "GAME_OVER";

    if (placing) {
        const ship = state.shipsToPlace[0];
        const left = state.shipsToPlace.length;
        el.status.textContent = `Place your ${prettyShip(ship)} (${SHIP_SIZES[ship]} cells). `
            + `${left} ship${left === 1 ? "" : "s"} left. Click a cell on your grid.`;
    } else if (state.phase === "IN_PROGRESS") {
        el.status.textContent = "Your turn. Click a cell in enemy waters to fire.";
    } else {
        const humanWon = state.winner !== BOT_NAME;
        el.status.textContent = "Game over.";
        el.gameOverText.textContent = humanWon
            ? "You win! The enemy fleet is sunk."
            : "You lose. The bot sank your fleet.";
        el.gameOver.classList.toggle("won", humanWon);
        el.gameOver.classList.toggle("lost", !humanWon);
    }
}

// Newest message first, so the latest shot is always visible.
function drawMessages(messages) {
    el.messages.replaceChildren(...[...messages].reverse().map(text => {
        const li = document.createElement("li");
        li.textContent = text;
        if (text.includes("sunk")) li.classList.add("sunk");
        if (text.startsWith("Game over")) li.classList.add("over");
        return li;
    }));
}

function showError(message) {
    el.error.hidden = !message;
    el.error.textContent = message || "";
}

const SHIP_SIZES = { CARRIER: 5, BATTLESHIP: 4, CRUISER: 3, SUBMARINE: 3, DESTROYER: 2 };

// "CARRIER" -> "Carrier", the same way MessageLog formats ship names.
function prettyShip(type) {
    return type.charAt(0) + type.slice(1).toLowerCase();
}

// ---------- input ----------

function cellFromEvent(event) {
    const cell = event.target.closest(".cell");
    if (!cell || cell.disabled) return null;
    return { row: Number(cell.dataset.row), col: Number(cell.dataset.col) };
}

el.playerBoard.addEventListener("click", event => {
    const target = cellFromEvent(event);
    if (target && current.phase === "PLACING") place(target.row, target.col);
});

el.enemyBoard.addEventListener("click", event => {
    const target = cellFromEvent(event);
    if (target && current.phase === "IN_PROGRESS") fire(target.row, target.col);
});

function toggleOrientation() {
    orientation = orientation === "HORIZONTAL" ? "VERTICAL" : "HORIZONTAL";
    el.orientation.textContent = orientation === "HORIZONTAL" ? "Horizontal" : "Vertical";
}

el.orientation.addEventListener("click", toggleOrientation);
document.addEventListener("keydown", event => {
    if (event.key.toLowerCase() === "r" && current && current.phase === "PLACING") {
        toggleOrientation();
    }
});

document.getElementById("new-game").addEventListener("click", newGame);
document.getElementById("play-again").addEventListener("click", newGame);

// Pick up the current game on page load (or refresh).
loadState();
