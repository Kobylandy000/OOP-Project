package project.services;

import project.interfaces.Researcher;
import project.models.ResearchPaper;
import project.users.User;

import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class ResearchAnalytics {

    private ResearchAnalytics() {
    }

    public static void printUniversityPapers(Collection<? extends Researcher> researchers,
                                             Comparator<ResearchPaper> comparator) {
        List<Map.Entry<Researcher, ResearchPaper>> papers = new ArrayList<>();

        for (Researcher researcher : safeResearchers(researchers)) {
            for (ResearchPaper paper : researcher.getPapers()) {
                papers.add(new AbstractMap.SimpleEntry<>(researcher, paper));
            }
        }

        if (papers.isEmpty()) {
            System.out.println("No research papers found.");
            return;
        }

        papers.sort(Map.Entry.comparingByValue(comparator));
        for (Map.Entry<Researcher, ResearchPaper> entry : papers) {
            ResearchPaper paper = entry.getValue();
            System.out.println(
                    researcherName(entry.getKey()) +
                            " -> " + paper.getTitle() +
                            " | Citations: " + paper.getCitationsCount() +
                            " | Date: " + paper.getDatePublished() +
                            " | Pages: " + paper.getPages()
            );
        }
    }

    public static Optional<Researcher> findTopCitedResearcher(Collection<? extends Researcher> researchers) {
        return safeResearchers(researchers).stream()
                .max(Comparator.comparingInt(ResearchAnalytics::totalCitations));
    }

    public static Optional<Researcher> findTopCitedResearcherOfYear(
            Collection<? extends Collection<? extends Researcher>> schools, int year) {
        List<Researcher> allResearchers = new ArrayList<>();
        for (Collection<? extends Researcher> school : schools) {
            allResearchers.addAll(safeResearchers(school));
        }

        return allResearchers.stream()
                .max(Comparator.comparingInt(researcher -> citationsForYear(researcher, year)));
    }

    public static int totalCitations(Researcher researcher) {
        int total = 0;
        for (ResearchPaper paper : researcher.getPapers()) {
            total += paper.getCitationsCount();
        }
        return total;
    }

    public static int citationsForYear(Researcher researcher, int year) {
        int total = 0;
        for (ResearchPaper paper : researcher.getPapers()) {
            LocalDate published = paper.getDatePublished();
            if (published != null && published.getYear() == year) {
                total += paper.getCitationsCount();
            }
        }
        return total;
    }

    public static String researcherName(Researcher researcher) {
        if (researcher instanceof User user) {
            return user.getFullName();
        }
        return researcher.toString();
    }

    private static List<Researcher> safeResearchers(Collection<? extends Researcher> researchers) {
        List<Researcher> result = new ArrayList<>();
        if (researchers == null) {
            return result;
        }

        for (Researcher researcher : researchers) {
            if (researcher != null) {
                result.add(researcher);
            }
        }
        return result;
    }
}
