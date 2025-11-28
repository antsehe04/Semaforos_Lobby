package example.psp;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Lobby {

    // Tamaño máximo de la partida
    private static final int CAPACIDAD = 10;

    // Semáforo que controla cuántos jugadores pueden entrar simultáneamente
    private final Semaphore entradaSemaphore = new Semaphore(CAPACIDAD, true);

    // Lista de jugadores actualmente dentro del lobby
    private final List<String> jugadoresEnLobby = new ArrayList<>();

    // Indica si hay una partida ya en marcha
    private volatile boolean partidaEnCurso = false;
    public Lobby() {
        // Al crear el lobby, arrancamos la máquina que gestionará las partidas
        MaquinaEmparejamiento maquina = new MaquinaEmparejamiento(this);
        new Thread(maquina).start();
    }


    // Metodo que permite a un jugador intentar entrar al lobby
    public void entrarLobby(String nombre) throws InterruptedException {

        // Si el semáforo no da permiso, es que está llena
        if (!entradaSemaphore.tryAcquire()) {
            System.out.println(nombre + " no pudo entrar. Lobby llena.");
            return;
        }

        // Entró correctamente
        synchronized (jugadoresEnLobby) {
            jugadoresEnLobby.add(nombre);


            System.out.println(nombre + " ha entrado en la lobby. Jugadores esperando: "
                    + jugadoresEnLobby.size() + "/" + CAPACIDAD);
        }

        // Si es el primero o se ha llenado, avisamos a la máquina de emparejamiento
        if (jugadoresEnLobby.size() == CAPACIDAD || jugadoresEnLobby.size() == 1) {

            if (jugadoresEnLobby.size() == CAPACIDAD) {
                System.out.println(nombre + " ha llenado la lobby. Iniciando partida...");
            }

            synchronized (jugadoresEnLobby) {
                jugadoresEnLobby.notifyAll();
            }
        }

    }

    // La máquina de emparejamiento llama a esto para montar una partida
    public void procesarPartida() throws InterruptedException {

        List<String> jugadoresPartida;

        synchronized (jugadoresEnLobby) {

            // Espera hasta que haya suficientes jugadores
            while (jugadoresEnLobby.size() < CAPACIDAD) {

                if (jugadoresEnLobby.isEmpty()) {
                    System.out.println("\nMáquina DESCONECTADA.");
                } else {
                    System.out.println("\nMáquina esperando a que se llenen "
                            + jugadoresEnLobby.size() + "/" + CAPACIDAD + " huecos...");
                }

                jugadoresEnLobby.wait();
            }

            // Cuando se llena, comenzamos la partida
            partidaEnCurso = true;
            jugadoresPartida = new ArrayList<>(jugadoresEnLobby);
            jugadoresEnLobby.clear();

            System.out.println("\nPARTIDA INICIADA con " + jugadoresPartida.size());
        }

        // Simula el combate entre jugadores
        simularBatalla(jugadoresPartida);

        // Reinicia el lobby tras la partida
        resetLobby(jugadoresPartida.get(0));
    }

    // Simula la eliminación uno por uno hasta quedar un ganador
    private void simularBatalla(List<String> jugadores) throws InterruptedException {

        Random random = new Random();

        while (jugadores.size() > 1) {

            // Pausa aleatoria para dar dramatismo
            Thread.sleep(random.nextInt(1000) + 500);

            // Selecciona un jugador aleatorio para eliminar
            int index = random.nextInt(jugadores.size());
            String eliminado = jugadores.remove(index);

            System.out.println(eliminado + " ha sido ELIMINADO. Quedan "
                    + jugadores.size() + " jugadores.");
        }
    }

    // Resetea la sala tras una partida y expulsa al ganador
    private void resetLobby(String ganador) throws InterruptedException {

        System.out.println("\nEL GANADOR ES: " + ganador + "!!");
        //Tres segundos de la victoria del ganador

        Thread.sleep(3000);

        partidaEnCurso = false;


        System.out.println(ganador
                + " ha sido expulsado del juego y vuelve a la lobby.");

        // Libera un hueco del semáforo
        entradaSemaphore.release();
    }

    public static int getCapacidad() {
        return CAPACIDAD;
    }
}
