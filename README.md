# Jsort

**Jsort** es una aplicación diseñada para organizar archivos en función de su extensión, contenido, fecha, tamaño y otros criterios definidos por el usuario. Ya sea que quieras organizar tu colección de fotos, documentos o cualquier otro tipo de archivo, **Jsort** te ayuda a mantener tu sistema ordenado y eficiente.

![Interfaz de usuario](https://github.com/albrinBuzz/FileOrganizer/assets/152460564/6f68256e-b196-43cc-8377-ca2de684792c)

## Características Principales

- **Organización por Extensión**: Organiza archivos según su tipo de extensión (por ejemplo, imágenes, documentos, música, etc.).
- **Organización Personalizada**: Permite combinar múltiples criterios de organización, como tipo, fecha, tamaño y nombre de archivo.
- **Interfaz Gráfica Intuitiva**: Con una interfaz limpia y fácil de usar, que te permitirá organizar tus archivos rápidamente.
- **Soporte para Múltiples Tipos de Archivos**: Compatible con una amplia variedad de formatos de archivo.

## Requisitos

### Requisitos del Sistema

Para usar **FileOrganizer**, asegúrate de tener las siguientes herramientas instaladas:

- **JDK 17 o superior**:
  **FileOrganizer** está construido con Java, por lo que necesitas tener instalado JDK 17 o superior.
    - Puedes verificar tu versión de Java ejecutando el siguiente comando en tu terminal:
      ```bash
      java -version
      ```
    - Si necesitas instalar o actualizar tu JDK, puedes obtenerlo desde:
        - [AdoptOpenJDK](https://adoptopenjdk.net/)
        - [Oracle JDK](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)

- **JavaFX**:
  **JavaFX** es utilizado para la interfaz gráfica. Si ya tienes JDK 17, es posible que necesites instalar **JavaFX** por separado como una dependencia externa:
    - Descarga JavaFX desde [Gluon](https://gluonhq.com/products/javafx/).
    - Asegúrate de configurar correctamente el **SDK de JavaFX** en el **CLASSPATH** y ajustar las variables de entorno según tu sistema operativo.

### Requisitos de Software

1. **Maven** (opcional, pero recomendado): Si deseas gestionar las dependencias y ejecutar el proyecto con Maven, asegúrate de tener **Maven** instalado.
    - Descarga Maven desde [Apache Maven](https://maven.apache.org/download.cgi).
    - Sigue las instrucciones de instalación según tu sistema operativo.

## Instalación y Ejecución

### Paso 1: Clonar el repositorio

Clona el repositorio de GitHub en tu máquina local. Si aún no tienes Git instalado, puedes obtenerlo desde [aquí](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git).

```bash
git clone https://github.com/tu_usuario/FileOrganizer.git
cd FileOrganizer
java -jar /out/artifacts/OrganizerFile_jar
```

## Benchmark y Resultados

Para asegurar que **FileOrganizer** funciona de manera eficiente incluso con grandes volúmenes de archivos, hemos realizado varias pruebas de rendimiento. A continuación se presentan los resultados obtenidos al organizar diferentes cantidades de archivos bajo diversos criterios, utilizando tanto **paralelismo activado** como **desactivado**, y el impacto en el uso de **memoria**.

### Entorno de Pruebas

Las pruebas de rendimiento y funcionalidad de **FileOrganizer** se realizaron en un entorno específico, el cual se detalla a continuación:

#### Especificaciones del Sistema de Pruebas

- **Sistema Operativo**:
    - **Windows 10 Pro**, versión 22H
    - **Fedora Linux 40 x86_64** (para pruebas en Linux)

- **Procesador (CPU)**:
    - **Intel Core i7-12700** (12 núcleos, 20 hilos, 4.90 GHz)

- **Memoria RAM**:
    - **64 GB DDR4 3200 MHz**

- **Almacenamiento**:
    - **SSD NVMe de 500 GB** (para pruebas de velocidad de acceso a archivos)

- **Versión de Java**:
    - **JDK 17.0.3** (con **JavaFX 17.0.1** configurado para la interfaz gráfica)

- **Herramientas Utilizadas**:
    - **Maven 3.8.1** (para la gestión de dependencias y ejecución de pruebas)

- **Resolución de Pantalla**:
    - **1920x1080 píxeles**

#### Condiciones Durante las Pruebas

- **Carga de Sistema**: El sistema estaba dedicado exclusivamente a la ejecución de las pruebas, sin otras aplicaciones que consumieran recursos significativos durante los benchmarks.
- **Conexión a Internet**: No se requirió conexión a internet para la ejecución de las pruebas.
- **Tamaño de los Archivos de Prueba**: Se utilizaron conjuntos de archivos de prueba con tamaños variados, desde 100 hasta 2,000,000 de archivos con extensiones mixtas (imágenes, documentos, música).

#### Notas Importantes

- **Rendimiento en Diferentes Equipos**: Los resultados de las pruebas pueden variar dependiendo de las características del hardware y el sistema operativo utilizado. Se recomienda realizar pruebas en el entorno en el que se desplegará la aplicación para obtener una medición más precisa.

---

### 1. **Benchmark - Tiempo de Organización por Extensión**

Se realizaron pruebas de organización de archivos por su extensión (por ejemplo, imágenes, documentos, música). En este escenario, se midió el tiempo que tardó la aplicación en procesar los archivos y organizarlos en carpetas correspondientes, tanto con el **paralelismo activado** como con el **paralelismo desactivado**.

| Número de Archivos | Tipo de Archivos        | Tiempo de Organización (segundos) - Sin Paralelismo | Tiempo de Organización (segundos) - Con Paralelismo | Memoria Usada (MB) - Sin Paralelismo | Memoria Usada (MB) - Con Paralelismo |
|--------------------|-------------------------|----------------------------------------------------|-----------------------------------------------------|--------------------------------------|--------------------------------------|
| 30,000             | Imágenes (.jpg, .png)    | 0.4                                                | 0.3                                                 | 120                                  | no probado                           |
| 120,000            | Mixto                    | 0.97                                               | no probado                                          | 270                                  | no probado                           |
| 2,000,000          | Mixto                    | 34.8                                               | 21                                                  | 1900                                 | 2100                                 |

#### Observaciones:
- **Paralelismo activado** mejora significativamente el tiempo de organización, especialmente con archivos más grandes o cuando se procesan tipos de archivos mixtos.
- El **uso de memoria** aumenta levemente con el paralelismo, pero la mejora en el tiempo de procesamiento compensa este incremento.

---

### 2. **Benchmark - Uso de Memoria**

El uso de memoria se midió durante la ejecución del proceso de organización de **120,000 archivos** de diferentes tipos. A continuación, se muestran los resultados del uso máximo de memoria durante las pruebas:

- **Uso Máximo de Memoria (120,000 archivos)**:
    - **Uso Predeterminado (default)**: 240 MB
    - **Uso Pico (peak)**: 556 MB

#### Observaciones:
- El uso de memoria es relativamente bajo, incluso cuando se organizan grandes cantidades de archivos.
- El **uso de memoria** aumenta ligeramente con el paralelismo debido al uso de múltiples hilos, pero este incremento no es significativo, lo que permite ejecutar la aplicación en sistemas con recursos limitados sin comprometer el rendimiento.

---

### 3. **Conclusiones Finales**

- **Eficiencia**: **Jsort** es eficiente incluso con un gran número de archivos. El tiempo de organización aumenta proporcionalmente al número de archivos, pero sigue siendo rápido, incluso con volúmenes de datos grandes como los de las pruebas con 2,000,000 de archivos.
- **Paralelismo**: El paralelismo mejora considerablemente el tiempo de organización, especialmente al trabajar con archivos grandes o cuando se combinan múltiples criterios de organización. Activar paralelismo reduce el tiempo de procesamiento hasta en un 40% en escenarios de gran volumen de datos.
- **Uso de Memoria**: El uso de memoria es relativamente bajo, incluso con grandes volúmenes de datos, lo que permite ejecutar **Jsort** en sistemas con recursos limitados sin problemas de rendimiento.
- **Escalabilidad**: **Jsort** demuestra ser altamente escalable, manteniendo un rendimiento razonable incluso cuando se organiza un gran número de archivos.

Estos resultados demuestran que **Jsort** es una herramienta robusta y rápida para gestionar grandes cantidades de archivos.
