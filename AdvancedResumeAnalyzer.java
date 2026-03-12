import javax.swing.*;
import java.io.*;
import java.util.*;

public class AdvancedResumeAnalyzer {

    public static void main(String[] args) {

        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(null);

        if(result != JFileChooser.APPROVE_OPTION){
            System.out.println("No file selected");
            return;
        }

        File file = chooser.getSelectedFile();

        String resumeText = readFile(file);

        Scanner sc = new Scanner(System.in);

        System.out.println("\nEnter Job Role (cyber security / ai engineer / frontend development / full stack development / data analytics):");
        String jobRole = sc.nextLine().toLowerCase();

        analyzeResume(resumeText, jobRole);
    }

    static String readFile(File file){

        String text = "";

        try{
            Scanner reader = new Scanner(file);

            while(reader.hasNextLine()){
                text += reader.nextLine().toLowerCase() + " ";
            }

            reader.close();

        }catch(Exception e){
            System.out.println("Error reading file");
        }

        return text;
    }

    static void analyzeResume(String resume, String jobRole){

        int score = 0;

        // ===== JOB ROLE SKILL DATABASE =====

        HashMap<String,String[]> jobSkills = new HashMap<>();

        jobSkills.put("frontend development", new String[]{
                "html","css","javascript","react","git"
        });

        jobSkills.put("full stack development", new String[]{
                "html","css","javascript","react","node","mongodb","sql"
        });

        jobSkills.put("ai engineer", new String[]{
                "python","machine learning","deep learning","tensorflow","pytorch","numpy","pandas"
        });

        jobSkills.put("data analytics", new String[]{
                "excel","sql","python","power bi","tableau","statistics","pandas"
        });

        jobSkills.put("cyber security", new String[]{
                "network security","cryptography","ethical hacking","linux","python","penetration testing"
        });

        // ===== DEFAULT SKILL LIST =====

        String skills[] = {
                "java","python","javascript","react","node",
                "c++","sql","mongodb","html","css",
                "spring","docker","aws","machine learning",
                "git","linux","tensorflow","pytorch",
                "pandas","numpy","excel","tableau","power bi"
        };

        String requiredSkills[];

        if(jobSkills.containsKey(jobRole))
            requiredSkills = jobSkills.get(jobRole);
        else
            requiredSkills = skills;

        LinkedList<String> suggestions = new LinkedList<>();
        Queue<String> matchedSkills = new LinkedList<>();
        LinkedList<String> missingSkills = new LinkedList<>();

        int matchCount = 0;

        // ===== SKILL MATCHING =====

        for(String skill : requiredSkills){

            boolean inResume = resume.contains(skill);

            if(inResume){
                score += 5;
                matchedSkills.add(skill);
                matchCount++;
            }
            else{
                missingSkills.add(skill);
            }
        }

        int matchPercentage = (matchCount * 100) / requiredSkills.length;

        // ===== SECTION DETECTION =====

        boolean hasProject =
                resume.contains("project") ||
                resume.contains("projects");

        if(hasProject)
            score += 15;
        else
            suggestions.add("Add projects section");

        boolean hasExperience =
                resume.contains("experience") ||
                resume.contains("intern") ||
                resume.contains("internship");

        if(hasExperience)
            score += 15;
        else
            suggestions.add("Add internship or work experience");

        boolean hasEducation =
                resume.contains("education") ||
                resume.contains("degree") ||
                resume.contains("university");

        if(hasEducation)
            score += 10;
        else
            suggestions.add("Add education section");

        boolean hasCertificate =
                resume.contains("certificate") ||
                resume.contains("certification") ||
                resume.contains("certifications");

        if(hasCertificate)
            score += 10;
        else
            suggestions.add("Add certifications");

        boolean hasSkills =
                resume.contains("skills") ||
                resume.contains("technical skills");

        if(!hasSkills)
            suggestions.add("Add skills section");

        if(!missingSkills.isEmpty())
            suggestions.add("Improve skills related to the selected job role");

        if(score > 100)
            score = 100;

        printReport(score, matchPercentage, matchedSkills, missingSkills, suggestions);
    }

    static void printReport(int score, int matchPercentage,
                            Queue<String> matched,
                            LinkedList<String> missing,
                            LinkedList<String> suggestions){

        System.out.println("\n========== AI RESUME ANALYSIS ==========");

        System.out.println("\nATS Score: " + score + "/100");

        System.out.println("Resume Rating: " + getStars(score));

        System.out.println("\nJob Match Percentage: " + matchPercentage + "%");

        System.out.println("\nMatched Skills:");

        if(matched.isEmpty())
            System.out.println("None");

        while(!matched.isEmpty())
            System.out.println("✔ " + matched.poll());

        System.out.println("\nMissing Skills:");

        if(missing.isEmpty())
            System.out.println("None");
        else
            for(String s : missing)
                System.out.println("✖ " + s);

        System.out.println("\nSuggestions:");

        if(suggestions.isEmpty())
            System.out.println("Resume looks strong!");
        else
            for(String s : suggestions)
                System.out.println("- " + s);
    }

    static String getStars(int score){

        int stars;

        if(score <= 20) stars = 1;
        else if(score <= 40) stars = 2;
        else if(score <= 60) stars = 3;
        else if(score <= 80) stars = 4;
        else stars = 5;

        String star = "";

        for(int i=0;i<stars;i++)
            star += "⭐";

        for(int i=stars;i<5;i++)
            star += "☆";

        return star;
    }
}