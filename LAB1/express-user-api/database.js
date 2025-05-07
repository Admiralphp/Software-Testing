const Database = require("better-sqlite3");

const db = new Database(":memory:"); // In-memory database for testing

db.exec(`
    CREATE TABLE users (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL,
        email TEXT NOT NULL
    )
`);

// Insert initial test data
db.prepare(`
    INSERT INTO users (name, email) VALUES
    ('Alice', 'alice@example.com'),
    ('Bob', 'bob@example.com')
`).run();

module.exports = db;