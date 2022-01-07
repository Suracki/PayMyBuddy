package com.paymybuddy.integration;

import com.paymybuddy.banking.BankController;
import com.paymybuddy.banking.MockBank;
import com.paymybuddy.data.dao.RelationshipsDAO;
import com.paymybuddy.data.dao.TransactionDAO;
import com.paymybuddy.data.dao.UsersDAO;
import com.paymybuddy.data.dao.dbConfig.DatabaseConnection;
import com.paymybuddy.data.dao.dbConfig.DatabaseTestConnection;
import com.paymybuddy.logic.RelationshipsService;
import com.paymybuddy.logic.TransactionService;
import com.paymybuddy.logic.UsersService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Bean;

public class IntegrationTestBeans {

    @Bean
    public DatabaseConnection databaseConnection() {
        return new DatabaseTestConnection();
    }

    @Bean
    public UsersService usersService() {
        UsersDAO usersDAO = new UsersDAO();
        usersDAO.databaseConnection = new DatabaseTestConnection();
        return new UsersService(usersDAO, passwordEncoder());
    }

    @Bean
    public RelationshipsService relationshipsService() {
        RelationshipsDAO relationshipsDAO = new RelationshipsDAO();
        relationshipsDAO.databaseConnection = new DatabaseTestConnection();
        return new RelationshipsService(relationshipsDAO);
    }

    @Bean
    TransactionService transactionService() {
        TransactionDAO transactionDAO = new TransactionDAO();
        transactionDAO.databaseConnection = new DatabaseTestConnection();
        UsersDAO usersDAO = new UsersDAO();
        usersDAO.databaseConnection = new DatabaseTestConnection();
        BankController bank = new MockBank();
        return new TransactionService(transactionDAO, usersDAO, bank);
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }




}
