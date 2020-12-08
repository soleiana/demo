package ru.sberbank.cseodo.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sberbank.cseodo.demo.entities.Document;

import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {
}
