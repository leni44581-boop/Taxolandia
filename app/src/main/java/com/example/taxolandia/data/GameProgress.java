package com.example.taxolandia.data;

public class GameProgress {
    private long id;
    private String createdAt;
    private int[] quizAnswers;
    private int characterType;
    private int startCourage, startKindness, startCunning, startWisdom;
    private int currentCourage, currentKindness, currentCunning, currentWisdom;
    private String currentSceneId;
    private String currentChapter;
    private String flags;
    private int status;
    private int endingType;
    private int endCourage, endKindness, endCunning, endWisdom;

    public GameProgress() {
        this.quizAnswers = new int[20];
        this.flags = "";
        this.status = 0;
        this.endingType = 0;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public int[] getQuizAnswers() { return quizAnswers; }
    public void setQuizAnswers(int[] quizAnswers) { this.quizAnswers = quizAnswers; }
    public int getCharacterType() { return characterType; }
    public void setCharacterType(int characterType) { this.characterType = characterType; }
    public int getStartCourage() { return startCourage; }
    public void setStartCourage(int v) { this.startCourage = v; }
    public int getStartKindness() { return startKindness; }
    public void setStartKindness(int v) { this.startKindness = v; }
    public int getStartCunning() { return startCunning; }
    public void setStartCunning(int v) { this.startCunning = v; }
    public int getStartWisdom() { return startWisdom; }
    public void setStartWisdom(int v) { this.startWisdom = v; }
    public int getCurrentCourage() { return currentCourage; }
    public void setCurrentCourage(int v) { this.currentCourage = v; }
    public int getCurrentKindness() { return currentKindness; }
    public void setCurrentKindness(int v) { this.currentKindness = v; }
    public int getCurrentCunning() { return currentCunning; }
    public void setCurrentCunning(int v) { this.currentCunning = v; }
    public int getCurrentWisdom() { return currentWisdom; }
    public void setCurrentWisdom(int v) { this.currentWisdom = v; }
    public String getCurrentSceneId() { return currentSceneId; }
    public void setCurrentSceneId(String v) { this.currentSceneId = v; }
    public String getCurrentChapter() { return currentChapter; }
    public void setCurrentChapter(String v) { this.currentChapter = v; }
    public String getFlags() { return flags; }
    public void setFlags(String flags) { this.flags = flags; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public int getEndingType() { return endingType; }
    public void setEndingType(int endingType) { this.endingType = endingType; }
    public int getEndCourage() { return endCourage; }
    public void setEndCourage(int v) { this.endCourage = v; }
    public int getEndKindness() { return endKindness; }
    public void setEndKindness(int v) { this.endKindness = v; }
    public int getEndCunning() { return endCunning; }
    public void setEndCunning(int v) { this.endCunning = v; }
    public int getEndWisdom() { return endWisdom; }
    public void setEndWisdom(int v) { this.endWisdom = v; }

    public boolean hasFlag(String flag) {
        if (flags == null || flags.isEmpty()) return false;
        for (String f : flags.split(",")) {
            if (f.trim().equals(flag)) return true;
        }
        return false;
    }

    public void addFlag(String flag) {
        if (flags == null || flags.isEmpty()) {
            flags = flag;
        } else if (!hasFlag(flag)) {
            flags = flags + "," + flag;
        }
    }
}
