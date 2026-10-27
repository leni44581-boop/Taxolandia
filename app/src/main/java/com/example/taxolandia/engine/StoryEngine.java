package com.example.taxolandia.engine;

import com.example.taxolandia.data.GameProgress;
import java.util.ArrayList;
import java.util.List;

public class StoryEngine {
    private GameProgress progress;
    private String currentSceneId;
    private List<StoryScene> scenes;

    public StoryEngine(GameProgress progress) {
        this.progress = progress;
        this.currentSceneId = progress.getCurrentSceneId();
        if (this.currentSceneId == null || this.currentSceneId.isEmpty()) {
            this.currentSceneId = "scene_1";
        }
        this.scenes = createScenes();
    }

    public StoryScene getCurrentScene() {
        for (StoryScene scene : scenes) {
            if (scene.getId().equals(currentSceneId)) {
                return scene;
            }
        }
        return null;
    }

    public StoryScene getSceneById(String sceneId) {
        if (sceneId == null) return null;
        for (StoryScene scene : scenes) {
            if (scene.getId().equals(sceneId)) {
                return scene;
            }
        }
        return null;
    }

    public void setCurrentSceneId(String sceneId) {
        this.currentSceneId = sceneId;
    }

    public List<StoryChoice> getAvailableChoices() {
        StoryScene scene = getCurrentScene();
        if (scene == null || scene.getChoices() == null) return new ArrayList<>();
        List<StoryChoice> available = new ArrayList<>();
        for (StoryChoice choice : scene.getChoices()) {
            if (isChoiceAvailable(choice)) {
                available.add(choice);
            }
        }
        return available;
    }

    private boolean isChoiceAvailable(StoryChoice choice) {
        if (choice.getRequiredFlag() != null && !choice.getRequiredFlag().isEmpty()) {
            if (!progress.hasFlag(choice.getRequiredFlag())) {
                return false;
            }
        }
        if (choice.getMinStat() > 0) {
            int maxStat = Math.max(Math.max(progress.getCurrentCourage(), progress.getCurrentKindness()),
                    Math.max(progress.getCurrentCunning(), progress.getCurrentWisdom()));
            if (maxStat < choice.getMinStat()) {
                return false;
            }
        }
        return true;
    }

    public boolean makeChoice(StoryChoice choice) {
        progress.setCurrentCourage(progress.getCurrentCourage() + choice.getCourageDelta());
        progress.setCurrentKindness(progress.getCurrentKindness() + choice.getKindnessDelta());
        progress.setCurrentCunning(progress.getCurrentCunning() + choice.getCunningDelta());
        progress.setCurrentWisdom(progress.getCurrentWisdom() + choice.getWisdomDelta());

        String nextId = choice.getNextSceneId();
        if ("END".equals(nextId)) {
            return false;
        }
        currentSceneId = nextId;
        progress.setCurrentSceneId(currentSceneId);

        StoryScene nextScene = getCurrentScene();
        if (nextScene != null) {
            progress.setCurrentChapter(nextScene.getChapter());
        }
        return true;
    }

    public String getCurrentSceneId() { return currentSceneId; }
    public GameProgress getProgress() { return progress; }

