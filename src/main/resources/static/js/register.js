document.addEventListener('DOMContentLoaded', () => {
    const registerForm = document.getElementById('register-form');
    if (!registerForm) return;

    registerForm.addEventListener('submit', async function (event) {
        event.preventDefault();

        const name = document.getElementById('name').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;


        const requestBody = {
            name: name,
            email: email,
            password: password,
            confirmPassword: confirmPassword
        };

        try {
            const response = await fetch('/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestBody)
            });

            if (response.status === 201) { // 201 Created es lo que devuelve tu endpoint
                // Registro exitoso, redirigir al login
                window.location.href = '/login?registrationSuccess=true';
            } else {
                // Manejar errores de validación del backend
                const errorData = await response.json();
                alert('Error en el registro: ' + (errorData.message || 'Por favor, revisa los datos.'));
            }
        } catch (error) {
            alert('No se pudo conectar al servidor para el registro.');
            console.error('Error en el registro:', error);
        }
    });
});