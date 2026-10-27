package com.example.taxolandia.engine;

import java.util.ArrayList;
import java.util.List;

public class QuizEngine {
    private List<Question> questions;
    private int[] answers;
    private int currentIndex;

    public QuizEngine() {
        questions = createQuestions();
        answers = new int[questions.size()];
        for (int i = 0; i < answers.length; i++) answers[i] = -1;
        currentIndex = 0;
    }

    public int getTotalQuestions() { return questions.size(); }
    public int getCurrentIndex() { return currentIndex; }
    public int[] getAnswers() { return answers; }

    public Question getQuestion(int index) {
        if (index >= 0 && index < questions.size()) {
            return questions.get(index);
        }
        return null;
    }

    public void selectAnswer(int questionIndex, int answerIndex) {
        if (questionIndex >= 0 && questionIndex < answers.length) {
            answers[questionIndex] = answerIndex;
        }
    }

    public boolean isFinished() {
        for (int a : answers) {
            if (a < 0) return false;
        }
        return true;
    }

    public int[] calculateStats() {
        int courage = 0, kindness = 0, cunning = 0, wisdom = 0;
        for (int i = 0; i < questions.size(); i++) {
            int ansIdx = answers[i];
            if (ansIdx < 0 || ansIdx >= 4) continue;
            Answer ans = questions.get(i).getAnswers().get(ansIdx);
            courage += ans.getCourage();
            kindness += ans.getKindness();
            cunning += ans.getCunning();
            wisdom += ans.getWisdom();
        }
        return new int[]{courage, kindness, cunning, wisdom};
    }

    public CharacterProfile determineType() {
        int[] stats = calculateStats();
        int max = Math.max(Math.max(stats[0], stats[1]), Math.max(stats[2], stats[3]));

        if (stats[0] == max) return new CharacterProfile(1, "Отважная такса",
                "Ты — воплощение смелости и решимости. Когда другие отступают, ты делаешь шаг вперёд.", 1);
        if (stats[1] == max) return new CharacterProfile(2, "Добрая такса",
                "Твоя сила — в доброте и эмпатии. Ты чувствуешь боль других как свою.", 2);
        if (stats[2] == max) return new CharacterProfile(3, "Хитрая такса",
                "Ты видишь то, что скрыто от других. Твой ум остёр как клинок.", 3);
        return new CharacterProfile(4, "Мудрая такса",
                "Ты понимаешь глубинные связи вещей. Твоя сила — в знании и спокойствии.", 4);
    }

