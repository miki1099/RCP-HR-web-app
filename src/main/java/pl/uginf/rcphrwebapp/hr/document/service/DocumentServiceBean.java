package pl.uginf.rcphrwebapp.hr.document.service;

import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import pl.uginf.rcphrwebapp.exceptions.NotFoundException;
import pl.uginf.rcphrwebapp.exceptions.WrongFileExtensionException;
import pl.uginf.rcphrwebapp.hr.document.Document;
import pl.uginf.rcphrwebapp.hr.document.dto.DocumentDTO;
import pl.uginf.rcphrwebapp.hr.document.repository.DocumentRepository;
import pl.uginf.rcphrwebapp.hr.user.service.UserSLO;
import pl.uginf.rcphrwebapp.utils.MsgCodes;

@AllArgsConstructor(onConstructor_ = @Autowired)
@Service
public class DocumentServiceBean implements DocumentService {

    private final ModelMapper modelMapper;

    private final DocumentRepository documentRepository;

    private final UserSLO userSLO;

    public void storeFile(MultipartFile file, String username) throws IOException {
        Document document = createDocument(file, username);
        try {
            documentRepository.save(document);
        } catch (RuntimeException e) {
            Throwable rootCause = getRootCause(e);
            if ( rootCause instanceof SQLException ) {
                if ( "23505".equals(((SQLException) rootCause).getSQLState()) ) {
                    throw new DuplicateKeyException(MsgCodes.NOT_UNIQUE.getMsg(file.getOriginalFilename()));
                }
            }
        }
    }

    private Document createDocument(MultipartFile file, String username) throws IOException {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String contentType = file.getContentType();
        if ( !Objects.equals(contentType, MediaType.APPLICATION_PDF_VALUE) ) {
            throw new WrongFileExtensionException(filename);
        }
        DocumentDTO documentDTO = DocumentDTO.builder()
                .filename(file.getOriginalFilename())
                .data(file.getBytes())
                .user(userSLO.getUserByUsername(username))
                .type(file.getContentType())
                .build();
        return modelMapper.map(documentDTO, Document.class);
    }

    @Transactional
    public DocumentDTO getFile(String filename) {
        Optional<Document> optionalDocument = documentRepository.findDocumentByFileName(filename);
        if ( optionalDocument.isEmpty() ) {
            throw new NotFoundException(filename);
        }
        return modelMapper.map(optionalDocument.get(), DocumentDTO.class);
    }

    @Transactional
    public void deleteFile(String fileName) {
        documentRepository.deleteByFileName(fileName);
    }

    public List<String> getAllFileNameForUser(String username) {
        Optional<Document> optionalDocument = documentRepository.findAllByUser_Username(username);
        if ( optionalDocument.isEmpty() ) {
            return Collections.emptyList();
        }
        return optionalDocument.stream()
                .map(Document::getFileName)
                .collect(Collectors.toList());
    } //TODO use in controller

}
