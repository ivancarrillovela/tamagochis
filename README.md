# Tamagochis

Proyecto Java (Maven) para una pequeña implementación de "tamagochis" — mascotas virtuales simples. Este README contiene instrucciones completas y operativas para compilar, probar y empaquetar el proyecto.

Repositorio: https://github.com/ivancarrillovela/tamagochis

Autor / Mantenedor: ivancarrillovela

---

## Resumen

- Grupo (groupId): org.cuatrovientos.dam.psp  
- Artefacto (artifactId): tamagochis  
- Versión: 0.0.1-SNAPSHOT  
- Descripción: A simple tamagochis.  
- Java: compilado con Java 8 (maven.compiler.source/target = 8)  
- Dependencias principales: junit:junit:3.8.1 (para pruebas)

El proyecto usa Maven como gestor de construcción. En el repositorio están presentes ficheros y metadatos de IDE (Eclipse: .classpath, .project, .settings) y la estructura típica Maven (carpeta src/ y target/).

---

## Requisitos

- Java JDK 8 (JAVA_HOME correctamente apuntado)
- Apache Maven 3.x
- Git (para clonar y contribuir)
- IDE opcional: Eclipse / IntelliJ / VS Code (la configuración de Eclipse está incluida)

Comprueba la versión de Java:
java -version

Comprueba la versión de Maven:
mvn -v

---

## Rápido: compilar, probar y empaquetar

1. Clona el repositorio:
git clone https://github.com/ivancarrillovela/tamagochis.git
cd tamagochis

2. Limpiar y compilar:
mvn clean compile

3. Ejecutar pruebas unitarias:
mvn test

4. Empaquetar (generará el JAR en target/):
mvn package

Después de `mvn package` encontrarás el artefacto:
target/tamagochis-0.0.1-SNAPSHOT.jar

Si el proyecto incluye una clase principal y el JAR contiene el Main-Class en el MANIFEST, puedes ejecutar:
java -jar target/tamagochis-0.0.1-SNAPSHOT.jar

Si el JAR no es ejecutable (no tiene Main-Class), pero quieres ejecutar una clase principal durante el desarrollo, usa:
mvn -q exec:java -Dexec.mainClass="org.cuatrovientos.dam.psp.Main"
(Nota: sustituye el nombre de la clase si tu proyecto tiene otra clase que actúe como entrypoint; revisa `src/main/java` para confirmar el nombre exacto.)

---

## Estructura del proyecto

(Contenido según convención Maven)
- pom.xml — descriptor de Maven (coordenadas y propiedades).
- src/
  - src/main/java — código fuente Java de la aplicación.
    - org.cuatrovientos.dam.psp.tamagochis - paquete para el programa.
      - Cuidador.java - clase Main.
      - Tamagochi.java - clase para crear el Runnable de los tamagochis.
  - src/test/java — pruebas unitarias (JUnit).
- target/ — salida de la compilación y empaquetado.
- .classpath, .project, .settings — metadatos de Eclipse.
- .mvn/ — soporte de wrapper/configuración de Maven.

---

## Información importante del pom.xml

- Encoding: UTF-8  
- Compilador: maven.compiler.source = 8, maven.compiler.target = 8  
- Dependencia de pruebas: junit:junit:3.8.1  
- Plugin management: versiones fijadas para limpieza, site, compilador, surefire, jar, install y deploy.

Esto significa que el proyecto está preparado para compilar en Java 8 y ejecutar tests con JUnit 3.

---

## Tests

El proyecto declara junit 3.8.1 en el POM. Ejecuta:
mvn test

Los informes y resultados de pruebas se generan en:
target/surefire-reports/

---

## Ejecutando desde un IDE

- Importa el proyecto como "Existing Maven Project" en Eclipse o IntelliJ.
- La configuración de Eclipse ya está incluida (.project, .classpath).
- Ejecuta la clase con método main (si existe) o ejecuta las pruebas con el runner de JUnit integrado.

---

## Contribuir

1. Haz fork del repositorio en GitHub.
2. Crea una rama para tu trabajo:
   git checkout -b feat/nombre-descriptivo
3. Haz commits pequeños y con mensaje claro.
4. Abre un Pull Request contra la rama `main` del repositorio original.
5. Añade pruebas cuando agregues lógica nueva o cambies comportamiento.

Normas básicas:
- Sigue las convenciones Java (nombres de paquetes, estilo).
- Mantén compatibilidad con Java 8.
- Asegura que `mvn test` pase antes de abrir el PR.

---

## Licencia

MIT License

Copyright (c) 2025 ivancarrillovela

Permiso por la presente, se concede a cualquier persona que obtenga una copia de este software y archivos de documentación asociados, para usar el Software sin restricción, incluyendo sin limitación los derechos de usar, copiar, modificar, fusionar, publicar, distribuir, sublicenciar y/o vender copias del Software, y permitir a las personas a las que se les proporcione el Software a hacer lo mismo, bajo las siguientes condiciones: el aviso de copyright anterior y este aviso de permiso deberán ser incluidos en todas las copias o partes sustanciales del Software.

EL SOFTWARE SE PROPORCIONA "TAL CUAL", SIN GARANTÍA DE NINGÚN TIPO, EXPRESA O IMPLÍCITA, INCLUYENDO PERO NO LIMITADO A GARANTÍAS DE COMERCIALIZACIÓN, IDONEIDAD PARA UN PROPÓSITO PARTICULAR Y NO INFRACCIÓN. EN NINGÚN CASO LOS AUTORES O TITULARES DEL COPYRIGHT SERÁN RESPONSABLES POR NINGUNA RECLAMACIÓN, DAÑO U OTRA RESPONSABILIDAD, YA SEA EN UNA ACCIÓN CONTRACTUAL, AGRAVIO U OTRA FORMA, DERIVADA DE, FUERA DE O EN CONEXIÓN CON EL SOFTWARE O EL USO U OTRO TIPO DE ACCIONES EN EL SOFTWARE.

---

## Contacto

GitHub: https://github.com/ivancarrillovela  
Autor: ivancarrillovela

---

Si quieres que cree y añada este README.md directamente al repositorio (commit/pull), indícalo y lo preparo para push con el contenido exacto que ves aquí.
