"# githubclient" 

Cómo ejecutar la aplicación:

La aplicación requiere Java versión 17 o superior.
- Para ejecutar la interfaz gráfica (JavaFX), abrir MainWindow.java desde el entorno de desarrollo o usar el comando mvn javafx:run.
- Para ejecutar desde consola (CLI), compilar el proyecto y ejecutar el archivo .jar con java -jar GitHubClient.jar.
- La versión web no está implementada en esta entrega.

Endpoints utilizados:
- https://api.github.com/users/{usuario}: obtiene información del perfil.
- https://api.github.com/users/{usuario}/repos: obtiene los repositorios públicos.
- https://api.github.com/repos/{owner}/{repo}/languages: obtiene el uso de lenguajes por repositorio.
- https://api.github.com/rate_limit: consulta el límite de peticiones disponibles.


Ejemplos de salida:

La interfaz gráfica muestra los datos del usuario y un gráfico de uso de lenguajes.
En consola, se imprime la información del perfil y los repositorios en formato de texto.




Notas sobre el límite de peticiones:
- Sin autenticación: máximo 60 peticiones por hora.
- Con autenticación mediante GITHUB_TOKEN: hasta 5000 peticiones por hora.
Para usar el token, se puede definir como variable de entorno:
set GITHUB_TOKEN=tu_token_aqui
