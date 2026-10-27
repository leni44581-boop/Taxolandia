let questions = [];
let currentQuestion = 0;
let answers = [];
let quizFinished = false;

window.onload = function() {
    if (window.Android) {
        try {
            const raw = window.Android.getQuizQuestions();
            console.log('Raw questions:', raw);
            questions = JSON.parse(raw);
            console.log('Parsed questions:', questions.length);

            if (questions.length === 0) {
                alert('Ошибка: вопросы не загружены!');
                return;
            }

            answers = new Array(questions.length).fill(-1);
            showQuestion();
        } catch (e) {
            alert('Ошибка загрузки вопросов: ' + e.message);
            console.error(e);
        }
    } else {
        alert('Android bridge не подключён!');
    }
};

function showQuestion() {
    if (currentQuestion >= questions.length) {
        finishQuiz();
        return;
    }

    const q = questions[currentQuestion];
    const qText = document.getElementById('questionText');
    if (qText) qText.textContent = q.text;

    const progress = ((currentQuestion + 1) / questions.length) * 100;
    const progressFill = document.getElementById('progressFill');
    if (progressFill) progressFill.style.width = progress + '%';

    const container = document.getElementById('answersContainer');
    if (!container) return;
    container.innerHTML = '';

    if (q.answers && q.answers.length > 0) {
        q.answers.forEach((answer, index) => {
            const btn = document.createElement('button');
            btn.className = 'answer-btn';
            btn.textContent = answer.text;
            btn.onclick = () => selectAnswer(index);
            container.appendChild(btn);
        });
    }

    const btnBack = document.getElementById('btnBack');
    if (btnBack) btnBack.style.display = currentQuestion > 0 ? 'block' : 'none';
}

function selectAnswer(index) {
    answers[currentQuestion] = index;

    if (window.Android) {
        window.Android.selectAnswer(currentQuestion, index);
    }

    currentQuestion++;
    showQuestion();
}

function goBack() {
    if (currentQuestion > 0) {
        currentQuestion--;
        showQuestion();
    } else {
        returnToMenu();
    }
}

function finishQuiz() {
    if (quizFinished) return;
    quizFinished = true;

    if (window.Android) {
        try {
            const result = JSON.parse(window.Android.getQuizResult());

            if (result.error) {
                alert(result.error);
                return;
            }

            let photoFile = 'type_brave.jpg';
            if (result.type === 2) photoFile = 'type_kind.jpg';
            else if (result.type === 3) photoFile = 'type_cunning.jpg';
            else if (result.type === 4) photoFile = 'type_wise.jpg';

            document.body.innerHTML = `
                <div class="container">
                    <div class="background" style="background-image: url('images/quiz_background.jpg');"></div>
                    <div class="overlay"></div>
                    <div class="content result-content">
                        <img src="images/${photoFile}" class="result-photo" onerror="this.style.display='none'">
                        <h1 style="color: #FFD700; font-size: 2em; margin-bottom: 15px;">${result.typeName}</h1>
                        <p style="font-size: 1.1em; line-height: 1.6; margin-bottom: 20px; max-width: 600px;">${result.description}</p>
                        <div class="stats-box">
                            <div class="stat"><span>Смелость:</span> <span style="color: #FFD700;">${result.courage}</span></div>
                            <div class="stat"><span>Доброта:</span> <span style="color: #FFD700;">${result.kindness}</span></div>
                            <div class="stat"><span>Хитрость:</span> <span style="color: #FFD700;">${result.cunning}</span></div>
                            <div class="stat"><span>Мудрость:</span> <span style="color: #FFD700;">${result.wisdom}</span></div>
                        </div>
                        <div class="buttons">
                            <button class="btn btn-primary" onclick="startAdventure()">Отправиться в Таксоландию</button>
                            <button class="btn btn-secondary" onclick="returnToMenu()">Вернуться в меню</button>
                        </div>
                    </div>
                </div>
            `;
        } catch (e) {
            alert('Ошибка результата: ' + e.message);
        }
    }
}

function startAdventure() {
    if (window.Android) {
        window.Android.startAdventure();
    }
}

function returnToMenu() {
    if (window.Android) {
        window.Android.returnToMenu();
    }
}

window.onpopstate = function() {
    if (!quizFinished) {
        goBack();
    } else {
        returnToMenu();
    }
    return false;
};