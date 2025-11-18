(function () {
  // Crear el HTML del cuadro de diálogo con botones "Sí" y "No"
  const dialogoHTML = `
    <div id="confirmacionDlg" class="dialogo">
      <div class="contenido-dialogo">
        <p id="textoPregunta">Pregunta aquí</p>
        <div class="botones">
          <button id="botonSi">Sí</button>
          <button id="botonNo">No</button>
        </div>
      </div>
    </div>
    
    <div id="mensajeDlg" class="dialogo">
      <div class="contenido-dialogo">
        <p id="textoMensaje">Mensaje aquí</p>
        <div class="botones">
          <button id="botonAceptar" onclick="cerrarMensajeDlg()">Aceptar</button>
        </div>
      </div>
    </div>
  `;

  // Crear el CSS para el cuadro de diálogo
  const estiloCSS = `
    .dialogo {
      display: none;
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background-color: rgba(0, 0, 0, 0.5);
      z-index: 1000;
      justify-content: center;
      align-items: center;
      animation: fadeIn 0.3s ease-in-out;
    }
    .contenido-dialogo {
      background-color: #fff;
      border-radius: 12px;
      padding: 20px;
      width: 80%;
      max-width: 400px;
      box-shadow: 0px 4px 20px rgba(0, 0, 0, 0.2);
      text-align: center;
      animation: slideUp 0.3s ease-in-out;
    }
    .contenido-dialogo p {
      font-size: 18px;
      font-weight: bold;
      margin-bottom: 20px;
      color: #333;
    }
    .botones {
      display: flex;
      justify-content: space-evenly;
      gap: 15px;
    }
    .botones button {
      padding: 12px 25px;
      background-color: #4d94ff;
      color: white;
      border: none;
      border-radius: 8px;
      font-size: 16px;
      cursor: pointer;
      transition: background-color 0.3s, transform 0.2s;
    }
    .botones button:hover {
      background-color: #337ab7;
      transform: scale(1.05);
    }
    .botones button:active {
      transform: scale(0.98);
    }
    @keyframes fadeIn {
      from { opacity: 0; }
      to { opacity: 1; }
    }
    @keyframes slideUp {
      from { transform: translateY(20px); opacity: 0; }
      to { transform: translateY(0); opacity: 1; }
    }
  `;

  // Insertar el CSS en el documento
  const styleSheet = document.createElement("style");
  styleSheet.type = "text/css";
  styleSheet.innerText = estiloCSS;
  document.head.appendChild(styleSheet);

  // Insertar el HTML del cuadro de diálogo en el cuerpo de la página
  document.body.insertAdjacentHTML("beforeend", dialogoHTML);

  // Variable para almacenar la respuesta
  let respuestaUsuario = null;

  // Función para manejar la respuesta del usuario
  window.respuesta = function (valor) {
    respuestaUsuario = valor;
    document.getElementById("miDialogo").style.display = "none"; // Cerrar el cuadro de diálogo
  };
  /*
   * Mostrar dialogo de pregunta al usuario
   */
  // Función que muestra el cuadro de diálogo y espera la respuesta de manera simulada
  //Ejemplo de uso: let respuesta = await mostrarConfirmacion("Se ha cerrado la aplicacion en esta ventana", "Intenar volver a abrirla", "Cerrar");

  window.mostrarConfirmacion = function (
    textoPregunta,
    textoSi = "Sí",
    textoNo = "No"
  ) {
    return new Promise((resolve) => {
      // Establecer el texto de la pregunta y los botones
      document.getElementById("textoPregunta").textContent = textoPregunta;
      document.getElementById("botonSi").textContent = textoSi;
      document.getElementById("botonNo").textContent = textoNo;

      // Mostrar el cuadro de diálogo
      document.getElementById("confirmacionDlg").style.display = "flex";

      // Establecer las respuestas de los botones
      document.getElementById("botonSi").onclick = function () {
        respuestaUsuario = true;
        document.getElementById("confirmacionDlg").style.display = "none"; // Cerrar el cuadro de diálogo
        resolve(respuestaUsuario); // Resolver la promesa con true
      };
      document.getElementById("botonNo").onclick = function () {
        respuestaUsuario = false;
        document.getElementById("confirmacionDlg").style.display = "none"; // Cerrar el cuadro de diálogo
        resolve(respuestaUsuario); // Resolver la promesa con false
      };
    });
  };
  // Función para cerrar el cuadro de diálogo
  window.cerrarDialogoMensaje = function () {
    // Cerrar el cuadro de diálogo
    document.getElementById("mensajeDlg").style.display = "none";
  };
  /*
   * Mostrar dialogo con un mensaje al usuario
   */

  // Función que muestra el cuadro de diálogo y devuelve una promesa
  window.mostrarMensaje = function (textoMensaje) {
    return new Promise((resolve) => {
      // Establecer el texto del mensaje
      document.getElementById("textoMensaje").textContent = textoMensaje;

      // Mostrar el cuadro de diálogo
      document.getElementById("mensajeDlg").style.display = "flex";

      // Esperar a que el usuario cierre el cuadro de diálogo
      document.getElementById("botonAceptar").onclick = function () {
        cerrarDialogoMensaje();
        resolve();
      };
    });
  };
})();

