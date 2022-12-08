package pl.uginf.rcphrwebapp.hr.document.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import pl.uginf.rcphrwebapp.hr.document.dto.DocumentDTO;

public interface DocumentService {

    void storeFile(MultipartFile file, String username) throws IOException;

    DocumentDTO getFile(String filename);

    void deleteFile(String fileName);
}
