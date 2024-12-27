package main

import (
	"fmt"
	"math/rand"
	"os"
	"time"
)

// Estructura que mapea tipos de archivo a sus extensiones
var extensions = map[string][]string{
	"text":       {".txt", ".rtf", ".md", ".nfo", ".log", ".ini", ".cfg", ".bat", ".cmd", ".doc", ".docx", ".odt", ".pages", ".wps", ".wks", ".docm", ".dotx", ".dotm"},
	"image":      {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".svg", ".ico", ".tif", ".tiff", ".webp", ".psd", ".tga", ".eps", ".cdr"},
	"audio":      {".mp3", ".wav", ".ogg", ".flac", ".aac", ".wma", ".m4a", ".opus", ".mid", ".midi", ".dsf", ".dffs"},
	"video":      {".mp4", ".avi", ".mov", ".mkv", ".wmv", ".flv", ".webm", ".mpeg", ".mpg", ".3gp", ".prores", ".dnxhd"},
	"document":   {".pdf", ".epub", ".mobi", ".azw3", ".azw4", ".cbz", ".cbr", ".xls", ".xlsx"},
	"spreadsheet":{".csv", ".ods", ".ots", ".sxc", ".stc"},
	"presentation":{".ppt", ".pptx", ".odp", ".pps", ".potx", ".pot", ".pptm", ".thmx"},
	"compressed": {".zip", ".rar", ".7z", ".tar.gz", ".tar", ".gz", ".bz2", ".xz", ".rar5", ".zpaq", ".pea"},
	"sourceCode": {".java", ".cpp", ".c", ".py", ".html", ".css", ".js", ".php", ".rb", ".swift", ".go", ".cs", ".vb", ".asm", ".pas", ".sql", ".db", ".sqlite", ".db3", ".accdb", ".mdb"},
	"executable": {".exe", ".jar", ".sh", ".bat", ".cmd", ".app", ".run", ".msi", ".com", ".vb", ".apk", ".ipa", ".elf", ".bin", ".out", ".exe.gz", ".exe.bz2", ".deb", ".rpm", ".apk", ".ipa", ".app", ".dmg", ".pkg", ".mpkg"},
	"config":     {".xml", ".json", ".properties", ".ini", ".cfg", ".yaml", ".yml", ".env"},
}

func main() {
	// Configuración del directorio de destino y la cantidad de archivos a generar
	directorio := "./prueba" // Ruta al directorio donde se crearán los archivos
	numArchivos := 2000000    // Número de archivos a crear
	tamanoArchivo := 1     // Tamaño de cada archivo en bytes (1 KB)

	// Crear el directorio si no existe
	err := os.MkdirAll(directorio, os.ModePerm)
	if err != nil {
		fmt.Println("Error al crear el directorio:", err)
		return
	}

	// Semilla para el generador de números aleatorios
	rand.Seed(time.Now().UnixNano())

	// Generación de archivos
	for i := 0; i < numArchivos; i++ {
		// Seleccionar un tipo de archivo aleatorio
		tipo := selectRandomType()
		// Seleccionar una extensión aleatoria del tipo seleccionado
		extension := selectRandomExtension(tipo)
		// Crear el archivo
		fileName := fmt.Sprintf("%s/file_%d%s", directorio, rand.Int(), extension)
		createFile(fileName, tamanoArchivo)
	}

	fmt.Printf("Se han creado %d archivos en %s.\n", numArchivos, directorio)
}

// Función para seleccionar un tipo aleatorio de archivo
func selectRandomType() string {
	types := []string{}
	for t := range extensions {
		types = append(types, t)
	}
	return types[rand.Intn(len(types))]
}

// Función para seleccionar una extensión aleatoria del tipo dado
func selectRandomExtension(tipo string) string {
	exts := extensions[tipo]
	return exts[rand.Intn(len(exts))]
}

// Función para crear un archivo con el tamaño especificado
func createFile(fileName string, size int) {
	// Crear el archivo vacío
	file, err := os.Create(fileName)
	if err != nil {
		fmt.Println("Error al crear el archivo:", err)
		return
	}
	defer file.Close()

	// Escribir el tamaño necesario
	// Si el archivo es un archivo vacío, solo lo creamos con el tamaño especificado
	_, err = file.Write(make([]byte, size))
	if err != nil {
		fmt.Println("Error al escribir en el archivo:", err)
		return
	}
}
