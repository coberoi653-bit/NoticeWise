package com.noticewise.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "notices")
public class Notice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 12000)
    private String originalText;
    private String title;
    @Column(length = 5000) private String simpleSummary;
    @Column(length = 3000) private String deadlines;
    @Column(length = 3000) private String eligibility;
    @Column(length = 3000) private String requiredDocuments;
    @Column(length = 5000) private String actionChecklist;
    private LocalDateTime createdAt = LocalDateTime.now();

    protected Notice() {}
    public Notice(String originalText, String title, String simpleSummary, String deadlines,
                  String eligibility, String requiredDocuments, String actionChecklist) {
        this.originalText = originalText; this.title = title; this.simpleSummary = simpleSummary;
        this.deadlines = deadlines; this.eligibility = eligibility;
        this.requiredDocuments = requiredDocuments; this.actionChecklist = actionChecklist;
    }
    public Long getId(){return id;}
    public String getOriginalText(){return originalText;}
    public String getTitle(){return title;}
    public String getSimpleSummary(){return simpleSummary;}
    public String getDeadlines(){return deadlines;}
    public String getEligibility(){return eligibility;}
    public String getRequiredDocuments(){return requiredDocuments;}
    public String getActionChecklist(){return actionChecklist;}
    public LocalDateTime getCreatedAt(){return createdAt;}
}
