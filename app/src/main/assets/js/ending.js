window.onload = function() {
    if (window.Android) {
        const data = JSON.parse(window.Android.getEndingData());

        if (data.error) {
            alert(data.error);
            return;
        }

        let photoFile = 'type_brave.jpg';
        if (data.characterType === 2) photoFile = 'type_kind.jpg';
        else if (data.characterType === 3) photoFile = 'type_cunning.jpg';
        else if (data.characterType === 4) photoFile = 'type_wise.jpg';

        const formatDelta = (d) => {
            if (d > 0) return '+' + d;
            if (d < 0) return '' + d;
            return '0';
        };

        // Устанавливаем смешную картинку концовки
        const endingImage = document.getElementById('endingImage');
        if (endingImage) {
            endingImage.style.backgroundImage = "url('images/ending_funny.jpg')";
        }

        const characterPhoto = document.getElementById('characterPhoto');
        if (characterPhoto) {
            characterPhoto.src = 'images/' + photoFile;
            characterPhoto.onerror = function() {
                this.style.display = 'none';
            };
        }

        const endingTitle = document.getElementById('endingTitle');
        if (endingTitle) endingTitle.textContent = data.endingTitle;

        const endingDesc = document.getElementById('endingDesc');
        if (endingDesc) endingDesc.textContent = data.endingDesc;

        const characterTitle = document.getElementById('characterTitle');
        if (characterTitle) characterTitle.textContent = 'Титул: ' + data.title;

        const statCourage = document.getElementById('statCourage');
        if (statCourage) statCourage.textContent = data.endCourage + ' (' + formatDelta(data.deltaCourage) + ')';

        const statKindness = document.getElementById('statKindness');
        if (statKindness) statKindness.textContent = data.endKindness + ' (' + formatDelta(data.deltaKindness) + ')';

        const statCunning = document.getElementById('statCunning');
        if (statCunning) statCunning.textContent = data.endCunning + ' (' + formatDelta(data.deltaCunning) + ')';

        const statWisdom = document.getElementById('statWisdom');
        if (statWisdom) statWisdom.textContent = data.endWisdom + ' (' + formatDelta(data.deltaWisdom) + ')';
    }
};

function startNewGame() {
    if (window.Android) {
        window.Android.startNewGame();
    }
}

function returnToMenu() {
    if (window.Android) {
        window.Android.returnToMenu();
    }
}