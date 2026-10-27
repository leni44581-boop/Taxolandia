window.onload = function() {
    refreshMenu();
};

function refreshMenu() {
    if (window.Android) {
        try {
            const data = JSON.parse(window.Android.getMainMenuData());
            const btnContinue = document.getElementById('btnContinue');
            if (btnContinue) {
                btnContinue.disabled = !data.hasInProgress;
                if (data.hasInProgress) {
                    btnContinue.style.opacity = '1';
                    btnContinue.style.cursor = 'pointer';
                } else {
                    btnContinue.style.opacity = '0.5';
                    btnContinue.style.cursor = 'not-allowed';
                }
            }
        } catch (e) {
            console.error('Error loading menu data:', e);
        }
    }
}

function startNewGame() {
    if (window.Android) {
        try {
            window.Android.startNewGame();
        } catch (e) {
            alert('Ошибка: ' + e.message);
        }
    }
}

function continueGame() {
    if (window.Android) {
        try {
            window.Android.continueGame();
        } catch (e) {
            alert('Ошибка продолжения: ' + e.message);
        }
    }
}

function showHistory() {
    if (window.Android) {
        try {
            const data = JSON.parse(window.Android.getMainMenuData());
            const modal = document.getElementById('historyModal');
            const list = document.getElementById('historyList');

            list.innerHTML = '';

            if (data.history && data.history.length > 0) {
                data.history.forEach(item => {
                    const div = document.createElement('div');
                    div.className = 'history-item';

                    const typeNames = ['', 'Отважная такса', 'Добрая такса', 'Хитрая такса', 'Мудрая такса'];
                    const endingTitles = ['', 'Концовка Смелости', 'Концовка Доброты', 'Концовка Хитрости', 'Концовка Мудрости'];

                    div.innerHTML = `
                        <div class="date">${item.date}</div>
                        <div class="type">${typeNames[item.type] || 'Неизвестно'}</div>
                        <div class="ending">${endingTitles[item.ending] || 'Неизвестно'}</div>
                        <div class="stats">Смелость: ${item.courage} | Доброта: ${item.kindness} | Хитрость: ${item.cunning} | Мудрость: ${item.wisdom}</div>
                        <button class="delete-btn" onclick="deleteItem(${item.id})">Удалить</button>
                    `;
                    list.appendChild(div);
                });
            } else {
                list.innerHTML = '<div class="empty-state">Вы ещё не завершили ни одного прохождения. Начните новую историю!</div>';
            }

            if (modal) {
                modal.classList.add('active');
            }
        } catch (e) {
            console.error('Error showing history:', e);
        }
    }
}

function closeHistory() {
    const modal = document.getElementById('historyModal');
    if (modal) {
        modal.classList.remove('active');
    }
}

function deleteItem(id) {
    if (window.Android && confirm('Удалить эту запись?')) {
        window.Android.deleteHistoryItem(id);
        showHistory();
    }
}

function returnToMenu() {
    if (window.Android) {
        window.Android.returnToMenu();
    }
}

// Обработка кнопки "Назад" на телефоне
window.onpopstate = function() {
    if (window.Android) {
        window.Android.returnToMenu();
    }
};