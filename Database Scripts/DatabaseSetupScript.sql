create database prod;
use prod;
create table Users(
	AcctID int PRIMARY KEY AUTO_INCREMENT,
	FirstName TEXT NOT NULL,
	LastName TEXT NOT NULL,
	Address TEXT NOT NULL,
	City TEXT NOT NULL,
	Zip TEXT NOT NULL,
	Phone TEXT NOT NULL,
	Email TEXT NOT NULL,
	Password TEXT NOT NULL,
	Balance DECIMAL(13,2) DEFAULT 0,
    BankAcctIBAN TEXT NOT NULL,
    BankAcctBIC TEXT NOT NULL,
	Active bool DEFAULT true);

create table UserRelationships(
	ListID int PRIMARY KEY AUTO_INCREMENT,
	ListOwnerID int NOT NULL,
	ListFriendID int NOT NULL,
	FOREIGN KEY (ListOwnerID)REFERENCES Users(AcctID),
	FOREIGN KEY (ListFriendID)REFERENCES Users(AcctID));

create table Transactions(
	TransactionID int PRIMARY KEY AUTO_INCREMENT,
	FromAcctID int NOT NULL,
    ToAcctID int NOT NULL,
    TransactionDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    Description TEXT,
    Amount DECIMAL(13,2) NOT NULL,
    Processed bool DEFAULT false,
    FOREIGN KEY (FromAcctID) REFERENCES Users(AcctID),
    FOREIGN KEY (ToAcctID) REFERENCES Users(AcctID));

create table BankTransactions(
	TransactionID int PRIMARY KEY AUTO_INCREMENT,
    AcctID int NOT NULL,
    Amount DECIMAL(13,2) NOT NULL,
    BankAcctIBAN TEXT NOT NULL,
    BankAcctBIC TEXT NOT NULL,
    Processed bool DEFAULT false,
    Cancelled bool DEFAULT false,
    TransactionDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (AcctID) REFERENCES Users(AcctID));
commit;

create database test;
use test;
create table Users(
	AcctID int PRIMARY KEY AUTO_INCREMENT,
	FirstName TEXT NOT NULL,
	LastName TEXT NOT NULL,
	Address TEXT NOT NULL,
	City TEXT NOT NULL,
	Zip TEXT NOT NULL,
	Phone TEXT NOT NULL,
	Email TEXT NOT NULL,
	Password TEXT NOT NULL,
	Balance DECIMAL(13,2) DEFAULT 0,
    BankAcctIBAN TEXT NOT NULL,
    BankAcctBIC TEXT NOT NULL,
	Active bool DEFAULT true);

create table UserRelationships(
	ListID int PRIMARY KEY AUTO_INCREMENT,
	ListOwnerID int NOT NULL,
	ListFriendID int NOT NULL,
	FOREIGN KEY (ListOwnerID)REFERENCES Users(AcctID),
	FOREIGN KEY (ListFriendID)REFERENCES Users(AcctID));

create table Transactions(
	TransactionID int PRIMARY KEY AUTO_INCREMENT,
	FromAcctID int NOT NULL,
    ToAcctID int NOT NULL,
    TransactionDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    Description TEXT,
    Amount DECIMAL(13,2) NOT NULL,
    Processed bool DEFAULT false,
    FOREIGN KEY (FromAcctID) REFERENCES Users(AcctID),
    FOREIGN KEY (ToAcctID) REFERENCES Users(AcctID));

create table BankTransactions(
	TransactionID int PRIMARY KEY AUTO_INCREMENT,
    AcctID int NOT NULL,
    Amount DECIMAL(13,2) NOT NULL,
    BankAcctIBAN TEXT NOT NULL,
    BankAcctBIC TEXT NOT NULL,
    Processed bool DEFAULT false,
    Cancelled bool DEFAULT false,
    TransactionDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (AcctID) REFERENCES Users(AcctID));
commit;