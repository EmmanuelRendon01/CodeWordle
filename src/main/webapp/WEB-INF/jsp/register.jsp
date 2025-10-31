<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CodeWordle - Registro</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        .blinking-cursor {
            font-weight: 100;
            font-size: 2rem;
            animation: 1s blink step-end infinite;
        }
        @keyframes blink {
            from, to { color: transparent; }
            50% { color: #34d399; }
        }
    </style>
</head>
<body class="bg-gray-900 text-gray-200 font-mono">

    <div class="min-h-screen flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
        <div class="max-w-md w-full bg-gray-800 p-8 rounded-lg shadow-2xl">

            <div class="text-center mb-8">
                <h1 class="text-3xl font-bold text-emerald-400">CodeWordle<span class="blinking-cursor">_</span></h1>
                <p class="text-gray-400 mt-2">Crea tu cuenta para empezar a jugar.</p>
            </div>

            <%-- El formulario debe hacer un POST a /auth/register --%>
            <form id="register-form">

                <%-- Aquí podrías mostrar errores de validación si los pasas desde el controlador --%>

                <div class="mb-4">
                    <label for="name" class="block mb-2 text-sm font-medium text-gray-400">Name (Username)</label>
                    <input type="text" id="name" name="name" required
                           class="w-full p-3 bg-gray-700 rounded border border-gray-600 focus:outline-none focus:ring-2 focus:ring-emerald-500"
                           placeholder="Ej: BjarneStroustrup">
                </div>

                <div class="mb-4">
                    <label for="email" class="block mb-2 text-sm font-medium text-gray-400">Email</label>
                    <input type="email" id="email" name="email" required
                           class="w-full p-3 bg-gray-700 rounded border border-gray-600 focus:outline-none focus:ring-2 focus:ring-emerald-500"
                           placeholder="tu@correo.com">
                </div>

                <div class="mb-4">
                    <label for="password" class="block mb-2 text-sm font-medium text-gray-400">Password</label>
                    <input type="password" id="password" name="password" required
                           class="w-full p-3 bg-gray-700 rounded border border-gray-600 focus:outline-none focus:ring-2 focus:ring-emerald-500"
                           placeholder="Mínimo 8 caracteres">
                </div>

                <div class="mb-6">
                    <label for="confirmPassword" class="block mb-2 text-sm font-medium text-gray-400">Confirm Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" required
                           class="w-full p-3 bg-gray-700 rounded border border-gray-600 focus:outline-none focus:ring-2 focus:ring-emerald-500"
                           placeholder="Repite la contraseña">
                </div>

                <button type="submit"
                        class="w-full bg-emerald-600 hover:bg-emerald-700 text-white font-bold py-3 px-4 rounded transition duration-300">
                    Crear Cuenta
                </button>
            </form>

            <div class="text-center mt-6 text-sm">
                <p class="text-gray-500">¿Ya tienes una cuenta?</p>
                <a href="<c:url value='/login' />" class="font-medium text-emerald-400 hover:underline">Inicia sesión aquí</a>
            </div>
        </div>
    </div>
    <script src="<c:url value='/js/register.js'/>"></script>
</body>
</html>