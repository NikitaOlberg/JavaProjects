class Report {
    private final String title;
    private final String content;
    private final String summary;

    private Report(ReportBuilder builder) {
        this.title = builder.title;
        this.content = builder.content;
        this.summary = builder.summary;
    }

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getSummary() { return summary; }

    @Override
    public String toString() {
        return String.format("Report: %s\nContent: %s\nSummary: %s", title, content, summary);
    }

    public static class ReportBuilder {
        private String title;
        private String content;
        private String summary;

        public ReportBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public ReportBuilder setContent(String content) {
            this.content = content;
            return this;
        }

        public ReportBuilder setSummary(String summary) {
            this.summary = summary;
            return this;
        }

        public Report build() {
            return new Report(this);
        }
    }
}

interface ReportFactory {
    Report createReport(Report.ReportBuilder builder);
}

class PDFReportFactory implements ReportFactory {
    @Override
    public Report createReport(Report.ReportBuilder builder) {
        System.out.println("Генерация PDF отчета...");
        return builder.build();
    }
}

class HTMLReportFactory implements ReportFactory {
    @Override
    public Report createReport(Report.ReportBuilder builder) {
        System.out.println("Генерация HTML отчета...");
        return builder.build();
    }
}

class DOCXReportFactory implements ReportFactory {
    @Override
    public Report createReport(Report.ReportBuilder builder) {
        System.out.println("Генерация DOCX отчета...");
        return builder.build();
    }
}

public class ReportGeneratorDemo {
    public static void main(String[] args) {
        Report.ReportBuilder builder = new Report.ReportBuilder()
                .setTitle("Ежеквартальный отчет")
                .setContent("Данные за 3 квартал 2024...")
                .setSummary("Выручка увеличилась на 15%");


        ReportFactory pdfFactory = new PDFReportFactory();
        Report pdfReport = pdfFactory.createReport(builder);
        System.out.println(pdfReport + "\n");

        ReportFactory htmlFactory = new HTMLReportFactory();
        Report htmlReport = htmlFactory.createReport(builder);
        System.out.println(htmlReport + "\n");

        ReportFactory docxFactory = new DOCXReportFactory();
        Report docxReport = docxFactory.createReport(builder);
        System.out.println(docxReport);
    }
}