    private List<StoryScene> createScenes() {
        List<StoryScene> list = new ArrayList<>();

        list.add(new StoryScene("scene_1", "Пролог",
                "Вы стоите перед огромными каменными вратами, покрытыми светящимися рунами. За ними раскинулась долина, о которой вы слышали только в легендах — Таксоландия. Воздух пахнет магией и приключениями.",
                null, 1, 0,
                new StoryChoice("Войти через главные врата", "scene_2", 1, 0, 0, 0),
                new StoryChoice("Осмотреться и найти тайный вход", "scene_2", 0, 0, 1, 0)));

        list.add(new StoryScene("scene_2", "Пролог",
                "Врата открываются с громким скрипом. Перед вами предстаёт проводник — такса Бобик. «Добро пожаловать, путник! Я — Бобик, и я знаю, зачем ты здесь.»",
                "Бобик", 1, 1,
                new StoryChoice("«Расскажи мне всё, что знаешь»", "scene_3", 0, 0, 0, 0),
                new StoryChoice("«Сначала покажи дорогу»", "scene_3", 1, 0, 0, 0)));

        list.add(new StoryScene("scene_3", "Глава 1",
                "Деревня такс встречает вас запахом свежего хлеба и звуками кузнечного молота. У фонтана в центре площади стоит бронзовая статуя таксы-основателя. Жители смотрят на вас с любопытством.",
                null, 3, 0,
                new StoryChoice("Подойти к фонтану и осмотреться", "scene_4", 0, 0, 0, 0),
                new StoryChoice("Заговорить с первым встречным", "scene_4", 1, 1, 0, 0)));

        list.add(new StoryScene("scene_4", "Глава 1",
                "К вам подбегает взволнованная такса по имени Булочка. «Путник! У нас пропал Лунный Кристалл — без него наша деревня теряет защиту! Ты поможешь нам?»",
                "Булочка", 3, 2,
                new StoryChoice("«Конечно помогу! Где его видели в последний раз?»", "scene_5", 1, 1, 0, 0),
                new StoryChoice("«Расскажи подробнее, прежде чем я соглашусь»", "scene_5", 0, 0, 0, 0),
                new StoryChoice("«А что я получу взамен?»", "scene_5", 0, 0, 2, 0)));

        list.add(new StoryScene("scene_5", "Глава 2",
                "Волшебный Лес встречает вас тишиной. Деревья здесь светятся изнутри, а под ногами хрустят светящиеся грибы. Тропа раздваивается.",
                null, 4, 0,
                new StoryChoice("Пойти по широкой тропе", "scene_6", 1, 0, 0, 0),
                new StoryChoice("Свернуть на узкую тропинку", "scene_6", 0, 0, 1, 0)));

        list.add(new StoryScene("scene_6", "Глава 2",
                "Тропа ведёт вас глубже в лес. Светящиеся грибы освещают путь, а вдалеке слышны странные звуки. Вы чувствуете, что кто-то наблюдает за вами.",
                null, 4, 0,
                new StoryChoice("Идти дальше смело", "scene_7", 2, 0, 0, 0),
                new StoryChoice("Оглядеться и спрятаться", "scene_7", 0, 0, 1, 0)));

        list.add(new StoryScene("scene_7", "Глава 2",
                "Широкая тропа приводит к поляне, где сидит грустная лиса-такса. «Мой малыш потерялся в лесу... Помогите найти его!»",
                "Лиса-такса", 4, 3,
                new StoryChoice("«Я найду его, не волнуйтесь!»", "scene_8", 1, 2, 0, 0),
                new StoryChoice("«Покажите, где он был в последний раз»", "scene_8", 0, 1, 0, 0)));

        list.add(new StoryScene("scene_8", "Глава 2",
                "След приводит вас к руинам древнего храма. Внутри — загадка на стене: «Три двери стоят перед тобой. Одна ведёт к свету, одна ко тьме, одна к истине. Выбирай мудро.»",
                null, 5, 0,
                new StoryChoice("Выбрать дверь света (левую)", "scene_9", 2, 0, 0, 0),
                new StoryChoice("Выбрать дверь истины (среднюю)", "scene_9", 0, 0, 0, 0),
                new StoryChoice("Осмотреть все двери внимательнее", "scene_9", 0, 0, 1, 0)));

        list.add(new StoryScene("scene_9", "Глава 2",
                "В центре зала возвышается светящийся кристалл. Рядом стоит тёмная фигура таксы-похитителя. «Ты не заберёшь его просто так», — шипит он.",
                "Похититель", 5, 4,
                new StoryChoice("Атаковать похитителя", "scene_10", 2, 0, 0, 0),
                new StoryChoice("Попытаться договориться", "scene_10", 0, 2, 0, 0),
                new StoryChoice("Отвлечь его и схватить кристалл", "scene_10", 0, 0, 2, 0),
                new StoryChoice("Разгадать, зачем ему кристалл", "scene_10", 0, 0, 0, 0)));

        list.add(new StoryScene("scene_10", "Глава 3",
                "С Кристаллом (или без него) вы отправляетесь в Древнюю Библиотеку. Хранитель библиотеки, старый пёс в очках, встречает вас у входа.",
                "Хранитель", 6, 5,
                new StoryChoice("«Мне нужна информация о Лунном Кристалле»", "scene_11", 0, 0, 0, 0),
                new StoryChoice("«Я нашёл Кристалл. Что мне с ним делать?»", "scene_11", 1, 1, 0, 0)));

        list.add(new StoryScene("scene_11", "Глава 3",
                "Хранитель проводит вас в запретный отдел. «Лунный Кристалл — ключ к силе королевства. Но у него есть тёмная сторона. Если поместить его в тронный зал замка, он либо усилит защиту, либо разрушит всё — зависит от намерений того, кто его несёт.»",
                "Хранитель", 6, 5,
                new StoryChoice("«Я отнесу его в замок и защищу королевство»", "scene_12", 2, 1, 0, 0),
                new StoryChoice("«Может, его лучше уничтожить?»", "scene_12", 0, 0, 0, 0),
                new StoryChoice("«А если использовать его силу для себя?»", "scene_12", 0, 0, 2, 0)));

        list.add(new StoryScene("scene_12", "Глава 4",
                "Замок Таксоландии возвышается над долиной. У ворот стоит страж — такса в доспехах. «Стой! Кто идёт?»",
                "Страж", 7, 6,
                new StoryChoice("«Я послан деревней с важным делом!»", "scene_13", 1, 1, 0, 0),
                new StoryChoice("Молча показать Кристалл", "scene_13", 0, 0, 1, 0),
                new StoryChoice("«У меня аудиенция у королевы»", "scene_13", 0, 0, 0, 0)));

        list.add(new StoryScene("scene_13", "Глава 4",
                "Вас проводят в тронный зал. Королева Таксоландии — величественная такса в золотой короне — сидит на троне. «Говори, путник. Зачем ты здесь?»",
                "Королева", 8, 7,
                new StoryChoice("Рассказать правду о Кристалле", "scene_14", 1, 2, 0, 0),
                new StoryChoice("Скрыть часть информации", "scene_14", 0, 0, 2, 0),
                new StoryChoice("Попросить королеву самой решить судьбу Кристалла", "scene_14", 0, 1, 0, 0)));

        list.add(new StoryScene("scene_14", "Глава 4",
                "Королева выслушивает вас внимательно. «Ты храбр, что пришёл сюда. Но финальное испытание ждёт тебя в Сердце Королевства — древних руинах за мостом. Только там решится судьба Кристалла.»",
                "Королева", 8, 7,
                new StoryChoice("«Я готов к испытанию!»", "scene_15", 2, 0, 0, 0),
                new StoryChoice("«Расскажите об испытании подробнее»", "scene_15", 0, 0, 0, 0)));

        list.add(new StoryScene("scene_15", "Глава 5",
                "Вы стоите в пещере, вся в светящихся рунах. В центре — пьедестал для Кристалла. Королева наблюдает издалека. Настало время финального выбора.",
                null, 9, 0,
                new StoryChoice("Поместить Кристалл с намерением защитить (требует смелости)", "ending_brave", 2, 0, 0, 0),
                new StoryChoice("Призвать жителей и объединить силы (требует доброты)", "ending_kind", 0, 2, 0, 0),
                new StoryChoice("Использовать Кристалл хитро, обманув тёмную силу", "ending_cunning", 0, 0, 2, 0),
                new StoryChoice("Понять истинную природу Кристалла и нейтрализовать его (требует мудрости)", "ending_wise", 0, 0, 0, 0)));

        list.add(new StoryScene("scene_16", "Финал",
                "Таксоландия спасена! Жители города ликуют на улицах. Ваше имя навсегда вписано в легенды королевства.",
                null, 10, 0,
                new StoryChoice("Узнать итоги путешествия", "END", 0, 0, 0, 0)));

        list.add(new StoryScene("ending_brave", "Финал",
                "Вы смело помещаете Кристалл на пьедестал, направив всю свою решимость на защиту королевства. Яркая вспышка света — и тёмная сила рассеивается. Таксоландия спасена благодаря вашему мужеству!",
                null, 7, 0,
                new StoryChoice("Узнать итоги путешествия", "scene_16", 0, 0, 0, 0)));

        list.add(new StoryScene("ending_kind", "Финал",
                "Вы призываете всех жителей Таксоландии, и вместе вы направляете силу Кристалла на созидание. Доброта каждого усиливает защиту. Королевство процветает как никогда!",
                null, 3, 0,
                new StoryChoice("Узнать итоги путешествия", "scene_16", 0, 0, 0, 0)));

        list.add(new StoryScene("ending_cunning", "Финал",
                "Вы хитро обманываете тёмную силу Кристалла, заставив её поверить, что он уже уничтожен. Тёмная сила уходит, а Кристалл остаётся у вас как инструмент для будущих свершений.",
                null, 6, 0,
                new StoryChoice("Узнать итоги путешествия", "scene_16", 0, 0, 0, 0)));

        list.add(new StoryScene("ending_wise", "Финал",
                "Вы понимаете, что Кристалл — не оружие и не защита, а зеркало намерений. Вы нейтрализуете его тёмную сторону, приняв свою тень. Таксоландия обретает истинный баланс.",
                null, 4, 0,
                new StoryChoice("Узнать итоги путешествия", "scene_16", 0, 0, 0, 0)));

        return list;
    }

