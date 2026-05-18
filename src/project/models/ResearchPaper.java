package project.models;

import project.enums.Format;
import project.interfaces.Researcher;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResearchPaper implements Serializable, Comparable<ResearchPaper> {
    private static final long serialVersionUID = 7L;

    private String title;
    private List<Researcher> authors;
    private String journal;
    private int pages;
    private LocalDate datePublished;
    private int citations;
    private String doi;
    private String paperAbstract;
    private List<String> keywords;

    public ResearchPaper() {
        this.authors = new ArrayList<>();
        this.keywords = new ArrayList<>();
    }

    public ResearchPaper(String title, String journal,
                         LocalDate datePublished, int pages) {
        this.title = title;
        this.journal = journal;
        this.datePublished = datePublished;
        this.pages = pages;
        this.authors = new ArrayList<>();
        this.keywords = new ArrayList<>();
        this.citations = 0;
    }

    public void addAuthor(Researcher author) {
        if (!authors.contains(author)) authors.add(author);
    }

    public void addKeyword(String keyword) {
        if (!keywords.contains(keyword)) keywords.add(keyword);
    }

    public void addCitation() {
        this.citations++;
    }

    public String getCitation(Format format) {
        String authorsStr = "";
        for (Researcher a : authors) {
            if (!authorsStr.isEmpty()) authorsStr += ", ";
            authorsStr += a.toString();
        }
        switch (format) {
            case PLAIN_TEXT:
                return String.format("%s. \"%s.\" %s, %s, %d pages, DOI: %s.",
                        authorsStr, title, journal,
                        datePublished.toString(), pages,
                        doi != null ? doi : "N/A");
            case BIBTEX:
                return String.format(
                        "@article{%s,\n  author = {%s},\n  title = {%s},\n" +
                                "  journal = {%s},\n  year = {%s},\n" +
                                "  pages = {%d},\n  doi = {%s}\n}",
                        doi != null ? doi : title.replaceAll("\\s+", "_"),
                        authorsStr, title, journal,
                        datePublished.getYear(), pages,
                        doi != null ? doi : "N/A");
            default:
                throw new IllegalArgumentException("Unsupported format: " + format);
        }
    }

    @Override
    public int compareTo(ResearchPaper other) {
        return Integer.compare(other.citations, this.citations); // citations бойынша кемуі
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResearchPaper that = (ResearchPaper) o;
        return Objects.equals(doi, that.doi) &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doi, title);
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<Researcher> getAuthors() { return new ArrayList<>(authors); }

    public String getJournal() { return journal; }
    public void setJournal(String journal) { this.journal = journal; }

    public int getPages() { return pages; }
    public void setPages(int pages) { this.pages = pages; }

    public LocalDate getDatePublished() { return datePublished; }
    public void setDatePublished(LocalDate date) { this.datePublished = date; }

    public int getCitationsCount() { return citations; }
    public void setCitations(int citations) { this.citations = citations; }

    public String getDoi() { return doi; }
    public void setDoi(String doi) { this.doi = doi; }

    public String getPaperAbstract() { return paperAbstract; }
    public void setPaperAbstract(String paperAbstract) {
        this.paperAbstract = paperAbstract;
    }

    public List<String> getKeywords() { return new ArrayList<>(keywords); }

    @Override
    public String toString() {
        return "ResearchPaper{" +
                "title='" + title + "'" +
                ", journal='" + journal + "'" +
                ", pages=" + pages +
                ", date=" + datePublished +
                ", citations=" + citations +
                ", doi='" + doi + "'" +
                "}";
    }
}