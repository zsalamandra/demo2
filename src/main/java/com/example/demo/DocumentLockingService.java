package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.roni.roniresourcelockstarter.annotation.CheckLock;
import ru.roni.roniresourcelockstarter.service.LockService;

@Service
@RequiredArgsConstructor
public class DocumentLockingService {

    private final LockService lockService;


    public void startEditing(String docId) {

        // просто фиксация блокировки
        System.out.println("Starting editing document: " + docId);
    }
}
