package pl.uginf.rcphrwebapp.hr.document.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.uginf.rcphrwebapp.hr.document.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    Optional<Document> findDocumentByFileName(String name);

    Optional<Document> findAllByUser_Username(String username);

    void deleteByFileName(String filename);
}
