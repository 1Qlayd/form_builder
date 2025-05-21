CREATE TABLE Role (
    id_role INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);
CREATE TABLE Users (
    id INT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE,
    idRole INT NOT NULL,
    FOREIGN KEY (idRole) REFERENCES Role(id_role)
);
CREATE TABLE Form (
    id INT NOT NULL,
    name VARCHAR(250) NOT NULL,
    userID INT NOT NULL,
    description VARCHAR(250) NOT NULL,
    content VARCHAR(250) NOT NULL,
    date TIMESTAMP NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (userID) REFERENCES Users(id)
);