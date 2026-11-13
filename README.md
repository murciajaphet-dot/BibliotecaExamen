Sistema de Gestión de Biblioteca - Examen POO

* Nombre:** Michael Japhet Murcia Maldonado
* Número de Cuenta:** 202410011214

Descripción del Proyecto
Aplicación de escritorio desarrollada en Java con Maven, arquitectura por capas y persistencia en SQLite.

Instrucciones para Ejecutar
1. Clonar o descargar este repositorio en su computadora.
2. Abrir el proyecto en **NetBeans** (o su IDE con soporte Maven).
3. Esperar a que Maven descargue las dependencias necesarias (`sqlite-jdbc`).
4. Ejecutar la aplicación. El archivo de base de datos (`biblioteca.db`) se generará automáticamente en la raíz.


Evidencias de Ejecución (Operaciones CRUD)

1. Registro de Autor
![Registro de Autor](capturas/Registro%20de%20un%20Autor.png)

2. Registro de Libro
![Registro de Libro](capturas/Registro%20de%20un%20Libro.png)

3. Listado General de Datos
![Listado de Datos](capturas/Listado%20de%20datos.png)


Validación de Reglas de Negocio

1. Restricción de Integridad Referencial (No eliminar autor con libros)
![Validación de Autor con Libros](capturas/Validación%20de%20Regla%20de%20Negocio%20Restricción%20de%20eliminación%20de%20autores%20con%20libros%20asociados.png)

2. Restricción de Unicidad (Rechazo de ISBN duplicado)
![Validación de ISBN Duplicado](capturas/ISBN%20repetido.png)