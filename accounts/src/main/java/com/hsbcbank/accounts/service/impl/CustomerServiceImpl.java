package com.hsbcbank.accounts.service.impl;

import com.hsbcbank.accounts.dto.AccountsDto;
import com.hsbcbank.accounts.dto.CardsDto;
import com.hsbcbank.accounts.dto.CustomerDetailsDto;
import com.hsbcbank.accounts.dto.LoansDto;
import com.hsbcbank.accounts.entity.Accounts;
import com.hsbcbank.accounts.entity.Customer;
import com.hsbcbank.accounts.exception.ResourceNotFoundException;
import com.hsbcbank.accounts.mapper.AccountsMapper;
import com.hsbcbank.accounts.mapper.CustomerMapper;
import com.hsbcbank.accounts.repository.AccountsRepository;
import com.hsbcbank.accounts.repository.CustomerRepository;
import com.hsbcbank.accounts.service.ICustomerService;
import com.hsbcbank.accounts.service.client.CardsFeignClient;
import com.hsbcbank.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;


    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber,String correlationId) {
        Customer customer = customerRepository.findCustomerByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        Accounts accounts = accountsRepository.findAccountsByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccounts(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(correlationId,mobileNumber);
        customerDetailsDto.setLoans(loansDtoResponseEntity.getBody());

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(correlationId,mobileNumber);
        customerDetailsDto.setCards(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;
    }
}
