document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('login-form');
    // Obtenemos los elementos de error fuera del listener
    const errorDiv = document.getElementById('error-message');
    const errorText = document.getElementById('error-text');

    if (!loginForm || !errorDiv || !errorText) {
        console.error("No se encontraron los elementos del formulario de login en la página.");
        return;
    }

    loginForm.addEventListener('submit', async function (event) {
        event.preventDefault();

        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        errorDiv.classList.add('hidden');

        try {
            const response = await fetch('/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email: email, password: password })
            });

            if (response.ok) {
                const data = await response.json();
                localStorage.setItem('jwtToken', data.token);
                window.location.href = '/dashboard';
            } else {
                errorText.textContent = 'Email o contraseña incorrectos.';
                errorDiv.classList.remove('hidden');
            }
        } catch (error) {
            errorText.textContent = 'No se pudo conectar al servidor.';
            errorDiv.classList.remove('hidden');
            console.error('Error en el login:', error);
        }
    });
});