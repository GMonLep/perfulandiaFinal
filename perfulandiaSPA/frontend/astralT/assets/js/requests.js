// PARA IR CAMBIANDO ENTRE MENUS
const tabs = {
    agregar: document.getElementById("formularioPerfume"),
    buscar: document.getElementById("formularioBuscar"),
    eliminar: document.getElementById("formularioEliminar"),
    actualizar: document.getElementById("formularioActualizar"),
};

const buttons = {
    agregar: document.getElementById("tab-agregar"),
    buscar: document.getElementById("tab-buscar"),
    eliminar: document.getElementById("tab-eliminar"),
    actualizar: document.getElementById("tab-actualizar"),
};

function switchTab(tabName) {
    Object.keys(tabs).forEach(key => {
        tabs[key].classList.toggle("hidden", key !== tabName);
        buttons[key].classList.toggle("bg-cyan-800", key === tabName);
        buttons[key].classList.toggle("text-white", key === tabName);
        buttons[key].classList.toggle("border", key !== tabName);
    });
}

buttons.agregar.addEventListener("click", () => switchTab("agregar"));
buttons.buscar.addEventListener("click", () => switchTab("buscar"));
buttons.eliminar.addEventListener("click", () => switchTab("eliminar"));
buttons.actualizar.addEventListener("click", () => switchTab("actualizar")); //

// agregar va por default
switchTab("agregar");

const listaCompletaProducto = document.getElementById('listaProductos');


//MOSTRAR PERFUMES DE LA BD
fetch('https://perfulandiafinal-production.up.railway.app/api/productos')
    .then(response => response.json())
    .then(data => {
        const list = document.getElementById('listaProductos');

        data.forEach(producto => {
            const li = document.createElement('li');
            li.className = "p-6 border rounded-xl shadow-sm bg-white hover:shadow-md transition list-none";
            li.innerHTML = `
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-2">
          <div>
            <p class="text-sm text-gray-500">SKU: <span class="font-medium text-gray-700">${producto.productoId}</span></p>
            <h2 class="text-lg font-semibold text-gray-800">${producto.nombre}</h2>
          </div>
          <div class="text-right ">
            <p class="text-sm text-gray-600 ">Precio: <span class="font-medium text-green-600">$${producto.precio}</span></p>
            <p class="text-sm text-gray-600">Stock: <span class="font-medium text-blue-600">${producto.stock}</span></p>
          </div>
        </div>
      `;
            list.appendChild(li);
        });
    })
    .catch(error => console.error('ERROR BUSCANDO PRODUCTOS:', error));

//AGREGAR PERFUME DE LA BD
document.getElementById("formularioPerfume").addEventListener("submit", function(e) {
    e.preventDefault();

    const nombre = document.getElementById("nombre").value.trim();
    const precio = parseFloat(document.getElementById("precio").value);
    const stock = parseInt(document.getElementById("stock").value);

    if (!nombre || isNaN(stock) || stock < 0 || isNaN(precio) || precio <= 0) return;

    const perfumeData = { nombre, stock, precio };

    fetch('https://perfulandiafinal-production.up.railway.app/api/productos', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(perfumeData),
    })
        .then(response => response.json())
        .then(data => {
            document.getElementById("formularioPerfume").reset();
            Toastify({
                text: "¡Perfume agregado con éxito!",
                backgroundColor: "#4CAF50",
                duration: 3000
            }).showToast();
        })
        .catch(error => {
            Toastify({
                text: "Error al agregar el perfume.",
                backgroundColor: "#FF6347",
                duration: 3000
            }).showToast();
        });
});

//BUSCAR PRODUCTO
//Declarar cada id del HTML y almacenarlo en una variable

const searchBtn = document.getElementById('searchBtn');
const searchInput = document.getElementById('searchInput');
const loader = document.getElementById('loader');
const results = document.getElementById('results');


searchBtn.addEventListener('click', () => {
    const query = searchInput.value.trim();
    if (query !== '') {
        listaCompletaProducto.classList.add('hidden');
        results.classList.remove('hidden');
        buscarProducto(query);
    } else {
        listaCompletaProducto.classList.remove('hidden');
        results.classList.add('hidden');
    }
});

