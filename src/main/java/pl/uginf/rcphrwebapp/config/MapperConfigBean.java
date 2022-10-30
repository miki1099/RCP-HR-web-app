package pl.uginf.rcphrwebapp.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.uginf.rcphrwebapp.hr.workinfo.WorkInfo;
import pl.uginf.rcphrwebapp.hr.workinfo.WorkInfoDto;

@Configuration
public class MapperConfigBean {

    @Bean
    public ModelMapper modelMapper() {
        return configureMapper(new ModelMapper());
    }

    private ModelMapper configureMapper(ModelMapper modelMapper) {
        modelMapper.addMappings(new PropertyMap<WorkInfoDto, WorkInfo>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });
        return modelMapper;
    }
}