/*
 * ********************** SERIALIZAR FORM*************************************
 */

//retorna los campos de un formulario en formato url-encoded para pasarlos a la vista
function serializarFormulario(idFormulario) {
  var form = document.getElementById(idFormulario);
  if (!form) {
    console.warn("Formulario con id '" + idFormulario + "' no encontrado.");
    return "";
  }

  var params = [];

  Array.from(form.elements).forEach(function (el) {
    if (!el.name) return; // Ignorar campos sin nombre

    // Ignorar botones
    if (["button", "submit", "reset", "fieldset"].includes(el.type)) return;

    // Ignorar campos deshabilitados manualmente, pero incluir con valor vacío
    if (el.disabled) {
      params.push(encodeURIComponent(el.name) + "=");
      return;
    }

    // Para checkboxes y radios: solo si están seleccionados
    if ((el.type === "checkbox" || el.type === "radio") && !el.checked) return;

    params.push(
      encodeURIComponent(el.name) + "=" + encodeURIComponent(el.value)
    );
  });

  return params.join("&");
}
/*
 * ********************** TABLA HTML*************************************
 */
//Genera el hmtl para mostrar una lista de objetos json en una tabla
//Si todos los json tienen algun campo en null no muestra ese campo
//Los nombres de los campos camelCase los muestra asi: nombreCampo -> Nombre Campo
//Ej. de uso (la funcion de seleccion es opcional) :
/*const html = crearTablaDesdeJson(clientes, function(indice, objeto) {
                alert(`Fila ${indice} seleccionada:\nNombre: ${objeto.nombre}`);
               
            });
 */

function crearTablaDesdeJson(data, onRowClick) {
  if (!Array.isArray(data) || data.length === 0) {
    return "<p>No hay datos para mostrar.</p>";
  }

  // Obtener todas las claves posibles
  const todasLasColumnas = Array.from(
    new Set(data.flatMap((obj) => Object.keys(obj)))
  );

  // Filtrar las columnas que tienen al menos un valor no nulo/undefined en alguna fila
  const columnas = todasLasColumnas.filter((col) =>
    data.some((fila) => fila[col] !== null && fila[col] !== undefined)
  );

  const tablaId = "tabla-json-" + Math.random().toString(36).substring(2, 10); // ID único

  // Crear HTML de tabla con ID
  let html = `<table id="${tablaId}"><thead><tr>`;
  columnas.forEach((col) => {
    const encabezado = formatearEncabezado(col);
    html += `<th>${encabezado}</th>`;
  });
  html += "</tr></thead><tbody>";

  data.forEach((fila, index) => {
    html += `<tr data-index="${index}">`;
    columnas.forEach((col) => {
      html += `<td>${
        fila[col] !== undefined && fila[col] !== null ? fila[col] : ""
      }</td>`;
    });
    html += "</tr>";
  });

  html += "</tbody></table>";

  // Devolver el HTML y dejar que el usuario lo inyecte (para luego instalar eventos)
  setTimeout(() => {
    if (typeof onRowClick === "function") {
      const tabla = document.getElementById(tablaId);
      if (tabla) {
        const filas = tabla.querySelectorAll("tbody tr");
        filas.forEach((fila) => {
          fila.addEventListener("click", () => {
            const idx = parseInt(fila.getAttribute("data-index"));
            onRowClick(idx, data[idx]);
          });
        });
      }
    }
  }, 0); // Espera a que el DOM lo pinte

  return html;
}

