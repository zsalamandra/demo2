package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.roni.roniresourcelockstarter.annotation.LockResource;


@RestController
@RequiredArgsConstructor
public class DocumentEditController {

    private final DocumentService documentService;
    public final DocumentLockingService documentLockingService;

    @LockResource(table = "documents", idParam = "docId")
    @PostMapping("/edit")
    public ResponseEntity<?> startEditing(@RequestParam String docId) {

        // просто подтверждаем, что можем редактировать
        documentLockingService.startEditing(docId);
        return ResponseEntity.ok("Editing locked");
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestParam String docId, @RequestBody String content) {
        documentService.save(docId, content);
        return ResponseEntity.ok("Saved");
    }
}
