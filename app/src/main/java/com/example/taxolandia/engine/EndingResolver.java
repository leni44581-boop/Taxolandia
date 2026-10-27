package com.example.taxolandia.engine;

import com.example.taxolandia.data.GameProgress;

public class EndingResolver {

    public static int resolveEnding(GameProgress progress) {
        int c = progress.getCurrentCourage();
        int k = progress.getCurrentKindness();
        int cu = progress.getCurrentCunning();
        int w = progress.getCurrentWisdom();

        int max = Math.max(Math.max(c, k), Math.max(cu, w));

        if (c == max) return 1;
        if (k == max) return 2;
        if (cu == max) return 3;
        return 4;
    }

    public static String getEndingTitle(int endingType) {
        switch (endingType) {
            case 1: return "Концовка Смелости: Герой Таксоландии";
            case 2: return "Концовка Доброты: Объединитель Королевства";
            case 3: return "Концовка Хитрости: Тень, Спасшая Свет";
            case 4: return "Концовка Мудрости: Хранитель Баланса";
            default: return "Неизвестная концовка";
        }
    }

    public static String getEndingDescription(int endingType) {
        switch (endingType) {
            case 1: return "Ты смело встретил опасность лицом к лицу и спас Таксоландию решительным действием. Твоё имя навсегда вписано в легенды королевства как имя великого героя.";
            case 2: return "Ты объединил жителей Таксоландии и нашёл решение, которое помогло всем. Королевство процветает благодаря твоей доброте и способности видеть лучшее в каждом.";
            case 3: return "Ты раскрыл обман и использовал нестандартный план, чтобы спасти королевство. Твоя хитрость оказалась сильнее любой тёмной магии.";
            case 4: return "Ты понял истинную природу артефакта и предотвратил катастрофу, восстановив баланс мира. Твоя мудрость стала основой новой эры Таксоландии.";
            default: return "История осталась незавершённой.";
        }
    }

    public static String getTitleByType(int characterType) {
        switch (characterType) {
            case 1: return "Отважный странник";
            case 2: return "Добрый защитник";
            case 3: return "Хитрый искатель";
            case 4: return "Мудрый наблюдатель";
            default: return "Путник";
        }
    }
}
