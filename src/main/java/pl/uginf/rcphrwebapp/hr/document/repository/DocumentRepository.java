package pl.uginf.rcphrwebapp.hr.document.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.uginf.rcphrwebapp.hr.document.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    Optional<Document> findDocumentByFilename(String name);

    List<Document> findAllByUser_Username(String username);
}
