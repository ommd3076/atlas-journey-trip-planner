CREATE TABLE IF NOT EXISTS attractions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    rating INTEGER NOT NULL,
    value INTEGER NOT NULL,
    cost INTEGER NOT NULL,
    x INTEGER NOT NULL,
    y INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS roads (
    source TEXT NOT NULL,
    destination TEXT NOT NULL,
    distance INTEGER NOT NULL,
    PRIMARY KEY (source, destination)
);