    private List<Question> createQuestions() {
        List<Question> list = new ArrayList<>();

        // 1. Переезд друга
        list.add(new Question("Друг попросил помочь с переездом в выходной. Что сделаете?",
                new Answer("Помогу, друг важнее отдыха", 2, 1, 0, 0),
                new Answer("Откажусь вежливо - у меня планы", 0, 0, 1, 0),
                new Answer("Помогу, но попрошу взамен услугу", 0, 0, 2, 0),
                new Answer("Предложу помочь на пару часов", 1, 1, 0, 0)));

        // 2. Кошелёк на улице
        list.add(new Question("Нашли кошелёк с деньгами на улице. Ваши действия?",
                new Answer("Отнесу в полицию", 1, 2, 0, 0),
                new Answer("Оставлю себе - повезло", 0, 0, 2, 0),
                new Answer("Попробую найти владельца", 1, 2, 0, 0),
                new Answer("Заберу деньги, а кошелёк выброшу", 0, 0, 2, 0)));

        // 3. Опоздания коллеги
        list.add(new Question("Коллега постоянно опаздывает на встречи. Что сделаете?",
                new Answer("Скажу ему прямо о проблеме", 2, 0, 0, 0),
                new Answer("Поговорю с начальником", 1, 0, 1, 0),
                new Answer("Ничего, пусть сам разберётся", 0, 0, 0, 0),
                new Answer("Начну приходить позже тоже", 0, 0, 2, 0)));

        // 4. Лишняя сдача
        list.add(new Question("В магазине вам дали лишнюю сдачу. Что сделаете?",
                new Answer("Верну сразу", 1, 2, 0, 0),
                new Answer("Оставлю себе - их проблема", 0, 0, 1, 0),
                new Answer("Подумаю, вернуть или нет", 0, 0, 0, 0),
                new Answer("Потрачу на благотворительность", 0, 2, 0, 0)));

        // 5. Громкая музыка ночью
        list.add(new Question("Соседи громко слушают музыку ночью. Ваша реакция?",
                new Answer("Пойду и попрошу потише", 2, 0, 0, 0),
                new Answer("Позвоню в полицию", 1, 0, 1, 0),
                new Answer("Включу музыку ещё громче", 0, 0, 2, 0),
                new Answer("Надену наушники и потерплю", 0, 1, 0, 0)));

        // 6. Работа в другом городе
        list.add(new Question("Вам предложили работу с большей зарплатой, но в другом городе.",
                new Answer("Соглашусь без раздумий", 2, 0, 1, 0),
                new Answer("Откажусь - тут моя семья", 0, 2, 0, 0),
                new Answer("Попрошу время подумать", 0, 0, 0, 0),
                new Answer("Соглашусь, но буду ездить домой", 1, 1, 0, 0)));

        // 7. Долг друга
        list.add(new Question("Друг занял у вас деньги и не возвращает. Что сделаете?",
                new Answer("Напомню прямо", 2, 0, 0, 0),
                new Answer("Перестану с ним общаться", 0, 0, 1, 0),
                new Answer("Буду намекать", 0, 0, 2, 0),
                new Answer("Прощу долг, но больше не дам", 0, 2, 0, 0)));

        // 8. Чужая ошибка на работе
        list.add(new Question("На работе свалили чужую ошибку на вас. Реакция?",
                new Answer("Буду доказывать свою правоту", 2, 0, 1, 0),
                new Answer("Промолчу, чтобы не конфликтовать", 0, 1, 0, 0),
                new Answer("Отплачу тем же", 0, 0, 2, 0),
                new Answer("Поговорю с виноватым наедине", 1, 1, 0, 0)));

        // 9. Парковка на месте для инвалидов
        list.add(new Question("Видите, как кто-то паркуется на месте для инвалидов. Что сделаете?",
                new Answer("Сделаю замечание", 2, 1, 0, 0),
                new Answer("Позвоню в ГИБДД", 1, 1, 0, 0),
                new Answer("Ничего, не моё дело", 0, 0, 0, 0),
                new Answer("Сфотографирую и выложу в сеть", 0, 0, 2, 0)));

        // 10. Не понравившийся подарок
        list.add(new Question("Вам подарили подарок, который не нравится. Что сделаете?",
                new Answer("Поблагодарю и буду использовать", 0, 2, 0, 0),
                new Answer("Скажу честно, что не нравится", 2, 0, 0, 0),
                new Answer("Передарю кому-нибудь", 0, 0, 2, 0),
                new Answer("Уберу подальше и забуду", 0, 0, 0, 0)));

        // 11. Подмена в выходной
        list.add(new Question("Коллега просит подменить его в выходной. Что ответите?",
                new Answer("Соглашусь, помогу", 1, 2, 0, 0),
                new Answer("Откажусь - это его работа", 0, 0, 1, 0),
                new Answer("Соглашусь, но попрошу отработать", 0, 0, 2, 0),
                new Answer("Предложу частичную помощь", 0, 1, 0, 0)));

        // 12. Очередь
        list.add(new Question("В очереди вас пропустили вперёд. Ваша реакция?",
                new Answer("Поблагодарю и пройду", 1, 2, 0, 0),
                new Answer("Откажусь, пусть другие идут", 0, 2, 0, 0),
                new Answer("Воспользуюсь случаем", 0, 0, 1, 0),
                new Answer("Предложу пройти другому", 0, 2, 0, 0)));

        // 13. Слухи о вас
        list.add(new Question("Узнали, что друг распускает о вас слухи. Что сделаете?",
                new Answer("Поговорю с ним напрямую", 2, 0, 0, 0),
                new Answer("Прекращу общение", 1, 0, 1, 0),
                new Answer("Распущу слухи о нём", 0, 0, 2, 0),
                new Answer("Спрошу, почему он так сделал", 1, 1, 0, 0)));

        // 14. Сомнительная сделка
        list.add(new Question("Вам предложили участвовать в сомнительной сделке. Что сделаете?",
                new Answer("Откажусь сразу", 1, 1, 0, 0),
                new Answer("Соглашусь - деньги нужны", 0, 0, 2, 0),
                new Answer("Изучу все риски", 0, 0, 0, 0),
                new Answer("Посоветуюсь с юристом", 0, 0, 0, 0)));

        // 15. Вечеринка
        list.add(new Question("На вечеринке все танцуют, а вы не хотите. Что сделаете?",
                new Answer("Останусь сидеть", 0, 0, 0, 0),
                new Answer("Пойду танцевать за компанию", 1, 1, 0, 0),
                new Answer("Уйду пораньше", 0, 0, 1, 0),
                new Answer("Найду тех, кто тоже не танцует", 0, 1, 0, 0)));

        // 16. Неправильный совет
        list.add(new Question("Вам дали неправильный совет, из-за которого вы потеряли время.",
                new Answer("Скажу человеку об этом", 2, 0, 0, 0),
                new Answer("Промолчу, чтобы не обижать", 0, 2, 0, 0),
                new Answer("Больше не буду у него спрашивать", 0, 0, 1, 0),
                new Answer("Пойму, что он хотел как лучше", 0, 2, 0, 0)));

        // 17. Стабильная работа vs стартап
        list.add(new Question("Нужно выбрать между стабильной работой и интересным стартапом.",
                new Answer("Выберу стабильность", 0, 0, 0, 0),
                new Answer("Рискну и выберу стартап", 2, 0, 1, 0),
                new Answer("Останусь, но буду искать варианты", 0, 0, 1, 0),
                new Answer("Попрошу испытательный срок", 1, 0, 0, 0)));

        // 18. Сосед затопил квартиру
        list.add(new Question("Ваш сосед затопил вашу квартиру. Что сделаете?",
                new Answer("Потребую компенсацию сразу", 2, 0, 1, 0),
                new Answer("Поговорю спокойно о решении", 1, 1, 0, 0),
                new Answer("Подам в суд", 1, 0, 1, 0),
                new Answer("Прощу, если он извинится", 0, 2, 0, 0)));

        // 19. Быстрое решение
        list.add(new Question("Вам нужно быстро принять важное решение. Как поступите?",
                new Answer("Действую сразу, потом разберусь", 2, 0, 1, 0),
                new Answer("Посоветуюсь с близкими", 0, 1, 0, 0),
                new Answer("Взвешу все за и против", 0, 0, 0, 0),
                new Answer("Послушаю интуицию", 1, 0, 1, 0)));

        // 20. Место в транспорте
        list.add(new Question("Кто-то занял ваше место в транспорте. Ваша реакция?",
                new Answer("Попрошу освободить место", 2, 0, 0, 0),
                new Answer("Промолчу и встану в другом месте", 0, 1, 0, 0),
                new Answer("Сделаю замечание грубо", 1, 0, 1, 0),
                new Answer("Попрошу вежливо", 1, 1, 0, 0)));

        return list;
    }