// Función para formatear encabezados separando camelCase con espacios
function formatearEncabezado(campo) {
  return campo
    .replace(/([a-z0-9])([A-Z])/g, "$1 $2") // inserta espacio entre minúscula/número y mayúscula
    .replace(/^./, (str) => str.toUpperCase()); // pone mayúscula inicial
}

/*
 * ********************** SELECT HTML*************************************
 */

//Genera el hmtl para mostrar una lista de objetos json en una lista (multiple o no)
//Ej. de uso (todos los parametro son opcionales menos los 2 primeros):
/*const html = crearListaDesdeJson({
                    data: clientes,
                    campoMostrar: 'nombre',
                    multiple : false,
                    onSelectItem: (indice, objeto) => {
                        alert(`Fila ${indice} seleccionada:\nNombre: ${objeto.cedula}`);
                        console.log("Seleccionado:", indice, objeto.cedula);
                    },
                    mostrarOpcionInicial : true, 
                    id : 'lista-clientes'
                 });
 */
function crearListaDesdeJson({
  data,
  campoMostrar,
  campoValor = null,
  multiple = false,
  onSelectItem = null,
  mostrarOpcionInicial = true,
  id = "lista-json",
}) {
  if (!Array.isArray(data) || data.length === 0) {
    return "<p>No hay datos para mostrar.</p>";
  }

  const selectId =
    id || "lista-json-" + Math.random().toString(36).substring(2, 10);
  const multipleAttr = multiple ? "multiple" : "";
  const listaIdMap = new Map(); // valor → { objeto, indice }

  let html = `<select name="${selectId}" id="${selectId}"  ${multipleAttr} style="width: 100%; padding: 8px; margin-top: 10px;">`;

  if (!multiple && mostrarOpcionInicial) {
    html += `<option value="-1" selected>Seleccione una opción...</option>`;
  }

  data.forEach((item, index) => {
    const texto =
      item[campoMostrar] !== undefined
        ? item[campoMostrar]
        : `(sin ${campoMostrar})`;
    const valor =
      campoValor && item[campoValor] !== undefined ? item[campoValor] : index;
    listaIdMap.set(String(valor), { objeto: item, indice: index });
    html += `<option value="${valor}">${texto}</option>`;
  });

  html += `</select>`;

  // Listeners
  setTimeout(() => {
    const select = document.getElementById(selectId);
    if (select && typeof onSelectItem === "function") {
      select.addEventListener("change", () => {
        const opcionesSeleccionadas = Array.from(select.selectedOptions)
          .map((opt) => opt.value)
          .filter((val) => val !== "-1"); // omitir la opción inicial

        opcionesSeleccionadas.forEach((val) => {
          const entry = listaIdMap.get(val);
          if (entry) {
            onSelectItem(entry.indice, entry.objeto);
          }
        });
      });
    }
  }, 0);

  return html;
}
function seleccionarPorTexto(idSelect, texto) {
  const select = document.getElementById(idSelect);
  for (let i = 0; i < select.options.length; i++) {
    if (select.options[i].text === texto) {
      select.selectedIndex = i;
      break;
    }
  }
}
