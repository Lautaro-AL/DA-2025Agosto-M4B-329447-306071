package ort.dda.obl.modelo;

import java.util.ArrayList;

public class DatosPrueba {

    public static void cargarDatos() {
        Fachada fachada = Fachada.getInstancia();

        // ==========================
        // 1) ADMINISTRADORES (aprox. 5)
        // campos: cédula, contraseña, nombre completo
        // ==========================
        fachada.agregarAdministrador("a", "a", "Juan Pérez");
        fachada.agregarAdministrador("a2", "a2", "María González");
        // entrada sugerida en el enunciado
        fachada.agregarAdministrador("12345678", "admin.123", "Usuario Administrador");
        // adicionales para pruebas
        fachada.agregarAdministrador("87654321", "pass001", "Luis Fernández");
        fachada.agregarAdministrador("11223344", "admin", "Sofía Ramírez");

        // ==========================
        // 2) PROPIETARIOS (aprox. 5)
        // campos: cédula, contraseña, nombre completo, saldo actual, saldo mínimo
        // alerta
        // ==========================
        fachada.agregarUsuarioPropietario("b", "b", "Carlos López", 2090, 500);
        fachada.agregarUsuarioPropietario("c", "c", "Ana Martínez", 3500, 1200);
        // entrada sugerida en el enunciado
        fachada.agregarUsuarioPropietario("23456789", "prop.123", "Usuario Propietario", 2000, 500);
        // adicionales para pruebas
        fachada.agregarUsuarioPropietario("34567890", "clave01", "Marcos Varela", 1500, 300);
        fachada.agregarUsuarioPropietario("98765432", "clave02", "Lucía Duarte", 1000, 50);

        // Obtener referencias a propietarios recién creados para asignarles vehículos
        ArrayList<Propietario> propietarios = fachada.obtenerPropietarios();

        // ==========================
        // Ajustar estados de propietarios (para pruebas)
        // - Carlos López (b): Habilitado
        // - Ana Martínez (c): Deshabilitado
        // - Usuario Propietario (23456789): Habilitado
        // - Marcos Varela (34567890): Penalizado
        // - Lucía Duarte (98765432): Suspendido
        // ==========================
        Propietario tmp;
        tmp = buscarPropietario(propietarios, "b");
        if (tmp != null) {
            tmp.cambiarEstado(new EstadoHabilitado(tmp));
        }
        tmp = buscarPropietario(propietarios, "c");
        if (tmp != null) {
            tmp.cambiarEstado(new EstadoDeshabilitado(tmp));
        }
        tmp = buscarPropietario(propietarios, "23456789");
        if (tmp != null) {
            tmp.cambiarEstado(new EstadoHabilitado(tmp));
        }
        tmp = buscarPropietario(propietarios, "34567890");
        if (tmp != null) {
            tmp.cambiarEstado(new EstadoPenalizado(tmp));
        }
        tmp = buscarPropietario(propietarios, "98765432");
        if (tmp != null) {
            tmp.cambiarEstado(new EstadoSuspendido(tmp));
        }

        // ==========================
        // 3) PUESTOS DE PEAJE (3 puestos)
        // campos: nombre, direccion
        // ==========================
        fachada.agregarPuesto("Puesto Centro", "Av. Principal 123");
        fachada.agregarPuesto("Puesto Norte", "Ruta 5 km 12");
        fachada.agregarPuesto("Puesto Sur", "Ruta 1 km 45");

        // ==========================
        // 4) CATEGORÍAS DE VEHÍCULOS (aprox. 5)
        // campos: nombre (ej. "Automóvil", "Camión")
        // ==========================
        fachada.agregarCategoria("Automóvil");
        fachada.agregarCategoria("Camión");
        fachada.agregarCategoria("Motocicleta");
        fachada.agregarCategoria("Bus");
        fachada.agregarCategoria("Tractor");

        // Obtener referencias a objetos Categoria recién creados
        ArrayList<Categoria> categorias = fachada.getCategorias();

        // pequeño helper local para buscar por nombre
        Categoria auto = buscarCategoria(categorias, "Automóvil");
        Categoria camion = buscarCategoria(categorias, "Camión");
        Categoria moto = buscarCategoria(categorias, "Motocicleta");
        Categoria bus = buscarCategoria(categorias, "Bus");
        Categoria tractor = buscarCategoria(categorias, "Tractor");

        // ==========================
        // 5) TARIFAS (monto y categoría)
        // Nota: el modelo actual guarda tarifas en SistemaVehiculos de forma global
        // (no están asociadas a un puesto individual en el modelo), por lo que
        // aquí se precargan tarifas por categoría que podrán usarse en la lógica.
        // ==========================
        fachada.agregarTarifa(50.0, auto);
        fachada.agregarTarifa(120.0, camion);
        fachada.agregarTarifa(20.0, moto);
        fachada.agregarTarifa(100.0, bus);
        fachada.agregarTarifa(80.0, tractor);

        // ==========================
        // 6) VEHÍCULOS (aprox. 5)
        // campos: matricula, modelo, color, categoría, propietario
        // Se asignan a los propietarios cargados
        // ==========================

        // Buscar propietarios por cédula para asignarles vehículos
        Propietario prop1 = buscarPropietario(propietarios, "b");
        Propietario prop2 = buscarPropietario(propietarios, "c");
        Propietario prop3 = buscarPropietario(propietarios, "23456789");
        Propietario prop4 = buscarPropietario(propietarios, "34567890");
        Propietario prop5 = buscarPropietario(propietarios, "98765432");

        // Agregar vehículos a los propietarios
        if (prop1 != null) {
            fachada.agregarVehiculo("ABC123", "Toyota Corolla", "Gris", auto, prop1);
        }
        if (prop1 != null) {
            fachada.agregarVehiculo("TRK890", "Volvo FH", "Blanco", camion, prop1);
        }
        if (prop1 != null) {
            fachada.agregarVehiculo("MOTO45", "Yamaha YBR", "Negro", moto, prop1);
        }
        if (prop4 != null) {
            fachada.agregarVehiculo("BUS001", "Mercedes O500", "Azul", bus, prop4);
        }
        if (prop5 != null) {
            fachada.agregarVehiculo("TRAC10", "John Deere", "Verde", tractor, prop5);
        }

        // Impresión simple para verificar que propietarios y categorías quedaron
        // cargados
        System.out.println("✓ Datos de prueba cargados exitosamente");
        System.out.println("  - Administradores: 5");
        System.out.println("  - Propietarios: " + fachada.obtenerPropietarios().size());
        System.out.println("  - Puestos de Peaje: " + fachada.getPuestosPeaje().size());
        System.out.println("  - Categorías: " + (categorias != null ? categorias.size() : 0));
        System.out.println("  - Vehículos: " + fachada.getTarifas().size() + " tarifas cargadas");
        System.out.println(" Propietario 1 " + prop1.getVehiculos());

        // ==========================
        // 7) ASIGNACIONES / BONIFICACIONES (6 bonificaciones)
        // Crear bonificaciones y asignarlas a propietarios para pruebas
        // ==========================
        Bonificacion bTrabajadores1 = new Trabajadores();
        Bonificacion bTrabajadores2 = new Trabajadores();
        Bonificacion bFrecuentes1 = new Frecuentes();
        Bonificacion bFrecuentes2 = new Frecuentes();
        Bonificacion bExonerados1 = new Exonerados();
        Bonificacion bExonerados2 = new Exonerados();

        ArrayList<PuestoPeaje> puestosAsign = fachada.getPuestosPeaje();
        if (puestosAsign != null && puestosAsign.size() >= 3) {
            // Propietario 1 (Carlos López) -> Frecuentes en Puesto Centro
            if (prop1 != null) {
                prop1.getAsignaciones().add(new Asignacion(bFrecuentes1, puestosAsign.get(0),
                        new java.sql.Date(System.currentTimeMillis())));
            }
            // Propietario 1 (Carlos López) -> Trabajadores en Puesto Norte
            if (prop1 != null) {
                prop1.getAsignaciones().add(new Asignacion(bTrabajadores1, puestosAsign.get(1),
                        new java.sql.Date(System.currentTimeMillis())));
            }
            // Propietario 3 (Usuario Propietario) -> Frecuentes en Puesto Sur
            if (prop3 != null) {
                prop3.getAsignaciones().add(new Asignacion(bFrecuentes2, puestosAsign.get(2),
                        new java.sql.Date(System.currentTimeMillis())));
            }
            // Propietario 4 (Marcos Varela) -> Trabajadores en Puesto Centro
            if (prop4 != null) {
                prop4.getAsignaciones().add(new Asignacion(bTrabajadores2, puestosAsign.get(0),
                        new java.sql.Date(System.currentTimeMillis())));
            }
            // Propietario 5 (Lucía Duarte) -> Exonerados en Puesto Norte
            if (prop5 != null) {
                prop5.getAsignaciones().add(new Asignacion(bExonerados1, puestosAsign.get(1),
                        new java.sql.Date(System.currentTimeMillis())));
            }
            // Propietario 2 (Ana Martínez) -> Exonerados en Puesto Sur
            if (prop2 != null) {
                prop2.getAsignaciones().add(new Asignacion(bExonerados2, puestosAsign.get(2),
                        new java.sql.Date(System.currentTimeMillis())));
            }
        }

        // Cargar datos de tránsitos de prueba
        System.out.println("\n--- Cargando tránsitos de prueba ---");
        cargarTransitos(fachada, propietarios, categorias);

        // Cargar notificaciones de prueba
        System.out.println("\n--- Cargando notificaciones de prueba ---");
        cargarNotificaciones(propietarios);

    }

