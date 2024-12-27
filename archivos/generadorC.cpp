#include <iostream>
#include <fstream>
#include <filesystem>
#include <map>
#include <vector>
#include <random>
#include <ctime>
#include <cstdlib>

namespace fs = std::filesystem;

// Estructura que mapea tipos de archivo a sus extensiones
std::map<std::string, std::vector<std::string>> extensions = {
    {"text", {".txt", ".rtf", ".md", ".nfo", ".log", ".ini", ".cfg", ".bat", ".cmd", ".doc", ".docx", ".odt", ".pages", ".wps", ".wks", ".docm", ".dotx", ".dotm"}},
    {"image", {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".svg", ".ico", ".tif", ".tiff", ".webp", ".psd", ".tga", ".eps", ".cdr"}},
    {"audio", {".mp3", ".wav", ".ogg", ".flac", ".aac", ".wma", ".m4a", ".opus", ".mid", ".midi", ".dsf", ".dffs"}},
    {"video", {".mp4", ".avi", ".mov", ".mkv", ".wmv", ".flv", ".webm", ".mpeg", ".mpg", ".3gp", ".prores", ".dnxhd"}},
    {"document", {".pdf", ".epub", ".mobi", ".azw3", ".azw4", ".cbz", ".cbr", ".xls", ".xlsx"}},
    {"spreadsheet", {".csv", ".ods", ".ots", ".sxc", ".stc"}},
    {"presentation", {".ppt", ".pptx", ".odp", ".pps", ".potx", ".pot", ".pptm", ".thmx"}},
    {"compressed", {".zip", ".rar", ".7z", ".tar.gz", ".tar", ".gz", ".bz2", ".xz", ".rar5", ".zpaq", ".pea"}},
    {"sourceCode", {".java", ".cpp", ".c", ".py", ".html", ".css", ".js", ".php", ".rb", ".swift", ".go", ".cs", ".vb", ".asm", ".pas", ".sql", ".db", ".sqlite", ".db3", ".accdb", ".mdb"}},
    {"executable", {".exe", ".jar", ".sh", ".bat", ".cmd", ".app", ".run", ".msi", ".com", ".vb", ".apk", ".ipa", ".elf", ".bin", ".out", ".exe.gz", ".exe.bz2", ".deb", ".rpm", ".apk", ".ipa", ".app", ".dmg", ".pkg", ".mpkg"}},
    {"config", {".xml", ".json", ".properties", ".ini", ".cfg", ".yaml", ".yml", ".env"}}
};

// Función para seleccionar un tipo aleatorio de archivo
std::string selectRandomType() {
    std::vector<std::string> types;
    for (const auto& kv : extensions) {
        types.push_back(kv.first);
    }
    int index = rand() % types.size();
    return types[index];
}

// Función para seleccionar una extensión aleatoria del tipo dado
std::string selectRandomExtension(const std::string& tipo) {
    const auto& exts = extensions[tipo];
    int index = rand() % exts.size();
    return exts[index];
}

// Función para crear un archivo con el tamaño especificado
void createFile(const std::string& fileName, int size) {
    std::ofstream file(fileName, std::ios::binary);
    if (!file) {
        std::cerr << "Error al crear el archivo: " << fileName << std::endl;
        return;
    }

    // Escribir el tamaño necesario (archivos vacíos con contenido mínimo)
    file.write(std::string(size, '\0').c_str(), size);
    file.close();
}

int main() {
    // Configuración del directorio de destino y la cantidad de archivos a generar
    std::string directorio = "./pruebaChica";  // Ruta al directorio donde se crearán los archivos
    int numArchivos = 30000;            // Número de archivos a crear
    int tamanoArchivo = 1;                // Tamaño de cada archivo en bytes (1 KB)

    // Crear el directorio si no existe
    if (!fs::exists(directorio)) {
        try {
            fs::create_directories(directorio);
        } catch (const fs::filesystem_error& e) {
            std::cerr << "Error al crear el directorio: " << e.what() << std::endl;
            return 1;
        }
    }

    // Semilla para el generador de números aleatorios
    srand(static_cast<unsigned int>(time(0)));

    // Generación de archivos
    for (int i = 0; i < numArchivos; ++i) {
        // Seleccionar un tipo de archivo aleatorio
        std::string tipo = selectRandomType();
        // Seleccionar una extensión aleatoria del tipo seleccionado
        std::string extension = selectRandomExtension(tipo);
        // Crear el archivo
        std::string fileName = directorio + "/file_" + std::to_string(rand()) + extension;
        createFile(fileName, tamanoArchivo);
    }

    std::cout << "Se han creado " << numArchivos << " archivos en " << directorio << "." << std::endl;
    return 0;
}