    public static class Question {
        private String text;
        private List<Answer> answers;

        public Question(String text, Answer... answers) {
            this.text = text;
            this.answers = new ArrayList<>();
            for (Answer a : answers) this.answers.add(a);
        }

        public String getText() { return text; }
        public List<Answer> getAnswers() { return answers; }
        public Answer getAnswer(int index) {
            return index >= 0 && index < answers.size() ? answers.get(index) : null;
        }
    }

    public static class Answer {
        private String text;
        private int courage, kindness, cunning, wisdom;

        public Answer(String text, int courage, int kindness, int cunning, int wisdom) {
            this.text = text;
            this.courage = courage;
            this.kindness = kindness;
            this.cunning = cunning;
            this.wisdom = wisdom;
        }

        public String getText() { return text; }
        public int getCourage() { return courage; }
        public int getKindness() { return kindness; }
        public int getCunning() { return cunning; }
        public int getWisdom() { return wisdom; }
    }

    public static class CharacterProfile {
        private int type;
        private String typeName;
        private String description;
        private int photoResId;

        public CharacterProfile(int type, String typeName, String description, int photoResId) {
            this.type = type;
            this.typeName = typeName;
            this.description = description;
            this.photoResId = photoResId;
        }

        public int getType() { return type; }
        public String getTypeName() { return typeName; }
        public String getDescription() { return description; }
        public int getPhotoResId() { return photoResId; }
    }
}