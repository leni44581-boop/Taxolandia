window.onload = function() {
    if (window.Android) {
        const data = JSON.parse(window.Android.getMainMenuData());
        const list = document.getElementById('historyList');
        
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
            list.innerHTML = '<div class="empty-state">Вы ещё не завершили ни одного прохождения.</div>';
        }
    }
};

function deleteItem(id) {
    if (window.Android && confirm('Удалить эту запись?')) {
        window.Android.deleteHistoryItem(id);
        location.reload();
    }
}

function returnToMenu() {
    if (window.Android) {
        window.Android.returnToMenu();
    }
}
