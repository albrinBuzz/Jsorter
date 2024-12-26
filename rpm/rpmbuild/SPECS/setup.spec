# Reemplaza "NombreDelPaquete" con el nombre deseado para el paquete
Name: Jsort
# Reemplaza "1.0" con la versión del paquete
Version: 1.0
# Reemplaza "1" con la revisión del paquete
Release: 1
License: Apache-2.0 license
Summary: Automated file organizer by extension for efficient folder management and sort
URL: https://github.com/albrinBuzz/Jsorter
Source0:        EasySorter.tar.gz
Source1:        Jsort.jar
Source2:        Jsort.desktop
Source3:        Jsort.png
Requires:       java-17
# Descripción detallada del paquete
%description
This software tool streamlines folder management by automatically organizing files based on their extensions. It simplifies file organization by creating subfolders categorized by file types, enhancing overall folder structure and accessibility.

# Comando para construir el paquete
%prep
# No es necesario hacer nada en esta sección si solo estás empaquetando un archivo .jar



# Comando para compilar o preparar el paquete (si es necesario)
%build
# No es necesario hacer nada en esta sección si solo estás empaquetando un archivo .jar

# Comando para instalar el paquete
%install
# Crear directorios de instalación
install -d %{buildroot}/opt/Jsort
install -d %{buildroot}/usr/share/applications
#copiar la imagen al directorio /share/icons
mkdir -p %{buildroot}/usr/share/icons/hicolor/48x48/apps/
cp -r %{_sourcedir}/Jsort.png %{buildroot}/usr/share/icons/hicolor/48x48/apps/

cp %{SOURCE1} %{buildroot}/opt/Jsort/

mkdir -p %{buildroot}/usr/bin
install -m 755 -d %{buildroot}/usr/bin/
ln -s /opt/Jsort/Jsort.jar %{buildroot}/usr/bin/Jsort



# Instalar archivos
install -m 644 %{SOURCE1} %{buildroot}/opt/Jsort/
install -m 644 %{SOURCE2} %{buildroot}/usr/share/applications/


# Establecer permisos
# Cambiar los permisos del directorio Jsort a 755
chmod -R 755 %{buildroot}/opt/Jsort
# Cambiar los permisos del archivo Jsort.jar a 644
chmod 644 %{buildroot}/opt/Jsort/Jsort.jar
# Cambiar los permisos del archivo Jsort.desktop a 644
chmod 655 %{buildroot}/usr/share/applications/Jsort.desktop
# Cambiar los permisos del directorio de iconos a 755
chmod 755 %{buildroot}/usr/share/icons/hicolor/48x48/apps/
# Cambiar los permisos del archivo de icono a 644
chmod 644 %{buildroot}/usr/share/icons/hicolor/48x48/apps/Jsort.png




# Comando para limpiar antes de construir el paquete
%clean
rm -rf %{buildroot}

# Comando para especificar los archivos incluidos en el paquete
%files
/opt/Jsort/Jsort.jar
/usr/share/applications/Jsort.desktop
/usr/share/icons/hicolor/48x48/apps/Jsort.png
/usr/bin/Jsort

%changelog
* Tue Apr 13 2024 cr <cris550@gmail.com> - 1.0-1
- Initial RPM release


