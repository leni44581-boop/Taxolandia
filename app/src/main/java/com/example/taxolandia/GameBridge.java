package com.example.taxolandia;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.webkit.JavascriptInterface;

import com.example.taxolandia.data.GameDatabase;
import com.example.taxolandia.data.GameProgress;
import com.example.taxolandia.engine.QuizEngine;
import com.example.taxolandia.engine.StoryEngine;
import com.example.taxolandia.engine.EndingResolver;

import org.json.JSONArray;
import org.json.JSONObject;

public class GameBridge {
    private Context context;
    private GameDatabase db;
    private QuizEngine quizEngine;
    private StoryEngine storyEngine;
    private GameProgress currentProgress;
    private Handler mainHandler;
    private MainActivity activity;

    public GameBridge(Context context) {
        this.context = context;
        if (context instanceof MainActivity) {
            this.activity = (MainActivity) context;
        }
        this.db = new GameDatabase(context);
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    @JavascriptInterface
    public String getMainMenuData() {
        try {
            JSONObject data = new JSONObject();
            GameProgress inProgress = db.getInProgress();
            data.put("hasInProgress", inProgress != null);

            JSONArray history = new JSONArray();
            for (GameProgress p : db.getCompletedHistory()) {
                JSONObject item = new JSONObject();
                item.put("id", p.getId());
                item.put("date", p.getCreatedAt());
                item.put("type", p.getCharacterType());
                item.put("ending", p.getEndingType());
                item.put("courage", p.getEndCourage());
                item.put("kindness", p.getEndKindness());
                item.put("cunning", p.getEndCunning());
                item.put("wisdom", p.getEndWisdom());
                history.put(item);
            }
            data.put("history", history);

            return data.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"hasInProgress\": false, \"history\": []}";
        }
    }

    @JavascriptInterface
    public void startNewGame() {
        try {
            GameProgress inProgress = db.getInProgress();
            if (inProgress != null) {
                db.deleteProgress(inProgress.getId());
            }

            quizEngine = new QuizEngine();
            storyEngine = null;
            currentProgress = null;

            navigateTo("quiz.html");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка startNewGame: " + e.getMessage());
        }
    }

    @JavascriptInterface
    public void continueGame() {
        try {
            GameProgress inProgress = db.getInProgress();
            if (inProgress != null) {
                currentProgress = inProgress;
                storyEngine = new StoryEngine(inProgress);
                navigateTo("story.html");
                mainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadCurrentScene();
                    }
                }, 500);
            } else {
                showAlert("Нет сохранённого прохождения");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка continueGame: " + e.getMessage());
        }
    }

    @JavascriptInterface
    public String getQuizQuestions() {
        try {
            if (quizEngine == null) {
                quizEngine = new QuizEngine();
            }
            JSONArray questions = new JSONArray();
            for (int i = 0; i < quizEngine.getTotalQuestions(); i++) {
                JSONObject q = new JSONObject();
                q.put("id", i);
                q.put("text", quizEngine.getQuestion(i).getText());
                JSONArray answers = new JSONArray();
                for (int j = 0; j < 4; j++) {
                    JSONObject a = new JSONObject();
                    a.put("text", quizEngine.getQuestion(i).getAnswer(j).getText());
                    answers.put(a);
                }
                q.put("answers", answers);
                questions.put(q);
            }
            return questions.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    @JavascriptInterface
    public void selectAnswer(int questionIndex, int answerIndex) {
        try {
            if (quizEngine != null) {
                quizEngine.selectAnswer(questionIndex, answerIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public String getQuizResult() {
        try {
            if (quizEngine == null || !quizEngine.isFinished()) {
                return "{\"error\": \"Тест не завершён\"}";
            }

            QuizEngine.CharacterProfile profile = quizEngine.determineType();
            int[] stats = quizEngine.calculateStats();

            JSONObject result = new JSONObject();
            result.put("type", profile.getType());
            result.put("typeName", profile.getTypeName());
            result.put("description", profile.getDescription());
            result.put("courage", stats[0]);
            result.put("kindness", stats[1]);
            result.put("cunning", stats[2]);
            result.put("wisdom", stats[3]);

            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    @JavascriptInterface
    public void startAdventure() {
        try {
            QuizEngine.CharacterProfile profile = quizEngine.determineType();
            int[] stats = quizEngine.calculateStats();

            currentProgress = new GameProgress();
            currentProgress.setCreatedAt(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm",
                    java.util.Locale.getDefault()).format(new java.util.Date()));
            currentProgress.setQuizAnswers(quizEngine.getAnswers());
            currentProgress.setCharacterType(profile.getType());
            currentProgress.setStartCourage(stats[0]);
            currentProgress.setStartKindness(stats[1]);
            currentProgress.setStartCunning(stats[2]);
            currentProgress.setStartWisdom(stats[3]);
            currentProgress.setCurrentCourage(stats[0]);
            currentProgress.setCurrentKindness(stats[1]);
            currentProgress.setCurrentCunning(stats[2]);
            currentProgress.setCurrentWisdom(stats[3]);
            currentProgress.setCurrentSceneId("scene_1");
            currentProgress.setCurrentChapter("Пролог");

            long id = db.insertProgress(currentProgress);
            currentProgress.setId(id);

            storyEngine = new StoryEngine(currentProgress);
            navigateTo("story.html");
            mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadCurrentScene();
                }
            }, 500);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка startAdventure: " + e.getMessage());
        }
    }

    @JavascriptInterface
    public String getCurrentScene() {
        try {
            if (storyEngine == null) {
                return "{\"error\": \"Движок истории не инициализирован\"}";
            }

            StoryEngine.StoryScene scene = storyEngine.getCurrentScene();
            if (scene == null) {
                return "{\"error\": \"Сцена не найдена: " + storyEngine.getCurrentSceneId() + "\"}";
            }

            JSONObject data = new JSONObject();
            data.put("id", scene.getId());
            data.put("chapter", scene.getChapter());
            data.put("text", scene.getText());
            data.put("speaker", scene.getSpeaker() != null ? scene.getSpeaker() : "");
            data.put("background", scene.getBackgroundResId());
            data.put("portrait", scene.getPortraitResId());

            JSONArray choices = new JSONArray();
            for (StoryEngine.StoryChoice choice : storyEngine.getAvailableChoices()) {
                JSONObject c = new JSONObject();
                c.put("text", choice.getText());
                c.put("nextScene", choice.getNextSceneId());
                c.put("courage", choice.getCourageDelta());
                c.put("kindness", choice.getKindnessDelta());
                c.put("cunning", choice.getCunningDelta());
                c.put("wisdom", choice.getWisdomDelta());
                c.put("special", choice.isSpecial());
                choices.put(c);
            }
            data.put("choices", choices);

            return data.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    @JavascriptInterface
    public void makeChoice(int choiceIndex) {
        try {
            if (storyEngine == null) return;

            java.util.List<StoryEngine.StoryChoice> choices = storyEngine.getAvailableChoices();
            if (choiceIndex < 0 || choiceIndex >= choices.size()) return;

            StoryEngine.StoryChoice choice = choices.get(choiceIndex);
            boolean continues = storyEngine.makeChoice(choice);

            if (!continues) {
                int endingType = EndingResolver.resolveEnding(currentProgress);
                currentProgress.setEndingType(endingType);
                currentProgress.setStatus(1);
                currentProgress.setEndCourage(currentProgress.getCurrentCourage());
                currentProgress.setEndKindness(currentProgress.getCurrentKindness());
                currentProgress.setEndCunning(currentProgress.getCurrentCunning());
                currentProgress.setEndWisdom(currentProgress.getCurrentWisdom());
                db.updateProgress(currentProgress);

                navigateTo("ending.html");
            } else {
                loadCurrentScene();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка makeChoice: " + e.getMessage());
        }
    }

    @JavascriptInterface
    public void goToScene(String sceneId) {
        try {
            if (storyEngine == null || sceneId == null) return;

            // Проверяем что сцена существует
            StoryEngine.StoryScene scene = storyEngine.getSceneById(sceneId);
            if (scene == null) {
                showAlert("Сцена не найдена: " + sceneId);
                return;
            }

            // Устанавливаем ID сцены
            storyEngine.setCurrentSceneId(sceneId);
            currentProgress.setCurrentSceneId(sceneId);
            currentProgress.setCurrentChapter(scene.getChapter());
            db.updateProgress(currentProgress);

            loadCurrentScene();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка goToScene: " + e.getMessage());
        }
    }

    @JavascriptInterface
    public String getEndingData() {
        try {
            if (currentProgress == null) {
                return "{\"error\": \"Прогресс не найден\"}";
            }

            JSONObject data = new JSONObject();
            int endingType = currentProgress.getEndingType();
            data.put("endingType", endingType);
            data.put("endingTitle", EndingResolver.getEndingTitle(endingType));
            data.put("endingDesc", EndingResolver.getEndingDescription(endingType));
            data.put("characterType", currentProgress.getCharacterType());
            data.put("title", EndingResolver.getTitleByType(currentProgress.getCharacterType()));

            int dC = currentProgress.getEndCourage() - currentProgress.getStartCourage();
            int dK = currentProgress.getEndKindness() - currentProgress.getStartKindness();
            int dCu = currentProgress.getEndCunning() - currentProgress.getStartCunning();
            int dW = currentProgress.getEndWisdom() - currentProgress.getStartWisdom();

            data.put("endCourage", currentProgress.getEndCourage());
            data.put("endKindness", currentProgress.getEndKindness());
            data.put("endCunning", currentProgress.getEndCunning());
            data.put("endWisdom", currentProgress.getEndWisdom());
            data.put("deltaCourage", dC);
            data.put("deltaKindness", dK);
            data.put("deltaCunning", dCu);
            data.put("deltaWisdom", dW);

            return data.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    @JavascriptInterface
    public void deleteHistoryItem(long id) {
        try {
            db.deleteProgress(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void returnToMenu() {
        try {
            if (currentProgress != null && currentProgress.getStatus() == 0) {
                db.updateProgress(currentProgress);
            }
            navigateTo("index.html");
        } catch (Exception e) {
            e.printStackTrace();
            navigateTo("index.html");
        }
    }

    private void navigateTo(String page) {
        mainHandler.post(() -> {
            try {
                if (activity != null) {
                    activity.loadPage("file:///android_asset/" + page);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void loadCurrentScene() {
        mainHandler.post(() -> {
            try {
                if (activity != null) {
                    activity.getWebView().evaluateJavascript("loadScene()", null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void showAlert(String message) {
        mainHandler.post(() -> {
            try {
                if (activity != null) {
                    String safeMessage = message.replace("'", "\\'").replace("\n", "\\n");
                    activity.getWebView().evaluateJavascript("alert('" + safeMessage + "')", null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}