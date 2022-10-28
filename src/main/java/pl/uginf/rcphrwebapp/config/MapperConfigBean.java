package pl.uginf.rcphrwebapp.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfigBean {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}