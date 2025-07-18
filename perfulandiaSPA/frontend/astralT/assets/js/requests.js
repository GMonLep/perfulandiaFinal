fetch('https://perfulandiafinal-production.up.railway.app/api/productos')
    .then(response => response.json())
    .then(data => {
        const list = document.getElementById('listaProductos');
        data.forEach(producto => {
            const li = document.createElement('li');
            li.textContent = `SKU: ${producto.productoId} - Nombre: ${producto.nombre} - Precio: $${producto.precio} - Stock: $${producto.stock}`;
            list.appendChild(li);
        });
    })
    .catch(error => console.error('ERROR BUSCANDO PRODUCTO', error));