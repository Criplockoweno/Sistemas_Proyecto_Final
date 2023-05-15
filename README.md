# Sistemas_Proyecto_Final
Integrantes:
- Sthefano Ulloa (00215689)
- Alexander Pastor (00211253)
- Sebastián Romero (00216765)
- Kristian Mendoza (00215773)

## Descripción del proyecto
El proyecto consiste en la creación de un sistema de manejo de subastas, en el cual se pueden crear subastas, pujar por ellas y ver el historial de pujas. El sistema está hecho con el patrón de diseño Observer, en el cual se tiene un sujeto que es la subasta y los observadores que son los usuarios que pujan por la subasta. El sistema está hecho en Java y se utiliza serversocket para la comunicación entre el cliente y el servidor.

## Instrucciones de uso
Para su ejecución, es necesario que haya primero un servidor en curso. Para ello, primero se ejecuta en consola el archivo Server, seguido de los clientes con el archivo Client.

En el caso del Server, este recibe notificaciones de las actividades que se llevan a cabo por los clientes, ya sea desde el registro de un nuevo cliente hasta las pujas que se llevan a cabo dentro de dicha sesión. Para cada cliente se tiene un menú con el cuál puede acceder a diferentes acciones sobre las sesiones de subasta existentes (las cuales fueron establecidad manualmente por código, siendo ese el único medio actual para establecerlas). 

El cliente puede suscribirse a una sesión de subasta y desuscribirse de una aplicando solamente el ID de la subasta que se muestra en pantalla, ademas que puede navegar entre las diferentes pantallas presionando el digito que este acorde a las opciones ofrecidas por el sistema. En otras palabras, el cliente si quiere ver sus sucripciones presiona la tecla 1 en el menu principal, por ejemplo, y para retroceder a un pagina previa escribe "exit", de otro modo el sistema no lo reconoce o dara mensaje de error en caso de que no este acorde a a las opciones dadas.

Para cada subasta, existe un tiempo límite para pujar, de modoq eu se notifica a los usuarios cuando entran a la sesión que hay un tiempo restante de la misma. En un inicio, se notifica el tiempo restante cada minuto que pasa. En caso de que falte menos de un minuto se notificada cada 10 segundos a los clientes, y finalmente cada segundo faltando solo 10 segundos para que temrine la sesión.

Finalmente, se puede ver las sesiones terminadas accediendo a la ocpión respectiva en el menú, en la cual se ingresa el ID y se muestra el resultado final, indicando al cliente ganador del artículo con su monto de puja final, siendo esto aplicado principalmente para las sesiones en las que el cliente estaba suscrito. 

## Contribuciones
Sebastián Romero: Codificación de comunicación Server-Cliente + manejo de hilos de subasta + mejoras en código e implementación.
Alexander Pástor: Creación de diagramas robustos del proyecto, además de las descripciones de casos de uso.
Stefano Ulloa: creación del diagrama de casos de uso y la descripción de los mismos.