    // Helper local para buscar una categoria por nombre en la lista
    private static Categoria buscarCategoria(ArrayList<Categoria> lista, String nombre) {
        if (lista == null)
            return null;
        for (Categoria c : lista) {
            if (c.getTipo().equalsIgnoreCase(nombre))
                return c;
        }
        return null;
    }

    // Helper local para buscar un propietario por cédula en la lista
    private static Propietario buscarPropietario(ArrayList<Propietario> lista, String cedula) {
        if (lista == null)
            return null;
        for (Propietario p : lista) {
            if (p.getCedula().equals(cedula))
                return p;
        }
        return null;
    }

    /**
     * Carga datos de prueba de tránsitos.
     * Simula el paso de vehículos por puestos de peaje con validación de saldo.
     */
    private static void cargarTransitos(Fachada fachada, ArrayList<Propietario> propietarios,
            ArrayList<Categoria> categorias) {
        // Obtener referencias necesarias
        ArrayList<PuestoPeaje> puestos = fachada.getPuestosPeaje();
        ArrayList<Tarifa> tarifas = fachada.getTarifas();

        if (puestos == null || puestos.isEmpty() || tarifas == null || tarifas.isEmpty()) {
            System.out.println("⚠ No hay puestos de peaje o tarifas cargados");
            return;
        }

        int transitosExitosos = 0;
        int transitosFallidos = 0;

        // Simular tránsitos de prueba
        // Propietario 1: Carlos López (saldo 200) - Transita con su Corolla
        Propietario p1 = buscarPropietario(propietarios, "b");
        if (p1 != null && p1.getVehiculos() != null && !p1.getVehiculos().isEmpty()) {
            Vehiculo v1 = p1.getVehiculos().get(0); // Toyota Corolla
            Tarifa t1 = buscarTarifaPorCategoria(tarifas, v1.getCategoria()); // Tarifa automóvil: 50
            if (t1 != null) {
                try {
                    fachada.registrarTransito(v1, puestos.get(0), t1, p1, new java.util.Date());
                    System.out.println(
                            "✓ Tránsito 1: " + p1.getNombreCompleto() + " pasó por " + puestos.get(0).getNombre());
                    transitosExitosos++;
                } catch (Exception e) {
                    System.out.println("✗ Tránsito 1 rechazado: " + e.getMessage());
                    transitosFallidos++;
                }
            }
        }

        // Propietario 2: Ana Martínez (saldo 0) - No puede transitar
        Propietario p2 = buscarPropietario(propietarios, "c");
        if (p2 != null && p2.getVehiculos() != null && !p2.getVehiculos().isEmpty()) {
            Vehiculo v2 = p2.getVehiculos().get(0);
            Tarifa t2 = buscarTarifaPorCategoria(tarifas, v2.getCategoria());
            if (t2 != null) {
                try {
                    fachada.registrarTransito(v2, puestos.get(1), t2, p2, new java.util.Date());
                    System.out.println(
                            "✓ Tránsito 2: " + p2.getNombreCompleto() + " pasó por " + puestos.get(1).getNombre());
                    transitosExitosos++;
                } catch (Exception e) {
                    System.out.println("✗ Tránsito rechazado: " + p2.getNombreCompleto()
                            + " - " + e.getMessage() + " (saldo: " + p2.getSaldoActual() + ")");
                    transitosFallidos++;
                }
            }
        }

        // Propietario 3: Usuario Propietario (saldo 2000) - Transita 2 veces
        Propietario p3 = buscarPropietario(propietarios, "23456789");
        if (p3 != null && p3.getVehiculos() != null && !p3.getVehiculos().isEmpty()) {
            Vehiculo v3 = p3.getVehiculos().get(0);
            Tarifa t3 = buscarTarifaPorCategoria(tarifas, v3.getCategoria());

            if (t3 != null) {
                try {
                    fachada.registrarTransito(v3, puestos.get(0), t3, p3, new java.util.Date());
                    System.out.println(
                            "✓ Tránsito 3: " + p3.getNombreCompleto() + " pasó por " + puestos.get(0).getNombre());
                    transitosExitosos++;
                } catch (Exception e) {
                    System.out.println("✗ Tránsito 3 rechazado: " + e.getMessage());
                    transitosFallidos++;
                }

                try {
                    fachada.registrarTransito(v3, puestos.get(2), t3, p3, new java.util.Date());
                    System.out.println(
                            "✓ Tránsito 4: " + p3.getNombreCompleto() + " pasó por " + puestos.get(2).getNombre());
                    transitosExitosos++;
                } catch (Exception e) {
                    System.out.println("✗ Tránsito 4 rechazado: " + e.getMessage());
                    transitosFallidos++;
                }
            }
        }

        // Propietario 4: Marcos Varela (saldo 1500) - Transita con su bus
        Propietario p4 = buscarPropietario(propietarios, "34567890");
        if (p4 != null && p4.getVehiculos() != null && !p4.getVehiculos().isEmpty()) {
            Vehiculo v4 = p4.getVehiculos().get(0);
            Tarifa t4 = buscarTarifaPorCategoria(tarifas, v4.getCategoria());
            if (t4 != null) {
                try {
                    fachada.registrarTransito(v4, puestos.get(3), t4, p4, new java.util.Date());
                    System.out.println(
                            "✓ Tránsito 5: " + p4.getNombreCompleto() + " pasó por " + puestos.get(3).getNombre());
                    transitosExitosos++;
                } catch (Exception e) {
                    System.out.println("✗ Tránsito 5 rechazado: " + e.getMessage());
                    transitosFallidos++;
                }
            }
        }

        // Propietario 5: Lucía Duarte (saldo 50) - Saldo bajo, intenta transitar pero
        // falla
        Propietario p5 = buscarPropietario(propietarios, "98765432");
        if (p5 != null && p5.getVehiculos() != null && !p5.getVehiculos().isEmpty()) {
            Vehiculo v5 = p5.getVehiculos().get(0);
            Tarifa t5 = buscarTarifaPorCategoria(tarifas, v5.getCategoria());
            if (t5 != null) {
                try {
                    fachada.registrarTransito(v5, puestos.get(4), t5, p5, new java.util.Date());
                    System.out.println(
                            "✓ Tránsito 6: " + p5.getNombreCompleto() + " pasó por " + puestos.get(4).getNombre());
                    transitosExitosos++;
                } catch (Exception e) {
                    System.out.println("✗ Tránsito rechazado: " + p5.getNombreCompleto()
                            + " - " + e.getMessage() + " (saldo: " + p5.getSaldoActual() + ", monto: " + t5.getMonto()
                            + ")");
                    transitosFallidos++;
                }
            }
        }

        System.out.println("\n--- Resumen de tránsitos ---");
        System.out.println("✓ Exitosos: " + transitosExitosos);
        System.out.println("✗ Rechazados: " + transitosFallidos);

        // Mostrar saldos finales (incluye estado del propietario)
        System.out.println("\n--- Saldos finales ---");
        for (Propietario p : propietarios) {
            String estadoNombre = p.getEstado() != null ? p.getEstado().getNombre() : "(sin estado)";
            System.out.println(p.getNombreCompleto() + " (" + estadoNombre + "): $" + p.getSaldoActual());
        }
    }

