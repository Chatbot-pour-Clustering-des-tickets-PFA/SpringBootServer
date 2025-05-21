package net.bouraoui.fetchingtickets.Entities;

public enum Category {
    MEDICAL_DATA_SECURITY("Medical Data Security"),
    SECURING_HOSPITAL_SYSTEMS("Securing Hospital Systems"),
    DIGITAL_STRATEGY_AND_BRAND_GROWTH("Digital Strategy & Brand Growth"),
    TECHNICAL_ERRORS_AND_TROUBLESHOOTING("Technical Errors & Troubleshooting"),
    PROJECT_AND_SAAS_INTEGRATION("Project & SaaS Integration"),
    GENERAL_SUPPORT_ISSUES("General Support Issues"),
    INVESTMENT_ANALYTICS_AND_TOOLS("Investment Analytics & Tools"),
    SOFTWARE_ISSUES_AND_BUG_REPORTS("Software Issues & Bug Reports"),
    MARKETING_CAMPAIGN_MANAGEMENT("Marketing Campaign Management"),
    DATA_BREACH_AND_SECURITY_INCIDENTS("Data Breach & Security Incidents");

    private final String label;

    Category(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
