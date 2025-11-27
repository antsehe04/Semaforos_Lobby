package example.psp;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        Lobby lobbyUnica = new Lobby();
        List<Thread> hilosJugador = new ArrayList<>();

        int totalJugadores = 12;

        System.out.println("Sistema de Lobby Iniciado.");
        System.out.println("Capacidad Máxima del Lobby: " + Lobby.getCapacidad() + "\n");

        for(int i = 1; i <= totalJugadores; i++) {
            Thread t = new Thread(new Jugador(i, lobbyUnica));
            hilosJugador.add(t);
            t.start();

            Thread.sleep(50);
        }

        for (Thread t : hilosJugador) {
            t.join();
        }

        System.out.println("\n--- Todos los jugadores han intentado entrar. ---\n");

        Thread.sleep(30000);

        System.out.println("\nPROGRAMA FINALIZADO DESPUÉS DE LA SIMULACIÓN.");
    }
}