function buscarProducto(idProducto) {
    loader.classList.remove('hidden');
    results.innerHTML = '';

    const url = `https://perfulandiafinal-production.up.railway.app/api/productos/buscar/${encodeURIComponent(idProducto)}`;

    fetch(url)
        .then(async response => {
            if (!response.ok) {y
                const text = await response.text();
                throw new Error(text || 'Producto no encontrado');
            }

            const contentType = response.headers.get('content-type') || '';
            if (!contentType.includes('application/json')) {
                throw new Error('No se ha encontrado perfume con ese ID');
            }

            return response.json();
        })
        .then(perfumeData => {
            mostrarResultados([perfumeData]);
        })
        .catch(error => {
            results.innerHTML = `<p class="text-red-600">Error: ${error.message}</p>`;
        })
        .finally(() => {
            loader.classList.add('hidden');
        });
}



function mostrarResultados(producto){
    if(producto.length===0){
        results.innerHTML = `<p class="text-red-600">No se encontraron perfumes</p>`;
        return
    }

    producto.forEach(producto => {
        const li = document.createElement('li');
        li.className = "p-6 border rounded-xl shadow-sm bg-white hover:shadow-md transition list-none";
        li.innerHTML = `
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-2">
          <div>
            <p class="text-sm text-gray-500">SKU: <span class="font-medium text-gray-700">${producto.productoId}</span></p>
            <h2 class="text-lg font-semibold text-gray-800">${producto.nombre}</h2>
          </div>
          <div class="text-right ">
            <p class="text-sm text-gray-600 ">Precio: <span class="font-medium text-green-600">$${producto.precio}</span></p>
            <p class="text-sm text-gray-600">Stock: <span class="font-medium text-blue-600">${producto.stock}</span></p>
          </div>
        </div>
      `;

        results.appendChild(li);
    });

}

//ELIMINAR PRODUCTO

const eliminarForm = document.getElementById('formularioEliminar');
const eliminarInput = document.getElementById('eliminarId');

eliminarForm.addEventListener('submit', (e) => {
    e.preventDefault(); // prevent form from reloading the page
    const id = eliminarInput.value.trim();
    if (id !== '') {
        eliminarProducto(id);
        eliminarInput.value = ''; // clear input
    }
});

function eliminarProducto(idProducto) {
    const url = `https://perfulandiafinal-production.up.railway.app/api/productos/${encodeURIComponent(idProducto)}`;

    if (!confirm(`¿Estás seguro de que quieres eliminar el producto con ID ${idProducto}?`)) {
        return;
    }

    fetch(url, {
        method: 'DELETE'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('No se pudo eliminar el producto. Puede que no exista.');
            }
            alert(`ID ${idProducto} eliminado exitosamente.`);
            // Optionally refresh the product list or clear fields
        })
        .catch(error => {
            alert(`Error al eliminar producto: ${error.message}`);
        });
}

//ACTUALIZAR PRODUCTO

const updateForm = document.getElementById('formularioActualizar');

updateForm.addEventListener('submit', (e) => {
    e.preventDefault();

    const id = document.getElementById('actualizarId').value.trim();
    const nombre = document.getElementById('nuevoNombre').value.trim();
    const precio = document.getElementById('nuevoePrecio').value.trim();
    const stock = document.getElementById('nuevoStock').value.trim();

    const campos = {};
    if (nombre !== '') campos.nombre = nombre;
    if (precio !== '') campos.precio = Number(precio);
    if (stock !== '') campos.stock = Number(stock);

    if (id && Object.keys(campos).length > 0) {
        actualizarProducto(id, campos);
        updateForm.reset();
    } else {
        alert('Ingresa un ID y campos válido');
    }
});

function actualizarProducto(idProducto, campos) {
    const url = `https://perfulandiafinal-production.up.railway.app/api/productos/${encodeURIComponent(idProducto)}`;

    fetch(url, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(campos)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('No se pudo actualizar el producto.');
            }
            return response.json();
        })
        .then(data => {
            alert(`Producto actualizado`);

        })
        .catch(error => {
            alert(`Error al actualizar: ${error.message}`);
        });
}

//cambiar usuario
const userTabs = {
    guardar: document.getElementById("formularioGuardarUsuario"),
    actualizar: document.getElementById("formularioActualizarUsuario"),
    eliminar: document.getElementById("formularioEliminarUsuario"),
    buscar: document.getElementById("formularioBuscarUsuario"),
};

const userButtons = {
    guardar: document.getElementById("tab-guardar-user"),
    actualizar: document.getElementById("tab-actualizar-user"),
    eliminar: document.getElementById("tab-eliminar-user"),
    buscar: document.getElementById("tab-buscar-user"),
};

