package com.paymybuddy.integration;

import com.paymybuddy.data.dao.RelationshipsDAO;
import com.paymybuddy.data.dao.TransactionDAO;
import com.paymybuddy.data.dao.UsersDAO;
import com.paymybuddy.data.dao.dbConfig.DatabaseConnection;
import com.paymybuddy.data.dao.dbConfig.DatabaseTestConnection;
import com.paymybuddy.logic.RelationshipsService;
import com.paymybuddy.logic.TransactionService;
import com.paymybuddy.logic.UsersService;
import com.paymybuddy.presentation.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class IntegrationTestBeans {

    @Bean
    public DatabaseConnection databaseConnection() {
        return new DatabaseTestConnection();
    }

    @Bean
    public UsersService usersService() {
        return new UsersService(new UsersDAO());
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





}
