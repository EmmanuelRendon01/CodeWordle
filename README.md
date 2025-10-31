# Codewordle

Codewordle es un proyecto basado en el popular juego Wordle, desarrollado con Spring Boot para el backend y tecnologías web modernas para el frontend. Este proyecto incluye funcionalidades como autenticación de usuarios, gestión de partidas y lógica de juego.

## Requisitos previos

Antes de ejecutar este proyecto, asegúrate de tener instalados los siguientes requisitos:

- Java Development Kit (JDK) 17 o superior
- Apache Maven
- Un entorno de base de datos compatible con H2 (ya incluido en el proyecto)

## Instalación y ejecución

Sigue estos pasos para ejecutar el proyecto en tu máquina local:

1. Clona este repositorio:
   ```bash
   git clone <URL_DEL_REPOSITORIO>
   ```

2. Navega al directorio del proyecto:
   ```bash
   cd codewordle
   ```

3. Compila el proyecto usando Maven:
   ```bash
   ./mvnw clean install
   ```

4. Ejecuta la aplicación:
   ```bash
   ./mvnw spring-boot:run
   ```

5. Accede a la aplicación en tu navegador en la URL: [http://localhost:8080](http://localhost:8080)

## Estructura del proyecto

El proyecto sigue la estructura estándar de un proyecto Spring Boot:

- **src/main/java**: Contiene el código fuente de la aplicación.
  - **controller**: Controladores que manejan las solicitudes HTTP.
  - **service**: Lógica de negocio.
  - **repository**: Interfaces para la interacción con la base de datos.
  - **model**: Clases que representan las entidades del dominio.
  - **dto**: Objetos de transferencia de datos.
  - **config**: Configuraciones de seguridad y otros aspectos del proyecto.

- **src/main/resources**: Archivos de configuración y recursos estáticos.
  - **application.properties**: Configuración de la aplicación.
  - **templates**: Plantillas HTML para las vistas.
  - **static/js**: Archivos JavaScript para la lógica del frontend.

## Funcionalidades principales

- **Autenticación y autorización**: Los usuarios pueden registrarse, iniciar sesión y acceder a sus partidas.
- **Gestión de partidas**: Los usuarios pueden iniciar nuevas partidas, realizar conjeturas y recibir retroalimentación.
- **Interfaz web interactiva**: Una experiencia de usuario sencilla e intuitiva.

## Base de datos

El proyecto utiliza una base de datos H2 en memoria para el almacenamiento de datos. Los archivos de la base de datos se encuentran en el directorio `data/`.

## Ejemplo de archivo application.properties

A continuación, se muestra un ejemplo del archivo `application.properties` utilizado en este proyecto. Este archivo contiene configuraciones básicas para la aplicación:

```properties
spring.application.name=codewordle

spring.datasource.url=jdbc:h2:file:./data/db

spring.mvc.view.prefix=/WEB-INF/jsp/
spring.mvc.view.suffix=.jsp

spring.datasource.driverClassName=org.h2.Driver

spring.datasource.username=sa
spring.datasource.password=password

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

spring.jpa.hibernate.ddl-auto=update
```

> **Nota:** Si necesitas personalizar estas configuraciones para tu entorno, asegúrate de no incluir información sensible como contraseñas reales al subir el archivo a un repositorio público.

## Contribuciones

Si deseas contribuir a este proyecto, por favor sigue estos pasos:

1. Haz un fork del repositorio.
2. Crea una nueva rama para tu funcionalidad o corrección de errores.
3. Envía un pull request con tus cambios.

## Licencia

Este proyecto está bajo la licencia MIT. Consulta el archivo LICENSE para más detalles.