    public static class StoryScene {
        private String id, chapter, text, speaker;
        private int backgroundResId, portraitResId;
        private List<StoryChoice> choices;

        public StoryScene(String id, String chapter, String text, String speaker,
                          int backgroundResId, int portraitResId, StoryChoice... choices) {
            this.id = id;
            this.chapter = chapter;
            this.text = text;
            this.speaker = speaker;
            this.backgroundResId = backgroundResId;
            this.portraitResId = portraitResId;
            this.choices = new ArrayList<>();
            for (StoryChoice c : choices) this.choices.add(c);
        }

        public String getId() { return id; }
        public String getChapter() { return chapter; }
        public String getText() { return text; }
        public String getSpeaker() { return speaker; }
        public int getBackgroundResId() { return backgroundResId; }
        public int getPortraitResId() { return portraitResId; }
        public List<StoryChoice> getChoices() { return choices; }
    }

    public static class StoryChoice {
        private String text, nextSceneId;
        private int courageDelta, kindnessDelta, cunningDelta, wisdomDelta;

        public StoryChoice(String text, String nextSceneId,
                           int courage, int kindness, int cunning, int wisdom) {
            this.text = text;
            this.nextSceneId = nextSceneId;
            this.courageDelta = courage;
            this.kindnessDelta = kindness;
            this.cunningDelta = cunning;
            this.wisdomDelta = wisdom;
        }

        public String getText() { return text; }
        public String getNextSceneId() { return nextSceneId; }
        public int getCourageDelta() { return courageDelta; }
        public int getKindnessDelta() { return kindnessDelta; }
        public int getCunningDelta() { return cunningDelta; }
        public int getWisdomDelta() { return wisdomDelta; }
        public String getRequiredFlag() { return null; }
        public int getMinStat() { return 0; }
        public boolean isSpecial() { return false; }
    }
}