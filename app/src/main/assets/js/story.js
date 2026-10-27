let sceneHistory = [];
let currentSceneIndex = -1;

function loadScene() {
    if (window.Android) {
        const scene = JSON.parse(window.Android.getCurrentScene());

        if (scene.error) {
            alert(scene.error);
            return;
        }

        const bgMap = {
            'scene_1': 'scene_1_gates.jpg',
            'scene_2': 'scene_2_bobik.jpg',
            'scene_3': 'scene_3_village.jpg',
            'scene_4': 'scene_4_bulochka.jpg',
            'scene_5': 'scene_5_forest.jpg',
            'scene_6': 'scene_6_path.jpg',
            'scene_7': 'scene_7_fox.jpg',
            'scene_8': 'scene_8_temple.jpg',
            'scene_9': 'scene_9_crystal.jpg',
            'scene_10': 'scene_10_library.jpg',
            'scene_11': 'scene_11_secret.jpg',
            'scene_12': 'scene_12_castle.jpg',
            'scene_13': 'scene_13_throne.jpg',
            'scene_14': 'scene_14_queen.jpg',
            'scene_15': 'scene_15_cave.jpg',
            'scene_16': 'scene_16_celebration.jpg',
            'ending_brave': 'scene_16_celebration.jpg',
            'ending_kind': 'scene_16_celebration.jpg',
            'ending_cunning': 'scene_16_celebration.jpg',
            'ending_wise': 'scene_16_celebration.jpg'
        };

        const bgFile = bgMap[scene.id] || 'scene_1_gates.jpg';
        document.getElementById('storyBackground').style.backgroundImage =
            `url('images/${bgFile}')`;

        const portrait = document.getElementById('portrait');
        if (portrait) {
            portrait.style.display = 'none';
        }

        const speaker = document.getElementById('speaker');
        if (scene.speaker) {
            speaker.textContent = scene.speaker;
            speaker.style.display = 'block';
        } else {
            speaker.style.display = 'none';
        }

        document.getElementById('storyText').textContent = scene.text;

        const container = document.getElementById('choicesContainer');
        container.innerHTML = '';

        scene.choices.forEach((choice, index) => {
            const btn = document.createElement('button');
            btn.className = 'answer-btn' + (choice.special ? ' special' : '');
            btn.textContent = choice.text;
            btn.onclick = () => makeChoice(index);
            container.appendChild(btn);
        });

        // Показываем/скрываем кнопку "Назад"
        const btnBack = document.getElementById('btnBack');
        if (btnBack) {
            btnBack.style.display = sceneHistory.length > 0 ? 'block' : 'none';
        }
    }
}

function makeChoice(index) {
    if (window.Android) {
        // Сохраняем текущую сцену в историю
        if (window.Android) {
            const currentScene = JSON.parse(window.Android.getCurrentScene());
            if (currentScene && !currentScene.error) {
                sceneHistory.push(currentScene.id);
            }
        }

        window.Android.makeChoice(index);
    }
}

function goBack() {
    if (sceneHistory.length > 0 && window.Android) {
        const previousSceneId = sceneHistory.pop();
        window.Android.goToScene(previousSceneId);
    }
}

function showStatus() {
    alert('Характеристики сохраняются автоматически');
}

function startNewGame() {
    if (window.Android) {
        window.Android.startNewGame();
    }
}

function returnToMenu() {
    if (window.Android) {
        sceneHistory = [];
        window.Android.returnToMenu();
    }
}

window.onload = function() {
    sceneHistory = [];
    loadScene();
};

window.onpopstate = function() {
    if (window.Android) {
        window.Android.returnToMenu();
    }
    return false;
};