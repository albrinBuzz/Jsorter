# FileOrganizer

**FileOrganizer** es una aplicación diseñada para organizar archivos en función de su extensión, contenido, fecha, tamaño y otros criterios definidos por el usuario. Ya sea que quieras organizar tu colección de fotos, documentos o cualquier otro tipo de archivo, **FileOrganizer** te ayuda a mantener tu sistema ordenado y eficiente.

![Interfaz de usuario](https://github.com/albrinBuzz/FileOrganizer/assets/152460564/6f68256e-b196-43cc-8377-ca2de684792c)

## Características Principales

- **Organización por Extensión**: Organiza archivos según su tipo de extensión (por ejemplo, imágenes, documentos, música, etc.).
- **Organización Personalizada**: Permite combinar múltiples criterios de organización, como tipo, fecha, tamaño y nombre de archivo.
- **Soporte para Archivos Multimedia**: Organiza imágenes, videos y música basados en metadatos como fecha EXIF, fecha de grabación y metadatos ID3.
- **Automatización de Procesos**: Configura tareas automáticas para organizar archivos en intervalos específicos o cuando se detecta un archivo nuevo.
- **Interfaz Gráfica de Usuario (GUI)**: Ofrece una interfaz fácil de usar para previsualizar y gestionar los archivos antes de organizarlos.
- **Detección de Archivos Duplicados**: La aplicación detecta archivos duplicados y te permite gestionar su eliminación o archivo en una carpeta específica.

## Requisitos

### Requisitos del Sistema

- **JDK 17** o superior.
   - Asegúrate de tener instalado el JDK (Java Development Kit) versión 17 o superior.
   - Puedes verificar tu versión de Java con el siguiente comando:
    ```bash
    java -version
    ```

- **JavaFX**: FileOrganizer utiliza JavaFX para la interfaz gráfica. Si no lo tienes instalado, sigue las instrucciones a continuación.

### Requisitos de Software

1. **Java Development Kit (JDK) 17 o superior**:
   - Si no tienes JDK instalado, puedes obtenerlo desde [AdoptOpenJDK](https://adoptopenjdk.net/) o [Oracle](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html).

2. **JavaFX**:
   - **JavaFX** es necesario para la interfaz gráfica de usuario (GUI). Si tienes JDK 17, JavaFX puede ser una dependencia externa que necesitarás agregar.
   - Descarga JavaFX desde [Gluon](https://gluonhq.com/products/javafx/).
   - Configura **JavaFX** añadiendo el SDK en el **CLASSPATH** y ajusta las variables de entorno según tu sistema operativo.

### Instalación de Dependencias con Maven

Si usas **Maven** para gestionar las dependencias, asegúrate de tener Maven instalado. Si no lo tienes, sigue estas instrucciones:

- Descarga Maven desde [Maven](https://maven.apache.org/download.cgi).
- Instala Maven según el sistema operativo que uses.

Una vez que Maven esté instalado, puedes proceder a instalar las dependencias del proyecto y compilarlo.

## Instalación y Ejecución

### Paso 1: Clonar el repositorio

Primero, clona el repositorio de GitHub a tu máquina local. Si aún no tienes Git instalado, puedes obtenerlo desde [aquí](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git).

```bash
git clone https://github.com/tu_usuario/FileOrganizer.git
cd FileOrganizer
mvn clean install
mvn exec:java
mvn package
java -jar target/FileOrganizer.jar