function switchUserTab(tabName) {
    Object.keys(userTabs).forEach(key => {
        userTabs[key].classList.toggle("hidden", key !== tabName);
        userButtons[key].classList.toggle("bg-cyan-800", key === tabName);
        userButtons[key].classList.toggle("text-white", key === tabName);
        userButtons[key].classList.toggle("border", key !== tabName);
    });
}

userButtons.guardar.addEventListener("click", () => switchUserTab("guardar"));
userButtons.actualizar.addEventListener("click", () => switchUserTab("actualizar"));
userButtons.eliminar.addEventListener("click", () => switchUserTab("eliminar"));
userButtons.buscar.addEventListener("click", () => switchUserTab("buscar"));

switchUserTab("guardar");

//MOSTRAR USUARIOS DE LA BD
fetch('https://resilient-tranquility-usuarios.up.railway.app/api/usuarios')
    .then(response => response.json())
    .then(data => {
        const list = document.getElementById('listaUsuarios');

        data.forEach(usuario => {
            const li = document.createElement('li');
            li.className = "p-6 border rounded-xl shadow-sm bg-white hover:shadow-md transition list-none";
            li.innerHTML = `
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-2">
          <div>
            <p class="text-sm text-gray-500">ID: <span class="font-medium text-gray-700">${usuario.id}</span></p>
            <h2 class="text-lg font-semibold text-gray-800">${usuario.nombre}</h2>
          </div>
          <div class="text-right ">
            <p class="text-sm text-gray-600 ">Correo: <span class="font-medium text-green-600">${usuario.correo}</span></p>
            <p class="text-sm text-gray-600">Rol: <span class="font-medium text-blue-600">${usuario.rol}</span></p>
          </div>
        </div>
      `;
            list.appendChild(li);
        });
    })
    .catch(error => console.error('ERROR BUSCANDO USUARIOS:', error));

//AGREGAR usuario DE LA BD
document.getElementById("guardarUsuario").addEventListener("submit", function(e) {
    e.preventDefault();

    const nombre = document.getElementById("username").value.trim();
    const correo = document.getElementById("correo").value.trim();
    const rol = document.getElementById("rol").value.trim();

    if (!nombre || !correo || !rol) return;

    const usuarioData = { nombre, correo, rol };

    fetch('https://resilient-tranquility-usuarios.up.railway.app/api/usuarios', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(usuarioData),
    })
        .then(async response => {
            if (!response.ok) throw new Error('Error al guardar usuario');

            const text = await response.text();
            if (text) return JSON.parse(text);
            return {};
        })
        .then(() => {
            document.getElementById("guardarUsuario").reset();
            Toastify({
                text: "¡Usuario agregado con éxito!",
                backgroundColor: "#4CAF50",
                duration: 3000
            }).showToast();
        })
        .catch(error => {
            console.error("Error:", error);
            Toastify({
                text: "Error al agregar el usuario.",
                backgroundColor: "#FF6347",
                duration: 3000
            }).showToast();
        });
});

//actualizar usuario
const updateUserForm = document.getElementById('formularioActualizarUsuario');

updateUserForm.addEventListener('submit', (e) => {
    e.preventDefault();

    const id = document.getElementById('idActUsuario').value.trim();
    const nombre = document.getElementById('nuevoUsername').value.trim();
    const correo = document.getElementById('nuevoCorreo').value.trim();
    const rol = document.getElementById('nuevoRol').value.trim();

    const campos = {};
    if (nombre !== '') campos.nombre = nombre;
    if (correo !== '') campos.correo = correo;
    if (rol !== '') campos.rol = rol;

    if (id && Object.keys(campos).length > 0) {
        actualizarUsuario(id, campos);
        updateUserForm.reset(); // ✅ works safely now
    } else {
        alert('Ingresa un ID y campos válido');
    }
});

function actualizarUsuario(idActUsuario, campos) {
    const url = `https://resilient-tranquility-usuarios.up.railway.app/api/usuarios/${encodeURIComponent(idActUsuario)}`;

    fetch(url, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(campos)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('No se pudo actualizar el Usuario.');
            }
            return response.json();
        })
        .then(data => {
            alert(`Usuario actualizado`);
        })
        .catch(error => {
            alert(`Error al actualizar: ${error.message}`);
        });
}


