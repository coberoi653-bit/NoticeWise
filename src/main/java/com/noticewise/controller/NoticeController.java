package com.noticewise.controller;
import com.noticewise.model.Notice;
import com.noticewise.service.NoticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
@RequestMapping("/api/notices")
public class NoticeController {
    private final NoticeService service;
    public NoticeController(NoticeService service){ this.service = service; }
    public record AnalyzeRequest(String noticeText) {}

    @PostMapping("/analyze")
    public ResponseEntity<?> analyze(@RequestBody AnalyzeRequest request) {
        if (request.noticeText() == null || request.noticeText().isBlank())
            return ResponseEntity.badRequest().body(Map.of("message","Please paste a notice first."));
        if (request.noticeText().length() > 11000)
            return ResponseEntity.badRequest().body(Map.of("message","Please keep the notice under 11,000 characters."));
        return ResponseEntity.ok(service.analyzeAndSave(request.noticeText()));
    }
    @GetMapping public List<Notice> history(){ return service.history(); }
    @GetMapping("/{id}") public ResponseEntity<?> get(@PathVariable Long id) {
        return service.find(id).<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id); return ResponseEntity.noContent().build();
    }
}
