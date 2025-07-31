En 8082 esta h2 para acceder
```
jdbc:h2:tcp://localhost:1521/tpiDb
```

Para levantar docker:
```
docker compose up -d
```

Verificar que se esté ejecutando y la direccion:
```
docker ps
```

Dirección de keycloak:
[http://localhost:8081/](http://localhost:8081/)

Para apagar
```
docker compose down
```

El archivo _docker-compose.yml_ es el archivo de configuracion de docker, no tiene la linea de version porque es obsoleta y redundante.

API key: AIzaSyASfDzUpGOZIaK6G90vJgrrpZehI02TRhU

swagger: http://localhost:9000/swagger-ui.html

# Prueba llamada a API maps
En post http://localhost:9000/api/solicitudes con:
```
{
    "contenedor": {
        "id": 1
    },
    "ciudadOrigenId": 1,
    "ciudadDestinoId": 3,
    "depositoId": 1
}
```
Da "costoEstimado": 109669.2885, "tiempoEstimadoHoras": 8.693462499999999.
```
{
    "contenedor": {
        "id": 2
    },
    "ciudadOrigenId": 2,
    "ciudadDestinoId": 3,
    "depositoId": 1
}
```
Da "costoEstimado": 50681.0, "tiempoEstimadoHoras": 4.0

# 1. Registrar una nueva solicitud de transporte de contenedor.
Ver pruebas de llamada a la api de maps


# 2. Consultar el estado de una solicitud por parte del cliente.
Get http://localhost:9000/api/solicitudes/44

Devuelve un 200 con la solicitud recien creada
```
{
    "id": 44,
    "contenedor": {
        "id": 2,
        "peso": 12500.0,
        "volumen": 55.0,
        "estado": "EN_ORIGEN",
        "cliente": {
            "id": 3,
            "nombre": "Tech Solutions SRL",
            "email": "contacto@techsolutions.com",
            "password": "techpass"
        }
    },
    "ciudadOrigenId": 2,
    "ciudadDestinoId": 3,
    "depositoId": 1,
    "camionId": 1,
    "costoEstimado": 50681.0,
    "tiempoEstimadoHoras": 4.0
}
```
GET http://localhost:9000/api/solicitudes/39 en cambio da 404 porque no existe una solicitud con ese id

# 3. Consultar todos los contenedores pendientes de entrega.
GET http://localhost:9000/api/contenedores?estado=EN_ORIGEN

# 4. Registrar el avance del contenedor por parte del administrador.
PATCH http://localhost:9000/api/tramos/1/completar

PATCH http://localhost:9000/api/tramos/2/completar

Despues de completar los dos tramos este cambia de estado a entregado

GET http://localhost:9000/api/contenedores/1

# 5. Calcular el costo total de la entrega, incluyendo:
- Recorrido total (distancia entre ciudad origen → depósito y depósito → ciudad destino)
- Peso y volumen del contenedor
- Estadía en depósito (calculada a partir de la diferencia entre fechas reales de entrada y salida del tramo correspondiente)

PATCH http://localhost:9000/api/tramos/1/iniciar

PATCH http://localhost:9000/api/tramos/1/completar

PATCHhttp://localhost:9000/api/tramos/2/iniciar

PATCH http://localhost:9000/api/tramos/2/completar

POST http://localhost:9000/api/solicitudes/1/finalizar-costo

# 6. Registrar y actualizar ciudades, depósitos, camiones, contenedores y tarifas.

POST http://localhost:9000/api/ciudades
```
{
    "nombre": "Ejemplo",
    "latitud": -45.8895,
    "longitud": -12.8458
}
```
# 7. Validar que un camión no supere su capacidad máxima en peso ni volumen.