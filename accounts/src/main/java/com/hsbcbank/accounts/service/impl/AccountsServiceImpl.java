package com.hsbcbank.accounts.service.impl;

import com.hsbcbank.accounts.constants.AccountsConstants;
import com.hsbcbank.accounts.dto.AccountsDto;
import com.hsbcbank.accounts.dto.CustomerDto;
import com.hsbcbank.accounts.entity.Accounts;
import com.hsbcbank.accounts.entity.Customer;
import com.hsbcbank.accounts.exception.CustomerAlreadyExistsException;
import com.hsbcbank.accounts.exception.ResourceNotFoundException;
import com.hsbcbank.accounts.mapper.AccountsMapper;
import com.hsbcbank.accounts.mapper.CustomerMapper;
import com.hsbcbank.accounts.repository.AccountsRepository;
import com.hsbcbank.accounts.repository.CustomerRepository;
import com.hsbcbank.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

/**
 * Implementation of {@link IAccountsService} responsible for handling customer account-related operations.
 * This class provides the business logic for creating accounts and fetching account details
 * utilizing the underlying repository layers and associated entities.
 * <p>
 * The main responsibilities include:
 * - Validating and processing customer information for account creation
 * - Mapping customer data between various layers
 * - Creating new account entries with initial configurations
 */
@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;


    /**
     * Creates a new account for a customer based on the provided customer details.
     * This method handles the business logic for account creation including:
     * - Validating customer information
     * - Creating necessary database entries
     * - Setting up initial account configurations
     *
     * @param customerDto Data transfer object containing customer information needed for account creation
     * @throws IllegalArgumentException if the customerDto contains invalid or incomplete data
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> otpCustomer = customerRepository.findCustomerByMobileNumber(customer.getMobileNumber());
        if (otpCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer with mobile number " + customer.getMobileNumber() + " already exists");
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Anonymous");
        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));
    }

    private Accounts createNewAccount(Customer customer) {
        Accounts accounts = new Accounts();
        accounts.setCustomerId(customer.getCustomerId());
        long randomAccountNumber = 1000000000L + new Random().nextInt(900000000);
        accounts.setAccountNumber(randomAccountNumber);
        accounts.setAccountType(AccountsConstants.SAVINGS);
        accounts.setBranchAddress(AccountsConstants.ADDRESS);
        accounts.setCreatedAt(LocalDateTime.now());
        accounts.setCreatedBy("Anonymous");
        return accounts;
    }


    @Override
    public CustomerDto fetchAccountDetails(String mobileNumber) {
        Customer customer = customerRepository.findCustomerByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        Accounts accounts = accountsRepository.findAccountsByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("account", "customerId", customer.getCustomerId().toString())
        );
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccounts(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        return customerDto;
    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {

        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccounts();
        if (accountsDto != null) {
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "accountNumber", accountsDto.getAccountNumber().toString())
            );
            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "customerId", customerId.toString())
            );

            CustomerMapper.mapToCustomer(customerDto, customer);
            customerRepository.save(customer);

            isUpdated = true;
        }

        return isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findCustomerByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }

}
