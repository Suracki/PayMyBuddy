create database prod;
use prod;

create table Users(
 UserID int PRIMARY KEY AUTO_INCREMENT,
 FirstName TEXT NOT NULL,
 LastName TEXT NOT NULL,
 Address TEXT NOT NULL,
 City TEXT NOT NULL,
 Zip TEXT NOT NULL,
 Phone TEXT NOT NULL,
 Email TEXT NOT NULL,
 Password TEXT NOT NULL,
 Balance DECIMAL(13,4) DEFAULT 0);
 
create table UserRelationships(
 ListID int PRIMARY KEY AUTO_INCREMENT,
 ListOwnerID int NOT NULL,
 ListFriendID int NOT NULL);

create table Transactions(
 TransactionID int PRIMARY KEY AUTO_INCREMENT,
 FromUserID int NOT NULL,
 ToUserID int NOT NULL,
 TransactionDate DATETIME NOT NULL,
 Description TEXT,
 Amount DECIMAL(13,4) NOT NULL,
 Processed bool NOT NULL,
 FOREIGN KEY (FromUserID)
 REFERENCES Users(UserID),
 FOREIGN KEY (ToUserID)
 REFERENCES Users(UserID));
 
commit;
 
create database test;
use test;

create table Users(
 UserID int PRIMARY KEY AUTO_INCREMENT,
 FirstName TEXT NOT NULL,
 LastName TEXT NOT NULL,
 Address TEXT NOT NULL,
 City TEXT NOT NULL,
 Zip TEXT NOT NULL,
 Phone TEXT NOT NULL,
 Email TEXT NOT NULL,
 Password TEXT NOT NULL,
 Balance DECIMAL(13,4) DEFAULT 0);
 
create table UserRelationships(
 ListID int PRIMARY KEY AUTO_INCREMENT,
 ListOwnerID int NOT NULL,
 ListFriendID int NOT NULL);

create table Transactions(
 TransactionID int PRIMARY KEY AUTO_INCREMENT,
 FromUserID int NOT NULL,
 ToUserID int NOT NULL,
 TransactionDate DATETIME NOT NULL,
 Description TEXT,
 Amount DECIMAL(13,4) NOT NULL,
 Processed bool NOT NULL,
 FOREIGN KEY (FromUserID)
 REFERENCES Users(UserID),
 FOREIGN KEY (ToUserID)
 REFERENCES Users(UserID));
