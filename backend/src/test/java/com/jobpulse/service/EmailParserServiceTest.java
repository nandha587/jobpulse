package com.jobpulse.service;

import com.jobpulse.dto.ParsedApplicationEmail;
import com.jobpulse.entity.enums.ApplicationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmailParserServiceTest {

    private EmailParserService parserService;

    @BeforeEach
    void setUp() {
        parserService = new EmailParserService();
    }

    @Test
    void testParseLinkedInApplied() {
        String sender = "jobs-listings@linkedin.com";
        String subject = "You applied to Software Engineer at Google";
        String body = "Thanks for applying to Google through LinkedIn.";

        ParsedApplicationEmail result = parserService.parse(sender, subject, body);

        assertTrue(result.isValid());
        assertEquals("Software Engineer", result.getJobTitle());
        assertEquals("Google", result.getCompanyName());
        assertEquals("LINKEDIN", result.getSource());
        assertEquals(ApplicationStatus.APPLIED, result.getStatus());
        assertFalse(result.isStatusUpdate());
    }

    @Test
    void testParseLinkedInInterview() {
        String sender = "messages-noreply@linkedin.com";
        String subject = "Invitation to interview from Microsoft for Cloud Architect";
        String body = "You have an interview invitation.";

        ParsedApplicationEmail result = parserService.parse(sender, subject, body);

        assertTrue(result.isValid());
        assertEquals("Cloud Architect", result.getJobTitle());
        assertEquals("Microsoft", result.getCompanyName());
        assertEquals("LINKEDIN", result.getSource());
        assertEquals(ApplicationStatus.INTERVIEW, result.getStatus());
        assertTrue(result.isStatusUpdate());
    }

    @Test
    void testParseNaukriApplied() {
        String sender = "jobsearch@naukri.com";
        String subject = "Application sent for Full Stack Java Developer at Infosys";
        String body = "Your application was forwarded to the employer.";

        ParsedApplicationEmail result = parserService.parse(sender, subject, body);

        assertTrue(result.isValid());
        assertEquals("Full Stack Java Developer", result.getJobTitle());
        assertEquals("Infosys", result.getCompanyName());
        assertEquals("NAUKRI", result.getSource());
        assertEquals(ApplicationStatus.APPLIED, result.getStatus());
    }

    @Test
    void testParseNaukriInterview() {
        String sender = "applications@naukri.com";
        String subject = "Interview invitation from TCS for Senior React Developer";
        String body = "Recruiter has invited you for an interview round.";

        ParsedApplicationEmail result = parserService.parse(sender, subject, body);

        assertTrue(result.isValid());
        assertEquals("Senior React Developer", result.getJobTitle());
        assertEquals("TCS", result.getCompanyName());
        assertEquals("NAUKRI", result.getSource());
        assertEquals(ApplicationStatus.INTERVIEW, result.getStatus());
        assertTrue(result.isStatusUpdate());
    }

    @Test
    void testParseIndeedApplied() {
        String sender = "alert@indeed.com";
        String subject = "Indeed Application: DevOps Engineer at Swiggy";
        String body = "Your application was sent.";

        ParsedApplicationEmail result = parserService.parse(sender, subject, body);

        assertTrue(result.isValid());
        assertEquals("DevOps Engineer", result.getJobTitle());
        assertEquals("Swiggy", result.getCompanyName());
        assertEquals("INDEED", result.getSource());
        assertEquals(ApplicationStatus.APPLIED, result.getStatus());
    }

    @Test
    void testUnrecognizedEmail() {
        String sender = "news@newsletter.com";
        String subject = "Weekly Tech Digest & Top 10 Stories";
        String body = "Check out our latest newsletter.";

        ParsedApplicationEmail result = parserService.parse(sender, subject, body);

        assertFalse(result.isValid());
    }
}
