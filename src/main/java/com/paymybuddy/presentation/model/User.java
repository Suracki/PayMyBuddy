package com.paymybuddy.presentation.model;

import com.paymybuddy.presentation.apimodels.UserDTO;
import com.paymybuddy.presentation.apimodels.UserPassDTO;

import java.math.BigDecimal;

public class User {

    private int acctID;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;
    private String password;
    private BigDecimal balance;

    public User() {
    }

    public User(UserDTO userDTO) {
        this.acctID = userDTO.acctID;
        this.firstName = userDTO.firstName;
        this.lastName = userDTO.lastName;
        this.address = userDTO.address;
        this.city = userDTO.city;
        this.zip = userDTO.zip;
        this.phone = userDTO.phone;
        this.email = userDTO.email;
        this.password = userDTO.password;
        this.balance = userDTO.balance;
    }

    public User(UserPassDTO userPassDTO) {
        this.acctID = userPassDTO.acctID;
        this.password = userPassDTO.password;
    }

    public User(String firstName, String lastName, String address, String city, String zip, String phone, String email, String password, BigDecimal balance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }

    public User(int acctID, String firstName, String lastName, String address, String city, String zip, String phone, String email, String password, BigDecimal balance) {
        this.acctID = acctID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }

    public User(int acctID, String password) {
        this.acctID = acctID;
        this.password = password;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String firstName, String lastName, String address, String city, String zip, String phone, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
        this.password = password;
        balance = new BigDecimal(0);
    }

    public int getAcctID() {
        return acctID;
    }

    public void setAcctID(int acctID) {
        this.acctID = acctID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
