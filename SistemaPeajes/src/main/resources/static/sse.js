//Se asume que esta incluida vistaWeb.js
//La pagina que incluya esta lib debe cargar esta variable
//Para registrar SSE debe ejecutarse siempre antes el inicio de la vista
var urlRegistroSSE = null;

//Esta funcion la llama vistaWeb.js al final del submit de inicio de la vista

function primerSubmitFinalizado() {
  registrarSSE();
}

function registrarSSE() {
  //LLamada al endpoint para recibir mensajes desde el servidor
  if (urlRegistroSSE !== null) {
    const eventSource = new EventSource(urlRegistroSSE, {
      withCredentials: true,
    });

    //LLEGA UN MENSAJE DESDE EL SERVIDOR!
    eventSource.onmessage = function (event) {
      //Se asume que todos los mensajes llegan en formtato JSON
      json = JSON.parse(event.data); // Convertir el JSON a objeto
      procesarMensajeSSE(json);
    };
    //ERROR EN LA CONEXION CON EL SERVIDOR
    eventSource.onerror = function (event) {
      //En todos los casos se cierra el event source
      eventSource.close();
      try {
        conexionSSECerrada(event); //Metodo que puede estar definido en la pagina que incluya esta lib
        //para personalizar el manejo del error en la conexion SSE
      } catch (e) {
        //por defecto se "borra" la pagina
        document.body.innerHTML = "";
      }
    };
  }
}
//Por defecto se asume que los mensajes se reciven via SSE tienen el mismo formato que las respuestas
//del submit.
function procesarMensajeSSE(mensaje) {
  procesarResultadosSubmit(mensaje);
}
