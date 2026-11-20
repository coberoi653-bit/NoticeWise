package com.noticewise.service;
import com.noticewise.model.Notice;
import com.noticewise.repository.NoticeRepository;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class NoticeService {
    private final NoticeAnalysisService analysisService;
    private final NoticeRepository repository;
    public NoticeService(NoticeAnalysisService analysisService, NoticeRepository repository) {
        this.analysisService = analysisService; this.repository = repository;
    }
    public Notice analyzeAndSave(String text) {
        Map<String,String> a = analysisService.analyze(text);
        return repository.save(new Notice(text, a.get("title"), a.get("simpleSummary"),
            a.get("deadlines"), a.get("eligibility"), a.get("requiredDocuments"), a.get("actionChecklist")));
    }
    public List<Notice> history(){ return repository.findTop30ByOrderByCreatedAtDesc(); }
    public Optional<Notice> find(Long id){ return repository.findById(id); }
    public void delete(Long id){ repository.deleteById(id); }
}
