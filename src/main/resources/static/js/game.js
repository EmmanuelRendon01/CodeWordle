document.addEventListener('DOMContentLoaded', () => {
    // --- REFERENCIAS A ELEMENTOS DEL DOM ---
    const elements = {
        topicSelection: document.getElementById('topic-selection'),
        gameContainer: document.getElementById('game-container'),
        board: document.getElementById('game-board'),
        guessForm: document.getElementById('guess-form'),
        guessInput: document.getElementById('guess-input'),
        startGameBtn: document.getElementById('start-game-btn'),
        topicSelect: document.getElementById('topic-select'),
        gameOver: document.getElementById('game-over-message'),
        gameResultText: document.getElementById('game-result-text'),
        correctWord: document.getElementById('correct-word'),
        playAgainBtn: document.getElementById('play-again-btn'),
        logoutBtn: document.getElementById('logout-btn')
    };

    // --- ESTADO DEL JUEGO ---
    let state = {
        gameId: null,
        wordLength: 0,
        maxAttempts: 6,
        currentAttempt: 0,
        isSubmitting: false
    };

    // --- CAPA DE API (con getActiveGame y manejo de respuesta 204) ---
    const api = {
        getToken: () => localStorage.getItem('jwtToken'),

        _fetch: async (url, options) => {
            const token = api.getToken();
            if (!token) {
                window.location.href = '/login';
                return;
            }

            const defaultOptions = {
                headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` }
            };
            const response = await fetch(url, { ...defaultOptions, ...options });

            // NUEVO: Manejo específico para 204 No Content
            if (response.status === 204) {
                return null;
            }

            if (response.status === 401 || response.status === 403) {
                localStorage.removeItem('jwtToken');
                window.location.href = '/login?sessionExpired=true';
                throw new Error('Session expired');
            }
            if (!response.ok) {
                throw new Error('API request failed');
            }
            return response.json();
        },

        // NUEVO: Método para buscar partidas activas
        getActiveGame: () => api._fetch('/api/games/active', { method: 'GET' }),

        startGame: (topic) => api._fetch('/api/games/start', { method: 'POST', body: JSON.stringify({ topic }) }),

        makeGuess: (gameId, word) => api._fetch(`/api/games/${gameId}/guess`, { method: 'POST', body: JSON.stringify({ word }) })
    };

    // --- FUNCIONES DE RENDERIZADO Y UI ---
    // (setupBoard, renderFeedback, showGameOver no cambian, son perfectas como están)
    function setupBoard(wordLength, maxAttempts) {
        elements.board.innerHTML = '';
        elements.board.style.gridTemplateColumns = `repeat(${wordLength}, minmax(0, 1fr))`;
        for (let i = 0; i < maxAttempts; i++) {
            for (let j = 0; j < wordLength; j++) {
                const tile = document.createElement('div');
                tile.className = 'tile w-14 h-14 md:w-16 md:h-16 flex items-center justify-center text-3xl font-bold uppercase';
                tile.id = `tile-${i}-${j}`;
                const front = document.createElement('div');
                front.className = 'front';
                const back = document.createElement('div');
                back.className = 'back';
                tile.append(front, back);
                elements.board.appendChild(tile);
            }
        }
        elements.guessInput.maxLength = wordLength;
        elements.guessInput.disabled = false;
        elements.guessForm.querySelector('button').disabled = false;
    }

    function renderFeedback(feedback, attemptIndex) {
        feedback.forEach((fb, i) => {
            const tile = document.getElementById(`tile-${attemptIndex}-${i}`);
            const frontOfTile = tile.querySelector('.front');
            const backOfTile = tile.querySelector('.back');
            frontOfTile.textContent = fb.letter;
            backOfTile.textContent = fb.letter;

            let feedbackClass;
            if (fb.status === 'CORRECT_POSITION') feedbackClass = 'correct-position';
            else if (fb.status === 'WRONG_POSITION') feedbackClass = 'wrong-position';
            else feedbackClass = 'incorrect';

            setTimeout(() => {
                tile.classList.add('flip');
                backOfTile.classList.add(feedbackClass);
            }, i * 250);
        });
    }

    function showGameOver(status, word) {
        state.isSubmitting = true;
        elements.guessInput.disabled = true;
        elements.guessForm.querySelector('button').disabled = true;

        setTimeout(() => {
            elements.gameContainer.classList.add('hidden');
            elements.gameOver.classList.remove('hidden');
            elements.correctWord.textContent = word;
            elements.gameResultText.className = 'text-3xl font-bold';
            if (status === 'WON') {
                elements.gameResultText.textContent = '¡Felicidades, has ganado!';
                elements.gameResultText.classList.add('text-emerald-400');
            } else {
                elements.gameResultText.textContent = '¡Has perdido!';
                elements.gameResultText.classList.add('text-red-500');
            }
        }, state.wordLength * 250 + 500);
    }

    // --- MANEJADORES DE EVENTOS Y LÓGICA DE JUEGO ---

    // NUEVO: Función principal que se ejecuta al cargar la página
    async function initializeApp() {
        try {
            const activeGame = await api.getActiveGame();

            if (activeGame) {
                // Si hay una partida activa, la reanudamos
                console.log("Resuming active game:", activeGame);
                state.gameId = activeGame.gameId;
                state.wordLength = activeGame.wordLength;
                state.maxAttempts = activeGame.maxAttempts;
                state.currentAttempt = activeGame.previousGuesses.length;

                setupBoard(state.wordLength, state.maxAttempts);

                // Reconstruimos el tablero con los intentos anteriores, sin animación
                activeGame.previousGuesses.forEach((feedback, index) => {
                    feedback.forEach((fb, i) => {
                        const tile = document.getElementById(`tile-${index}-${i}`);
                        tile.querySelector('.front').textContent = fb.letter; // Mostramos la letra al frente
                        // Podrías añadir clases para mostrar el color directamente si lo prefieres
                    });
                });

                elements.topicSelection.classList.add('hidden');
                elements.gameContainer.classList.remove('hidden');
                elements.guessInput.focus();

            } else {
                // Si no hay partida, mostramos la selección de tema
                console.log("No active game found. Showing topic selection.");
                elements.topicSelection.classList.remove('hidden');
                elements.gameContainer.classList.add('hidden');
            }
        } catch (error) {
            console.error("Failed to initialize app:", error);
            // El _fetch ya maneja el caso de sesión expirada, redirigiendo al login
        }
    }

    async function handleStartGame() {
        try {
            const initialState = await api.startGame(elements.topicSelect.value);
            state = { ...state, gameId: initialState.gameId, wordLength: initialState.wordLength, maxAttempts: initialState.maxAttempts, currentAttempt: 0, isSubmitting: false };
            setupBoard(state.wordLength, state.maxAttempts);
            elements.topicSelection.classList.add('hidden');
            elements.gameContainer.classList.remove('hidden');
            elements.gameOver.classList.add('hidden');
            elements.guessInput.focus();
        } catch (error) {
            console.error(error);
            alert('Error al iniciar la partida. Intenta de nuevo.');
        }
    }

    async function handleGuessSubmit(event) {
        event.preventDefault();
        if (state.isSubmitting) return;

        const word = elements.guessInput.value.toUpperCase();
        if (word.length !== state.wordLength) return;

        state.isSubmitting = true;
        try {
            const result = await api.makeGuess(state.gameId, word);
            renderFeedback(result.feedback, state.currentAttempt);

            state.currentAttempt++;
            if (result.gameStatus !== 'IN_PROGRESS') {
                showGameOver(result.gameStatus, result.correctWord);
            }
        } catch (error) {
            console.error(error);
            alert('Error al enviar el intento.');
        } finally {
            elements.guessInput.value = '';
            if (state.currentAttempt < state.maxAttempts && state.gameStatus !== 'WON' && state.gameStatus !== 'LOST') {
                state.isSubmitting = false;
            }
        }
    }

    function handlePlayAgain() {
        elements.gameOver.classList.add('hidden');
        elements.topicSelection.classList.remove('hidden');
    }

    function handleLogout() {
        localStorage.removeItem('jwtToken');
        window.location.href = '/login';
    }

    // --- REGISTRO DE EVENTOS E INICIALIZACIÓN ---
    elements.startGameBtn.addEventListener('click', handleStartGame);
    elements.guessForm.addEventListener('submit', handleGuessSubmit);
    elements.playAgainBtn.addEventListener('click', handlePlayAgain);
    elements.logoutBtn.addEventListener('click', handleLogout);

    // Ejecutamos la lógica de inicialización al cargar la página
    initializeApp();
});