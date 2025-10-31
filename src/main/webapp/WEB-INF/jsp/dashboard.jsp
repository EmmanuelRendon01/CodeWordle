<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>CodeWordle - The Game</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        /* Estilos para el tablero y la animación de volteo 3D */
        .tile {
            border: 2px solid #374151; /* gray-700 */
            transition: transform 0.6s, background-color 0.6s, border-color 0.6s;
            transform-style: preserve-3d;
            position: relative; /* Necesario para que front y back se posicionen correctamente */
        }
        .tile.flip {
            transform: rotateX(180deg);
        }
        .tile .front, .tile .back {
            position: absolute;
            width: 100%;
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            backface-visibility: hidden;
            -webkit-backface-visibility: hidden;
        }
        .tile .back {
            transform: rotateX(180deg);
        }
        /* Colores de retroalimentación para la parte trasera de la celda */
        .correct-position { background-color: #16a34a; border-color: #16a34a; } /* Verde */
        .wrong-position { background-color: #ca8a04; border-color: #ca8a04; } /* Amarillo */
        .incorrect { background-color: #374151; border-color: #374151; } /* Gris oscuro */
    </style>
</head>
<body class="bg-gray-900 text-gray-200 font-mono flex flex-col items-center min-h-screen pt-8 pb-4">

    <header class="mb-6 text-center">
        <h1 class="text-4xl md:text-5xl font-bold text-emerald-400 tracking-wider">CodeWordle</h1>
        <p class="text-gray-500">Adivina la palabra del día sobre programación</p>
    </header>

    <main class="w-full max-w-md mx-auto flex-grow flex flex-col justify-center">
        <!-- SECCIÓN 1: SELECCIÓN DE TEMA -->
        <div id="topic-selection" class="text-center p-4 transition-opacity duration-500">
            <h2 class="text-xl mb-4 text-gray-300">Selecciona un tema para empezar</h2>
            <select id="topic-select" class="bg-gray-700 text-white p-2 rounded w-full md:w-auto focus:outline-none focus:ring-2 focus:ring-emerald-500">
                <option value="Java">Java</option>
                <option value="Spring">Spring</option>
                <option value="DevOps">DevOps</option>
            </select>
            <button id="start-game-btn" class="mt-4 w-full md:w-auto bg-emerald-600 hover:bg-emerald-700 text-white font-bold py-2 px-4 rounded transition duration-300">
                Iniciar Partida
            </button>
        </div>

        <!-- SECCIÓN 2: EL JUEGO (oculto por defecto) -->
        <div id="game-container" class="hidden transition-opacity duration-500">
            <div id="game-board" class="grid gap-2 mb-4"></div>
            <form id="guess-form" class="flex gap-2">
                <input type="text" id="guess-input" required
                       class="flex-grow p-3 bg-gray-700 rounded border border-gray-600 focus:outline-none focus:ring-2 focus:ring-emerald-500 uppercase text-center text-2xl font-bold tracking-[.2em]"
                       autocomplete="off" disabled>
                <button type="submit" class="bg-gray-600 hover:bg-gray-700 text-white font-bold py-2 px-6 rounded" disabled>Enviar</button>
            </form>
        </div>

        <!-- SECCIÓN 3: MENSAJE DE FIN DE JUEGO (oculto por defecto) -->
        <div id="game-over-message" class="hidden text-center mt-4 p-6 bg-gray-800 rounded-lg shadow-lg transition-opacity duration-500">
            <h2 id="game-result-text" class="text-3xl font-bold"></h2>
            <p class="mt-2 text-gray-400">La palabra era: <span id="correct-word" class="text-emerald-400 font-bold uppercase tracking-wider"></span></p>
            <button id="play-again-btn" class="mt-6 w-full md:w-auto bg-emerald-600 hover:bg-emerald-700 text-white font-bold py-2 px-4 rounded">
                Jugar de Nuevo
            </button>
        </div>
    </main>

    <footer class="mt-8 text-center">
        <button id="logout-btn" class="text-gray-500 hover:text-red-500 transition duration-300">Cerrar Sesión</button>
    </footer>

    <script src="<c:url value='/js/game.js'/>"></script>
</body>
</html>