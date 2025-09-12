package com.hsbcbank.accounts.service;

import com.hsbcbank.accounts.dto.CustomerDto;


/**
 * Provides operations related to customer account management.
 * This interface defines the contract for creating customer accounts.
 */
public interface IAccountsService {

    void createAccount(CustomerDto customerDto);

    CustomerDto fetchAccountDetails(String mobileNumber);

    boolean updateAccount(CustomerDto customerDto);

    boolean deleteAccount(String mobileNumber);


}
