package com.hsbc.cards.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;

@ConfigurationProperties(prefix = "cards")
@Getter
@Setter
public class CardsContactInfoDto {
    String message;
    HashMap<String, String> contactDetails;
    List<String> onCallSupport;
}
