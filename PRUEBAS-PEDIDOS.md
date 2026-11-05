# Prueba del flujo de pedidos

Usa una base de práctica con los datos iniciales de ventas.sql. El script elimina y vuelve a crear la base ventas. Si esa base ya tiene datos de clase que quieras conservar, utiliza una instancia de MySQL separada.

Con Java 21 y MySQL disponibles, arranca el proyecto desde la carpeta ventas con `./mvnw spring-boot:run`. Abre http://localhost:8080/pedidos.

1. En el listado inicial deben aparecer 16 pedidos. Abre el detalle del pedido 8. El cliente debe ser «Adrián Suárez» y el comercial «Manuel Domínguez Hernández».
2. Crea un pedido con total 42.50, fecha 2026-11-04, cliente Pilar Ruiz y comercial Juan Gómez López. Anota el ID nuevo. Comprueba los nombres y el importe en el listado y en su detalle.
3. Abre Editar y comprueba que aparecen seleccionadas esas dos personas y la fecha indicada. Cambia ambos selectores a Adrián Suárez y Marta Herrera Gil, cambia el importe a 55.75 y guarda. El detalle del mismo ID debe reflejar los tres cambios.
4. En el formulario de edición, usa el inspector del navegador para cambiar el valor de la opción seleccionada de cliente a 999999. Envía el formulario. Debes ver «Selecciona un cliente existente». Vuelve al detalle y comprueba que sigue figurando Adrián Suárez. Repite con un comercial inexistente. En alta, repite ambas comprobaciones y verifica que no apareció otro pedido.
5. Con ese pedido todavía presente, intenta eliminar a Adrián desde Clientes y a Marta desde Comerciales. Cada listado debe mostrar el aviso de pedidos asociados. Vuelve al detalle y comprueba que el pedido y ambos nombres siguen ahí.
6. Elimina únicamente el pedido nuevo desde Pedidos. Deben volver a quedar los 16 pedidos originales.
