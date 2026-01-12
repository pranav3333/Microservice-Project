package com.hsbcbank.accounts.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;

@ConfigurationProperties(prefix = "accounts")
@Getter
@Setter
public class AccountsContactInfoDto{

    String message;
    HashMap<String, String> contactDetails;
    List<String> onCallSupport;

}


