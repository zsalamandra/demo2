package com.example.demo;

import org.springframework.stereotype.Service;
import ru.roni.roniresourcelockstarter.annotation.CheckLock;

@Service
public class DocumentService {

    @CheckLock(table = "documents", idParam = "docId")
    public void save(String docId, String content) {
        // здесь сохранение в базу данных или другое хранилище
        System.out.printf("Saving document %s with content: %s%n", docId, content);
    }
}
