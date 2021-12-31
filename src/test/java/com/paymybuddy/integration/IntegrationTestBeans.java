package com.paymybuddy.integration;

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
        return new UsersService(new UsersDAO(), passwordEncoder());
    }

    @Bean
    public RelationshipsService relationshipsService() {
        return new RelationshipsService(new RelationshipsDAO());
    }

    @Bean
    TransactionService transactionService() {
        TransactionDAO transactionDAO = new TransactionDAO();
        transactionDAO.databaseConnection = new DatabaseTestConnection();
        return new TransactionService(transactionDAO);
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }




}
