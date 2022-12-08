package pl.uginf.rcphrwebapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:customerValues.properties")
public class CompanyInfoConfig {

    @Value("${company.name}")
    private String name;

    @Value("${company.street}")
    private String street;

    @Value("${company.address}")
    private String address;

    @Value("${company.nip}")
    private String nip;

    @Value("${company.currency}")
    private String currency;

    @Bean
    public CompanyInfoRecord companyInfo() {
        return new CompanyInfoRecord(name, street, address, nip, currency);
    }
}
