<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CodeWordle - Iniciar Sesión</title>
    <%-- 1. Carga de Tailwind CSS a través del CDN --%>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        /* Pequeña personalización para el cursor parpadeante */
        .blinking-cursor {
            font-weight: 100;
            font-size: 2rem;
            animation: 1s blink step-end infinite;
        }
        @keyframes blink {
            from, to { color: transparent; }
            50% { color: #34d399; } /* Un color verde esmeralda */
        }
    </style>
</head>
<body class="bg-gray-900 text-gray-200 font-mono">
    <div class="min-h-screen flex items-center justify-center">
        <div class="max-w-md w-full bg-gray-800 p-8 rounded-lg shadow-2xl">
            <div class="text-center mb-8">
                <h1 class="text-3xl font-bold text-emerald-400">CodeWordle<span class="blinking-cursor">_</span></h1>
            </div>

            <form id="login-form">

                <%-- ¡ESTE ES EL DIV QUE FALTABA O ERA INCORRECTO! --%>
                <%-- Asegúrate de que este bloque exista exactamente así. --%>
                <div id="error-message" class="hidden bg-red-800 border border-red-600 text-red-200 px-4 py-3 rounded relative mb-4" role="alert">
                    <strong class="font-bold">Error:</strong>
                    <span id="error-text" class="block sm:inline"></span>
                </div>

                <div class="mb-4">
                    <label for="email" class="block mb-2 text-sm font-medium text-gray-400">Email</label>
                    <input type="email" id="email" name="email" required
                           class="w-full p-3 bg-gray-700 rounded border border-gray-600 focus:outline-none focus:ring-2 focus:ring-emerald-500"
                           placeholder="tu@correo.com">
                </div>

                <div class="mb-6">
                    <label for="password" class="block mb-2 text-sm font-medium text-gray-400">Password</label>
                    <input type="password" id="password" name="password" required
                           class="w-full p-3 bg-gray-700 rounded border border-gray-600 focus:outline-none focus:ring-2 focus:ring-emerald-500"
                           placeholder="************">
                </div>

                <button type="submit"
                        class="w-full bg-emerald-600 hover:bg-emerald-700 text-white font-bold py-3 px-4 rounded transition duration-300">
                    Login
                </button>
            </form>
            <div class="text-center mt-6 text-sm">
                <a href="<c:url value='/register' />" class="font-medium text-emerald-400 hover:underline">Regístrate aquí</a>
            </div>
        </div>
    </div>

    <script src="<c:url value='/js/login.js'/>"></script>
</body>
</html>