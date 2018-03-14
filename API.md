# One Control Home - API REST System

## Acerca de nuestra API:
Lo que se encuentra en nuestra API Rest es información sobre nuestros usuarios, tanto administradores como clientes y sus datos adjuntos como nombres, apellidos, telefono..., los productos con sus características, precio y cantifades, las casas con sus direcciones, analíticas de consumos, facturas de los clientes y ordenes de pedidos.

## Cómo usar nuestra API:
-   Descargar Postman.
-  Solo puede enviar solicitudes GET, POST, PUT y DELETE.
-  Nuestra API tiene un lado público donde puedes ver la información de quienes somos, lo que hacemos y nuestros productos. 
    Dos partes privadas. La primera como cliente registrado puediendo ver tus facturas, graficas, consumos y pedidos, para ello debes iniciar sesión como usuario. Una segunda parte privada que es como administrador, que tienes acceso a los pedidos y a las características de los usuarios, para ello tienes que registrarte con los permisos de administrador
-  Una vez que haya iniciado sesión o sin iniciarla, puede enviar todas las solicitudes que desee (siempre siguiendo nuestras reglas).
-  Si quieres terminar tu sesión, simplemente cierra sesión en la API.

## Solicitudes API:
### Productos:
La API de productos tiene métodos GET, - POST, - PUT
 
El usuario no registrado, solo puede enviar solicitudes GET. 
Igual que el usuario registrado, solo puede enviar solicitudes GET. 
Un usuario administrador, puede enviar cualquier solicitud de las opciones posibles de Productos.
Todas las URL de solicitud se pueden enviar escribiendo http://localhost:8443 seguido de la URL de solicitud contenida en las siguientes tablas.

### Métodos GET

| Tipo | Solicitud de descripción | URL de solicitud | Respuesta de éxito | Respuesta de error |
| --- | --- | --- | --- | --- |
| 1 | Muestra todos los productos. | /api/products | Lista de productos y OK (200). | NOT_FOUND (404) |
| 2 | Muestra un recurso determinado. | /api/products/id | Producto y OK (200). | NOT_FOUND (404) |
borrar| 3 | Muestra la imagen del recurso (en Base64 String) | / api / files / resource / id | Base64 String y OK (200). | NOT_FOUND (404) |

[1]
````
[
    {
        "id": 1,
        "description": "Actuador de bombilla para domótica.  Así, podrás subir o bajar las persianas desde la App, ya sea desde dispositivos móviles, ordenador o incluso hacer que estas persianas se bajen de forma automática.",
        "cost": 15.5,
        "type": "LIGHT",
        "img": "product-1.jpg",
        "stock": 36
    },
    {
        "id": 2,
        "description": "Motor actuador de persiana para domótica. Así, podrás subir o bajar las persianas desde la App, ya sea desde dispositivos móviles, ordenador o incluso hacer que estas persianas se bajen de forma automática.",
        "cost": 32.5,
        "type": "BLIND",
        "img": "product-2.jpg",
        "stock": 34
    },
    {
        "id": 3,
        "description": "Raspberry pi programada para domótica. Así, podrás actuar desde la App, ya sea desde dispositivos móviles, ordenador sobre los diferentes elementos domóticos.",
        "cost": 32.5,
        "type": "RASPBERRYPI",
        "img": "product-3.jpg",
        "stock": 67
    }
]
``
[2]	
```
{
    "id": 1,
    "description": "Actuador de bombilla para domótica.  Así, podrás subir o bajar las persianas desde la App, ya sea desde dispositivos móviles, ordenador o incluso hacer que estas persianas se bajen de forma automática.",
    "cost": 15.5,
    "type": "LIGHT",
    "img": "product-1.jpg",
    "stock": 36
}```

### Método POST
| Tipo | Solicitud de descripción |	URL de solicitud | Solicitar cuerpo | Respuesta de éxito | Respuesta de error |
| --- | --- | --- | --- | --- | --- |
| 1 | Crea un nuevo producto. | /api/adminDashboard/inventory/id | Vea abajo |	Nuevo recurso y CREADO (201) | NOT_FOUND (404)
[1]
```
{
    "timestamp": 1521046055861,
    "status": 405,
    "error": "Method Not Allowed",
    "exception": "org.springframework.web.HttpRequestMethodNotSupportedException",
    "message": "Request method 'POST' not supported",
    "path": "/api/adminDashboard/inventory/1"
}
```

### Método PUT
| Tipo | Solicitud de descripción |	URL de solicitud | Solicitar cuerpo | Respuesta de éxito | Respuesta de error |
| --- | --- | --- | --- | --- | --- |
| 1 | Modifica un recurso existente (puede modificar atributos limitados como precio, cantidad, descripción e imagen). | /api/adminDashboard/inventory | Vea abajo | Recurso modificado y OK (200) | NOT_FOUND (404) |
[1] (ejemplo usando ID 4)
``
{
    "timestamp": 1521046668016,
    "status": 400,
    "error": "Bad Request",
    "exception": "org.springframework.http.converter.HttpMessageNotReadableException",
    "message": "Required request body is missing: public void daw.spring.restcontroller.AdminDashboardRestController.editProduct(long,daw.spring.model.Product)",
    "path": "/api/adminDashboard/inventory/4"
}
```