    /**
     * Carga notificaciones de prueba para los propietarios.
     */
    private static void cargarNotificaciones(ArrayList<Propietario> propietarios) {
        if (propietarios == null || propietarios.isEmpty()) {
            System.out.println("⚠ No hay propietarios para cargar notificaciones");
            return;
        }

        int notificacionesCargadas = 0;

        // Propietario 1: Carlos López
        Propietario p1 = buscarPropietario(propietarios, "b");
        if (p1 != null) {
            p1.getNotificaciones()
                    .add(new Notificacion("Tránsito registrado en Puesto Centro"));
            p1.getNotificaciones().add(
                    new Notificacion("Saldo bajo: $2040. Recarga recomendada"));
            p1.getNotificaciones()
                    .add(new Notificacion("Bonificación Frecuentes activada"));
            notificacionesCargadas += 3;
        }

        // Propietario 2: Ana Martínez
        Propietario p2 = buscarPropietario(propietarios, "c");
        if (p2 != null) {
            p2.getNotificaciones()
                    .add(new Notificacion("Saldo insuficiente para transitar"));
            p2.getNotificaciones().add(new Notificacion(
                    "Bonificación Exonerados disponible en Puesto Sur"));
            notificacionesCargadas += 2;
        }

        // Propietario 3: Usuario Propietario
        Propietario p3 = buscarPropietario(propietarios, "23456789");
        if (p3 != null) {
            p3.getNotificaciones()
                    .add(new Notificacion("Bienvenido! Tu saldo es $2000"));
            p3.getNotificaciones().add(new Notificacion(
                    "Tienes descuento Frecuentes en Puesto Sur"));
            p3.getNotificaciones().add(new Notificacion(
                    "Tránsito completado: Automóvil ABC123 - $50"));
            notificacionesCargadas += 3;
        }

        // Propietario 4: Marcos Varela
        Propietario p4 = buscarPropietario(propietarios, "34567890");
        if (p4 != null) {
            p4.getNotificaciones()
                    .add(new Notificacion("Descuento Trabajadores: Tránsito $100"));
            p4.getNotificaciones()
                    .add(new Notificacion("Viernes: 20% de descuento aplicado"));
            notificacionesCargadas += 2;
        }

        // Propietario 5: Lucía Duarte
        Propietario p5 = buscarPropietario(propietarios, "98765432");
        if (p5 != null) {
            p5.getNotificaciones().add(new Notificacion(
                "Exoneración registrada en Puesto Norte"));
            // Actualizar mensaje para reflejar el estado de prueba (Suspendido)
            p5.getNotificaciones().add(new Notificacion(
                "Estado actual: Suspendido. Saldo: $50"));
            notificacionesCargadas += 2;
        }

        System.out.println("✓ Notificaciones cargadas: " + notificacionesCargadas);
    }

    /**
     * Busca una tarifa por categoría (retorna la primera encontrada).
     */
    private static Tarifa buscarTarifaPorCategoria(ArrayList<Tarifa> tarifas, Categoria categoria) {
        if (tarifas == null || categoria == null)
            return null;
        for (Tarifa t : tarifas) {
            if (t.getCategoria().getTipo().equalsIgnoreCase(categoria.getTipo())) {
                return t;
            }
        }
        return null;
    }

}